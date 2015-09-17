package me.thomblr.announce.message;

import me.thomblr.announce.Main;
import me.thomblr.announce.util.ChatUtil;
import me.thomblr.announce.util.Numbers;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jdom2.Document;

import java.util.List;

public class ChatLang extends Thread {

    private Document document;
    private String locale;
    private List<String> messages;
    private int lastMessageIndex;

    public ChatLang(Document document, String locale, List<String> messages) {
        this.document = document;
        this.locale = locale;
        this.messages = messages;
        this.lastMessageIndex = 0;
    }

    public Document getDocument() {
        return document;
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
                            p.sendMessage(buildMessage(lang.getRandomMessage(), true));
                        } else {
                            if (getLastMessageIndex() >= getMessages().size()) {
                                lang.setLastMessageIndex(0);
                            }
                            if (getLastMessageIndex() < getMessages().size()) {
                                p.sendMessage(buildMessage(lang.getMessages().get(getLastMessageIndex()), true));
                            }
                        }
                    }
                }
            }

            lastMessageIndex ++;
        }
    }

    public BaseComponent buildMessage(String message, boolean prefix) {
        BaseComponent msg = new TextComponent();
        if (prefix) {
            msg.addExtra(new TextComponent(ChatUtil.convertChatColor(Main.get().getAnnouncerPrefix())));
            msg.addExtra(" ");
        }
        msg.addExtra(new TextComponent(ChatUtil.convertChatColor(message)));
        return msg;
    }
}
