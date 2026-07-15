package raf.graffito.dsw.bridge.window;

import javax.swing.*;
import java.awt.*;

public class FullScreenMode extends WindowMode{

    public FullScreenMode(Dimension originalSize) {
        super(new FullScreenScaling(), originalSize);
    }

    @Override
    public String getModeName() {
        return "Fullscreen";
    }

    @Override
    public String getDescription() {
        return "Režim punog ekrana";
    }

    @Override
    protected void onModeApplied(JFrame frame) {
        // Dodatna logika za fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        System.out.println("Fullscreen mod aktiviran");
    }
}
