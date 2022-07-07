package net.ismeup.watcher.interfaces;

public interface Checker {
    void runCheck(CheckOperationParameter checkOperationParameter);
    CheckOperationResult getOperationResult();
}
