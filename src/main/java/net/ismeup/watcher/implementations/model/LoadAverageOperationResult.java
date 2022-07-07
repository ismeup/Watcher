package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.interfaces.CheckOperationResult;
import org.json.JSONObject;

public class LoadAverageOperationResult extends CheckOperationResult  {

    private double loadAverage;

    public LoadAverageOperationResult(boolean status, double loadAverage) {
        super(status);
        this.loadAverage = loadAverage;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("loadavg", loadAverage);
    }
}
