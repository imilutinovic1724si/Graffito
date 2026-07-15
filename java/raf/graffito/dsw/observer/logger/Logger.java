package raf.graffito.dsw.observer.logger;

import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;

public abstract class Logger implements ISubscriber {

    // ima ulogu Subscriber-a

    @Override
    public void update(Message message) {
        log(message);
    }

    // console i file Logger implementiraju ovu metodu na svoj nacin
    protected abstract void log(Message message);



}
