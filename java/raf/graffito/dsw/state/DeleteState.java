package raf.graffito.dsw.state;

import raf.graffito.dsw.commands.CompositeCommand;
import raf.graffito.dsw.commands.DeleteElementCommand;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.event.MouseEvent;
import java.util.List;

public class DeleteState implements State{
    private SlideView slideView;

    public DeleteState(SlideView slideView) {
       this.slideView = slideView;
    }

    @Override
    public void mousePressed(MouseEvent e){
        if (slideView == null) return;

        //Brisemo selektovane elemente
        List<SlideElement> selectedElements = slideView.getSlide().getSelectedElements();

        if (selectedElements.isEmpty()) return;

        if (selectedElements.size() == 1) {
            DeleteElementCommand command = new DeleteElementCommand(slideView.getSlide(), selectedElements.get(0));
            slideView.getCommandManager().executeCommand(command);
        }
        else {
            CompositeCommand compositeCommand = new CompositeCommand("Brisanje " + selectedElements.size() + " elemenata");

            for (SlideElement element : selectedElements) {
                DeleteElementCommand command = new DeleteElementCommand(slideView.getSlide(), element);
                compositeCommand.addCommand(command);
            }

            slideView.getCommandManager().executeCommand(compositeCommand);
        }

        slideView.repaint();

    }



    // nemaju implementaciju
    @Override
    public void mouseDragged(MouseEvent e){

    }
    @Override
    public void mouseReleased(MouseEvent e){

    }
    @Override
    public void mouseClicked(MouseEvent e){

    }
}
