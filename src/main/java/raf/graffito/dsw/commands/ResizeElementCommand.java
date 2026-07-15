package raf.graffito.dsw.commands;

import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.*;

public class ResizeElementCommand implements Command {

    private SlideElement element;
    private Dimension oldDimension;
    private Dimension newDimension;

    public ResizeElementCommand(SlideElement element, Dimension oldDimension, Dimension newDimension) {
        this.element = element;
        this.oldDimension = new Dimension(oldDimension);
        this.newDimension = new Dimension(newDimension);
    }

    @Override
    public void execute() {
        element.setDimension(newDimension);
        System.out.println("Resize executed: " + newDimension);
    }

    @Override
    public void undo() {
        element.setDimension(oldDimension);
        System.out.println("Resize undone: " + oldDimension);
    }
}
