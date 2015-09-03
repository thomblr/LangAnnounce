package me.thomblr.announce;

import me.thomblr.announce.message.ChatHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private ChatHandler chanHandler;

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdir();

        try {
            chanHandler = new ChatHandler().buildMessages();
        } catch(JDOMException | IOException e) {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main get() {
        return instance;
    }

    public ChatHandler getChanHandler() {
        return chanHandler;
    }

}
