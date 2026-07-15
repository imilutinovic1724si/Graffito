package raf.graffito.dsw.controller;

import javax.swing.*;
import raf.graffito.dsw.gui.swing.MainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class AboutUs extends AbstractGraffAction{
    // klasa nasledjuje AbstractGraffAction koja nasledjuje AbstractAction
    // kreira prozor sa nasim podacima

    public AboutUs() {
        putValue(NAME, "About Us"); //samo naziv i opis
        putValue(SHORT_DESCRIPTION, "About Us");
    }

    // u ovoj metodi se sve kreira
    @Override
    public void actionPerformed(ActionEvent e){
        JDialog dialog = new JDialog(MainFrame.getInstance(), "O nama", true);
        // MainFrame.getInstance je roditelj tj, moramo da znamo od koga je ovaj novi prozor
        // True - oznacava da rucno moramo da ga zatvorimo
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(MainFrame.getInstance());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // ima ulogu velikog kontejnera
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240,240, 245));

        // naslov
        JLabel title = new JLabel("O timu");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        // manji kontejner
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new GridLayout(1, 2, 20, 0));
        teamPanel.setBackground(new Color(240, 240, 245));

        // kartice za nas dve -> dole postoji metoda koja ih dizajnira
        JPanel member1 = createMemberCard("images/iva.png","Iva Milutinovic", "SI 17/2024");
        teamPanel.add(member1);
        JPanel member2 = createMemberCard("images/elena.png","Elena Mijajlovic", "SI 14/2024");
        teamPanel.add(member2);

        mainPanel.add(teamPanel); // mali panel zalepimo na veliki
        mainPanel.add(Box.createVerticalStrut(20));

        // dugme za gasenje prozora (a moze i na x)
        JButton closeButton = new JButton("Zatvori");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(event -> dialog.dispose());
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        mainPanel.add(closeButton);

        dialog.add(mainPanel); //na glavi prozor smo dodali mainPanel i prikazali ga
        dialog.setVisible(true);
    }

    // Ova metoda tacno kreira izgled nasih kartica i vraca ih
    private JPanel createMemberCard(String photoPath, String name, String indexNumber) {
       JPanel card = new JPanel();
       card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
       card.setBackground(Color.WHITE);
       card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
       JLabel photoLabel = new JLabel();

       try{
           URL imageURL = getClass().getClassLoader().getResource(photoPath);
           if(imageURL != null) {
               ImageIcon icon = new ImageIcon(imageURL);
               Image img = icon.getImage().getScaledInstance(80, 80,Image.SCALE_SMOOTH);
               photoLabel.setIcon(new ImageIcon(img));
           }
       }catch(Exception e) {
           photoLabel.setText("Greska");
           photoLabel.setFont(new Font("Arial", Font.PLAIN, 40));
       }

       photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       card.add(photoLabel);
       card.add(Box.createVerticalStrut(10));

       JLabel nameLabel = new JLabel(name);
       nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
       nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       card.add(nameLabel);

       card.add(Box.createVerticalStrut(10));

       JLabel indexLabel =  new JLabel("Broj indeksa "+ indexNumber);
       indexLabel.setFont(new Font("Arial", Font.PLAIN, 14));
       indexLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       card.add(indexLabel);

       card.setMaximumSize(new Dimension(200, 150));
       return card;
    }
}
