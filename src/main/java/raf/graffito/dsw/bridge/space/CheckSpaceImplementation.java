package raf.graffito.dsw.bridge.space;

import raf.graffito.dsw.model.implementation.Slide;

public interface CheckSpaceImplementation {
    /**
     * Implementation interfejs za Bridge pattern
     * Definiše kako se proverava prostor
     */
    boolean hasEnoughSpace(Slide slide);
    double getOccupiedPercentage(Slide slide);
    String getMethodName(); // Naziv metode (za prikaz)
}
