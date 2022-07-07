package net.ismeup.watcher.interfaces;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;

public interface JsonableParameter {
    void fromJson(JSONObject jsonObject) throws OperationParseException;
}
