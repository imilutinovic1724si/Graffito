package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RedoAction extends AbstractGraffAction{

    public RedoAction() {
        putValue(SMALL_ICON, loadIcon("/images/redoIcon.png"));
        putValue(NAME, "Redo");
        putValue(SHORT_DESCRIPTION, "Ponovi poslednju akciju (Ctrl+Y)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView != null) {
            currentSlideView.redo();
            System.out.println("Redo akcija pozvana");
        }
    }
}
