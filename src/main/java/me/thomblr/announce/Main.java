package me.thomblr.announce;

import me.thomblr.announce.message.ChatHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private ChatHandler chatHandler;

    private boolean announcerEnabled;
    private boolean announcerRandom;
    private String announcerPrefix;
    private int announcerInterval;

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdir();

        try {
            chatHandler = new ChatHandler().buildMessages();
        } catch(JDOMException | IOException e) {
            e.printStackTrace();
        }

        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();

        registerSettings();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ChatThread(), getAnnouncerInterval() * 20L, getAnnouncerInterval() * 20L);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main get() {
        return instance;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public boolean isAnnouncerEnabled() {
        return announcerEnabled;
    }

    public boolean isAnnouncerRandom() {
        return announcerRandom;
    }

    public String getAnnouncerPrefix() {
        return announcerPrefix;
    }

    public int getAnnouncerInterval() {
        return announcerInterval;
    }

    private void registerSettings() {
        FileConfiguration config = getConfig();
        if (config.contains("settings")) {
            this.announcerEnabled = config.getBoolean("settings.enabled", true);
            this.announcerRandom = config.getBoolean("settings.random", false);
            this.announcerPrefix = config.getString("settings.prefix", "[LangAnnounce]");
            this.announcerInterval = config.getInt("settings.interval", 120);
        }
    }

}
