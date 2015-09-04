package me.thomblr.announce.util;

import com.sk89q.minecraft.util.commands.ChatColor;

public class ChatUtil {

    public static String convertChatColor(String message) {
        return ChatColor.translateAlternateColorCodes('`', message);
    }

}
