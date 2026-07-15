package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public abstract class WindowMode {
    protected ScalingImplementation implementation;
    protected Dimension originalSize;

    public WindowMode(ScalingImplementation implementation, Dimension originalSize) {
        this.implementation = implementation;
        this.originalSize = originalSize;
    }
    // Dinamička promena implementacije
    public void setImplementation(ScalingImplementation implementation) {
        this.implementation = implementation;
    }

    public ScalingImplementation getImplementation() {
        return implementation;
    }

    // Osnovne operacije - delegiraju na implementaciju
    public void apply(JFrame frame) {
        implementation.applyToFrame(frame, originalSize);
        onModeApplied(frame);
    }

    public double getScaleFactor() {
        return implementation.getScaleFactor();
    }
    // Hook metoda - konkretne klase mogu override-ovati
    protected void onModeApplied(JFrame frame) {
        // Default - ne radi ništa
    }

    // Apstraktne metode
    public abstract String getModeName();
    public abstract String getDescription();
}
