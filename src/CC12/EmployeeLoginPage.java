package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class EmployeeLoginPage extends JPanel {
    private Image bgImage;

    public EmployeeLoginPage(AppFrame appFrame) {
        // Load the background image for the employee login page.
        bgImage = new ImageIcon("src/CC12/assets/emplog.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        setOpaque(true); // Ensure the panel is opaque so the background image is drawn.

        // Employee ID Field
        JTextField employeeIdField = new JTextField();
        employeeIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        employeeIdField.setBounds(78, 362, 180, 40);
        employeeIdField.setOpaque(false);
        employeeIdField.setBackground(new Color(0, 0, 0, 0));
        employeeIdField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(employeeIdField, "Employee ID");
        add(employeeIdField);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(78, 417, 180, 40);
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(passwordField, "Password");
        add(passwordField);

        // Login Button
        JButton loginButton = new JButton();
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setBounds(74, 465, 190, 35);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(false);
        add(loginButton);

        // Back Button
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
            String employeeId = employeeIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (employeeId.isEmpty() || password.isEmpty() || employeeId.equals("Employee ID") || password.equals("Password")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean valid = false;
            String companyId = null;
            String empName = null;

            try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
                while (scanner.hasNextLine()) {
                    String[] empData = scanner.nextLine().split(",", -1);
                    // Format: empname,empid,password,companyid,cardnumber,expdate,cvv,YES/NO
                    if (empData.length >= 4 &&
                        empData[1].trim().equals(employeeId) &&
                        empData[2].trim().equals(password)) {
                        valid = true;
                        companyId = empData[3].trim();
                        empName = empData[0].trim();
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error reading employee data!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (valid) {
                // Track the logged-in employee and their company
                UserManager userManager = appFrame.getUserManager();
                userManager.setLoggedInUsername(empName); // Store name if needed
                userManager.setLoggedInEmployeeId(employeeId); // Store employee ID for access checks
                userManager.setLoggedInCompanyId(companyId);

                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                appFrame.switchTo("EMPdash");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother background rendering.
        Graphics2D g2 = (Graphics2D) g;
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
