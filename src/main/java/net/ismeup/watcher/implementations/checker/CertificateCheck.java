package net.ismeup.watcher.implementations.checker;

import net.ismeup.watcher.implementations.model.CertificateCheckParameter;
import net.ismeup.watcher.implementations.model.CertificateCheckResult;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class CertificateCheck implements Checker {

    private String url;
    private int days;
    private boolean status = false;
    private int restDays;

    private void parseConfiguration(CertificateCheckParameter certificateCheckParameter) {
       this.url = certificateCheckParameter.getUrl();
       this.days = certificateCheckParameter.getDays();
    }

    @Override
    public void runCheck(CheckOperationParameter checkOperationParameter) {
        parseConfiguration( (CertificateCheckParameter) checkOperationParameter);
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
            byte[] bytes = new byte[10];
            urlConnection.getInputStream().read(bytes);
            Certificate[] certificates = httpsURLConnection.getServerCertificates();
            httpsURLConnection.disconnect();
            Date date = null;
            for (Certificate currentCertificate : certificates) {
                X509Certificate x509Certificate = (X509Certificate) currentCertificate;
                date = x509Certificate.getNotAfter();
                break;
            }
            if (date != null) {
                LocalDateTime nowTime = LocalDateTime.now();
                LocalDateTime certificateDateTime = LocalDateTime.ofEpochSecond((date.getTime() / 1000), 0, ZoneOffset.UTC);
                if (certificateDateTime.isAfter(nowTime)) {
                    long certificateTimeStamp = certificateDateTime.toEpochSecond(ZoneOffset.UTC);
                    long currentTimeStamp = nowTime.toEpochSecond(ZoneOffset.UTC);
                    long resultTime = certificateTimeStamp - currentTimeStamp;
                    restDays = (int) (resultTime / (24 * 60 * 60));
                    status = restDays >= days;
                }
            }
        } catch (IOException e) {

        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new CertificateCheckResult(status, restDays);
    }
}
