package raf.graffito.dsw.commands;

import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.*;

public class MoveElementCommand implements Command{

    private SlideElement element;
    private Point oldLocation;
    private Point newLocation;

    public MoveElementCommand(SlideElement element, Point oldLocation, Point newLocation) {
        this.element = element;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    @Override
    public void execute() {
        element.setLocation(newLocation);
    }

    @Override
    public void undo() {
        element.setLocation(oldLocation);
    }
}
