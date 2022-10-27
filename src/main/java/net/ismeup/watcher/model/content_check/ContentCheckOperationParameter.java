package net.ismeup.watcher.model.content_check;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.CheckOperationParameter;

public class ContentCheckOperationParameter extends CheckOperationParameter  {

    private String url;
    private String text;
    private boolean mustContain;

    @Override
    public void fromJson(JSONObject jsonObject) throws OperationParseException {
        try {
            this.url = jsonObject.getString("url");
            this.text = jsonObject.getString("text");
            this.mustContain = jsonObject.getBoolean("must_contain");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public boolean isMustContain() {
        return mustContain;
    }
}
