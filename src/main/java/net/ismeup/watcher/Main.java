package net.ismeup.watcher;

import net.ismeup.watcher.entry_controllers.RegistrationController;
import net.ismeup.watcher.entry_controllers.RunController;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--register")) {
            new RegistrationController().run(args);
        } else {
            new RunController().run(args);
        }
    }


}
