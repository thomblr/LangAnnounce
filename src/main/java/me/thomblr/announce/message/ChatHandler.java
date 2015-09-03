package me.thomblr.announce.message;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.thomblr.announce.Main;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ChatHandler {

    private final Set<Document> documents;
    private final Set<ChatLang> langs = Sets.newHashSet();

    public ChatHandler() throws JDOMException, IOException {
        this.documents = Sets.newHashSet();
        SAXBuilder builder = new SAXBuilder();

        File messages = new File(Main.get().getDataFolder(), "messages");

        if (!messages.exists()) {
            messages.mkdir();
        }

        for (File file : messages.listFiles()) {
            if (file.getName().endsWith(".xml")) {
                documents.add(builder.build(file));
            }
        }
    }

    public ChatHandler buildMessages() {
        for (Document document : documents) {
            Element root = document.getRootElement();
            String locale = root.getAttributeValue("lang");
            List<String> messages = Lists.newArrayList();
            for (Element child : root.getChildren("message")) {
                if (child.getAttributeValue("value") != null) {
                    messages.add(child.getAttributeValue("value"));
                } else {
                    messages.add(child.getText());
                }
            }
            langs.add(new ChatLang(locale, messages));
        }
        return this;
    }

    public Document getMessagesDoc(String locale) {
        for (Document doc : documents) {
            if (locale.equals(doc.getRootElement().getAttributeValue("lang"))) {
                return doc;
            }
        }
        return getMessagesDoc("en");
    }

    public Set<ChatLang> getLangs() {
        return langs;
    }

    public ChatLang getLang(String locale) {
        locale = locale.split("_")[0];
        for (ChatLang lang : getLangs()) {
            if (locale.equals(lang.getLocale())) return lang;
        }
        return getLang("en");
    }

}
