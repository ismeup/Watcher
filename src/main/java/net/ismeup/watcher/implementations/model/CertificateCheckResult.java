package net.ismeup.watcher.implementations.model;

import org.json.JSONObject;
import net.ismeup.watcher.interfaces.CheckOperationResult;

public class CertificateCheckResult extends CheckOperationResult {

    private int daysRest;

    public CertificateCheckResult(boolean status, int daysRest) {
        super(status);
        this.daysRest = daysRest;
    }

    @Override
    public JSONObject getAnswer() {
        return new JSONObject().put("status", isStatusOk()).put("daysRest", daysRest);
    }
}
