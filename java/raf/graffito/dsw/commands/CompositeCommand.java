package raf.graffito.dsw.commands;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements Command {

    private List<Command> commands;
    private String description;

    public CompositeCommand(String description) {
        this.commands = new ArrayList<>();
        this.description = description;
    }

    public void addCommand(Command command) {
        if (command != null) {
            commands.add(command);
        }
    }

    @Override
    public void execute() {
        System.out.println("CompositeCommand execute: " + description + " ( " + commands.size() + " komandi )");
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        System.out.println("CompositeCommand undo: " + description + " ( " + commands.size() + " komandi )");
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    public boolean isEmpty() {
        return commands.isEmpty();
    }
    public int size(){
        return commands.size();
    }
    public String getDescription() {
        return description;
    }
}
