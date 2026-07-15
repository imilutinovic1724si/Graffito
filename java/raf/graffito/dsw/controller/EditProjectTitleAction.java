package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditProjectTitleAction extends AbstractGraffAction{
    // Akcija za izmenu naslova za projekat

    public EditProjectTitleAction() {
        putValue(SMALL_ICON, loadIcon("/images/renameIcon.jpg"));
        putValue(NAME, "Izmeni naslov");
        putValue(SHORT_DESCRIPTION, "Izmeni naslov projekta");

    }

    @Override
    public void actionPerformed(ActionEvent e){

        // dobija selektovani cvor
        GraffNode selected = MainFrame.getInstance().getTree().getSelectedNode();

        // proveravamo da li je cvor selektovan
        if (selected == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Morate selektovati projekat");
            return;
        }

        if (!(selected instanceof Project)){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Morate selektovati projekat");
            return;
        }

        Project project = (Project) selected;
        String current = project.getName();

        //novi naslov
        String novi= JOptionPane.showInputDialog(MainFrame.getInstance(), "Unesite novi naslov projekta", current);

        // postavljamo novi naslov
        if (novi != null && !novi.trim().isEmpty()){
            project.setName(novi.trim());

            // na ekranu prikazujemo novi naslov
            if (MainFrame.getInstance().getProjectView().getCurrentProject() == project){
                MainFrame.getInstance().getProjectView().openProject(project);
            }

            //obavestenje
            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Naslov promenjen u "+novi);
        }
    }
}
