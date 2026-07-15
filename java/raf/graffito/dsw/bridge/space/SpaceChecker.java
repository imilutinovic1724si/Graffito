package raf.graffito.dsw.bridge.space;

import raf.graffito.dsw.model.implementation.Slide;

public abstract class SpaceChecker {
    protected CheckSpaceImplementation implementation;

    public SpaceChecker(CheckSpaceImplementation implementation) {
        this.implementation = implementation;
    }

    public CheckSpaceImplementation getImplementation() {
        return implementation;
    }

    public void setImplementation(CheckSpaceImplementation implementation) {
        this.implementation = implementation;
    }
    // Osnovne operacije - delegiraju na implementaciju
    public boolean checkSpace(Slide slide) {
        return implementation.hasEnoughSpace(slide);
    }

    public double getOccupiedPercentage(Slide slide) {
        return implementation.getOccupiedPercentage(slide);
    }

    // Apstraktne metode koje konkretne klase implementiraju
    public abstract void displayStatus(Slide slide);
}
