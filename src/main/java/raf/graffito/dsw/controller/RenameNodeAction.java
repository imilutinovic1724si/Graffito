package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.GraffTree;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RenameNodeAction extends AbstractGraffAction{

    // Akcija za preimenovanje cvora

    public RenameNodeAction() {
        putValue(SMALL_ICON, loadIcon("/images/rename.png"));
        putValue(NAME, "Rename");
        putValue(SHORT_DESCRIPTION, "Preimenovati selektovani cvor");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // dobija selektovani cvor
        GraffNode selected = MainFrame.getInstance().getTree().getSelectedNode();

        // proverava da li je cvor selektovan
        if (selected == null) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Morate selektovati cvor");
            return;
        }

        //novi naziv cvora
        String novoIme = JOptionPane.showInputDialog(MainFrame.getInstance(), "Unesite novo ime", selected.getName());

        // prikazuje se novi naziv cvora
        if (novoIme != null && !novoIme.trim().isEmpty()) {
            MainFrame.getInstance().getTree().renameNode(selected, novoIme);
        }
    }
}
