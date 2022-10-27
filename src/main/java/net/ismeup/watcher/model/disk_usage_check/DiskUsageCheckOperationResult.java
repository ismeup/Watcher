package net.ismeup.watcher.model.disk_usage_check;

import net.ismeup.watcher.interfaces.CheckOperationResult;
import org.json.JSONObject;

public class DiskUsageCheckOperationResult extends CheckOperationResult  {

    private int usage;

    public DiskUsageCheckOperationResult(boolean status, int usage) {
        super(status);
        this.usage = usage;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("usage", usage);
    }
}
