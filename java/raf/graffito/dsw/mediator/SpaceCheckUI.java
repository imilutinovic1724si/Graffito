package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.space.SlideSpaceChecker;
import raf.graffito.dsw.model.implementation.Slide;

import javax.swing.*;
import java.awt.*;

public class SpaceCheckUI extends JPanel {

    private SpaceCheckMediator mediator;

    private JComboBox<String> methodComboBox;
    private JButton checkButton;
    private JLabel statusLabel;
    private JProgressBar spaceBar;

    public SpaceCheckUI(SpaceCheckMediator mediator) {
        this.mediator = mediator;
        mediator.registerUI(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Provera prostora"));
        setPreferredSize(new Dimension(250, 150));

        // ComboBox za izbor metode
        JLabel methodLabel = new JLabel("Metoda provere:");
        methodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(methodLabel);
        add(Box.createVerticalStrut(5));

        String[] methods = {
                "Simple Area (Sabiranje površina)",
                "Pixel Matrix (Binarna matrica)"
        };
        methodComboBox = new JComboBox<>(methods);
        methodComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        methodComboBox.addActionListener(e -> {
            String selected = (String) methodComboBox.getSelectedItem();
            mediator.onMethodChanged(selected); // Delegira na Mediator!
        });
        add(methodComboBox);
        add(Box.createVerticalStrut(10));

        // Dugme za proveru
        checkButton = new JButton("Proveri prostor");
        checkButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkButton.addActionListener(e -> {
            mediator.onCheckRequested(); // Delegira na Mediator!
        });
        add(checkButton);
        add(Box.createVerticalStrut(10));

        // Progress bar za vizuelni prikaz
        spaceBar = new JProgressBar(0, 100);
        spaceBar.setStringPainted(true);
        spaceBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        add(spaceBar);
        add(Box.createVerticalStrut(5));

        // Status labela
        statusLabel = new JLabel("Status: /");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statusLabel);
    }

    // Mediator poziva ovu metodu da ažurira prikaz
    public void updateDisplay(Slide slide, SlideSpaceChecker checker) {
        if (slide == null || checker == null) {
            spaceBar.setValue(0);
            statusLabel.setText("Status: Nema slajda");
            return;
        }

        double occupiedPercentage = checker.getOccupiedPercentage(slide) * 100;
        boolean hasSpace = checker.checkSpace(slide);

        spaceBar.setValue((int) occupiedPercentage);

        if (occupiedPercentage < 50) {
            spaceBar.setForeground(Color.GREEN);
        } else if (occupiedPercentage < 80) {
            spaceBar.setForeground(Color.ORANGE);
        } else {
            spaceBar.setForeground(Color.RED);
        }

        statusLabel.setText(String.format(
                "Zauzeto: %.1f%% | %s",
                occupiedPercentage,
                hasSpace ? "Ima mesta" : "Puno"
        ));
    }
}
