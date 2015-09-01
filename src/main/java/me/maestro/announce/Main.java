package me.maestro.announce;

import me.maestro.announce.util.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        Log.debug("Plugin enabled");
    }

    @Override
    public void onDisable() {
        Log.debug("Plugin disabled");
        instance = null;
    }

    public static Main get() {
        return instance;
    }

}
