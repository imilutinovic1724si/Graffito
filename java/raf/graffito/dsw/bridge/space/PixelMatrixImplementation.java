package raf.graffito.dsw.bridge.space;

import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

import java.awt.*;

public class PixelMatrixImplementation implements CheckSpaceImplementation {

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
        // Binarna matrica
        boolean[][] matrix = new boolean[SLIDE_HEIGHT][SLIDE_WIDTH];

        // Označi zauzete piksele
        for (SlideElement element : slide.getElementi()) {
            Rectangle bounds = element.getBounds();

            for (int y = bounds.y; y < bounds.y + bounds.height && y < SLIDE_HEIGHT; y++) {
                for (int x = bounds.x; x < bounds.x + bounds.width && x < SLIDE_WIDTH; x++) {
                    if (x >= 0 && y >= 0) {
                        matrix[y][x] = true;
                    }
                }
            }
        }

        // Prebroj zauzete piksele
        int occupiedPixels = 0;
        for (int y = 0; y < SLIDE_HEIGHT; y++) {
            for (int x = 0; x < SLIDE_WIDTH; x++) {
                if (matrix[y][x]) {
                    occupiedPixels++;
                }
            }
        }

        int totalPixels = SLIDE_WIDTH * SLIDE_HEIGHT;
        return (double) occupiedPixels / totalPixels;
    }

    @Override
    public String getMethodName() {
        return "Pixel Matrix (Binarna matrica)";
    }

}
