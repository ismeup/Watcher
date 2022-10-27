package net.ismeup.watcher.model.ping_check;

import net.ismeup.watcher.interfaces.CheckOperationResult;

public class PingCheckOperationResult extends CheckOperationResult {

    public PingCheckOperationResult(boolean status) {
        super(status);
    }
}
