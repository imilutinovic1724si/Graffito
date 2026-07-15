package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.window.*;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.ProjectView;
import raf.graffito.dsw.gui.swing.SlideView;

import javax.swing.*;
import java.awt.*;

public class WindowModeMediatorImpl implements WindowModeMediator {

    private WindowModeUI ui;
    private JFrame frame;
    private WindowMode currentMode;

    // Dostupni režimi
    private WindowMode normalMode;
    private WindowMode fullscreenMode;
    private WindowMode smallMode;

    // Originalna veličina prozora
    private Dimension originalSize;

    public WindowModeMediatorImpl(Dimension originalSize) {
        this.originalSize = originalSize;

        // Inicijalizuj režime
        this.normalMode = new NormalMode(originalSize);
        this.fullscreenMode = new FullScreenMode(originalSize);
        this.smallMode = new SmallMode(originalSize);

        // Default je Normal
        this.currentMode = normalMode;
    }

    @Override
    public void registerUI(WindowModeUI ui) {
        this.ui = ui;
    }

    @Override
    public void registerFrame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onModeSelected(String modeName) {
        if (frame == null) {
            System.err.println("Frame nije registrovan!");
            return;
        }

        // Odaberi režim na osnovu naziva
        WindowMode newMode = null;

        if (modeName.equals("Normal")) {
            newMode = normalMode;
        } else if (modeName.equals("Fullscreen")) {
            newMode = fullscreenMode;
        } else if (modeName.equals("Small")) {
            newMode = smallMode;
        }

        if (newMode == null) {
            System.err.println("Nepoznat režim: " + modeName);
            return;
        }

        // Primeni novi režim
        currentMode = newMode;
        applyCurrentMode();

        // Obavesti korisnika
        ApplicationFramework.getInstance().getMessageGenerator()
                .generateInfo("Režim promenjen na: " + currentMode.getModeName());

        System.out.println(String.format(
                " Režim: %s | Skala: %.2fx | Implementacija: %s",
                currentMode.getModeName(),
                currentMode.getScaleFactor(),
                currentMode.getImplementation().getImplementationName()
        ));
    }

    private void applyCurrentMode() {
        if (frame == null || currentMode == null) return;

        // Primeni režim na prozor
        currentMode.apply(frame);

        // Skaliraj sve slajdove
        scaleAllSlides();

        // Ažuriraj UI
        if (ui != null) {
            ui.updateDisplay(currentMode);
        }

        // Revalidate i repaint
        frame.revalidate();
        frame.repaint();
    }

    private void scaleAllSlides() {
        double scaleFactor = currentMode.getScaleFactor();

        System.out.println("DEBUG: Pokušavam skaliranje");
        System.out.println("Scale factor: " + scaleFactor);

        // Dobij ProjectView iz MainFrame
        MainFrame mainFrame = MainFrame.getInstance();
        if (mainFrame == null) {
            System.err.println("MainFrame je null!");
            return;
        }

        ProjectView projectView = mainFrame.getProjectView();
        if (projectView == null) {
            System.err.println("ProjectView je null!");
            return;
        }

        // Dobij trenutnu prezentaciju
        JTabbedPane projectTabbedPane = projectView.getTabbedPane();
        if (projectTabbedPane == null) {
            System.err.println("Project TabbedPane je null!");
            return;
        }

        System.out.println("Broj tabova u projektu: " + projectTabbedPane.getTabCount());

        // Prođi kroz sve tabove (prezentacije)
        for (int i = 0; i < projectTabbedPane.getTabCount(); i++) {
            Component tabComponent = projectTabbedPane.getComponentAt(i);
            System.out.println("Tab " + i + " tip: " + tabComponent.getClass().getName());

            // OPTION 1: Tab je direktno PresentationView
            if (tabComponent instanceof PresentationView) {
                PresentationView presView = (PresentationView) tabComponent;
                scalePresentation(presView, scaleFactor);
            }
            // OPTION 2: Tab je JScrollPane koji sadrži PresentationView
            else if (tabComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) tabComponent;
                Component viewport = scrollPane.getViewport().getView();
                if (viewport instanceof PresentationView) {
                    PresentationView presView = (PresentationView) viewport;
                    scalePresentation(presView, scaleFactor);
                }
            }
            // OPTION 3: Tab je JPanel koji sadrži PresentationView
            else if (tabComponent instanceof JPanel) {
                JPanel panel = (JPanel) tabComponent;
                Component[] components = panel.getComponents();
                System.out.println("  Panel ima " + components.length + " komponenti");

                for (Component comp : components) {
                    System.out.println("    Component: " + comp.getClass().getName());

                    if (comp instanceof PresentationView) {
                        PresentationView presView = (PresentationView) comp;
                        scalePresentation(presView, scaleFactor);
                    } else if (comp instanceof JScrollPane) {
                        JScrollPane scroll = (JScrollPane) comp;
                        Component view = scroll.getViewport().getView();
                        if (view instanceof PresentationView) {
                            PresentationView presView = (PresentationView) view;
                            scalePresentation(presView, scaleFactor);
                        }
                    }
                }
            }
        }

