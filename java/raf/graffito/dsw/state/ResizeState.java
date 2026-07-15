package raf.graffito.dsw.state;

import raf.graffito.dsw.commands.CompositeCommand;
import raf.graffito.dsw.commands.ResizeElementCommand;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResizeState implements State {

    private SlideView slideView;
    private Point startPoint;
    private Map<SlideElement,Dimension> startDimensions;

    public ResizeState(SlideView slideView) {
        this.slideView = slideView;
        this.startDimensions = new HashMap<>();
    }

    @Override
    public void mousePressed(MouseEvent e){
        startPoint = e.getPoint();
        startDimensions.clear();

        // Pamti sve početne dimenzije
        for (SlideElement element : slideView.getSlide().getSelectedElements()){
            startDimensions.put(element, new Dimension(element.getDimension()));
        }
    }
    @Override
    public void mouseDragged(MouseEvent e){
        if (slideView == null || startPoint == null) return;

        Point curr = e.getPoint();
        int deltaWidth = curr.x - startPoint.x;
        int deltaHeight = curr.y - startPoint.y;

        //resizujem sve selektovane elem
        for (SlideElement element : slideView.getSlide().getSelectedElements()){
            Dimension newDimension = new Dimension(Math.max(20, element.getDimension().width + deltaWidth), Math.max(20, element.getDimension().height + deltaHeight));
            element.setDimension(newDimension);
        }
        slideView.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e){
        if (slideView == null || startDimensions.isEmpty()) {
            startPoint = null;
            return;
        }

        List<SlideElement> selectedElements = slideView.getSlide().getSelectedElements();

        if (selectedElements.isEmpty()) {
            startPoint = null;
            startDimensions.clear();
            return;
        }

        if (selectedElements.size() == 1) {
            SlideElement element = selectedElements.get(0);
            Dimension startDim = startDimensions.get(element);
            Dimension endDim = new Dimension(element.getDimension());

            if (!startDim.equals(endDim)) {
                ResizeElementCommand command = new ResizeElementCommand(element, startDim, endDim);
                slideView.getCommandManager().executeCommand(command);
            }
        }
        else {
            CompositeCommand compositeCommand = new CompositeCommand("Resize " + selectedElements.size() + " elemenata");

            for (SlideElement element : selectedElements) {
                Dimension startDim = startDimensions.get(element);
                Dimension endDim = new Dimension(element.getDimension());

                if (!startDim.equals(endDim)) {
                    ResizeElementCommand command = new ResizeElementCommand(element, startDim, endDim);
                    compositeCommand.addCommand(command);
                }
            }

            if (!compositeCommand.isEmpty()){
                slideView.getCommandManager().executeCommand(compositeCommand);
            }
        }

        startPoint = null;
        startDimensions.clear();
    }




    // nema implementaciju
    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
