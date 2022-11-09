package net.ismeup.watcher.entry_controllers;

import net.ismeup.apiclient.exceptions.ConnectionFailException;
import net.ismeup.apiclient.model.ApiResult;
import net.ismeup.watcher.Main;
import net.ismeup.apiclient.controller.ApiConnector;
import net.ismeup.apiclient.controller.CmdLineInput;
import net.ismeup.apiclient.controller.OneTimeTokenStorage;
import net.ismeup.apiclient.model.ApiConnectionData;
import net.ismeup.apiclient.model.LoginData;
import net.ismeup.apiclient.model.TokenStorage;
import net.ismeup.watcher.model.ServerWatcher;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class RegistrationController {
    public void run(String[] args) {
        ApiConnectionData connectionData = null;
        if (args.length == 2) {
            try {
                connectionData = ApiConnectionData.parse(args[1]);
            } catch (MalformedURLException e) {
                System.out.println("Can't parse URL");
                System.exit(1);
            }
        } else {
            connectionData = ApiConnectionData.defaultUrl();
        }
        if (!getIdentityFile().exists()) {
            TokenStorage tokenStorage = new OneTimeTokenStorage();
            ApiConnector apiConnector = new ApiConnector(connectionData, tokenStorage, false);
            CmdLineInput cmdLineInput = new CmdLineInput();
            LoginData loginData = cmdLineInput.authenticate("Watcher", 3600);
            try {
                if (apiConnector.authenticate(loginData)) {
                    System.out.println("Logged in");
                    String newWatcherName = cmdLineInput.requestString("Enter name for new Watcher");
                    ServerWatcher serverWatcher = ServerWatcher.createByName(newWatcherName);
                    ApiResult apiResult = apiConnector.postOperation("server_watchers", "generate", new JSONObject().put("serverWatcher", serverWatcher.toJson()));
                    if (apiResult.isOk()) {
                        try {
                            ServerWatcher newWatcher = ServerWatcher.fromJson(apiResult.getAnswer().getJSONObject("watcher"));
                            if (newWatcher.getKey() != null && !newWatcher.getKey().isEmpty()) {
                                System.out.println("Watcher registered! Key is: " + newWatcher.getKey());
                                System.out.print("Creating identity.key file...");
                                boolean isOk = saveIdentity(newWatcher.getKey());
                                System.out.println(isOk ? "OK" : "FAIL");
                                if (!isOk) {
                                    System.out.println("File identity.key is not created! Check directory permissions");
                                } else {
                                    System.out.println("File identity.key created. Now you can run Watcher as usual!");
                                }
                            } else {
                                System.out.println("Unknown error!");
                            }
                        } catch (JSONException e) {
                            System.out.println("Something went wrong. Try again");
                        }
                    }
                } else {
                    System.out.println("Login or password mismatch");
                }
            } catch (ConnectionFailException e) {

            }
        } else {
            System.out.println("identity.key file exists. Remove it first, if you want to register new Watcher, or run Watcher without --register argument");
        }
    }

    private File getIdentityFile() {
        File file = null;
        try {
            String fileName = "identity.key";
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String inputFilePath = jarFile.getParent() + File.separator + fileName;

            file = new File(inputFilePath);
        } catch (URISyntaxException e) {

        }
        return file;
    }

    private boolean saveIdentity(String key) {
        boolean ok = false;
        try {
            FileOutputStream outputStream = new FileOutputStream(getIdentityFile());
            outputStream.write(key.getBytes());
            outputStream.close();
            ok = true;
        } catch (Exception e) {

        }
        return ok;
    }
}
