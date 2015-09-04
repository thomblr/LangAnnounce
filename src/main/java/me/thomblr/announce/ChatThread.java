package me.thomblr.announce;

import me.thomblr.announce.util.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatThread extends Thread {

    private int lastAnnounceIndex;

    public ChatThread() {
        lastAnnounceIndex = 0;
    }

    public void run() {
        if (Main.get().isAnnouncerEnabled()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                int size = Main.get().getChatHandler().getLang(p.getLocale()).getMessages().size();
                if (Main.get().isAnnouncerRandom()) {
                    p.sendMessage(buildMessage(Main.get().getChatHandler().getLang(p.getLocale()).getRandomMessage()));
                } else {
                    if (++lastAnnounceIndex >= size) {
                        lastAnnounceIndex = 0;
                    }
                    if (lastAnnounceIndex < size) {
                        p.sendMessage(buildMessage(Main.get().getChatHandler().getLang(p.getLocale()).getMessages().get(lastAnnounceIndex)));
                    }
                }
            }
        }
    }

    public BaseComponent buildMessage(String message) {
        BaseComponent prefix = new TextComponent(ChatUtil.convertChatColor(Main.get().getAnnouncerPrefix()));
        prefix.addExtra(" ");
        prefix.addExtra(new TextComponent(ChatUtil.convertChatColor(message)));
        return prefix;
    }

}
