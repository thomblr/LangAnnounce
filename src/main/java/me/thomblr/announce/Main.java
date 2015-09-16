package me.thomblr.announce;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.thomblr.announce.command.AnnounceCommand;
import me.thomblr.announce.message.ChatHandler;
import me.thomblr.announce.message.ChatLang;
import me.thomblr.announce.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private ChatHandler chatHandler;
    private CommandsManager<CommandSender> commands;

    private boolean announcerEnabled;
    private boolean announcerRandom;
    private String announcerPrefix;
    private int announcerInterval;
    private ChatLang defolt;

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
        setupCommands();
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

    public ChatLang getDefaultLang() {
        return defolt;
    }

    private void registerSettings() {
        FileConfiguration config = getConfig();
        if (config.contains("settings")) {
            this.announcerEnabled = config.getBoolean("settings.enabled", true);
            this.announcerRandom = config.getBoolean("settings.random", false);
            this.announcerPrefix = config.getString("settings.prefix", "[LangAnnounce]");
            this.announcerInterval = config.getInt("settings.interval", 120);
            this.defolt = getChatHandler().getLang(config.getString("settings.default", "en"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        String locale = ChatUtil.getLocale(sender);
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage().replace("{cmd}", cmd.getName()));
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An unknown error has occurred. Please refer to the server console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(AnnounceCommand.AnnounceParentCommand.class);
    }

}
