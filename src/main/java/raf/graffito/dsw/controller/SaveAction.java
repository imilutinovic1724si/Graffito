package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.serializer.Serializer;
import raf.graffito.dsw.serializer.SerializerJSON;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveAction extends AbstractGraffAction {

    private Serializer serializer;

    public SaveAction() {
        putValue(SMALL_ICON, loadIcon("/images/saveIcon.png"));
        putValue(NAME, "Save");
        putValue(SHORT_DESCRIPTION, "Sačuvaj projekat (Ctrl+S)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));

        this.serializer = new SerializerJSON();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Dobijamo selektovani projekat
        GraffNode selected = MainFrame.getInstance().getTree().getSelectedNode();

        if (selected == null) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Morate selektovati projekat");
            return;
        }

        if (!(selected instanceof Project)) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Selektovani cvor nije projekat");
            return;
        }

        Project project = (Project) selected;

        // Proveravamo da li je projekat promenjen
        if (!project.isChanged() && project.getFilePath() != null) {
            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Projekat nije promenjen, nije potrebno čuvanje");
            return;
        }

        // Ako projekat ima putanju, čuvamo na istu putanju
        if (project.getFilePath() != null) {
            saveToFile(project, new File(project.getFilePath()));
        } else {
            // Ako nema putanju, otvaramo JFileChooser
            saveWithFileChooser(project);
        }
    }

    // ako projekat nema putanju
    private void saveWithFileChooser(Project project) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sačuvaj projekat");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON fajlovi (*.json)", "json");
        fileChooser.setFileFilter(filter);

        // predlog imena
        fileChooser.setSelectedFile(new File(project.getName() + ".json"));

        int result = fileChooser.showSaveDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // dodaj .json ekstenziju ako je nema
            if (!file.getName().endsWith(".json")) {
                file = new File(file.getAbsolutePath() + ".json");
            }

            saveToFile(project, file);
        }
    }

    // ako projekat ima putanju
    private void saveToFile(Project project, File file) {
        try {
            serializer.saveProject(project, file);

            project.setFilePath(file.getAbsolutePath());
            project.markAsSaved(); // oznaci projekat da NIJE promenjen

            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Projekat '" + project.getName() + "' uspešno sačuvan u: " + file.getName());

        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Greška pri čuvanju projekta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}