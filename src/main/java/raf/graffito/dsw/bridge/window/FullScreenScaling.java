package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public class FullScreenScaling implements ScalingImplementation{

    @Override
    public void applyToFrame(JFrame frame, Dimension originalSize) {
        // Uzmi dimenzije ekrana
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // Postavi na fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenSize);

        System.out.println(String.format(
                "Fullscreen mod primenjen: %dx%d",
                screenSize.width, screenSize.height
        ));
    }

    @Override
    public double getScaleFactor() {
        // Računamo na osnovu ekrana vs. normalne veličine
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // Pretpostavimo da je normal 800x600
        double widthFactor = screenSize.width / 800.0;
        double heightFactor = screenSize.height / 600.0;

        // Uzmi manji faktor da održimo aspect ratio
        return Math.min(widthFactor, heightFactor);
    }

    @Override
    public String getImplementationName() {
        return "Fullscreen Scaling";
    }
}
