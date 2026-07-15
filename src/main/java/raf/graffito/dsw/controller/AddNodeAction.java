package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;

import java.awt.event.ActionEvent;

public class AddNodeAction extends AbstractGraffAction{
    // Akcija za dodavanje novog cvora u stablo

    public AddNodeAction() {
        putValue(SMALL_ICON, loadIcon("/images/add.png"));
        putValue(NAME, "Add");
        putValue(SHORT_DESCRIPTION, "Dodaj novi cvor");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // dobija selektovani cvor
        GraffNode selected = MainFrame.getInstance().getTree().getSelectedNode();
        // dodaje ga
        MainFrame.getInstance().getTree().addChild(selected);
    }
}
