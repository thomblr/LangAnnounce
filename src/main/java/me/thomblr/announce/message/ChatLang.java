package me.thomblr.announce.message;

import me.thomblr.announce.util.Numbers;

import java.util.List;

public class ChatLang {

    private String locale;
    private List<String> messages;

    public ChatLang(String locale, List<String> messages) {
        this.locale = locale;
        this.messages = messages;
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


}
