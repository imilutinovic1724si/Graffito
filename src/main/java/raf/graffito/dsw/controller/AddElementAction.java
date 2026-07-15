package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import java.awt.event.ActionEvent;

public class AddElementAction extends AbstractGraffAction{

    public AddElementAction() {
        putValue(SMALL_ICON, loadIcon("/images/add_element.png"));
        putValue(NAME, "Add Element");
        putValue(SHORT_DESCRIPTION, "Dodaj elemente na slajd");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.getStateManager().setAddState();;
            System.out.println("State promenjen na: AddState");
        }
    }

}
