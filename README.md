# Watcher
Watcher - is agent application for monitoring your server or network.
You need Watcher in next cases:
- If you want to check availability of hosts in your network, that doesn't have a real IP-address
- If you want use Monitor, but don't wan't to have any open port on your server
Watcher connects to IsMeUp's server by encrypted channel and gets from IsMeUp check tasks.
Watcher needs installed Java 1.8 or above

## Building from source
The best way to build executable jar-file is using Maven:

    git clone https://github.com/ismeup/Watcher.git
    cd Watcher
    mvn assembly:assembly

if you don't wan't to build Watcher yourself, you can download compiled version, which is available here: https://github.com/ismeup/Watcher/releases

## Running
For running Watcher you need, in addition to Watcher executable, also a file called identity.key, which provides your secret key for Watcher. 
To get this key, you need to go to your Client Area, open Servers page, switch to Watchers tab, click "+" button and provide Watcher's name.
You also can get identity.key file in standalone mode by running Watcher with `--register` argument:

    java -jar Watcher.jar --register

After that you will get identity.key file and put it in same directory with Watcher.jar and start Watcher with command:

    java -jar Watcher.jar

## Using
All of using of this application is running it on server. All rest of work you will do in your Client Area, by adding check tasks with setting your Watcher as source.
