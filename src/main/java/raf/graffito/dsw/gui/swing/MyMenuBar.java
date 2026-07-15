package raf.graffito.dsw.gui.swing;





import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MyMenuBar extends JMenuBar {

    // meni u gornjoj liniji

    public MyMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(MainFrame.getInstance().getActionManager().getOpenAction());
        fileMenu.addSeparator();
        fileMenu.add(MainFrame.getInstance().getActionManager().getSaveAction());
        fileMenu.add(MainFrame.getInstance().getActionManager().getSaveAsAction());
        fileMenu.addSeparator();
        fileMenu.add(MainFrame.getInstance().getActionManager().getExitAction());

        add(fileMenu);

        JMenu templateMenu = new JMenu("Templates");
        templateMenu.setMnemonic(KeyEvent.VK_T);
        templateMenu.add(MainFrame.getInstance().getActionManager().getSaveAsTemplateAction());
        templateMenu.add(MainFrame.getInstance().getActionManager().getLoadTemplateAction());
        add(templateMenu);

        //meni za edit
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        edit.add(MainFrame.getInstance().getActionManager().getAddNodeAction());
        edit.add(MainFrame.getInstance().getActionManager().getDeleteNodeAction());
        edit.add(MainFrame.getInstance().getActionManager().getRenameNodeAction());
        add(edit);

        //dodala novi meni za projekat
        JMenu projectMenu = new JMenu("Projekat");
        projectMenu.setMnemonic(KeyEvent.VK_P);
        projectMenu.add(MainFrame.getInstance().getActionManager().getEditProjectTitleAction());
        projectMenu.add(MainFrame.getInstance().getActionManager().getEditProjectAuthorAction());
        add(projectMenu);

        JMenu slideMenu = new JMenu("Slide");
        slideMenu.setMnemonic(KeyEvent.VK_S);
        slideMenu.add(MainFrame.getInstance().getActionManager().getSelectAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getAddElementAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getDeleteElementAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getMoveElementAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getResizeElementAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getLoadImageAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getRotateLeftAction());
        slideMenu.add(MainFrame.getInstance().getActionManager().getRotateRightAction());
        add(slideMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(MainFrame.getInstance().getActionManager().getAboutUsAction());
        add(helpMenu);
    }
}
