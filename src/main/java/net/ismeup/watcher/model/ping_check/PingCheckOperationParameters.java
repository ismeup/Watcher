package net.ismeup.watcher.model.ping_check;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class PingCheckOperationParameters extends CheckOperationParameter {

    String host;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            host = jsonObject.getString("host");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public String getHost() {
        return host;
    }
}
