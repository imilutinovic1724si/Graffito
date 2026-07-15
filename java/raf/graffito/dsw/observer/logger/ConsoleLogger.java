package raf.graffito.dsw.observer.logger;

import raf.graffito.dsw.observer.Message;

public class ConsoleLogger extends Logger{
    @Override
    protected void log(Message message) {
        System.out.println(message.getFormattedMessage());
    }
}
