package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ResizeElementAction extends AbstractGraffAction{

    public ResizeElementAction() {
        putValue(SMALL_ICON, loadIcon("/images/resize.png"));
        putValue(NAME, "Resize");
        putValue(SHORT_DESCRIPTION, "Promeni velicinu selektovanih elemenata");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.getStateManager().setResizeState();
            System.out.println("State promenjen na: ResizeState");
        }
    }
}
