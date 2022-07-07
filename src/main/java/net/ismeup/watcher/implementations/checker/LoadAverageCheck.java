package net.ismeup.watcher.implementations.checker;

import net.ismeup.watcher.implementations.model.LoadAverageOperationParameter;
import net.ismeup.watcher.implementations.model.LoadAverageOperationResult;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.CheckerMonitor;

public class LoadAverageCheck extends CheckerMonitor  {

    private LoadAverageType loadAverageType;
    private double limit;
    private double gotLoadAverage = 0;
    private boolean status = false;

    @Override
    public CheckOperationResult getOperationResult() {
        return new LoadAverageOperationResult(status, gotLoadAverage);
    }

    @Override
    protected String getOperationName() {
        switch (loadAverageType) {
            case LA_1: return "la_1";
            case LA_5: return "la_5";
            case LA_15: return "la_15";
        }
        return "la_1";
    }

    @Override
    protected void parseResult(String result) {
        gotLoadAverage = Double.parseDouble(result);
        status = gotLoadAverage >= 0 && gotLoadAverage <= limit;
    }

    @Override
    protected void parseCheckParameters(CheckOperationParameter checkOperationParameter) {
        loadAverageType = ( (LoadAverageOperationParameter) checkOperationParameter).getType();
        limit = ( (LoadAverageOperationParameter) checkOperationParameter ).getLimit();
    }

    public static enum LoadAverageType {
        LA_1,
        LA_5,
        LA_15;

        public static int getIntFromType(LoadAverageType type) {
            switch (type) {
                case LA_1: return 1;
                case LA_5: return 2;
                case LA_15: return 3;
            }
            return 1;
        }

        public static LoadAverageType getTypeFromInt(int type) {
            switch (type) {
                case 1: return LA_1;
                case 2: return LA_5;
                case 3: return LA_15;
            }
            return LA_1;
        }
    }
}
