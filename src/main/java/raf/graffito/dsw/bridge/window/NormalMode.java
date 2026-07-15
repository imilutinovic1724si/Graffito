package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public class NormalMode extends WindowMode{

    public NormalMode(Dimension originalSize) {
        super(new ProportionalScaling(1.0), originalSize);
    }

    @Override
    public String getModeName() {
        return "Normal";
    }

    @Override
    public String getDescription() {
        return "Normalna veličina prozora";
    }
    @Override
    protected void onModeApplied(JFrame frame) {
        // Resetuj fullscreen ako je bio aktivan
        frame.setExtendedState(JFrame.NORMAL);

        System.out.println("Normal mod aktiviran");
    }
}
