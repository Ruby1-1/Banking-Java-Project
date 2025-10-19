package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;    
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class PersonalRegisterPage extends JPanel {
    private JTextField nameField;
    private JPasswordField passwordField;
    private Image bgImage;

    public PersonalRegisterPage(AppFrame appFrame) {
        // Load your background image.
        bgImage = new ImageIcon("src/CC12/assets/personalreg.png").getImage();
        
       
        setLayout(null);
        setOpaque(false);

        // Name Field (adjust coordinates to align with PNG)
        nameField = new JTextField("Name");
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBounds(80, 272, 200, 40);
        nameField.setOpaque(false); // Make field transparent
        nameField.setBackground(new Color(0, 0, 0, 0)); // fully transparent background
nameField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(nameField, "Name");
        add(nameField);

        // Password Field (adjust coordinates to align with PNG)
        passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(80, 355, 200, 40);
        passwordField.setOpaque(false); // Make field transparent
        passwordField.setBackground(new Color(0, 0, 0, 0));
passwordField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(passwordField, "Password");
        add(passwordField);

        // Register Button (adjust coordinates to align with PNG)
        JButton registerButton = new JButton();
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setBounds(70, 514, 236, 40);
        // Make button transparent but clickable:
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        add(registerButton);

        // Back Button (adjust coordinates to align with PNG)
        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(42, 18, 30, 30); // Adjust as needed
        // Make button transparent but clickable:
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        add(backButton);

        // Register Button Action
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (name.isEmpty() || name.equals("Name") || password.isEmpty() || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            } else if (!name.matches("^[a-zA-Z0-9 ]+$")) {
                // Allows letters, numbers, and spaces. Special characters are disallowed.
                JOptionPane.showMessageDialog(this, "Name must contain only letters and numbers!");
            } else if (password.length() < 8) {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long!");
            } else if (isUsernameTaken(name)) {
                JOptionPane.showMessageDialog(this, "Username already taken! Please choose a different username.");
            } else {
                try (FileWriter writer = new FileWriter("users.txt", true)) {
                    writer.write(name + "," + password + ",personal\n");
                    JOptionPane.showMessageDialog(this, "Personal Registered Successfully!");
                    appFrame.switchTo("HomePage");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving user data!");
                }
            }
        });

        // Back Button Action
        backButton.addActionListener(e -> appFrame.switchTo("RoleSelectionPage"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image scaled to the panel's size.
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Clear all input fields when the page becomes visible.
            nameField.setText("Name");
            nameField.setForeground(Color.LIGHT_GRAY);
            passwordField.setText("Password");
            passwordField.setForeground(Color.LIGHT_GRAY);
        }
    }

   private boolean isUsernameTaken(String username) {
    try (Scanner scanner = new Scanner(new File("users.txt"))) {
        while (scanner.hasNextLine()) {
            String[] user = scanner.nextLine().split(",");
            if (user[0].equalsIgnoreCase(username)) { // Case-insensitive comparison
                return true; // Username already exists
            }
        }
    } catch (IOException ex) {
        System.out.println("Error reading user data!");
    }
    return false; // Username is available
}

    // Helper method to add placeholder behavior for JTextField.
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

    // Overloaded helper method for JPasswordField.
    private void addPlaceholderBehavior(JPasswordField field, String placeholder) {
        field.setForeground(Color.LIGHT_GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
    }
}
