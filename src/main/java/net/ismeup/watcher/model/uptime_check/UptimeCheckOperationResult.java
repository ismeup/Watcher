package net.ismeup.watcher.model.uptime_check;

import net.ismeup.watcher.interfaces.CheckOperationResult;
import org.json.JSONObject;

public class UptimeCheckOperationResult extends CheckOperationResult  {

    private int uptime;

    public UptimeCheckOperationResult(boolean status, int uptime) {
        super(status);
        this.uptime = uptime;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("uptime", uptime);
    }
}
