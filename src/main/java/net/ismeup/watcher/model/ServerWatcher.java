package net.ismeup.watcher.model;

import org.json.JSONObject;

public class ServerWatcher {
    private int id;
    private String name;
    private String key;
    private long lastOnline;
    private boolean isMain;
    private int checksCount;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public boolean isMain() {
        return isMain;
    }

    public int getChecksCount() {
        return checksCount;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("key", key)
                .put("lastOnline", lastOnline)
                .put("isMain", isMain)
                .put("checksCount", checksCount);
    }

    public static ServerWatcher fromJson(JSONObject jsonObject) {
        ServerWatcher serverWatcher = new ServerWatcher();
        serverWatcher.id = jsonObject.optInt("id", 0);
        serverWatcher.name = jsonObject.optString("name", "");
        serverWatcher.key = jsonObject.optString("key", "");
        serverWatcher.lastOnline = jsonObject.optLong("lastOnline", 0);
        serverWatcher.isMain = jsonObject.optBoolean("isMain", false);
        serverWatcher.checksCount = jsonObject.optInt("checksCount", 0);
        return serverWatcher;
    }

    public static ServerWatcher empty() {
        ServerWatcher serverWatcher = new ServerWatcher();
        serverWatcher.id = 0;
        serverWatcher.name = "";
        serverWatcher.key = "";
        serverWatcher.lastOnline = 0;
        serverWatcher.isMain = false;
        serverWatcher.checksCount = 0;
        return serverWatcher;
    }

    public static ServerWatcher createByName(String name) {
        ServerWatcher serverWatcher = empty();
        serverWatcher.name = name;
        return serverWatcher;
    }
}
