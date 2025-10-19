package CC12;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    private Image bgImage;

    public HomePage(AppFrame appFrame) {
        // Load the background image.
        bgImage = new ImageIcon("src/CC12/assets/homep.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        // Ensure the panel is opaque to draw the background.
        setOpaque(true);

        // Login Button
        JButton loginButton = new JButton();
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(false);
        loginButton.setBounds(70, 300, 195, 30);
        loginButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Navigating to ProfileSelectionPage");
            appFrame.switchTo("ProfileSelectionPage"); // Ensure this page exists in AppFrame
        });
        add(loginButton);

        // Register Button
        JButton registerButton = new JButton();
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setBounds(70, 340, 195, 30);
        registerButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Navigating to RoleSelectionPage");
            appFrame.switchTo("RoleSelectionPage"); // Ensure this page exists in AppFrame
        });
        add(registerButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering.
        Graphics2D g2 = (Graphics2D) g;
        // Apply bilinear interpolation for a slightly blurred (softer) scaling.
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw the background image scaled to the panel's size.
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