        System.out.println("=== Skaliranje završeno ===");
    }

    private void scalePresentation(PresentationView presView, double scaleFactor) {
        System.out.println("Skaliram prezentaciju...");

        // Prođi kroz sve komponente PresentationView
        for (Component comp : presView.getComponents()) {
            System.out.println("    PresentationView component: " + comp.getClass().getName());

            // Tražimo JTabbedPane sa slajdovima
            if (comp instanceof JTabbedPane) {
                JTabbedPane slideTabPane = (JTabbedPane) comp;
                System.out.println("    Našao JTabbedPane sa " + slideTabPane.getTabCount() + " slajdova");

                scaleSlideTabPane(slideTabPane, scaleFactor);
            }
            // Ako je JScrollPane, gledaj šta je unutra
            else if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                Component view = scroll.getViewport().getView();

                if (view instanceof JTabbedPane) {
                    JTabbedPane slideTabPane = (JTabbedPane) view;
                    scaleSlideTabPane(slideTabPane, scaleFactor);
                }
            }
            // Ako je JSplitPane (stara struktura)
            else if (comp instanceof JSplitPane) {
                JSplitPane splitPane = (JSplitPane) comp;
                Component rightComp = splitPane.getRightComponent();

                if (rightComp instanceof JTabbedPane) {
                    JTabbedPane slideTabPane = (JTabbedPane) rightComp;
                    scaleSlideTabPane(slideTabPane, scaleFactor);
                }
            }
        }
    }

    private void scaleSlideTabPane(JTabbedPane slideTabPane, double scaleFactor) {
        // Prođi kroz sve slajdove
        for (int i = 0; i < slideTabPane.getTabCount(); i++) {
            Component slideComponent = slideTabPane.getComponentAt(i);

            // Slajd može biti direktno SlideView
            if (slideComponent instanceof SlideView) {
                SlideView slideView = (SlideView) slideComponent;
                slideView.setModeScaleFactor(scaleFactor);

                System.out.println(String.format(
                        "     Slajd '%s' skaliran na %.2fx",
                        slideView.getSlide().getName(),
                        scaleFactor
                ));
            }
            // Ili može biti u JScrollPane
            else if (slideComponent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) slideComponent;
                Component viewport = scrollPane.getViewport().getView();

                if (viewport instanceof SlideView) {
                    SlideView slideView = (SlideView) viewport;
                    slideView.setModeScaleFactor(scaleFactor);

                    System.out.println(String.format(
                            "    Slajd '%s' skaliran na %.2fx",
                            slideView.getSlide().getName(),
                            scaleFactor
                    ));
                }
            }
        }
    }

    public WindowMode getCurrentMode() {
        return currentMode;
    }

    public void applyDefaultMode() {
        currentMode = normalMode;
        applyCurrentMode();
    }
}