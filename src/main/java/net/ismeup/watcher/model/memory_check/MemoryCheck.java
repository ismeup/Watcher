package net.ismeup.watcher.model.memory_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.CheckerMonitor;

public class MemoryCheck extends CheckerMonitor {

    private int limit;
    private boolean status = false;
    private int gotValue = 0;

    @Override
    public CheckOperationResult getOperationResult() {
        return new MemoryCheckOperationResult(status, gotValue);
    }

    @Override
    protected String getOperationName() {
        return "mem";
    }

    @Override
    protected void parseResult(String result) {
        int value = Integer.parseInt(result);
        gotValue = value;
        status = gotValue <= limit;
    }

    @Override
    protected void parseCheckParameters(CheckOperationParameter checkOperationParameter) {
        limit = ((MemoryCheckOperationParameter) checkOperationParameter).getLimit();
    }
}
