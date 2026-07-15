package raf.graffito.dsw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.graffito.dsw.observer.IPublisher;
import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.observer.MessageType;

import java.util.ArrayList;
import java.util.List;

public abstract class GraffNode implements IPublisher {

    private String name;
    @JsonIgnore
    private GraffNode parent;
    @JsonIgnore
    private List<ISubscriber> subscribers;

    public GraffNode(String name, GraffNode parent) {
        this.name = name;
        this.parent = parent;
        this.subscribers = new ArrayList<>();
    }

    // Metode za Composite
    public void addChild(GraffNode child) {
        throw new UnsupportedOperationException("Leaf cvor ne moze imati decu");
    }
    public void removeChild(GraffNode child) {
        throw new UnsupportedOperationException("Leaf cvor ne moze imati decu");
    }
    public abstract GraffNode findByName(String name);

    // geteri i seteri
    public GraffNode getParent() {
        return parent;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;

        notifySubscribers(new Message(
                "Cvor preimenovan iz '"+oldName+"' u '"+name+"'",
                MessageType.INFO
                ));
    }
    public void setParent(GraffNode parent) {
        this.parent = parent;
    }


    //Metode implementirane iz IPublisher-a
    @Override
    public void addSubscriber(ISubscriber subscriber) {
        if (subscriber!= null && !subscribers.contains(subscriber)) {
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

        if (parent != null) {
            parent.notifySubscribers(message);
        }
    }

    // geteri i toString
    public List<ISubscriber> getSubscribers() {
        return subscribers;
    }
    @Override
    public String toString() {
        return name;
    }
}
