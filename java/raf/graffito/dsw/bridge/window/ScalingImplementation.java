package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public interface ScalingImplementation {
    /**
     * Primenjuje skaliranje na glavni prozor
     */
    void applyToFrame(JFrame frame, Dimension originalSize);

    double getScaleFactor();
    /**
     * Naziv implementacije (za prikaz)
     */
    String getImplementationName();
}
