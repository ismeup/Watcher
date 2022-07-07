package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.MonitorCheckOperationParameter;
import org.json.JSONObject;

public class UptimeCheckOperationParameter extends MonitorCheckOperationParameter  {

    private int uptime;

    @Override
    public void fromJsonMonitor(JSONObject jsonObject) throws OperationParseException {
        try {
            uptime = jsonObject.getInt("uptime");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public int getUptime() {
        return uptime;
    }
}
