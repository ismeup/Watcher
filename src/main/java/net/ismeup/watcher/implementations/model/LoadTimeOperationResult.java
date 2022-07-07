package net.ismeup.watcher.implementations.model;

import org.json.JSONObject;
import net.ismeup.watcher.interfaces.CheckOperationResult;

public class LoadTimeOperationResult extends CheckOperationResult {

    private double time;

    public LoadTimeOperationResult(boolean status, double time) {
        super(status);
        this.time = time;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("time", time);
    }
}
