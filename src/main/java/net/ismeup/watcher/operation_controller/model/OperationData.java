package net.ismeup.watcher.operation_controller.model;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.Checker;

public class OperationData {
    public Operations type;
    public CheckOperationParameter data;
    public Checker checker;

    public OperationData(Operations type, CheckOperationParameter data, Checker checker) {
        this.type = type;
        this.data = data;
        this.checker = checker;
    }

    public Operations getType() {
        return type;
    }

    public CheckOperationParameter getData() {
        return data;
    }

    public Checker getChecker() {
        return checker;
    }
}
