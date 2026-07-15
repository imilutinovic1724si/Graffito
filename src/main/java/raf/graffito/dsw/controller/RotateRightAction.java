package raf.graffito.dsw.controller;

import raf.graffito.dsw.commands.CompositeCommand;
import raf.graffito.dsw.commands.RotateElementCommand;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.model.implementation.Slide;

import java.awt.event.ActionEvent;
import java.util.List;

public class RotateRightAction extends AbstractGraffAction{

    public RotateRightAction() {
        putValue(SMALL_ICON, loadIcon("/images/rotate_right.png"));
        putValue(NAME, "Rotate Right");
        putValue(SHORT_DESCRIPTION, "Rotiraj 90 stepeni udesno");
    }
    @Override
    public void actionPerformed(ActionEvent e){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView == null) {
            return;
        }

        List<SlideElement> selectedElements = currentSlideView.getSlide().getSelectedElements();

        if (selectedElements.isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Nije selektovan nijedan element");
            return;
        }

        if (selectedElements.size() == 1){
            SlideElement element = selectedElements.get(0);
            double oldRotation = element.getRotation();
            double newRotation = oldRotation + Math.PI / 2;
            RotateElementCommand command = new RotateElementCommand(element, oldRotation, newRotation);
            currentSlideView.getCommandManager().executeCommand(command);
        }
        else {
            CompositeCommand compositeCommand = new CompositeCommand("Rotacija " + selectedElements.size() + " elemenata");

            for (SlideElement element : selectedElements) {
                double oldRotation = element.getRotation();
                double newRotation = oldRotation + Math.PI / 2;

                RotateElementCommand command = new RotateElementCommand(element, oldRotation, newRotation);
                compositeCommand.addCommand(command);
            }

            currentSlideView.getCommandManager().executeCommand(compositeCommand);
        }

        currentSlideView.repaint();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Elementi rotirani udesno!");
    }
}
