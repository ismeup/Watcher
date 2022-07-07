package net.ismeup.watcher.client;

public class ConnectionData {
    private String host;
    private int port;

    public ConnectionData() {
        this("ismeup.net", "8787");
    }

    public ConnectionData(String host, String port) {
        this.host = host;
        int iPort = Integer.parseInt(port);
        this.port = iPort;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}
