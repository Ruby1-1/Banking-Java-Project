package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OwnerLoginPage extends JPanel {
    private Image bgImage;

    public OwnerLoginPage(AppFrame appFrame) {
        // Load the background image for the owner login page.
        bgImage = new ImageIcon("src/CC12/assets/ownerlog.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        // Ensure the panel is opaque so the background image is drawn.
        setOpaque(true);

        // Company ID Field
        JTextField companyIdField = new JTextField("Company ID");
        companyIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        companyIdField.setBounds(78, 362, 180, 40);
        companyIdField.setOpaque(false);
        companyIdField.setBackground(new Color(0, 0, 0, 0));
        companyIdField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(companyIdField, "Company ID");
        add(companyIdField);

        // Password Field
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(78, 417, 180, 40);
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(passwordField, "Password");
        add(passwordField);

        // Login Button (invisible but functional)
        JButton loginButton = new JButton();
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setBounds(74, 465, 190, 35);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(false);
        add(loginButton);

        // Back Button (invisible but functional)
        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(45, 53, 30, 30);
        backButton.addActionListener(e -> appFrame.switchTo("ProfileSelectionPage"));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        add(backButton);

        // Login Button Action
        loginButton.addActionListener(event -> {
            String companyId = companyIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            if (companyId.isEmpty() || companyId.equals("Company ID") ||
                password.isEmpty() || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            } else {
                try (Scanner scanner = new Scanner(new File("users.txt"))) {
                    boolean valid = false;
                    while (scanner.hasNextLine()) {
                        String[] user = scanner.nextLine().split(",");
                        // Updated format: name,password,role,card number,exp date,cvv,company id,company name
                        if (user.length >= 8 &&
                            user[6].equals(companyId) && // Check company ID
                            user[1].equals(password) && // Check password
                            user[2].equals("owner")) {  // Check role
                            valid = true;

                            // Track the logged-in user in UserManager
                            appFrame.getUserManager().setLoggedInUsername(user[0]); // Set username
                           // After setting the logged-in company ID
appFrame.getUserManager().setLoggedInCompanyId(user[6]); // Set company ID
System.out.println("[DEBUG] Logged-in company ID: " + user[6]);

                            JOptionPane.showMessageDialog(this, "Login Successful as Owner!");
                            appFrame.switchTo("OwnerDashboard");
                            break;
                        }
                    }
                    if (!valid) {
                        JOptionPane.showMessageDialog(this, "Invalid Credentials!");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error reading user data!");
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering.
        Graphics2D g2 = (Graphics2D) g;
        // Apply bilinear interpolation for smoother, softer scaling.
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw the background image scaled to the panel's current size.
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    // Helper method to add placeholder behavior for text fields.
    private void addPlaceholderBehavior(JTextField field, String placeholder) {
        field.setForeground(Color.LIGHT_GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
    }
}
