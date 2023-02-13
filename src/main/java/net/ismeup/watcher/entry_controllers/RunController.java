package net.ismeup.watcher.entry_controllers;

import net.ismeup.watcher.Main;
import net.ismeup.watcher.client.ConnectionData;
import net.ismeup.watcher.client.RemoteClient;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RunController {

    public void run(String[] args) {
        ConnectionData connectionData = null;
        if (args.length == 2) {
            try {
                connectionData = new ConnectionData(args[0], args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Port number is invalid");
                System.exit(1);
            }
        } else {
            connectionData = new ConnectionData();
        }
        System.out.println("Connecting to " + connectionData.getHost() + ":" + connectionData.getPort());
        disableCertificateValidation();

        String identity = loadIdentity();
        Cipher cipher = getCipher();
        if (cipher != null && !identity.isEmpty()) {
            new Thread(new RemoteClient(connectionData, identity, cipher)).start();
        } else {
            System.out.println("Can not init RSA cipher");
        }
    }

    public static void disableCertificateValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {

        }
    }

    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            InputStream keyStream = Main.class.getClassLoader().getResourceAsStream("public.key");
            int i = 0;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ( (i = keyStream.read()) != -1) {
                byteArrayOutputStream.write(i);
            }
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(byteArrayOutputStream.toByteArray());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey privateKey = keyFactory.generatePublic(publicKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        }
        catch (IOException e) {}
        catch (NoSuchAlgorithmException e) {}
        catch (InvalidKeySpecException e) {}
        catch (InvalidKeyException e) {}
        catch (NoSuchPaddingException e) {}
        return cipher;
    }

    private String loadIdentity() {
        String identity = "";
        try {
            String fileName = "identity.key";
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String inputFilePath = jarFile.getParent() + File.separator + fileName;
            FileInputStream inStream = new FileInputStream(new File(inputFilePath));
            int size = inStream.available();
            byte[] data = new byte[size];
            inStream.read(data);
            identity = new String(data).replace("\r", "").replace("\n", "").trim();
            System.out.println(identity);
        } catch (FileNotFoundException e) {
            System.out.println("File identity.key is not found! Register new Watcher in your application and provide identity.key file or register Watcher in standalone mode by passing --register argument");
            System.exit(1);
        } catch (URISyntaxException e) {
            System.out.println("Error opening identity.key");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Can't read identity.key!");
            System.exit(1);
        }
        return identity;
    }
}
