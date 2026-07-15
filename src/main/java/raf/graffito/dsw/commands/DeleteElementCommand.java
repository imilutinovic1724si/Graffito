package raf.graffito.dsw.commands;

import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

public class DeleteElementCommand implements Command{

    private Slide slide;
    private SlideElement element;

    public DeleteElementCommand(Slide slide, SlideElement slideElement) {
        this.slide = slide;
        this.element = slideElement;
    }

    @Override
    public void execute() {
        slide.removeElement(element);
    }

    @Override
    public void undo() {
        slide.addElement(element);
    }
}
