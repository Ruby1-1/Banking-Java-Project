package CC12;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionPage extends JPanel {
    private Image bgImage;

    public RoleSelectionPage(AppFrame appFrame) {
        // Load the background image for the Role Selection page.
        bgImage = new ImageIcon("src/CC12/assets/rolesel.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        // Set the panel as opaque so the background image is drawn.
        setOpaque(true);

        // Owner Button 
        JButton ownerButton = new JButton();
        ownerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        ownerButton.setBounds(68, 340, 220, 45); // x, y, width, height
        ownerButton.addActionListener(e -> appFrame.switchTo("OwnerRegisterPage"));
        ownerButton.setContentAreaFilled(false);
        ownerButton.setBorderPainted(false);
        ownerButton.setFocusPainted(false);
        ownerButton.setOpaque(false);
        add(ownerButton);

        // Personal Button 
        JButton personalButton = new JButton();
        personalButton.setFont(new Font("Arial", Font.PLAIN, 18));
        personalButton.setBounds(68, 535, 220, 45); // x, y, width, height
        personalButton.addActionListener(e -> appFrame.switchTo("PersonalRegisterPage"));
        personalButton.setContentAreaFilled(false);
        personalButton.setBorderPainted(false);
        personalButton.setFocusPainted(false);
        personalButton.setOpaque(false);
        add(personalButton);

        // Back Button 
        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(42, 18, 30, 30); 
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
        // Use Graphics2D for smoother rendering.
        Graphics2D g2 = (Graphics2D) g;
        // Apply bilinear interpolation for softer scaling.
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw the background image scaled to the panel's current size.
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
