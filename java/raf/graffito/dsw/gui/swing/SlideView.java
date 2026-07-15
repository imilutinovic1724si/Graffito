package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.bridge.space.CheckSpaceImplementation;
import raf.graffito.dsw.bridge.space.SimpleAreaImplementation;
import raf.graffito.dsw.bridge.space.SlideSpaceChecker;
import raf.graffito.dsw.commands.CommandManager;
import raf.graffito.dsw.mediator.SpaceCheckMediator;
import raf.graffito.dsw.mediator.SpaceCheckMediatorImpl;
import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;
import raf.graffito.dsw.observer.ISubscriber;
import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.state.StateManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.BasicStroke;


public class SlideView extends JPanel  implements ISubscriber {

    private Slide slide; // prikazujemo slajd
    private StateManager stateManager; // upravlja stanjima slajda
    private CommandManager commandManager; // za Undo/Redo
    private double zoomLevel; // zoom opcija
    private double modeScaleFactor; // skaliranje mediator
    private AffineTransform transform; // transform opcija
    private Rectangle selectionRectangle; // za crtanje pravougaonika pri selektovanju

    // BRIDGE + MEDIATOR
    private SlideSpaceChecker spaceChecker;
    private SpaceCheckMediator spaceMediator;

    public SlideView(Slide slide) {
        this.slide = slide;
        this.stateManager = new StateManager(this);
        this.commandManager = new CommandManager();
        CheckSpaceImplementation defaultImpl = new SimpleAreaImplementation();
        this.spaceChecker = new SlideSpaceChecker(defaultImpl);

        this.spaceMediator = new SpaceCheckMediatorImpl();
        this.spaceMediator.registerChecker(spaceChecker);
        this.spaceMediator.onSlideChanged(slide);

        this.zoomLevel = 1.0;
        this.modeScaleFactor = 1.0;
        this.transform = new AffineTransform();


        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600)); // fiksna dimenzija za sve slajdove

        // osluškuje klikove miša
        addMouseListener(new MouseAdapter() {

            // poziva se kada pritisnemo dugme miša, ali ga nismo pustili
            @Override
            public void mousePressed(MouseEvent e){
                stateManager.getCurrentState().mousePressed(transformEvent(e));
                repaint();
            }

            // poziva se kada pustimo dugme miša - završetak drag operacije
            @Override
            public void mouseReleased(MouseEvent e){
                stateManager.getCurrentState().mouseReleased(transformEvent(e));
                repaint();
            }

            // za običan klik
            @Override
            public void mouseClicked(MouseEvent e){
                stateManager.getCurrentState().mouseClicked(transformEvent(e));
                repaint();
            }
        });

        // osluškuje povlačenje (drag)
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                stateManager.getCurrentState().mouseDragged(transformEvent(e));
                repaint();
            }
        });

        // skrolovanje za zumiranje
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double delta = -e.getPreciseWheelRotation()*0.1;
                zoomLevel += delta;

                //limitiramo zum
                if (zoomLevel<0.5) zoomLevel = 0.5;
                if (zoomLevel>3.0) zoomLevel = 3.0;

                repaint();
            }
        });

        // prečice na tastaturi za UNDO/REDO
        setupKeyboardShortcuts();
    }


    private void setupKeyboardShortcuts(){
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Ctrl+Z za Undo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
        actionMap.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        // Ctrl+Y za Redo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
        actionMap.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
    }

    public void undo(){
        commandManager.undo();
        repaint();
    }
    public void redo(){
        commandManager.redo();
        repaint();
    }
    public void setModeScaleFactor(double factor) {
        System.out.println("!!! PRE: modeScaleFactor = " + this.modeScaleFactor);
        System.out.println("!!! POSTAVLJAM NA: " + factor);

        this.modeScaleFactor = factor;

        System.out.println("!!! POSLE: modeScaleFactor = " + this.modeScaleFactor);

        // Ažuriraj preferred size
        int newWidth = (int) (800 * factor);
        int newHeight = (int) (600 * factor);

        setPreferredSize(new Dimension(newWidth, newHeight));
        setSize(newWidth, newHeight);
        setMinimumSize(new Dimension(newWidth, newHeight));

        invalidate();

        // Ako je SlideView u JScrollPane, revalidate parent
        Container parent = getParent();
        if (parent != null) {
            parent.invalidate();
            parent.revalidate();
        }

        revalidate();
        repaint();

        System.out.println(String.format(
                "SlideView skalirano: %.2fx - %dx%d (modeScaleFactor sada: %.2f)",
                factor, newWidth, newHeight, this.modeScaleFactor
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // crta pozadinu i briše stari sadržaj
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double totalScale = zoomLevel * modeScaleFactor;

        g2d.scale(totalScale, totalScale);

        //crtamo sve elemente
        for (SlideElement element : slide.getElementi()){
            element.paint(g2d);
        }

        //crtamo pravougaonik za selekciju
        if (selectionRectangle != null){
            g2d.setColor(new Color(0, 120, 215, 80));
            g2d.fill(selectionRectangle);
            g2d.setColor(new Color(0, 120, 215));
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(selectionRectangle);
        }
    }


    //transformise koordinate misa u koordinate slajda (zbog zoom-a)
    private Point transformPoint(Point point) {
        double totalScale = zoomLevel * modeScaleFactor;
        return new Point(
                (int)(point.x / totalScale),
                (int)(point.y / totalScale)
        );
    }
    private MouseEvent transformEvent(MouseEvent event) {
        Point transformed = transformPoint(event.getPoint());
        return  new MouseEvent(
                event.getComponent(),
                event.getID(),
                event.getWhen(),
                event.getModifiersEx(),
                transformed.x,
                transformed.y,
                event.getClickCount(),
                event.isPopupTrigger(),
                event.getButton()
        );
    }
    //JAVNE METODE za proveru prostora
    public boolean canAddElement() {
        return spaceChecker.checkSpace(slide);
    }

    public double getOccupiedPercentage() {
        return spaceChecker.getOccupiedPercentage(slide);
    }

    public SpaceCheckMediator getSpaceMediator() {
        return spaceMediator;
    }

    // geteri i seteri
    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }
    public void setSelectionRectangle(Rectangle selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
    }
    public Slide getSlide() {
        return slide;
    }
    public AffineTransform getTransform() {
        return transform;
    }
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
    public double getZoomLevel() {
        return zoomLevel;
    }
    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
    public StateManager getStateManager() {
        return stateManager;
    }
    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }
    public CommandManager getCommandManager() {
        return commandManager;
    }

    // osvežavamo prikaz - Observer šablon
    @Override
    public void update(Message message) {
        repaint();
    }

}
