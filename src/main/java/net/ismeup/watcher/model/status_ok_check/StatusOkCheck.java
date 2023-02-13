package net.ismeup.watcher.model.status_ok_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;
import net.ismeup.watcher.model.UserAgentSetter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StatusOkCheck implements Checker {

    private String url;

    private boolean status = false;

    private void parseConfiguration(StatusOkCheckOperationParameter statusOkCheckOperationParameter) {
        this.url = statusOkCheckOperationParameter.getUrl();
    }

    @Override
    public void runCheck(CheckOperationParameter checkOperationParameter) {
        parseConfiguration( (StatusOkCheckOperationParameter) checkOperationParameter);
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            UserAgentSetter.set(httpURLConnection);
            int code = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            status = code == 200;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new StatusOkCheckOperationResult(status);
    }
}
