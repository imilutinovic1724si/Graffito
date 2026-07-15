package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public class SmallMode extends WindowMode{

    public SmallMode(Dimension originalSize) {
        super(new ProportionalScaling(0.5), originalSize);
    }
    @Override
    public String getModeName() {
        return "Small";
    }

    @Override
    public String getDescription() {
        return "Mala veličina prozora (2x manja)";
    }

    @Override
    protected void onModeApplied(JFrame frame) {
        // Resetuj fullscreen ako je bio aktivan
        frame.setExtendedState(JFrame.NORMAL);

        System.out.println("Small mod aktiviran");
    }

}
