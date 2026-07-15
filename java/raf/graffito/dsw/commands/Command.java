package raf.graffito.dsw.commands;

public interface Command {

    // primena: Command Pattern
    void execute();
    void undo();
}
