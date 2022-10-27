package net.ismeup.watcher.model.ping_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;

import java.io.IOException;

public class PingCheck implements Checker {

    static final int MAX_FAIL_CHECK = 3;
    String host;
    boolean status;
    int failCounter = 0;

    private void parseConfiguration(PingCheckOperationParameters pingCheckOperationParameters) {
        this.host = pingCheckOperationParameters.getHost();
    }

    @Override
    public void runCheck(CheckOperationParameter checkOperationParameter) {
        parseConfiguration( (PingCheckOperationParameters) checkOperationParameter);
        if (!status && failCounter < MAX_FAIL_CHECK) {
            Process process;
            try {
                if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                    process = Runtime.getRuntime().exec("ping -n 1 " + host);
                } else {
                    process = Runtime.getRuntime().exec("ping -c 1 " + host);
                }

                process.waitFor();
                status = process.exitValue() == 0;
            } catch (IOException | InterruptedException e) {
                status = false;
            }
            if (!status) {
                failCounter++;
                runCheck(checkOperationParameter);
            }
        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new PingCheckOperationResult(status);
    }
}
