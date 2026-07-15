package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.serializer.SerializerJSON;
import raf.graffito.dsw.serializer.Serializer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveAsTemplateAction extends AbstractGraffAction {

    private Serializer serializer;
    private static final String TEMPLATES_FOLDER = "src/main/resources/templates";

    public SaveAsTemplateAction() {
        putValue(SMALL_ICON, loadIcon("/images/templateIcon.png"));
        putValue(NAME, "Save as Template");
        putValue(SHORT_DESCRIPTION, "Sačuvaj projekat kao šablon");

        this.serializer = new SerializerJSON();

        // Kreiraj templates folder ako ne postoji
        createTemplatesFolder();
    }

    private void createTemplatesFolder() {
        try {
            Path templatesPath = Paths.get(TEMPLATES_FOLDER);
            if (!Files.exists(templatesPath)) {
                Files.createDirectories(templatesPath);
                System.out.println("Templates folder kreiran: " + TEMPLATES_FOLDER);
            }
        } catch (Exception e) {
            System.err.println("Greška pri kreiranju templates foldera: " + e.getMessage());
        }
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
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Selektovani čvor nije projekat");
            return;
        }

        Project project = (Project) selected;

        // Dijalog za unos imena šablona
        String templateName = JOptionPane.showInputDialog(MainFrame.getInstance(), "Unesite ime šablona:", project.getName() + "_template");

        if (templateName == null || templateName.trim().isEmpty()) {
            return;
        }

        // Sačuvaj kao šablon
        saveAsTemplate(project, templateName.trim());
    }

    private void saveAsTemplate(Project project, String templateName) {
        try {
            File templateFile = new File(TEMPLATES_FOLDER, templateName + ".json");

            // Proveri da li šablon već postoji
            if (templateFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "Šablon sa ovim imenom već postoji. Želite da ga prepišete?", "Potvrda", JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Sačuvaj projekat kao šablon
            serializer.saveProject(project, templateFile);

            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Šablon '" + templateName + "' uspešno sačuvan!");

        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Greška pri čuvanju šablona: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}