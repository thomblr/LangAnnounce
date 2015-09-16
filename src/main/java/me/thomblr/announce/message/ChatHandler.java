package me.thomblr.announce.message;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.thomblr.announce.Main;
import me.thomblr.announce.util.Log;
import org.apache.commons.io.FileUtils;
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
    private final Set<ChatLang> langs;

    public ChatHandler() throws JDOMException, IOException {
        this.documents = Sets.newHashSet();
        this.langs = Sets.newHashSet();
        SAXBuilder builder = new SAXBuilder();

        File messages = new File(Main.get().getDataFolder(), "messages");

        if (!messages.exists()) {
            messages.mkdir();
        }

        File[] files = { new File(messages, "en.xml"), new File(messages, "fr.xml") };
        for (File file : files) {
            if (!file.exists()) {
                try {
                    FileUtils.copyInputStreamToFile(Main.get().getResource("messages/" + file.getName()), file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
            ChatLang lang = new ChatLang(locale, messages);
            langs.add(lang);
            Log.info("Added " + lang.getLocale() + " with " + lang.getMessages().size() + " messages.");
        }
        Log.info("Builded " + langs.size() + " languages.");
        return this;
    }

    public Set<ChatLang> getLangs() {
        return langs;
    }

    public ChatLang getLang(String locale) {
        locale = locale.split("_")[0];
        for (ChatLang lang : getLangs()) {
            if (locale.equals(lang.getLocale())) return lang;
        }
        return Main.get().getDefaultLang();
    }

}
