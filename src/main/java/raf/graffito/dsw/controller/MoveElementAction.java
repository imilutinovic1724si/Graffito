package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import java.awt.event.ActionEvent;

public class MoveElementAction extends AbstractGraffAction{

    public MoveElementAction() {
        putValue(SMALL_ICON, loadIcon("/images/move.png"));
        putValue(NAME, "Move");
        putValue(SHORT_DESCRIPTION, "Pomeri selektovane elemente");
    }
    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.getStateManager().setMoveState();
            System.out.println("State promenjen na: MoveState");
        }
    }
}
