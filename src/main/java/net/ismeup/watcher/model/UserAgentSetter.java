package net.ismeup.watcher.model;

import java.net.HttpURLConnection;

public class UserAgentSetter {
    public static void set(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty("User-Agent", "isMeUp-Watcher");
    }
}
