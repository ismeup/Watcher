package net.ismeup.watcher.interfaces;

import org.json.JSONObject;

public abstract class CheckOperationResult implements JsonableAnswer {

    private boolean status;

    public CheckOperationResult(boolean status) {
        this.status = status;
    }

    public boolean isStatusOk() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return isStatusOk();
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", getStatus());
    }
}
