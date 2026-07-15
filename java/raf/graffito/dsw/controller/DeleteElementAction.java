package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import java.awt.event.ActionEvent;

public class DeleteElementAction extends AbstractGraffAction{

    public DeleteElementAction() {

        putValue(SMALL_ICON, loadIcon("/images/delete_element.png"));
        putValue(NAME, "Delete");
        putValue(SHORT_DESCRIPTION, "Obrisi selektovane elemente");
    }
    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.getStateManager().setDeleteState();
            System.out.println("State promenjen na: DeleteState");
        }
    }
}
