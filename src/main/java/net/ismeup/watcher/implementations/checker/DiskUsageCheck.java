package net.ismeup.watcher.implementations.checker;

import net.ismeup.watcher.implementations.model.DiskUsageCheckOperationResult;
import net.ismeup.watcher.implementations.model.DiskUsageCheckOperationParameters;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.CheckerMonitor;
import org.json.JSONObject;

public class DiskUsageCheck extends CheckerMonitor  {

    private String disk;
    private int limit;

    private boolean status = false;
    private int usage = 0;

    @Override
    public CheckOperationResult getOperationResult() {
        return new DiskUsageCheckOperationResult(status, usage);
    }

    @Override
    protected String getOperationName() {
        return "disk";
    }

    @Override
    protected void parseResult(String result) {
        usage = Integer.parseInt(result);
        status = usage > 0 && usage < limit;
    }

    @Override
    protected void parseCheckParameters(CheckOperationParameter checkOperationParameter) {
        disk = ((DiskUsageCheckOperationParameters) checkOperationParameter).getDisk();
        limit = ((DiskUsageCheckOperationParameters) checkOperationParameter).getLimit();
    }

    @Override
    protected JSONObject getRequestJson() {
        return new JSONObject().put("disk", disk);
    }
}
