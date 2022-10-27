package net.ismeup.watcher.model.load_average_check;

import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.interfaces.MonitorCheckOperationParameter;
import org.json.JSONObject;

public class LoadAverageOperationParameter extends MonitorCheckOperationParameter  {

    private LoadAverageCheck.LoadAverageType type;
    private double limit;

    @Override
    public void fromJsonMonitor(JSONObject jsonObject) throws OperationParseException {
        try {
            type = LoadAverageCheck.LoadAverageType.getTypeFromInt(jsonObject.getInt("type"));
            limit = jsonObject.getDouble("limit");
        } catch (Exception e) {
            throw new OperationParseException();
        }
    }

    public LoadAverageCheck.LoadAverageType getType() {
        return type;
    }

    public double getLimit() {
        return limit;
    }
}
