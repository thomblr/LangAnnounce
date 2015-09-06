package me.thomblr.announce.util;

import com.sk89q.minecraft.util.commands.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ChatUtil {

    public static String convertChatColor(String message) {
        return ChatColor.translateAlternateColorCodes('`', message);
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString();
    }

}
