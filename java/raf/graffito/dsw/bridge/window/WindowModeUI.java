package raf.graffito.dsw.bridge.window;

import raf.graffito.dsw.mediator.WindowModeMediator;

import javax.swing.*;
import java.awt.*;

public class WindowModeUI extends JPanel {
    private WindowModeMediator mediator;

    private JRadioButton normalButton;
    private JRadioButton fullscreenButton;
    private JRadioButton smallButton;

    private JLabel statusLabel;

    public WindowModeUI(WindowModeMediator mediator) {
        this.mediator = mediator;
        mediator.registerUI(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Režim prozora"));
        setPreferredSize(new Dimension(200, 150));

        // Button group za radio buttons
        ButtonGroup group = new ButtonGroup();

        // Normal button
        normalButton = new JRadioButton("Normal");
        normalButton.setSelected(true); // Default
        normalButton.addActionListener(e -> {
            mediator.onModeSelected("Normal"); // Delegira na Mediator!
        });
        group.add(normalButton);
        add(normalButton);

        // Fullscreen button
        fullscreenButton = new JRadioButton("Fullscreen");
        fullscreenButton.addActionListener(e -> {
            mediator.onModeSelected("Fullscreen"); // Delegira na Mediator!
        });
        group.add(fullscreenButton);
        add(fullscreenButton);

        // Small button
        smallButton = new JRadioButton("Small (2x manji)");
        smallButton.addActionListener(e -> {
            mediator.onModeSelected("Small"); // Delegira na Mediator!
        });
        group.add(smallButton);
        add(smallButton);

        add(Box.createVerticalStrut(10));

        // Status labela
        statusLabel = new JLabel("Trenutni režim: Normal");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statusLabel);
    }
    // Mediator poziva ovu metodu da ažurira prikaz
    public void updateDisplay(WindowMode mode) {
        if (mode == null) return;

        statusLabel.setText(String.format(
                "Režim: %s (%.1fx)",
                mode.getModeName(),
                mode.getScaleFactor()
        ));
    }
}
