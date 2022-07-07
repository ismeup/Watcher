package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.interfaces.CheckOperationResult;

public class StatusOkCheckOperationResult extends CheckOperationResult {

    public StatusOkCheckOperationResult(boolean status) {
        super(status);
    }
}
