package raf.graffito.dsw.observer;

public interface IPublisher {

    // implementirano po sablonu sa casa

    void addSubscriber(ISubscriber subscriber);

    void removeSubscriber(ISubscriber subscriber);

    void notifySubscribers(Message message);
}
