package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.mediator.SpaceCheckUI;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;
import raf.graffito.dsw.model.decorator.ColorDecorator;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProjectView extends JPanel implements ISubscriber {

    private JTabbedPane tabbedPane;
    private JLabel projectNameLabel;
    private JLabel authorLabel;
    private JLabel presentationNameLabel;

    // Novi paneli za galeriju i proveru prostora
    private JPanel imageListPanel;
    private JScrollPane imageScrollPane;
    private SpaceCheckUI spaceCheckUI;

    private Project current;
    private Map<Project, ColorDecorator> projectDecorators;
    private Map<Presentation, PresentationView> presentationViews;
    private Set<Color> usedColors;

    public ProjectView(){
        projectDecorators = new HashMap<>();
        presentationViews = new HashMap<>();
        usedColors = new HashSet<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.EAST);
        tabbedPane.addChangeListener(e->{
            updateInfoLabels();
            notifyCurrentPresentationOfSlideChange();
        });
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(new Color(240,240,240));
        panel.setPreferredSize(new Dimension(280, 0));

        // Informacije o prezentaciji
        presentationNameLabel = new JLabel("Prezentacija: /");
        projectNameLabel = new JLabel("Projekat: /");
        authorLabel = new JLabel("Autor: /");
        Font font = new Font("Arial",Font.BOLD, 13);
        presentationNameLabel.setFont(font);
        projectNameLabel.setFont(font);
        authorLabel.setFont(font);

        presentationNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        projectNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(presentationNameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(projectNameLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(authorLabel);
        panel.add(Box.createVerticalStrut(15));

        // Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(15));

        // Galerija slika - povećana
        JLabel galerijaLabel = new JLabel("Galerija slika");
        galerijaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        galerijaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(galerijaLabel);
        panel.add(Box.createVerticalStrut(8));

        imageListPanel = new JPanel();
        imageListPanel.setLayout(new BoxLayout(imageListPanel, BoxLayout.Y_AXIS));
        imageListPanel.setBackground(Color.WHITE);
        imageListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        imageListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel imageListWrapper = new JPanel(new BorderLayout());
        imageListWrapper.add(imageListPanel, BorderLayout.NORTH);

        imageScrollPane = new JScrollPane(imageListWrapper);
        imageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        imageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        imageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        imageScrollPane.setPreferredSize(new Dimension(250, 600));
        imageScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageScrollPane.setBorder(null);

        panel.add(imageScrollPane);
        panel.add(Box.createVerticalStrut(15));

        // Provera prostora
        JLabel proveraLabel = new JLabel("Provera prostora");
        proveraLabel.setFont(new Font("Arial", Font.BOLD, 12));
        proveraLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(proveraLabel);
        panel.add(Box.createVerticalStrut(8));

        JPanel spaceCheckPlaceholder = new JPanel();
        spaceCheckPlaceholder.setLayout(new BorderLayout());
        spaceCheckPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);
        spaceCheckPlaceholder.setPreferredSize(new Dimension(250, 180));
        spaceCheckPlaceholder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        spaceCheckPlaceholder.setName("spaceCheckPlaceholder");
        spaceCheckPlaceholder.setBackground(Color.WHITE);

        panel.add(spaceCheckPlaceholder);
        panel.add(Box.createVerticalStrut(10));

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    public void openProject(Project project) {
        if (project == null) {return;}

        if (this.current != null){
            this.current.removeSubscriber(this);
        }
        this.current = project;
        this.current.addSubscriber(this);

        closeAllTabs();

        ColorDecorator decorator = projectDecorators.get(project);
        if (decorator == null){
            decorator = new ColorDecorator(project);
            projectDecorators.put(project, decorator);
        }

        if (decorator.getColor() == null){
            Color selectedColor = chooseColor();
            if (selectedColor != null){
                decorator.setColor(selectedColor);
                usedColors.add(selectedColor);
            } else {
                decorator.setColor(Color.LIGHT_GRAY);
                usedColors.add(Color.LIGHT_GRAY);
            }
        }

        if (project instanceof GraffNodeComposite){
            GraffNodeComposite composite = (GraffNodeComposite) project;
            for (GraffNode child : composite.getChildren()) {
                if (child instanceof Presentation) {
                    openPresentation((Presentation) child);
                }
            }
        }
        updateInfoLabels();
    }

    public void openPresentation(Presentation presentation) {
        if (presentation == null || presentationViews.containsKey(presentation)) {
            return;
        }

        GraffNode parent = presentation.getParent();
        Project project = null;

        if (parent instanceof Project){
            project = (Project) parent;
        }
        else {
            while (parent != null && !(parent instanceof Project)){
                parent = parent.getParent();
            }
            project = (Project) parent;
        }

        if (project == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Prezentacija mora pripadati projektu");
            return;
        }

        ColorDecorator decorator = projectDecorators.get(project);
        if (decorator == null) {
            decorator = new ColorDecorator(project);
            projectDecorators.put(project, decorator);
        }

        if (decorator.getColor() == null) {
            Color selectedColor = chooseColor();
            if (selectedColor != null) {
                decorator.setColor(selectedColor);
                usedColors.add(selectedColor);
            } else {
                decorator.setColor(Color.LIGHT_GRAY);
                usedColors.add(Color.LIGHT_GRAY);
            }
        }

        Color tabColor = decorator.getColor();

        PresentationView presentationView = new PresentationView(presentation, this);
        presentationViews.put(presentation, presentationView);
        presentation.addSubscriber(presentationView);

        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(tabColor);
        tabPanel.add(presentationView, BorderLayout.CENTER);
        tabbedPane.addTab(presentation.getName(), tabPanel);
        int tabIndex = tabbedPane.getTabCount() - 1;
        setCustomTabColor(tabIndex, tabColor, presentation.getName());
        tabbedPane.setSelectedIndex(tabIndex);

        updateInfoLabels();
    }

    private Color getContrastColor(Color backgroundColor) {
        double luminance = (0.299 * backgroundColor.getRed() + 0.587 * backgroundColor.getGreen() + 0.114 * backgroundColor.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    public void closePresentation(Presentation presentation) {
        PresentationView  view = presentationViews.remove(presentation);
        if (view != null){
            presentation.removeSubscriber(view);
            for (int i =0; i<tabbedPane.getTabCount(); i++){
                if (tabbedPane.getComponentAt(i) instanceof JPanel){
                    JPanel panel = (JPanel) tabbedPane.getComponentAt(i);
                    if (panel.getComponentCount()>0 && panel.getComponent(0)== view){
                        tabbedPane.removeTabAt(i);
                        break;
                    }
                }
            }
            updateInfoLabels();
        }
    }

    public void closeAllTabs() {
        for (Map.Entry<Presentation, PresentationView> entry:presentationViews.entrySet()) {
            entry.getKey().removeSubscriber(entry.getValue());
        }
        tabbedPane.removeAll();
        presentationViews.clear();

        // Očisti galeriju i proveru prostora
        imageListPanel.removeAll();
        imageListPanel.revalidate();
        imageListPanel.repaint();

        if (spaceCheckUI != null) {
            // Pronađi i ukloni spaceCheckUI
            JPanel infoPanel = (JPanel) getComponent(1); // Info panel je EAST component
            for (Component comp : infoPanel.getComponents()) {
                if (comp.getName() != null && comp.getName().equals("spaceCheckPlaceholder")) {
                    ((JPanel) comp).removeAll();
                    comp.revalidate();
                    comp.repaint();
                    break;
                }
            }
            spaceCheckUI = null;
        }

        updateInfoLabels();
    }

    private Color chooseColor() {
        while (true){
            Color color = JColorChooser.showDialog(this, "Izaberite boju za prezentaciju", Color.WHITE);

            if (color == null) return  null;

            if (usedColors.contains(color)){
                JOptionPane.showMessageDialog(this,
                        "Ova boja je vec iskoristecena",
                        "Boja zauzeta",
                        JOptionPane.WARNING_MESSAGE);
            }else{
                return color;
            }
        }
    }

    private void updateInfoLabels() {
        int selectedIndex = tabbedPane.getSelectedIndex();

        if (selectedIndex>=0 && current!=null){
            String tabTitle = tabbedPane.getTitleAt(selectedIndex);
            presentationNameLabel.setText("Prezentacija: " + tabTitle);
            projectNameLabel.setText("Projekat: " + current.getName());
            authorLabel.setText("Autor: " + current.getAuthor());
        }else{
            presentationNameLabel.setText("Prezentacija: /");
            projectNameLabel.setText("Projekat: /");
            authorLabel.setText("Autor: /");
        }
    }

    private void updatePresentationTabName(Presentation presentation) {
        PresentationView view = presentationViews.get(presentation);
        if (view == null) return;

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component component = tabbedPane.getComponentAt(i);

            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getComponentCount() > 0 && panel.getComponent(0) == view) {
                    Color currentColor = tabbedPane.getBackgroundAt(i);
                    tabbedPane.setTitleAt(i, presentation.getName());
                    if (currentColor != null) {
                        tabbedPane.setBackgroundAt(i, currentColor);
                        tabbedPane.setForegroundAt(i, getContrastColor(currentColor));
                    }
                    break;
                }
            }
        }
    }

    // Metoda koju PresentationView poziva kada se promeni slajd
    public void updateSpaceCheckUI(SlideView slideView) {
        // Pronađi placeholder panel
        Component[] components = getComponents();
        JPanel infoPanel = null;

        for (Component comp : components) {
            if (comp instanceof JPanel && ((JPanel) comp).getComponentCount() > 3) {
                infoPanel = (JPanel) comp;
                break;
            }
        }

        if (infoPanel == null) return;

        for (Component comp : infoPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals("spaceCheckPlaceholder")) {
                JPanel placeholder = (JPanel) comp;
                placeholder.removeAll();

                if (slideView != null) {
                    // Kreiraj ili ažuriraj SpaceCheckUI
                    if (spaceCheckUI == null) {
                        spaceCheckUI = new SpaceCheckUI(slideView.getSpaceMediator());
                    }

                    placeholder.add(spaceCheckUI, BorderLayout.CENTER);
                    slideView.getSpaceMediator().onSlideChanged(slideView.getSlide());
                }

                placeholder.revalidate();
                placeholder.repaint();
                break;
            }
        }
    }

    private void notifyCurrentPresentationOfSlideChange() {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        if (selectedTabIndex < 0) {
            updateSpaceCheckUI(null);
            return;
        }

        Component tabComponent = tabbedPane.getComponentAt(selectedTabIndex);
        if (tabComponent instanceof JPanel) {
            JPanel tabPanel = (JPanel) tabComponent;
            if (tabPanel.getComponentCount() > 0) {
                Component firstComp = tabPanel.getComponent(0);
                if (firstComp instanceof PresentationView) {
                    PresentationView presView = (PresentationView) firstComp;
                    presView.notifySlideChanged();
                }
            }
        }
    }

    public Project getCurrentProject() {
        return current;
    }

    public Map<Project, ColorDecorator> getPresentationDecorators(){
        return projectDecorators;
    }

    public JPanel getImageListPanel() {
        return imageListPanel;
    }

    @Override
    public void update(Message message) {
        SwingUtilities.invokeLater(()->{
            if (current == null) return;

            String content = message.getContent().toLowerCase();
            System.out.println("ProjectView primio: " + content);

            if(content.contains("dodat") || content.contains("obrisan")){
                refreshPresentations();
                updateInfoLabels();
            }
            else if(content.contains("element")) {
                repaint();
                revalidate();
            }
            else if (content.contains("naslov promenjen") || content.contains("autor promenjen")){
                updateInfoLabels();
            }
            else if(content.contains("preimenovan")){
                updateInfoLabels();

                for (Presentation p: presentationViews.keySet()){
                    updatePresentationTabName(p);
                }
            }
        });
    }

    private void refreshPresentations() {
        if (current == null){
            return;
        }

        Set<Presentation> currentPresentations = new HashSet<>();
        if (current instanceof GraffNodeComposite){
            GraffNodeComposite composite = (GraffNodeComposite) current;
            for (GraffNode child : composite.getChildren()){
                if (child instanceof Presentation){
                    currentPresentations.add((Presentation) child);
                }
            }
        }

        Set<Presentation> removePres = new HashSet<>();
        for (Presentation p: presentationViews.keySet()){
            if (!currentPresentations.contains(p)){
                removePres.add(p);
            }
        }

        for (Presentation p: removePres){
            closePresentation(p);
        }

        for (Presentation p: currentPresentations){
            if (!presentationViews.containsKey(p)){
                openPresentation(p);
            }
        }
    }

    public void closeProject(Project project) {
        if (project == null) return;
        closeAllTabs();

        ColorDecorator decorator = projectDecorators.get(project);
        if (decorator == null) {
            decorator = new ColorDecorator(project);
            projectDecorators.put(project, decorator);
        }
        if (decorator.getColor() == null) {
            Color selectedColor = chooseColor();
            if (selectedColor != null) {
                decorator.setColor(selectedColor);
                usedColors.add(selectedColor);
            } else {
                decorator.setColor(Color.LIGHT_GRAY);
                usedColors.add(Color.LIGHT_GRAY);
            }
        }

        projectDecorators.remove(project);

        if (current == project) {
            current.removeSubscriber(this);
            current = null;
        }
        updateInfoLabels();
    }

    private void setCustomTabColor(int tabIndex, Color color, String title) {
        JLabel tabLabel = new JLabel(title);
        tabLabel.setOpaque(true);
        tabLabel.setBackground(color);
        tabLabel.setForeground(getContrastColor(color));
        tabLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tabbedPane.setTabComponentAt(tabIndex, tabLabel);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}