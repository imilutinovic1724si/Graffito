package raf.graffito.dsw.controller;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.gui.swing.PresentationView;
import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.ImageElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class LoadImageAction extends AbstractGraffAction{

    public LoadImageAction() {
        putValue(SMALL_ICON, loadIcon("/images/load.png"));
        putValue(NAME, "Load Image");
        putValue(SHORT_DESCRIPTION, "Učitaj sliku/slike sa diska");
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // File chooser za izbor slika
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Izaberite slike");

        // omogući multiple selection
        fileChooser.setMultiSelectionEnabled(true);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Slike (PNG, JPG, JPEG, GIF, BMP)", "png", "jpg", "jpeg", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(MainFrame.getInstance());

        if (result == JFileChooser.APPROVE_OPTION) {
           // učitaj jedan ili više fajlova odjednom
            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles.length == 0) {
                // single selection
                File singleFile = fileChooser.getSelectedFile();
                if (singleFile != null) {
                    selectedFiles = new File[]{singleFile};
                }
            }

            // Učitaj sve selektovane slike
            int successCount = 0;
            int failCount = 0;

            for (File selectedFile : selectedFiles) {
                try {
                    // Učitaj sliku
                    BufferedImage image = ImageIO.read(selectedFile);

                    if (image == null) {
                        failCount++;
                        System.err.println("Nije moguće učitati sliku: " + selectedFile.getName());
                        continue;
                    }

                    addImageToPanel(image, selectedFile.getName());
                    successCount++;

                } catch (Exception ex) {
                    failCount++;
                    System.err.println("Greška pri učitavanju slike " + selectedFile.getName() + ": " + ex.getMessage());
                }
            }

            // Prikazi rezultat
            if (successCount > 0) {
                ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Uspešno učitano " + successCount + " slika" + (failCount > 0 ? " (" + failCount + " neuspešno)" : ""));
            } else {
                ApplicationFramework.getInstance().getMessageGenerator().generateError("Nije moguće učitati slike");
            }
        }
    }

    // dodavanje slike u galeriju
    private void addImageToPanel(BufferedImage image, String filename){
        PresentationView currentPresView = SlideVewHelper.getCurrentPresentationView();
        if (currentPresView == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Nema otvorene prezentacije");
            return;
        }
        JPanel imageListPanel = currentPresView.getImageListPanel();

        // Thumbnail - umanjeni prikaz
        Image thumbnailImage = image.getScaledInstance(120, 90, Image.SCALE_SMOOTH);
        ImageIcon thumbnailIcon = new ImageIcon(thumbnailImage);

        // Dugme sa slikom
        JButton imageButton = new JButton(thumbnailIcon);
        imageButton.setPreferredSize(new Dimension(130, 100));
        imageButton.setMaximumSize(new Dimension(130, 100));
        imageButton.setToolTipText(filename);

        // izgled dugmeta
        imageButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        imageButton.setFocusPainted(false);

        // Čuvamo originalnu sliku u button property
        imageButton.putClientProperty("originalImage", image);
        imageButton.putClientProperty("fileName", filename);

        // Klik na sliku - dodaj je na slajd
        imageButton.addActionListener(ev -> {
            addImageToCurrentSlide(image, filename);
        });

        imageListPanel.add(imageButton);
        imageListPanel.add(Box.createVerticalStrut(5)); // ✅ IZMENJENO - Vertikalni razmak
        imageListPanel.revalidate();
        imageListPanel.repaint();
    }

    // Dodaje sliku na trenutni slajd
    private void addImageToCurrentSlide(BufferedImage image, String filename){
        SlideView currentSlideView = SlideVewHelper.getCurrentSlideView();
        if (currentSlideView == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Nema otvorenog slajda");
            return;
        }

        // Dodaj sliku u centar slajda
        Point location = new Point(200, 150);
        Dimension dimension = new Dimension(200, 150);

        ImageElement imageElement = new ImageElement(location, dimension, image);
        currentSlideView.getSlide().addElement(imageElement);
        currentSlideView.repaint();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Slika '" + filename + "' dodata na slajd");
    }
}