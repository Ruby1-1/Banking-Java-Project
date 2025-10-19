package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class OwnerRegisterPage extends JPanel {
    private JTextField companyNameField;
    private JTextField companyIdField;
    private JTextField ownerNameField;
    private JPasswordField passwordField;
    private Image bgImage;

    public OwnerRegisterPage(AppFrame appFrame) {
        // Load the background image for the Owner Register page.
        bgImage = new ImageIcon("src/CC12/assets/ownerreg.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        setOpaque(false);

        // Company Name Field
        companyNameField = new JTextField("Company Name");
        companyNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        companyNameField.setBounds(80, 308, 200, 40);
        companyNameField.setOpaque(false);
        companyNameField.setBackground(new Color(0, 0, 0, 0));
        companyNameField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(companyNameField, "Company Name");
        add(companyNameField);

        // Company ID Field
        companyIdField = new JTextField("Company ID");
        companyIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        companyIdField.setBounds(80, 377, 200, 40);
        companyIdField.setOpaque(false);
        companyIdField.setBackground(new Color(0, 0, 0, 0));
        companyIdField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(companyIdField, "Company ID");
        add(companyIdField);

        // Owner Name Field
        ownerNameField = new JTextField("Owner Name");
        ownerNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        ownerNameField.setBounds(80, 242, 200, 40);
        ownerNameField.setOpaque(false);
        ownerNameField.setBackground(new Color(0, 0, 0, 0));
        ownerNameField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(ownerNameField, "Owner Name");
        add(ownerNameField);

        // Password Field
        passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(80, 442, 200, 40);
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(passwordField, "Password");
        add(passwordField);

        // Register Button
        JButton registerButton = new JButton();
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setBounds(69, 511, 220, 36);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        add(registerButton);

        // Back Button
        JButton backButton = new JButton();
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(42, 18, 30, 30);
        add(backButton);

        // Register Button Action
        registerButton.addActionListener(e -> {
            String companyName = companyNameField.getText().trim();
            String companyId = companyIdField.getText().trim();
            String ownerName = ownerNameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (companyName.isEmpty() || companyName.equals("Company Name") ||
                companyId.isEmpty() || companyId.equals("Company ID") ||
                ownerName.isEmpty() || ownerName.equals("Owner Name") ||
                password.isEmpty() || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            } else if (!companyId.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Company ID must be numeric!");
            } else if (!ownerName.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Owner Name must contain only letters!");
            } else if (password.length() < 8) {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long!");
            } else if (isUsernameTaken(ownerName)) {
                JOptionPane.showMessageDialog(this, "Username already taken! Please choose a different username.");
            } else if (isCompanyIdTaken(companyId)) {
                JOptionPane.showMessageDialog(this, "Company ID already exists! Please choose a different Company ID.");
            } else {
                try (FileWriter writer = new FileWriter("users.txt", true)) {
                    // Save the details in the new format
                    writer.write(ownerName + "," + password + ",owner, , , ," + companyId + "," + companyName + "\n");
                    JOptionPane.showMessageDialog(this, "Owner Registered Successfully!");
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
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            companyNameField.setText("Company Name");
            companyNameField.setForeground(Color.LIGHT_GRAY);
            companyIdField.setText("Company ID");
            companyIdField.setForeground(Color.LIGHT_GRAY);
            ownerNameField.setText("Owner Name");
            ownerNameField.setForeground(Color.LIGHT_GRAY);
            passwordField.setText("Password");
            passwordField.setForeground(Color.LIGHT_GRAY);
        }
    }

    /**
     * Checks if the given username is already taken.
     *
     * @param username The username to check.
     * @return True if the username is already taken, false otherwise.
     */
    private boolean isUsernameTaken(String username) {
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] user = scanner.nextLine().split(",");
                if (user[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading user data!");
        }
        return false;
    }

    /**
     * Checks if the given companyId is already taken.
     *
     * @param companyId The company ID to check.
     * @return True if the company ID is already taken, false otherwise.
     */
    private boolean isCompanyIdTaken(String companyId) {
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] user = scanner.nextLine().split(",");
                if (user.length >= 7 && user[6].equals(companyId)) { // Check the companyId field
                    return true;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading user data!");
        }
        return false;
    }

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
