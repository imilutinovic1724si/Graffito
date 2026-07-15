package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public class ProportionalScaling implements ScalingImplementation{

    private double scaleFactor;

    public ProportionalScaling(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    public void applyToFrame(JFrame frame, Dimension originalSize) {
        int newWidth = (int) (originalSize.width * scaleFactor);
        int newHeight = (int) (originalSize.height * scaleFactor);

        frame.setSize(newWidth, newHeight);
        frame.setLocationRelativeTo(null); // Centriraj na ekranu

        System.out.println(String.format(
                "Proporcionalno skaliranje primenjeno: %.1fx (%dx%d)",
                scaleFactor, newWidth, newHeight
        ));
    }
    @Override
    public double getScaleFactor() {
        return scaleFactor;
    }

    @Override
    public String getImplementationName() {
        return "Proportional Scaling";
    }

}
