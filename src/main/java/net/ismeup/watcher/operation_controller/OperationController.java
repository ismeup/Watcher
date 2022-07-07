package net.ismeup.watcher.operation_controller;

import org.json.JSONObject;
import net.ismeup.watcher.exceptions.OperationParseException;
import net.ismeup.watcher.implementations.checker.*;
import net.ismeup.watcher.implementations.model.*;
import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.Checker;
import net.ismeup.watcher.interfaces.JsonableAnswer;
import net.ismeup.watcher.operation_controller.model.OperationData;
import net.ismeup.watcher.operation_controller.model.Operations;

public class OperationController {

    public JsonableAnswer startCheck(JSONObject jsonObject) {
        JsonableAnswer operationResult;
        try {
            OperationData operationData = createOperation(jsonObject);
            operationResult = executeOperation(operationData);
        } catch (OperationParseException e) {
            operationResult = new FailOperationResult();
        }
        return operationResult;
    }

    public OperationData createOperation(JSONObject jsonObject) throws OperationParseException {
        OperationData operationInformation;
        try {
            String operation = jsonObject.getString("operation");
            Operations type;
            switch (operation) {
                case ("ping") :
                    type = Operations.PING;
                    break;
                case ("load_time") :
                    type = Operations.LOAD_TIME;
                    break;
                case ("port_check") :
                    type = Operations.PORT_OPEN;
                    break;
                case ("cert_check") :
                    type = Operations.CERTIFICATE_OK;
                    break;
                case ("status_ok") :
                    type = Operations.STATUS_OK;
                    break;
                case ("content_check") :
                    type = Operations.CONTENT_CHECK;
                    break;
                case ("mem") :
                    type = Operations.MEMORY;
                    break;
                case ("uptime") :
                    type = Operations.UPTIME;
                    break;
                case ("loadavg") :
                    type = Operations.LOAD_AVERAGE;
                    break;
                case ("disk") :
                    type = Operations.DISK_USAGE;
                    break;
                default:
                    throw new OperationParseException();
            }
            CheckOperationParameter operationParamaters;
            Checker checker;
            switch (type) {
                case PING:
                    operationParamaters = new PingCheckOperationParameters();
                    checker = new PingCheck();
                    break;
                case LOAD_TIME:
                    operationParamaters = new LoadTimeOperationParameters();
                    checker = new LoadTimeChecker();
                    break;
                case PORT_OPEN:
                    operationParamaters = new PortCheckOperationParameter();
                    checker = new PortChecker();
                    break;
                case CERTIFICATE_OK:
                    operationParamaters = new CertificateCheckParameter();
                    checker = new CertificateCheck();
                    break;
                case STATUS_OK:
                    operationParamaters = new StatusOkCheckOperationParameter();
                    checker = new StatusOkChecker();
                    break;
                case CONTENT_CHECK:
                    operationParamaters = new ContentCheckOperationParameter();
                    checker = new ContentChecker();
                    break;
                case MEMORY:
                    operationParamaters = new MemoryCheckOperationParameter();
                    checker = new MemoryCheck();
                    break;
                case UPTIME:
                    operationParamaters = new UptimeCheckOperationParameter();
                    checker = new UptimeCheck();
                    break;
                case LOAD_AVERAGE:
                    operationParamaters = new LoadAverageOperationParameter();
                    checker = new LoadAverageCheck();
                    break;
                case DISK_USAGE:
                    operationParamaters = new DiskUsageCheckOperationParameters();
                    checker = new DiskUsageCheck();
                    break;
                default:
                    throw new OperationParseException();
            }
            operationParamaters.fromJson(jsonObject.getJSONObject("data"));
            operationInformation = new OperationData(type, operationParamaters, checker);
        } catch (Exception e) {
            throw new OperationParseException();
        }
        return operationInformation;
    }

    private JsonableAnswer executeOperation(OperationData operationData) throws OperationParseException {
        operationData.getChecker().runCheck(operationData.getData());
        return operationData.getChecker().getOperationResult();
    }
}
