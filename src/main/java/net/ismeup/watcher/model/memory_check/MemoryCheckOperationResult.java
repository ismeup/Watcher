package net.ismeup.watcher.model.memory_check;

import net.ismeup.watcher.interfaces.CheckOperationResult;
import org.json.JSONObject;

public class MemoryCheckOperationResult extends CheckOperationResult {

    private int usage;

    public MemoryCheckOperationResult(boolean status, int usage) {
        super(status);
        this.usage = usage;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("usage", usage);
    }
}
