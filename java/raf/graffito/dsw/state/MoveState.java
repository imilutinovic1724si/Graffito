package raf.graffito.dsw.state;

import raf.graffito.dsw.commands.CompositeCommand;
import raf.graffito.dsw.commands.MoveElementCommand;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveState implements State{

    private SlideView slideView;
    private Point lastPoint;
    private Map<SlideElement, Point> startPositions;

    public MoveState(SlideView slideView) {
        this.slideView = slideView;
        this.startPositions = new HashMap<>();
    }

    @Override
    public void mousePressed(MouseEvent e){
        lastPoint = e.getPoint();
        startPositions.clear();

        // Zapamti početne pozicije svih selektovanih elemenata
        for (SlideElement element : slideView.getSlide().getSelectedElements()){
            startPositions.put(element,new Point(element.getLocation()));
        }
    }
    @Override
    public void mouseDragged(MouseEvent e){
        if (slideView == null || lastPoint == null) return;

        Point curr = e.getPoint();

        // Koliko je pomeren miš
        int dx  = curr.x - lastPoint.x;
        int dy  = curr.y - lastPoint.y;

        //Pomeramo sve selektovane elemente za isti offset
        for (SlideElement element : slideView.getSlide().getSelectedElements()){
            Point newLocation = new Point(element.getLocation().x + dx, element.getLocation().y + dy);
            element.setLocation(newLocation);
        }
        lastPoint = curr;
        slideView.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e){
        // Kreira komande za Undo/Redo

        if (slideView == null || startPositions.isEmpty()){
            lastPoint = null;
            return;
        }

        List<SlideElement> selectedElements = slideView.getSlide().getSelectedElements();
        if (selectedElements.isEmpty()) {
            lastPoint = null;
            startPositions.clear();
            return;
        }

        if (selectedElements.size() == 1){
            SlideElement element = selectedElements.get(0);
            Point startPos = startPositions.get(element);
            Point endPos = new Point(element.getLocation());

            if (!startPos.equals(endPos)){
                MoveElementCommand command = new MoveElementCommand(element, startPos, endPos);
                slideView.getCommandManager().executeCommand(command);
            }
        }
        else {
            CompositeCommand compositeCommand = new CompositeCommand("Pomeranje " + selectedElements.size() + " elemenata");

            for (SlideElement element : selectedElements){
                Point startPos = startPositions.get(element);
                Point endPos = new Point(element.getLocation());

                if (!startPos.equals(endPos)){
                    MoveElementCommand command = new MoveElementCommand(element, startPos, endPos);
                    compositeCommand.addCommand(command);
                }
            }

            if (!compositeCommand.isEmpty()){
                slideView.getCommandManager().executeCommand(compositeCommand);
            }
        }

        lastPoint = null;
        startPositions.clear();
    }




    // nema implementaciju
    @Override
    public void mouseClicked(MouseEvent e){

    }
}
