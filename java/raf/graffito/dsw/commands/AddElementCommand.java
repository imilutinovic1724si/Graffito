package raf.graffito.dsw.commands;

import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

public class AddElementCommand implements Command{

    private Slide slide;
    private SlideElement element;

    public AddElementCommand(Slide slide, SlideElement element) {
        this.slide = slide;
        this.element = element;
    }

    @Override
    public void execute() {
        slide.addElement(element);
    }

    @Override
    public void undo() {
        slide.removeElement(element);
    }
}
