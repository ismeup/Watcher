package net.ismeup.watcher.interfaces;

import net.ismeup.watcher.exceptions.OperationParseException;
import org.json.JSONObject;

public abstract class MonitorCheckOperationParameter extends CheckOperationParameter {

    private String host;
    private int port;
    private String key;

    @Override
    public final void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            host = jsonObject.getString("host");
            port = jsonObject.getInt("port");
            key = jsonObject.getString("key");
        } catch (Exception e) {
            throw new OperationParseException();
        }
        fromJsonMonitor(jsonObject);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getKey() {
        return key;
    }

    abstract public void fromJsonMonitor(JSONObject jsonObject) throws OperationParseException;
}
