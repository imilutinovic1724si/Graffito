package raf.graffito.dsw.state;

import raf.graffito.dsw.commands.AddElementCommand;
import raf.graffito.dsw.controller.SlideVewHelper;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.ImageElement;
import raf.graffito.dsw.model.elementi.LogoElement;
import raf.graffito.dsw.model.elementi.TextElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AddState implements State {

    private SlideView slideView;

    public AddState(SlideView slideView) {
        this.slideView = slideView;
    }


    @Override
    public void mousePressed(MouseEvent e){
        Point clickPoint = e.getPoint();

        // pitaj korisnika šta želi da doda
        String[] options = {"Text", "Logo", "Slika"};
        int choice = JOptionPane.showOptionDialog(slideView, "Sta želite da dodate?", "Dodaj element", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(choice == 0){
            // dodaj tekst
            addTextElement(clickPoint);
        }else if (choice == 1){
            // dodaj logo
            addLogoElement(clickPoint);
        }else if (choice == 2){
            // dodaj sliku
            addImageElement(clickPoint);
        }
    }

    private void addTextElement(Point location){
        // Unesi tekst koji hoćeš da dodaš
        String tekst = JOptionPane.showInputDialog(slideView, "Unesite tekst: ");
        if (tekst == null || tekst.trim().isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Tekst ne sme biti prazan");
            return;
        }
        TextElement textElement = new TextElement(location, new Dimension(200, 50), tekst);

        // kreiraj komandu i izvrši je za undo/redo
        AddElementCommand command = new AddElementCommand(slideView.getSlide(), textElement);
        slideView.getCommandManager().executeCommand(command);

        slideView.repaint();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Tekst element dodat");
    }
    private void addLogoElement(Point location){
        LogoElement logoElement = new LogoElement(location, new Dimension(200, 50));

        AddElementCommand command = new AddElementCommand(slideView.getSlide(), logoElement);
        slideView.getCommandManager().executeCommand(command);

        slideView.repaint();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Logo element dodat");
    }
    private void addImageElement(Point location){
        // Uzmi listu učitanih slika iz galerije
        PresentationView currentPresView = SlideVewHelper.getCurrentPresentationView();
        if (currentPresView == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Nema otvorene prezentacije");
            return;
        }

        // Proverava da li ima učitanih slika
        JPanel imageListPanel = currentPresView.getImageListPanel();
        Component[] components = imageListPanel.getComponents();
        if (components.length == 0){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Nema učitanih slika, prvo učitajte sliku");
            return;
        }

        // Dijalog sa listom slika
        // Svaka slika je reprezentovana kao dugme
        java.util.List<JButton> imageButtons = new java.util.ArrayList<>();
        for (Component component : components) {
            if (component instanceof JButton){
                imageButtons.add((JButton) component);
            }
        }
        // Ako nema slika
        if (imageButtons.isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Nema učitanih slika");
            return;
        }
        //Ako ima samo jednu odmah je dodaj
        if (imageButtons.size() == 1) {
            JButton button = imageButtons.get(0);
            BufferedImage image = (BufferedImage) button.getClientProperty("originalImage");
            String fileNAme = (String) button.getClientProperty("fileName");
            ImageElement imageElement = new ImageElement(location, new Dimension(200, 150), image);

            AddElementCommand command = new AddElementCommand(slideView.getSlide(), imageElement);
            slideView.getCommandManager().executeCommand(command);

            slideView.repaint();
            ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Slika '" + fileNAme + "' dodata!");
            return;
        }

            //Ako ih ima više treba da izabere koju hoće
            String[] fileNames = new String[imageButtons.size()];
            for (int i = 0; i<imageButtons.size(); i++){
                fileNames[i] = (String) imageButtons.get(i).getClientProperty("fileName");
            }
            String selected = (String) JOptionPane.showInputDialog(slideView, "Izaberite sliku:", "Dodaj sliku", JOptionPane.QUESTION_MESSAGE, null, fileNames, fileNames[0]);
            if (selected == null) return;

            //Nađi selektovanu sliku
             for (JButton button : imageButtons) {
                 String fileName = (String) button.getClientProperty("fileName");
                 if (fileName.equals(selected)){
                     BufferedImage image = (BufferedImage) button.getClientProperty("originalImage");
                     ImageElement imageElement = new ImageElement(location, new Dimension(200, 150), image);

                     AddElementCommand command = new AddElementCommand(slideView.getSlide(), imageElement);
                     slideView.getCommandManager().executeCommand(command);

                     slideView.repaint();
                     ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Slika '" + fileName + "' dodata!");
                     break;
                 }
             }

        }




        // Nemaju implementaciju
    @Override
    public void mouseDragged(MouseEvent e){

    }
    @Override
    public void mouseReleased(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){}

}
