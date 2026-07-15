package raf.graffito.dsw.controller;

import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.ProjectView;
import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.*;

public class SlideVewHelper {

    //dobija trenutno aktivni SlideView
    //to nam treba za akcije, pa je bolje da imamo klasu
    // nego da u svakoj akciji pisemo pojedinacno sve to

    public static SlideView getCurrentSlideView() {
        PresentationView pressView = getCurrentPresentationView();
        if (pressView == null) return null;

        // NOVA STRUKTURA - nema vise JSplitPane u PresentationView
        // PresentationView sada ima direktno JTabbedPane kao prvi component
        Component presComponent = pressView.getComponent(0);

        if (presComponent instanceof JTabbedPane){
            JTabbedPane slideTabPane = (JTabbedPane) presComponent;
            Component slideComponent = slideTabPane.getSelectedComponent();

            if (slideComponent instanceof JScrollPane){
                JScrollPane scrollPane = (JScrollPane) slideComponent;
                Component viewport = scrollPane.getViewport().getView();

                if (viewport instanceof SlideView){
                    return (SlideView) viewport;
                }
            }
        }
        return null;
    }

    //dobija presentation view

    public static PresentationView getCurrentPresentationView() {
        ProjectView projectView = MainFrame.getInstance().getProjectView();
        if (projectView == null || projectView.getCurrentProject() == null) return null;

        JTabbedPane tabbedPane = projectView.getTabbedPane();
        if (tabbedPane == null) return null;

        Component tabComponent = tabbedPane.getSelectedComponent();
        if (tabComponent instanceof JPanel){
            JPanel panel = (JPanel) tabComponent;
            if (panel.getComponentCount()>0 && panel.getComponent(0) instanceof PresentationView){
                return (PresentationView) panel.getComponent(0);
            }
        }
        return null;
    }
}