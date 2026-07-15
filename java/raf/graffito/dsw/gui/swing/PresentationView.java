package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Slide;
import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PresentationView extends JPanel implements ISubscriber {
    private Presentation presentation;
    private JTabbedPane slideTabPane;
    private Map<Slide, SlideView> slideViewMap;
    private ProjectView parentProjectView;

    public PresentationView(Presentation presentation, ProjectView parentProjectView) {
        this.presentation = presentation;
        this.parentProjectView = parentProjectView;
        this.slideViewMap = new HashMap<>();

        initializeUI();
        loadSlides();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        slideTabPane = new JTabbedPane();
        slideTabPane.setTabPlacement(JTabbedPane.TOP);

        add(slideTabPane, BorderLayout.CENTER);

        slideTabPane.addChangeListener(e -> onSlideChanged());
    }

    private void onSlideChanged() {
        int selectedIndex = slideTabPane.getSelectedIndex();
        if (selectedIndex < 0) {
            if (parentProjectView != null) {
                parentProjectView.updateSpaceCheckUI(null);
            }
            return;
        }

        Component component = slideTabPane.getComponentAt(selectedIndex);

        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            Component view = scrollPane.getViewport().getView();

            if (view instanceof SlideView) {
                SlideView slideView = (SlideView) view;

                // Obavesti ProjectView da ažurira SpaceCheckUI
                if (parentProjectView != null) {
                    parentProjectView.updateSpaceCheckUI(slideView);
                }
            }
        }
    }

    public void notifySlideChanged() {
        onSlideChanged();
    }

    private void loadSlides() {
        slideTabPane.removeAll();
        slideViewMap.clear();

        if (presentation instanceof GraffNodeComposite){
            GraffNodeComposite comp = (GraffNodeComposite) presentation;
            for(GraffNode child : comp.getChildren()){
                if (child instanceof Slide){
                    Slide slide = (Slide) child;
                    SlideView slideView = new SlideView(slide);
                    slideViewMap.put(slide, slideView);

                    JScrollPane scrollPane = new JScrollPane(slideView);
                    slideTabPane.addTab(slide.getName(), scrollPane);

                    slide.addSubscriber(slideView);
                }
            }
        }

        // Nakon učitavanja slajdova, ažuriraj SpaceCheckUI za prvi slajd
        if (slideTabPane.getTabCount() > 0) {
            slideTabPane.setSelectedIndex(0);
            onSlideChanged();
        }

        revalidate();
        repaint();
    }

    public void refreshSlides() {
        loadSlides();
    }

    @Override
    public void update(Message message) {
        SwingUtilities.invokeLater(() -> {
            String content = message.getContent().toLowerCase();

            if (content.contains("dodat")
                    || content.contains("obrisan") ||
                    content.contains("preimenovan")){
                refreshSlides();
            }
        });
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public JPanel getImageListPanel() {
        return parentProjectView.getImageListPanel();
    }

    public SlideView getCurrentSlideView() {
        int selectedIndex = slideTabPane.getSelectedIndex();
        if (selectedIndex < 0) return null;

        Component component = slideTabPane.getComponentAt(selectedIndex);
        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            Component view = scrollPane.getViewport().getView();
            if (view instanceof SlideView) {
                return (SlideView) view;
            }
        }
        return null;
    }

    public JTabbedPane getSlideTabPane() {
        return slideTabPane;
    }

    public Map<Slide, SlideView> getSlideViewMap() {
        return slideViewMap;
    }
}