package me.thomblr.announce.message;

import me.thomblr.announce.Main;
import me.thomblr.announce.util.ChatUtil;
import me.thomblr.announce.util.Numbers;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatLang extends Thread {

    private String locale;
    private List<String> messages;
    private int lastMessageIndex;

    public ChatLang(String locale, List<String> messages) {
        this.locale = locale;
        this.messages = messages;
        this.lastMessageIndex = 0;
    }

    public String getLocale() {
        return locale;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getRandomMessage() {
        return messages.get(Numbers.getRandom(0, messages.size()));
    }

    public int getLastMessageIndex() {
        return lastMessageIndex;
    }

    public void setLastMessageIndex(int lastMessageIndex) {
        this.lastMessageIndex = lastMessageIndex;
    }

    @Override
    public void run() {
        if (Main.get().isAnnouncerEnabled() && Bukkit.getOnlinePlayers().size() > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                ChatLang lang = Main.get().getChatHandler().getLang(p.getLocale());
                if (lang != null && lang.equals(this)) {
                    if (getMessages().size() > 0) {
                        if (Main.get().isAnnouncerRandom()) {
                            p.sendMessage(buildMessage(lang.getRandomMessage()));
                        } else {
                            if (getLastMessageIndex() >= getMessages().size()) {
                                lang.setLastMessageIndex(0);
                            }
                            if (getLastMessageIndex() < getMessages().size()) {
                                p.sendMessage(buildMessage(lang.getMessages().get(getLastMessageIndex())));
                            }
                        }
                    }
                }
            }

            lastMessageIndex ++;
        }
    }

    public BaseComponent buildMessage(String message) {
        BaseComponent prefix = new TextComponent(ChatUtil.convertChatColor(Main.get().getAnnouncerPrefix()));
        prefix.addExtra(" ");
        prefix.addExtra(new TextComponent(ChatUtil.convertChatColor(message)));
        return prefix;
    }
}
