package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.MonitorCheckOperationParameter;
import org.json.JSONObject;

public class DiskUsageCheckOperationParameters extends MonitorCheckOperationParameter {

    private String disk;
    private int limit;

    @Override
    public void fromJsonMonitor(JSONObject jsonObject) throws OperationParseException {
        try {
            disk = jsonObject.getString("disk");
            limit = jsonObject.getInt("limit");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public String getDisk() {
        return disk;
    }

    public int getLimit() {
        return limit;
    }
}
