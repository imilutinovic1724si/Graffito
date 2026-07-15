package raf.graffito.dsw.bridge.space;

import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

public class SimpleAreaImplementation implements CheckSpaceImplementation {
    private static final double REQUIRED_FREE_SPACE = 0.20; // 20%
    private static final int SLIDE_WIDTH = 800;
    private static final int SLIDE_HEIGHT = 600;

    @Override
    public boolean hasEnoughSpace(Slide slide) {
        double occupiedPercentage = getOccupiedPercentage(slide);
        double freePercentage = 1.0 - occupiedPercentage;
        return freePercentage >= REQUIRED_FREE_SPACE;
    }
    @Override
    public double getOccupiedPercentage(Slide slide) {
        double totalArea = SLIDE_WIDTH * SLIDE_HEIGHT;

        double occupiedArea = 0.0;
        for (SlideElement element : slide.getElementi()) {
            int width = element.getDimension().width;
            int height = element.getDimension().height;
            occupiedArea += (width * height);
        }

        return occupiedArea / totalArea;
    }

    @Override
    public String getMethodName() {
        return "Simple Area (Sabiranje površina)";
    }
}
