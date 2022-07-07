package net.ismeup.watcher.interfaces;

import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public abstract class CheckerMonitor implements Checker {

    private String url;
    private String key;
    private Cipher encryptAesCipher;
    private Cipher decryptAesCipher;
    boolean ciphersInited = false;

    private final void parseMonitorParameters(MonitorCheckOperationParameter parameter) {
        url = "http://" + parameter.getHost() + ":" + parameter.getPort();
        key = parameter.getKey();
        initCiphers();
    }

    @Override
    public final void runCheck(CheckOperationParameter checkOperationParameter) {
        parseMonitorParameters((MonitorCheckOperationParameter) checkOperationParameter);
        if (ciphersInited) {
            parseCheckParameters(checkOperationParameter);
            OutputStream outputStream = null;
            InputStream inputStream = null;
            String stringResult = "_not_received";
            UUID salt = UUID.randomUUID();
            try {
                JSONObject data = getRequestJson()
                        .put("operation", getOperationName())
                        .put("salt", salt.toString());
                byte[] encrypted = encryptAesCipher.doFinal(data.toString().getBytes("UTF-8"));
                byte[] dataToSend = Base64.getEncoder().encode(encrypted);
                URLConnection urlConnection = new URL(url).openConnection();
                urlConnection.setDoOutput(true);
                outputStream = urlConnection.getOutputStream();
                outputStream.write(dataToSend);
                inputStream = urlConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int c;
                while ( (c = inputStream.read()) != -1 ) {
                    byteArrayOutputStream.write(c);
                }
                byte[] encryptedDataReceived = Base64.getDecoder().decode(byteArrayOutputStream.toByteArray());
                byte[] decryptedDataReceived = decryptAesCipher.doFinal(encryptedDataReceived);
                stringResult = new String(decryptedDataReceived);
                JSONObject jsonObject = new JSONObject(stringResult);
                String result = jsonObject.getString("answer");
                parseResult(result);
            } catch (Exception e) {
                System.out.println("Message was: " + stringResult);
                System.out.println("Salt was: " + salt);
                System.out.println(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {

                }
                try {
                    outputStream.close();
                } catch (Exception e) {

                }
            }
        } else {
            System.out.println("Can't init ciphers");
        }
    }

    private void initCiphers() {
        MessageDigest sha = null;
        byte[] key = new byte[0];
        try {
            key = this.key.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            encryptAesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encryptAesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            decryptAesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decryptAesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            ciphersInited = true;
        } catch (Exception e) {
        }
    }

    protected JSONObject getRequestJson() {
        return new JSONObject();
    }

    abstract protected String getOperationName();

    abstract protected void parseResult(String result);

    abstract protected void parseCheckParameters(CheckOperationParameter checkOperationParameter);

}
