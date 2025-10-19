package CC12;

import javax.swing.*;
import java.awt.*;

public class PersonalLoginPage extends JPanel {
    private Image bgImage;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public PersonalLoginPage(AppFrame appFrame) {
        // Load the background image for the personal login page.
        bgImage = new ImageIcon("src/CC12/assets/personallog.png").getImage();

        // Use null layout for absolute positioning.
        setLayout(null);
        setOpaque(true); // Ensure the panel is opaque to draw the background.

        // Initialize UI components
        initializeFields();
        initializeButtons(appFrame);
    }

    /**
     * Initializes the username and password fields.
     */
    private void initializeFields() {
        // Username Field
        usernameField = new JTextField("Username");
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBounds(78, 362, 180, 40);
        usernameField.setOpaque(false);
        usernameField.setBackground(new Color(0, 0, 0, 0));
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(usernameField, "Username");
        add(usernameField);

        // Password Field
        passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(78, 417, 180, 40);
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        addPlaceholderBehavior(passwordField, "Password");
        add(passwordField);
    }

    /**
     * Initializes the login and back buttons.
     */
    private void initializeButtons(AppFrame appFrame) {
        // Login Button
        JButton loginButton = new JButton();
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setBounds(74, 465, 190, 35);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> handleLogin(appFrame));
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
    }

    /**
     * Handles the login process.
     */
    private void handleLogin(AppFrame appFrame) {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    if (username.isEmpty() || username.equals("Username") ||
        password.isEmpty() || password.equals("Password")) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields!");
        return;
    }

    // Delegate user validation to UserManager
    UserManager userManager = appFrame.getUserManager();
    if (userManager.isValidUser(username, password, "personal")) {
        userManager.setLoggedInUsername(username); // Set the logged-in username
        System.out.println("[DEBUG] Logged-in username set to: " + username);

        // Delegate card loading to CardManager
        CardManager cardManager = appFrame.getCardManager();
        cardManager.loadCardData(username); // Load the user's card data

        JOptionPane.showMessageDialog(this, "Login Successful as Personal!");
        appFrame.switchTo("PersonalDashboard"); // Navigate to the PersonalDashboard
    } else {
        JOptionPane.showMessageDialog(this, "Invalid Credentials!");
    }
}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Clear input fields when the page becomes visible.
            usernameField.setText("Username");
            usernameField.setForeground(Color.LIGHT_GRAY);
            passwordField.setText("Password");
            passwordField.setForeground(Color.LIGHT_GRAY);
        }
    }

    /**
     * Adds placeholder behavior to a JTextField.
     */
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

    /**
     * Adds placeholder behavior to a JPasswordField.
     */
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
