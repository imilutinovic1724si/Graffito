package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.Slide;
import raf.graffito.dsw.serializer.SerializerJSON;
import raf.graffito.dsw.serializer.Serializer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

public class LoadTemplateAction extends AbstractGraffAction {

    private Serializer serializer;
    private static final String TEMPLATES_FOLDER = "src/main/resources/templates";

    public LoadTemplateAction() {
        putValue(SMALL_ICON, loadIcon("/images/loadTemplateIcon.jpg"));
        putValue(NAME, "Load Template");
        putValue(SHORT_DESCRIPTION, "Učitaj šablon u projekat");

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
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Selektovani čvor nije projekat");
            return;
        }

        Project targetProject = (Project) selected;

        // Otvori JFileChooser za izbor šablona
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izaberite šablon");
        fileChooser.setCurrentDirectory(new File(TEMPLATES_FOLDER));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON šabloni (*.json)", "json");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
            File templateFile = fileChooser.getSelectedFile();
            loadTemplateIntoProject(targetProject, templateFile);
        }
    }

    private void loadTemplateIntoProject(Project targetProject, File templateFile) {
        try {
            // Učitaj šablon kao projekat
            Project templateProject = serializer.loadProject(templateFile);

            // Kopiraj sve prezentacije i slajdove iz šablona u ciljni projekat
            if (templateProject instanceof GraffNodeComposite) {
                GraffNodeComposite templateComposite = (GraffNodeComposite) templateProject;

                for (GraffNode child : templateComposite.getChildren()) {
                    if (child instanceof Presentation) {
                        Presentation templatePresentation = (Presentation) child;

                        // Kloniraj prezentaciju
                        Presentation newPresentation = clonePresentation(templatePresentation, targetProject);
                        targetProject.addChild(newPresentation);

                    } else if (child instanceof Slide) {
                        Slide templateSlide = (Slide) child;

                        // Kloniraj slajd
                        Slide newSlide = cloneSlide(templateSlide, targetProject);
                        targetProject.addChild(newSlide);
                    }
                }
            }

            // Refresh stabla
            MainFrame.getInstance().getTree().getModel();
            MainFrame.getInstance().getTree().updateUI();

            // Refresh ProjectView
            MainFrame.getInstance().getProjectView().openProject(targetProject);

            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Šablon '" + templateFile.getName() + "' uspešno učitan!");

        } catch (Exception ex) {
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Greška pri učitavanju šablona: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Presentation clonePresentation(Presentation original, GraffNode newParent) {
        Presentation cloned = new Presentation(original.getName() + "_copy", newParent);

        if (original instanceof GraffNodeComposite) {
            GraffNodeComposite originalComposite = (GraffNodeComposite) original;

            for (GraffNode child : originalComposite.getChildren()) {
                if (child instanceof Slide) {
                    Slide clonedSlide = cloneSlide((Slide) child, cloned);
                    cloned.addChild(clonedSlide);
                }
            }
        }

        return cloned;
    }

    private Slide cloneSlide(Slide original, GraffNode newParent) {
        Slide cloned = new Slide(original.getName() + "_copy", newParent);

        // Kopiraj sve elemente
        for (var element : original.getElementi()) {
            cloned.addElement(element.clone());
        }

        return cloned;
    }
}