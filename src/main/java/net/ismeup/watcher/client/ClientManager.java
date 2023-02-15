package net.ismeup.watcher.client;

import javax.crypto.Cipher;
import java.util.ArrayList;

public class ClientManager {
    private ArrayList<RemoteClient> remoteClients = new ArrayList<>();
    private ConnectionData connectionData;
    private String identity;
    private Cipher cipher;
    private Object listSynchronizer = new Object();

    private int threadsRequested = 0;

    public ClientManager(ConnectionData connectionData, String identity, Cipher cipher) {
        this.connectionData = connectionData;
        this.identity = identity;
        this.cipher = cipher;
        start();
        disconnectByKeepAliveTimeOut();
    }

    private void start() {
        RemoteClient mainThread = new RemoteClient(this, connectionData, identity, cipher);
        new Thread(mainThread).start();
    }

    public void addThread(RemoteClient remoteClient) {
        synchronized (listSynchronizer) {
            remoteClients.add(remoteClient);
            printThreadsCount();
        }
    }

    private void printThreadsCount() {
        System.out.println("### Current thread count: " + remoteClients.size());
    }

    public void removeThread(RemoteClient remoteClient) {
        synchronized (listSynchronizer) {
            remoteClients.remove(remoteClient);
            threadsRequested--;
            if (remoteClients.size() == 0) {
                try {
                    System.out.println("### No threads online. Reconnecting in 5 seconds");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                start();
            }
        }
    }

    public void requestThreads(int count) {
        synchronized (listSynchronizer) {
            if (count != threadsRequested) {
                threadsRequested = count;
                int size = remoteClients.size();
                if (size < count) {
                    System.out.println("### Server requested more threads. Current count is " + size + "; Requested: " + count);
                    for (int i = size; i < count; i++) {
                        new Thread(new RemoteClient(this, connectionData, identity, cipher)).start();
                    }
                }
            }
        }
    }


    public String getThread(RemoteClient remoteClient) {
        return remoteClients.indexOf(remoteClient) + "";
    }

    private void disconnectByKeepAliveTimeOut() {
        new Thread( () -> {
            while (true) {
                synchronized (listSynchronizer) {
                    for (RemoteClient remoteClient : remoteClients) {
                        remoteClient.disconnectByKeepAlive();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        } ).start();
    }
}
