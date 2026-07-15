package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditProjectAuthorAction extends AbstractGraffAction{

    // Akcija za izmenu autora za projekat
    public EditProjectAuthorAction() {
        putValue(SMALL_ICON, loadIcon("/images/renameAuthorIcon.jpg"));
        putValue(NAME, "Izmeni autora");
        putValue(SHORT_DESCRIPTION, "Izmeni autora projekta");
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
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Selektovani cvor nije projekat");
            return;
        }


        Project project = (Project) selected;
        String currentAuthor = project.getAuthor();

        // korisnik unosi novog autora
        String newAuthor = JOptionPane.showInputDialog(MainFrame.getInstance(), "Unesite ime novog autora", currentAuthor);

        // postavljamo novog autora
        if (newAuthor != null && !newAuthor.trim().isEmpty()){
            project.setAuthor(newAuthor.trim());

            // na ekranu prikazujemo novog autora
            if (MainFrame.getInstance().getProjectView().getCurrentProject() == project){
                MainFrame.getInstance().getProjectView().openProject(project);
            }

            //obavestenje
            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Autor promenjen u: "+newAuthor);
        }
    }
}