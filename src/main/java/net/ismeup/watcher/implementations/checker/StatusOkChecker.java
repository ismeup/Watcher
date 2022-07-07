package net.ismeup.watcher.implementations.checker;

import net.ismeup.watcher.implementations.model.StatusOkCheckOperationParameter;
import net.ismeup.watcher.implementations.model.StatusOkCheckOperationResult;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StatusOkChecker implements Checker {

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
            int code = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            status = code == 200;
        } catch (IOException e) {

        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new StatusOkCheckOperationResult(status);
    }
}
