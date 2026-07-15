package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.MainFrame;

import java.awt.event.ActionEvent;

public class DeleteNodeAction extends AbstractGraffAction{

    // Akcija za brisanje cvora iz stabla
    public DeleteNodeAction() {
        putValue(SMALL_ICON, loadIcon("/images/delete.png"));
        putValue(NAME, "Delete");
        putValue(SHORT_DESCRIPTION, "Obrisi selektovani cvor");

    }

    @Override
    public void actionPerformed(ActionEvent e){
        // brise selektovani cvor
        MainFrame.getInstance().getTree().deleteNode();
    }
}

