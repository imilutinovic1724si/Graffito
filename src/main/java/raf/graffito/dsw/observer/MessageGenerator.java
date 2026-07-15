package raf.graffito.dsw.observer;

import java.util.ArrayList;
import java.util.List;

public class MessageGenerator implements IPublisher{

    // MessageGenerator ima ulogu Publisher-a

    private List<ISubscriber> subscribers;

    public MessageGenerator() {
        this.subscribers = new ArrayList<>();
    }

    // implementiramo metode iz interfejsa
    @Override
    public void addSubscriber(ISubscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }
    @Override
    public void removeSubscriber(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void notifySubscribers(Message message) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(message);
        }
    }
    // Metoda koja generise i objavljuje poruku za GRESKA
    public void generateError(String content) {
        Message message = new Message(content, MessageType.ERROR);
        notifySubscribers(message);
    }
    // Metoda koje generise i objavljuje poruku za UPOZORENJE
    public void generateWarning(String content) {
        Message message = new Message(content, MessageType.WARNING);
        notifySubscribers(message);
    }
    // Metofa koje generise i objavljuje informaciju
    public void generateInfo(String content) {
        Message message = new Message(content, MessageType.INFO);
        notifySubscribers(message);
    }

}
