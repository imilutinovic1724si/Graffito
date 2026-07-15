package raf.graffito.dsw.commands;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> history = new Stack<>(); // stek za Undo
    private Stack<Command> redoStack = new Stack<>(); // stek za Redo

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();
        System.out.println("Komanda izvršena. History size: " + history.size());
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
            System.out.println("Undo izvršen. History size: " + history.size());
        }
        else {
            System.out.println("Nema akcija za Undo.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
            System.out.println("Redo izvršen. History size: " + history.size());
        }
        else {
            System.out.println("Nema akcija za Redo.");
        }
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    public void clear(){
        history.clear();
        redoStack.clear();
        System.out.println("CommandManager očišćen.");
    }
}
