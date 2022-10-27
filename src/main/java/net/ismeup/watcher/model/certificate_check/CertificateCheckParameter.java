package net.ismeup.watcher.model.certificate_check;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class CertificateCheckParameter extends CheckOperationParameter {

    private String url;
    private int days;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            url = jsonObject.getString("url");
            days = jsonObject.getInt("days");
        } catch (Exception e) {
            if (url != null) {
                days = 443;
            } else {
                throw new OperationParseException();
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public int getDays() {
        return days;
    }
}
