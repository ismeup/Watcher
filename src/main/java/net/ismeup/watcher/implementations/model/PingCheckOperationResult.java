package net.ismeup.watcher.implementations.model;

import net.ismeup.watcher.interfaces.CheckOperationResult;

public class PingCheckOperationResult extends CheckOperationResult {

    public PingCheckOperationResult(boolean status) {
        super(status);
    }
}
