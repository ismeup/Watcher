package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.MonitorCheckOperationParameter;
import org.json.JSONObject;

public class MemoryCheckOperationParameter extends MonitorCheckOperationParameter {
    private int limit;

    @Override
    public void fromJsonMonitor(JSONObject jsonObject) throws OperationParseException {
        try {
            limit = jsonObject.getInt("limit");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public int getLimit() {
        return limit;
    }
}
