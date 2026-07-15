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

public class OpenAction extends AbstractGraffAction {

    private Serializer serializer;

    public OpenAction() {
        putValue(SMALL_ICON, loadIcon("/images/openIcon.png"));
        putValue(NAME, "Open Project");
        putValue(SHORT_DESCRIPTION, "Otvori projekat (Ctrl+O)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));

        this.serializer = new SerializerJSON();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Otvori projekat");

        // Filter za JSON fajlove
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON fajlovi (*.json)", "json");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.exists()) {
                ApplicationFramework.getInstance().getMessageGenerator().generateError("Fajl ne postoji: " + file.getName());
                return;
            }

            loadFromFile(file);
        }
    }

    private void loadFromFile(File file) {
        try {
            // Deserijalizujemo projekat
            Project loadedProject = serializer.loadProject(file);

            // Postavljamo putanju fajla
            loadedProject.setFilePath(file.getAbsolutePath());
            loadedProject.markAsSaved(); // Upravo učitan, nije promenjen

            // Dodajemo projekat u Workspace
            GraffNode workspace = ApplicationFramework.getInstance().getRepository().getWorkspace();
            workspace.addChild(loadedProject);

            // Rekonstruišemo parent reference (jer JSON ih ne čuva)
            reconstructParentReferences(loadedProject);

            // Refresh stabla
            MainFrame.getInstance().getTree().getModel();
            MainFrame.getInstance().getTree().updateUI();

            // Otvaramo projekat u ProjectView
            MainFrame.getInstance().getProjectView().openProject(loadedProject);

            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Projekat '" + loadedProject.getName() + "' uspešno učitan");

        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Greška pri učitavanju projekta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // JSON ne čuva parente i zbog toga mi ručno moramo da ih povezujemo
    private void reconstructParentReferences(Project project) {
        for (GraffNode child : project.getChildren()) {
            child.setParent(project);

            // Rekurzivno za svu decu
            if (child instanceof raf.graffito.dsw.model.GraffNodeComposite) {
                reconstructParentReferencesRecursive(child);
            }
        }
    }

    private void reconstructParentReferencesRecursive(GraffNode node) {
        if (node instanceof raf.graffito.dsw.model.GraffNodeComposite) {
            raf.graffito.dsw.model.GraffNodeComposite composite = (raf.graffito.dsw.model.GraffNodeComposite) node;

            for (GraffNode child : composite.getChildren()) {
                child.setParent(node);
                reconstructParentReferencesRecursive(child);
            }
        }
    }
}