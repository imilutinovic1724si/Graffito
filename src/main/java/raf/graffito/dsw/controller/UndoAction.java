package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class UndoAction extends AbstractGraffAction{

    public UndoAction() {
        putValue(SMALL_ICON, loadIcon("/images/undoIcom.png"));
        putValue(NAME, "Undo");
        putValue(SHORT_DESCRIPTION, "Vrati poslednju akciju (Ctrl+Z)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.undo();
            System.out.println("Undo akcija pozvana");
        }
    }
}
