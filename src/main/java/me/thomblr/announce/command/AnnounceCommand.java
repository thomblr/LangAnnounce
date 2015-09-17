package me.thomblr.announce.command;

import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.ChatColor;
import me.thomblr.announce.Main;
import me.thomblr.announce.message.ChatLang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jdom2.Element;

public class AnnounceCommand {

    @Command(aliases = { "list" }, desc = "List all the localized messages", flags = "a")
    @CommandPermissions({ "announce.list", "announce.*" })
    public static void list(CommandContext cmd, CommandSender sender) throws CommandException {
        sender.sendMessage("§7§m---------§6 Localized Messages §7§m---------");
        for (ChatLang lang : Main.get().getChatHandler().getLangs()) {
            if (cmd.hasFlag('a')) {
                    sender.sendMessage("§7--- §e" + lang.getLocale().toUpperCase() + " §7---");
                int start = 1;
                for (String msg : lang.getMessages()) {
                    sender.sendMessage(ChatColor.YELLOW.toString() + start + " §7- " + lang.buildMessage(lang.getMessages().get(start - 1), false));
                    start ++;
                }
            } else {
                int size = lang.getMessages().size();
                sender.sendMessage(ChatColor.YELLOW + lang.getLocale().toUpperCase() + "§7: §e" + size + " §7message" + (size != 1 ? "s" : "") + ".");
            }
        }
    }

    @Command(aliases = "add", desc = "Add a new localized message", usage = "<locale> <message>", min = 2)
    @CommandPermissions({ "announce.add", "announce.*" })
    public static void add(CommandContext cmd, CommandSender sender) throws CommandException {
        ChatLang lang = Main.get().getChatHandler().getLang(cmd.getString(0));
        if (lang != null) {
            String message = cmd.getJoinedStrings(1);
            lang.getDocument().getRootElement().addContent(new Element("message").setText(message));
            lang.getMessages().add(message);
            sender.sendMessage(ChatColor.GREEN + "Your message has been added to the document.");
        }
    }

    @Command(aliases = { "remove", "delete", "del" }, desc = "Remove a localized message", usage = "<locale> <id>", min = 2, max = 2)
    @CommandPermissions({ "announce.remove", "announce.*" })
    public static void remove(CommandContext cmd, CommandSender sender) throws CommandException {
        ChatLang lang = Main.get().getChatHandler().getLang(cmd.getString(0));
        if (lang != null) {
            int id = cmd.getInteger(1);
            if (id > 0 || id <= lang.getMessages().size()) {
                lang.getMessages().remove(id - 1);
                lang.getDocument().getRootElement().getChildren("message").remove(id - 1);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid ID provided.");
            }
        }
    }

    @Command(aliases = { "setprefix" }, desc = "Modify the current prefix of localized messages", usage = "<prefix>", min = 1, max = 1)
    @CommandPermissions({ "announce.prefix", "announce.*" })
    public static void prefix(CommandContext cmd, CommandSender sender) throws CommandException {
        Main.get().getConfig().set("settings.prefix", cmd.getString(0));
        sender.sendMessage(ChatColor.GREEN + "You have successfully changed the prefix.");
    }

    @Command(aliases = { "broadcast", "bc" }, desc = "Broadcast manually a localized message", usage = "<locale> <id>", min = 2, max = 2)
    @CommandPermissions({ "announce.broadcast", "announce.*" })
    public static void broadcast(CommandContext cmd, CommandSender sender) throws CommandException {
        ChatLang lang = Main.get().getChatHandler().getLang(cmd.getString(0).toLowerCase());
        if (lang != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocale().equals(lang.getLocale())) {
                    p.sendMessage(lang.buildMessage(lang.getMessages().get(cmd.getInteger(1)), true));
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Message broadcasted.");
        } else sender.sendMessage(ChatColor.RED + "Unable to find this language.");
    }

    public static class AnnounceParentCommand {
        @Command(aliases = { "announce", "ann" }, desc = "Manage the localized announcements.", min = 1)
        @NestedCommand({AnnounceCommand.class})
        public static void announce(final CommandContext cmd, CommandSender sender) throws CommandException { }
    }

}
