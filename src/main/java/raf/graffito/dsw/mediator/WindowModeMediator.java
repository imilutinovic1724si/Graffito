package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.window.WindowModeUI;

public interface WindowModeMediator {
    // UI komponente pozivaju ove metode
    void onModeSelected(String modeName);

    // Registracija komponenti
    void registerUI(WindowModeUI ui);
    void registerFrame(javax.swing.JFrame frame);
    void applyDefaultMode();
}
