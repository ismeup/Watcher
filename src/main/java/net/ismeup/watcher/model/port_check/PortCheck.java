package net.ismeup.watcher.model.port_check;

import net.ismeup.watcher.interfaces.CheckOperationParameter;
import net.ismeup.watcher.interfaces.CheckOperationResult;
import net.ismeup.watcher.interfaces.Checker;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class PortCheck implements Checker {

    private String host;
    private int port;
    private boolean notEmpty;
    private boolean status = false;

    private void parseConfiguration(PortCheckOperationParameter portCheckOperationParameter) {
        this.host = portCheckOperationParameter.getHost();
        this.port = portCheckOperationParameter.getPort();
        this.notEmpty = portCheckOperationParameter.isNotEmpty();
    }

    @Override
    public void runCheck(CheckOperationParameter checkOperationParameter) {
        parseConfiguration( (PortCheckOperationParameter) checkOperationParameter );
        Socket socket = null;
        InputStream inputStream = null;
        try {
            socket = new Socket(host, port);
            if (notEmpty) {
                socket.setSoTimeout(2000);
                inputStream = socket.getInputStream();

                System.out.println("Читаем вывод из сокета!");
                int c = inputStream.read();
                status = c != -1;
            } else {
                status = true;
            }
        } catch (IOException e) {

        } finally {
            try { inputStream.close(); } catch (Exception e) {}
            try { socket.close(); } catch (Exception e) {}
        }
    }

    @Override
    public CheckOperationResult getOperationResult() {
        return new PortCheckOperationResult(status);
    }
}
