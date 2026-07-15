package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.serializer.SerializerJSON;
import raf.graffito.dsw.serializer.Serializer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveAsAction extends AbstractGraffAction {

    private Serializer serializer;

    public SaveAsAction() {
        putValue(SMALL_ICON, loadIcon("/images/saveAsIcon.png"));
        putValue(NAME, "Save As");
        putValue(SHORT_DESCRIPTION, "Sačuvaj projekat kao... (Ctrl+Shift+S)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));

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

        // Save As UVEK otvara JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sačuvaj projekat kao");

        // Filter za JSON fajlove
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON fajlovi (*.json)", "json");
        fileChooser.setFileFilter(filter);

        // Predlog imena fajla
        if (project.getFilePath() != null) {
            fileChooser.setSelectedFile(new File(project.getFilePath()));
        } else {
            fileChooser.setSelectedFile(new File(project.getName() + ".json"));
        }

        int result = fileChooser.showSaveDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Dodajemo .json ekstenziju ako je nema
            if (!file.getName().endsWith(".json")) {
                file = new File(file.getAbsolutePath() + ".json");
            }

            // Proveravamo da li fajl već postoji
            if (file.exists()) {
                int confirm = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Fajl već postoji. Da li želite da ga prepišete?", "Potvrda", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            saveToFile(project, file);
        }
    }

    private void saveToFile(Project project, File file) {
        try {
            // Serijalizujemo projekat
            serializer.saveProject(project, file);

            // Postavljamo novu putanju i označavamo kao sačuvan
            project.setFilePath(file.getAbsolutePath());
            project.markAsSaved();

            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Projekat '" + project.getName() + "' uspešno sačuvan kao: " + file.getName());

        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .generateError("Greška pri čuvanju projekta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}