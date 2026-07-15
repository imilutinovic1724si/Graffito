package raf.graffito.dsw.commands;

import raf.graffito.dsw.model.elementi.SlideElement;

public class RotateElementCommand implements Command {

    private SlideElement element;
    private double oldRotation;
    private double newRotation;

    public RotateElementCommand(SlideElement element, double oldRotation, double newRotation) {
        this.element = element;
        this.oldRotation = oldRotation;
        this.newRotation = newRotation;
    }

    @Override
    public void execute() {
        element.setRotation(newRotation);
        System.out.println("Rotate executed: " + newRotation);
    }

    @Override
    public void undo() {
        element.setRotation(oldRotation);
        System.out.println("Rotate undone: " + oldRotation);
    }
}
