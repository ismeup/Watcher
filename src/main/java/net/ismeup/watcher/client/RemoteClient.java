package net.ismeup.watcher.client;

import net.ismeup.watcher.interfaces.JsonableAnswer;
import net.ismeup.watcher.operation_controller.OperationController;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

public class RemoteClient implements Runnable {
    public final String identity;
    private final Cipher cipher;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private final Object synchronizer = new Object();
    private long lastSuccessPacket = 0;
    private boolean ready = false;
    private String aesKey;
    private Cipher encryptAesCipher;
    private Cipher decryptAesCipher;
    private final boolean master;
    private int threadNumber = 0;
    private ConnectionData connectionData;

    public RemoteClient(ConnectionData connectionData, String identity, Cipher cipher) {
        this(connectionData, identity, cipher, true, 0);
    }

    public RemoteClient(ConnectionData connectionData, String identity, Cipher cipher, boolean master, int threadNumber) {
        this.connectionData = connectionData;
        this.identity = identity;
        this.cipher = cipher;
        this.master = master;
        this.threadNumber = threadNumber;
    }

    private void connect() throws RemoteConnectException {
        try {
            socket = new Socket(connectionData.getHost(), connectionData.getPort());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            configureCiphers();
            negotiate();
            watchKeepAliveThread();
            readMessages();
        } catch (IOException e) {
            throw new RemoteConnectException();
        }
    }

    private boolean configureCiphers() {
        aesKey = UUID.randomUUID().toString();
        MessageDigest sha = null;
        byte[] key = new byte[0];
        try {
            key = aesKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            encryptAesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptAesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decryptAesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptAesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            return true;
        } catch (UnsupportedEncodingException e) {
            disconnect();
        } catch (NoSuchAlgorithmException e) {
            disconnect();
        } catch (NoSuchPaddingException e) {
            disconnect();
        } catch (InvalidKeyException e) {
            disconnect();
        }
        return false;
    }

    void disconnect() {
        ready = false;
        try {
            inputStream.close();
        } catch (IOException e) {

        }
        try {
            outputStream.close();
        } catch (IOException e) {

        }
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    private void printThread() {
        System.out.print("TH " + threadNumber + " : ");
    }

    private void watchKeepAliveThread() {
        new Thread(
                () -> {
                    while (true) {
                        if (System.currentTimeMillis() - getLastSuccessPacket() > 60000) {
                            printThread();
                            System.out.println("Disconnection by keepalive timeout");
                            disconnect();
                            break;
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {

                            }
                        }
                    }
                }
        ).start();
    }

    private long getLastSuccessPacket() {
        synchronized (this) {
            return lastSuccessPacket;
        }
    }

    private void updateKeepAlive() {
        printThread();
        System.out.println("Updating keep-alive");
        synchronized (this) {
            lastSuccessPacket = System.currentTimeMillis();
        }
    }

    void negotiate() {
        JSONObject jsonObject = new JSONObject().put("iam", identity).put("aes", aesKey).put("master", master);
        String message = jsonObject.toString();
        byte[] data = new byte[0];
        try {
            synchronized (cipher) {
                data = cipher.doFinal(message.getBytes());
            }
        } catch (IllegalBlockSizeException e) {
            disconnect();
        } catch (BadPaddingException e) {
            disconnect();
        }
        sendBytesRaw(data);
        updateKeepAlive();
    }

    private void readMessages() {
        byte b = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (inputStream != null && b != -1 && byteArrayOutputStream.size() < 16) {
            try {
                b = (byte) inputStream.read();
                if (b == 0) {
                    int length = parseLength(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.reset();
                    byte[] data = new byte[length];
                    int count = inputStream.read(data);
                    if (count == length) {
                        parseMessage(data);
                    }
                } else {
                    byteArrayOutputStream.write(b);
                }
            } catch (IOException e) {
                disconnect();
                return;
            }
        }
    }

    private int parseLength(byte[] data) {
        int length = 0;
        String lengthData = new String(data);
        if (lengthData.contains("len:")) {
            int end = lengthData.lastIndexOf(":");
            String lengthStr = lengthData.substring(4, end);
            try {
                length = Integer.parseInt(lengthStr);
            } catch (NumberFormatException e) {
                disconnect();
            }
        } else {
            disconnect();
        }
        return length;
    }

    private void sendBytes(byte[] dataBytes) {
        printThread();
        System.out.println(">>>>>>> " + new String(dataBytes));
        try {
            synchronized (this) {
                byte[] encryptedDataBytes = encryptAesCipher.doFinal(dataBytes);
                sendBytesRaw(encryptedDataBytes);
            }
        } catch (IllegalBlockSizeException e) {
            disconnect();
        } catch (BadPaddingException e) {
            disconnect();
        }
    }

    private void sendBytesRaw(byte[] dataBytes) {
        synchronized (synchronizer) {
            byte[] lengthText = ("len:" + dataBytes.length + ":").getBytes();
            try {
                outputStream.write(lengthText);
                outputStream.write(0);
                outputStream.write(dataBytes);
            } catch (IOException e) {
                disconnect();
            }
        }
    }

    void parseMessage(byte[] messageData) {
        try {
            String message = null;
            synchronized (this) {
                message = new String(decryptAesCipher.doFinal(messageData));
            }
            printThread();
            System.out.println("<<<<<<< " + message);
            if (!ready && message.equals("HELLO" + identity)) {
                updateKeepAlive();
                ready = true;
                return;
            }
            if (ready) {
                if (master && message.startsWith("THREADS: ")) {
                    try {
                        int threadsCount = Integer.parseInt(message.replaceFirst("THREADS: ", ""));
                        if (threadsCount > 1 && threadsCount < 64) {
                            for (int i = 0; i < threadsCount - 1; i++) {
                                new Thread(new RemoteClient(connectionData, identity, cipher, false, i + 1)).start();
                            }
                        }
                    } catch (Exception e) {
                        disconnect();
                    }
                }
                if (message.equals("AREYOUOK")) {
                    updateKeepAlive();
                    sendBytes("IMOK".getBytes());
                    return;
                }
                if (message.startsWith("message ")) {
                    int start = message.indexOf(" ");
                    int end = message.indexOf(":");
                    String uuidString = message.substring(start + 1, end);
                    UUID uuid = UUID.fromString(uuidString);
                    String jsonString = message.substring(end + 1);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        new Thread(() -> {
                            OperationController operationController = new OperationController();
                            JsonableAnswer jsonableAnswer = operationController.startCheck(jsonObject);
                            sendAnswer(jsonableAnswer.getAnswer().toString(), uuid);
                            updateKeepAlive();
                        }).start();
                    } catch (JSONException jsonException) {
                        disconnect();
                    }
                    return;
                }
            }
        } catch (IllegalBlockSizeException e) {
            disconnect();
        } catch (BadPaddingException e) {
            disconnect();
        }
    }

    void sendAnswer(String message, UUID id) {
        String answerMessage = "answer " + id + ": " + message;
        sendBytes(answerMessage.getBytes());
    }

    @Override
    public void run() {
        boolean started = false;
        while (!started || master) {
            started = true;
            printThread();
            System.out.println("Connecting to server...");
            try {
                connect();
            } catch (RemoteConnectException e) {
                printThread();
                System.out.println("Connection aborted!");
            }
            printThread();
            System.out.println("Closing connection");
            printThread();
            System.out.println("Waiting 5 seconds to reconnect");
            ready = false;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
        }
    }
}
