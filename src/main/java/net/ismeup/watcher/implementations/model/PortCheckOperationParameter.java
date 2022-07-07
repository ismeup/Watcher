package net.ismeup.watcher.implementations.model;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class PortCheckOperationParameter extends CheckOperationParameter {

    private String host;
    private int port;
    private boolean notEmpty;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            host = jsonObject.getString("host");
            port = jsonObject.getInt("port");
            notEmpty = jsonObject.getBoolean("notEmpty");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }
}
