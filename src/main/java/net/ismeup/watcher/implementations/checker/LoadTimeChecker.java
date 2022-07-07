package net.ismeup.watcher.implementations.checker;

import net.ismeup.watcher.implementations.model.LoadTimeOperationParameters;
import net.ismeup.watcher.implementations.model.LoadTimeOperationResult;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class LoadTimeChecker implements Checker {

    private String url;
    private float timeLimit = 3;
    private boolean status = false;
    private double timeElapsed = 0.0;

    private void parseParameters(LoadTimeOperationParameters loadTimeOperationParameters) {
        this.url = loadTimeOperationParameters.getUrl();
        this.timeLimit = loadTimeOperationParameters.getTimeLimit();
    }

    @Override
    public void runCheck(CheckOperationParameter loadTimeOperationParameters) {
        parseParameters((LoadTimeOperationParameters) loadTimeOperationParameters);
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            long before = System.currentTimeMillis();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            urlConnection.getDoInput();
            byte[] readBuffer = new byte[1024];
            httpURLConnection.getInputStream().read(readBuffer);
            httpURLConnection.disconnect();
            long after = System.currentTimeMillis();
            int getTime = (int) (after - before);
            double elapsedTime = (double) getTime / 1000f;
            timeElapsed = elapsedTime;
            status = elapsedTime < timeLimit;
        } catch (IOException e) {

        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new LoadTimeOperationResult(status, timeElapsed);
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }
}
