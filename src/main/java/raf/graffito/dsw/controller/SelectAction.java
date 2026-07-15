package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.SlideView;

import java.awt.event.ActionEvent;

public class SelectAction extends AbstractGraffAction{

    public SelectAction() {
        putValue(SMALL_ICON, loadIcon("/images/select.png"));
        putValue(NAME, "Select");
        putValue(SHORT_DESCRIPTION, "Selektuj elemente");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        // Koristim helper klasu
        if (currentSlideView != null) {
            currentSlideView.getStateManager().setSelectState();
            System.out.println("State promenjen na: SelectState");
        }
    }
}
