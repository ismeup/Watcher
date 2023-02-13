package net.ismeup.watcher.model.content_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;
import net.ismeup.watcher.model.UserAgentSetter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ContentCheck implements Checker {

    private boolean status = false;

    @Override
    public void runCheck(CheckOperationParameter checkOperationParameter) {
        ContentCheckOperationParameter contentCheckOperationParameter = (ContentCheckOperationParameter) checkOperationParameter;
        try {
            URLConnection urlConnection = new URL(contentCheckOperationParameter.getUrl()).openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            UserAgentSetter.set(httpURLConnection);
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int c;
            while ( (c = inputStream.read()) != -1 ) {
                byteArrayOutputStream.write(c);
            }
            String content = new String(byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            httpURLConnection.disconnect();
            inputStream.close();
            if (contentCheckOperationParameter.isMustContain()) {
                status = content.contains(contentCheckOperationParameter.getText());
            } else {
                status = !content.contains(contentCheckOperationParameter.getText());
            }
        } catch (IOException e) {

        } catch (Exception e) {

        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new ContentCheckOperationResult(status);
    }
}
