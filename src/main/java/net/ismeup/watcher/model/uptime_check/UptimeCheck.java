package net.ismeup.watcher.model.uptime_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.CheckerMonitor;

public class UptimeCheck extends CheckerMonitor  {

    private int remoteUptime = 0;
    private int requestUptime = 0;
    private boolean status = false;

    @Override
    public CheckOperationResult getOperationResult() {
        return new UptimeCheckOperationResult(status, remoteUptime);
    }

    @Override
    protected String getOperationName() {
        return "uptime";
    }

    @Override
    protected void parseResult(String result) {
        int value = Integer.parseInt(result);
        remoteUptime = value;
        status = remoteUptime > requestUptime;
    }

    @Override
    protected void parseCheckParameters(CheckOperationParameter checkOperationParameter) {
        requestUptime = ((UptimeCheckOperationParameter) checkOperationParameter).getUptime();
    }
}
