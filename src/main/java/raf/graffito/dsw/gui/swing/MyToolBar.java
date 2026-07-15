package raf.graffito.dsw.gui.swing;

import javax.swing.*;

public class MyToolBar extends JToolBar {

    // meni u donjoj liniji

    public MyToolBar() {
        super(HORIZONTAL);
        setFloatable(false);

        add(MainFrame.getInstance().getActionManager().getExitAction());
        add(MainFrame.getInstance().getActionManager().getOpenAction());
        add(MainFrame.getInstance().getActionManager().getSaveAction());
        add(MainFrame.getInstance().getActionManager().getSaveAsAction());
        addSeparator();

        add(MainFrame.getInstance().getActionManager().getSaveAsTemplateAction());
        add(MainFrame.getInstance().getActionManager().getLoadTemplateAction());
        addSeparator();

        add(MainFrame.getInstance().getActionManager().getAddNodeAction());
        add(MainFrame.getInstance().getActionManager().getDeleteNodeAction());
        add(MainFrame.getInstance().getActionManager().getRenameNodeAction());
        addSeparator();

        add(MainFrame.getInstance().getActionManager().getEditProjectTitleAction());
        add(MainFrame.getInstance().getActionManager().getEditProjectAuthorAction());
        addSeparator();

        add(MainFrame.getInstance().getActionManager().getSelectAction());
        add(MainFrame.getInstance().getActionManager().getAddElementAction());
        add(MainFrame.getInstance().getActionManager().getDeleteElementAction());
        add(MainFrame.getInstance().getActionManager().getMoveElementAction());
        add(MainFrame.getInstance().getActionManager().getResizeElementAction());
        add(MainFrame.getInstance().getActionManager().getLoadImageAction());
        add(MainFrame.getInstance().getActionManager().getRotateLeftAction());
        add(MainFrame.getInstance().getActionManager().getRotateRightAction());
        addSeparator();

        add(MainFrame.getInstance().getActionManager().getUndoAction());
        add(MainFrame.getInstance().getActionManager().getRedoAction());

    }
}
