package raf.graffito.dsw.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private String content;
    private MessageType type;
    private LocalDateTime timestamp;

    public Message(String content, MessageType type) {
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // geteri
    public String getContent() {
        return content;
    }
    public MessageType getType() {
        return type;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

     // metoda formatira poruke u sledecem obliku:
     // [ERROR][12.11.2024. 22:56] Workspace ne može biti obrisan.
    public String getFormattedMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedTime = timestamp.format(formatter);
        return String.format("[%s][%s] %s", type, formattedTime, content);
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}
