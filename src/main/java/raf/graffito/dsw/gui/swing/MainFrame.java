package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.bridge.window.WindowModeUI;
import raf.graffito.dsw.controller.ActionManager;
import raf.graffito.dsw.mediator.WindowModeMediator;
import raf.graffito.dsw.mediator.WindowModeMediatorImpl;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.observer.MessageType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame implements ISubscriber {

    // Singleton
    private static MainFrame instance;
    private ActionManager actionManager;
    private GraffTreeImplementation tree; // levi panel -> stablo
    private ProjectView projectView; // desni panel -> project view
    private JTabbedPane tabbedPane; // dodato  - tabovi za prezentacije
    private JSplitPane mainSplitPane;// dodato - split izmedju stabla i ostatka

    private WindowModeMediator windowModeMediator;
    private WindowModeUI windowModeUI;
    private Dimension originalSize;


    private MainFrame() {
    }
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
            instance.initialize();
        }
        return instance;
    }

    // postavlja sve komponente MainFrame-a
    private void initialize() {
        actionManager = new ActionManager();

        // Kreiranje glavnog prozora
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        //  Zapamti originalnu veličinu
        originalSize = new Dimension(screenSize.width / 2, screenSize.height / 2);
        setSize(originalSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Graffito");

        // Inicijalizuj Window Mode Mediator
        windowModeMediator = new WindowModeMediatorImpl(originalSize);
        windowModeMediator.registerFrame(this);

        // Meni bar
        MyMenuBar menu = new MyMenuBar();
        setJMenuBar(menu);

        // Tool bar
        MyToolBar toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);

        // Kreiranje stabla
        tree = new GraffTreeImplementation();
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(250, 0));

        // Window Mode UI
        windowModeUI = new WindowModeUI(windowModeMediator);

        // Levi panel sa stablom i WindowModeUI
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(windowModeUI, BorderLayout.SOUTH);

        // ProjectView
        projectView = new ProjectView();

        // SplitPane između levog panela i ProjectView
        mainSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftPanel,
                projectView
        );
        mainSplitPane.setDividerLocation(250);
        mainSplitPane.setResizeWeight(0.2);

        add(mainSplitPane, BorderLayout.CENTER);

        windowModeMediator.applyDefaultMode();

    }

    // geteri
    public static void setInstance(MainFrame instance) {
        MainFrame.instance = instance;
    }
    public ActionManager getActionManager() {
        return actionManager;
    }
    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }
    public GraffTreeImplementation getTree() {
        return tree;
    }
    public void setTree(GraffTreeImplementation tree) {
        this.tree = tree;
    }
    public ProjectView getProjectView() {
        return projectView;
    }
    public void setProjectView(ProjectView projectView) {
        this.projectView = projectView;
    }
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
    public JSplitPane getMainSplitPane() {
        return mainSplitPane;
    }
    public void setMainSplitPane(JSplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
    }
    public WindowModeMediator getWindowModeMediator() {
        return windowModeMediator;
    }

    // dodajemo metode iz interfejsa ISubscriber
    // MainFrame dobija obavestenja kao prozorcice
    @Override
    public void update(Message message) {
        // Mainframe izbacuje samo ERROR i INFO
        // WARNING se ignosrise
        if (message.getType() == MessageType.WARNING){
            return;
        }
        // U suprotnom izbacuje dijalog
        showMessageDialog(message);
    }
    // kreiranje tog dialog prozorcica
    private void showMessageDialog(Message message) {
        int messageType;
        String title;

        switch (message.getType()) {
            case ERROR:
                messageType = JOptionPane.ERROR_MESSAGE;
                title = "Error";
                break;
            case INFO:
                messageType = JOptionPane.INFORMATION_MESSAGE;
                title = "Info";
                break;
            case WARNING:
                messageType = JOptionPane.WARNING_MESSAGE;
                title = "Warning";
                break;
            default:
                messageType = JOptionPane.PLAIN_MESSAGE;
                title = "Message";
        }
        JOptionPane.showMessageDialog(this, message.getContent(), title, messageType);
    }
}
