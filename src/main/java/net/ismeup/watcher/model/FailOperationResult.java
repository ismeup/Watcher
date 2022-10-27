package net.ismeup.watcher.model;

import net.ismeup.watcher.interfaces.CheckOperationResult;

public class FailOperationResult extends CheckOperationResult {
    public FailOperationResult() {
        super(false);
    }
}
