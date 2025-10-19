package CC12;

import javax.swing.*;
import java.awt.*;

public class ProfileSelectionPage extends JPanel {
    private Image bgImage;

    public ProfileSelectionPage(AppFrame appFrame) {
        // Load the background image for the profile selection page.
        bgImage = new ImageIcon("src/CC12/assets/profilesel.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        // Set the panel as non-opaque so the background image shows.
        setOpaque(false);

        // Employee Button (transparent but functional)
        JButton employeeButton = new JButton();
        employeeButton.setFont(new Font("Arial", Font.PLAIN, 18));
        employeeButton.setBounds(43, 513, 250, 60);  // x, y, width, height
        employeeButton.addActionListener(e -> appFrame.switchTo("EmployeeLoginPage"));
        employeeButton.setContentAreaFilled(false);
        employeeButton.setBorderPainted(false);
        employeeButton.setFocusPainted(false);
        employeeButton.setOpaque(false);
        add(employeeButton);

        // Owner Button (transparent but functional)
        JButton ownerButton = new JButton();
        ownerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        ownerButton.setBounds(43, 434, 250, 60);  // Adjust as needed
        ownerButton.addActionListener(e -> appFrame.switchTo("OwnerLoginPage"));
        ownerButton.setContentAreaFilled(false);
        ownerButton.setBorderPainted(false);
        ownerButton.setFocusPainted(false);
        ownerButton.setOpaque(false);
        add(ownerButton);

        // Personal Button (transparent but functional)
        JButton personalButton = new JButton();
        personalButton.setFont(new Font("Arial", Font.PLAIN, 18));
        personalButton.setBounds(43, 355, 250, 60);  // Adjust as needed
        personalButton.addActionListener(e -> appFrame.switchTo("PersonalLoginPage"));
        personalButton.setContentAreaFilled(false);
        personalButton.setBorderPainted(false);
        personalButton.setFocusPainted(false);
        personalButton.setOpaque(false);
        add(personalButton);

        // Back Button (transparent but functional)
        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(40, 54, 40, 40);  // Adjust as needed
        backButton.addActionListener(e -> appFrame.switchTo("HomePage"));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image scaled to the panel's current size.
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
