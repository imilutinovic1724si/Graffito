package raf.graffito.dsw.controller;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public abstract  class AbstractGraffAction extends AbstractAction {

    // nasledjuje AbstractAction
    // metoda loadIcon da SVE akcije jednostavno pristupe ikonama

    protected Icon loadIcon(String path) {
        Icon icon = null;
        URL imageURL = getClass().getResource(path);
        if (imageURL != null) {
            Image img = new ImageIcon(imageURL).getImage();
            Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
        }else{
            System.err.println("Resource not found: " + path);
        }
        return icon;
    }
}
