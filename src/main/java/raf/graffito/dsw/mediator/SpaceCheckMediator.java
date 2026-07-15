package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.space.SlideSpaceChecker;
import raf.graffito.dsw.model.implementation.Slide;

public interface SpaceCheckMediator {
    // UI komponente pozivaju ove metode
    void onMethodChanged(String methodName);
    void onCheckRequested();
    void onSlideChanged(Slide newSlide);

    // Registracija komponenti
    void registerUI(SpaceCheckUI ui);
    void registerChecker(SlideSpaceChecker checker);
}
