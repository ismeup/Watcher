package net.ismeup.watcher.implementations.model;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class LoadTimeOperationParameters extends CheckOperationParameter {

    String url;
    float timeLimit;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            url = jsonObject.getString("url");
            timeLimit = jsonObject.getFloat("limit");
        } catch (Exception e) {
            if (url != null) {
                timeLimit = 3;
            } else {
                throw new OperationParseException();
            }
        }

    }

    public String getUrl() {
        return url;
    }

    public float getTimeLimit() {
        return timeLimit;
    }
}
