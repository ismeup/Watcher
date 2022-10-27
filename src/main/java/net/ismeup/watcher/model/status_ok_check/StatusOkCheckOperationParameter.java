package net.ismeup.watcher.model.status_ok_check;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class StatusOkCheckOperationParameter extends CheckOperationParameter {
    private String url;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            url = jsonObject.getString("url");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public String getUrl() {
        return url;
    }
}
