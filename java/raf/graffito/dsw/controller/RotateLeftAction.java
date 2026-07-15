package raf.graffito.dsw.controller;

import raf.graffito.dsw.commands.CompositeCommand;
import raf.graffito.dsw.commands.RotateElementCommand;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

import java.awt.event.ActionEvent;
import java.util.List;

public class RotateLeftAction extends AbstractGraffAction{

    public RotateLeftAction() {
        putValue(SMALL_ICON, loadIcon("/images/rotate_left.png"));
        putValue(NAME, "Rotate Left");
        putValue(SHORT_DESCRIPTION, "Rotiraj 90 stepeni ulevo");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView == null) return;

        List<SlideElement> selectedElements = currentSlideView.getSlide().getSelectedElements();

        if (selectedElements.isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Nije selektovan ni jedan element.");
            return;
        }

        if (selectedElements.size() == 1){
            SlideElement element = selectedElements.get(0);
            double oldRotation = element.getRotation();
            double newRotation = oldRotation - Math.PI / 2;

            RotateElementCommand command = new RotateElementCommand(element, oldRotation, newRotation);
            currentSlideView.getCommandManager().executeCommand(command);
        }
        else {
            CompositeCommand compositeCommand = new CompositeCommand("Rotacija " + selectedElements.size() + " elemenata");

            for (SlideElement element : selectedElements){
                double oldRotation = element.getRotation();
                double newRotation = oldRotation - Math.PI / 2;

                RotateElementCommand command = new RotateElementCommand(element, oldRotation, newRotation);
                compositeCommand.addCommand(command);
            }

            currentSlideView.getCommandManager().executeCommand(compositeCommand);
        }

        currentSlideView.repaint();
        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Elementi rotirani ulevo");
    }

}
