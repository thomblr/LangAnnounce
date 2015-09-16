package me.thomblr.announce.command;

import com.sk89q.minecraft.util.commands.*;
import org.bukkit.command.CommandSender;

public class AnnounceCommand {

    @Command(aliases = { "list" }, desc = "List all the localized messages")
    @CommandPermissions({ "announce.list", "announce.*" })
    public static void list(CommandContext cmd, CommandSender sender) throws CommandException {

    }

    @Command(aliases = "add", desc = "Add a new localized message", usage = "<locale> <message>", min = 2)
    @CommandPermissions({ "announce.add", "announce.*" })
    public static void add(CommandContext cmd, CommandSender sender) throws CommandException {

    }

    @Command(aliases = { "remove", "delete", "del" }, desc = "Remove a localized message", usage = "<locale> <id>", min = 2, max = 2)
    @CommandPermissions({ "announce.remove", "announce.*" })
    public static void remove(CommandContext cmd, CommandSender sender) throws CommandException {

    }

    @Command(aliases = { "setprefix" }, desc = "Modify the current prefix of localized messages", usage = "<prefix>", min = 1, max = 1)
    @CommandPermissions({ "announce.prefix", "announce.*" })
    public static void prefix(CommandContext cmd, CommandSender sender) throws CommandException {

    }

    @Command(aliases = { "broadcast", "bc" }, desc = "Broadcast manually a localized message", usage = "<id>", min = 1, max = 1)
    @CommandPermissions({ "announce.broadcast", "announce.*" })
    public static void broadcast(CommandContext cmd, CommandSender sender) throws CommandException {

    }

    public static class AnnounceParentCommand {
        @Command(aliases = { "announce", "ann" }, desc = "Manage the localized announcements.", min = 1)
        @NestedCommand({AnnounceCommand.class})
        public static void announce(final CommandContext cmd, CommandSender sender) throws CommandException { }
    }

}
