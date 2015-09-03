package me.thomblr.announce.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ChatUtil {

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString();
    }

}
