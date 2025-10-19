package CC12;

import javax.swing.*;
import java.awt.*;


public class EmpmanagerPage extends JPanel {

    private AppFrame appFrame; // Reference to the main application frame

    public EmpmanagerPage(AppFrame appFrame) {
        this.appFrame = appFrame; // Initialize the AppFrame instance

        // Use null layout for absolute positioning
        setLayout(null);

        // Add the back button
        addBackButton();

        // Add the "Add Employee" button
        addAddEmployeeButton();

        // Add the "Remove Employee" button
        addRemoveEmployeeButton();

        // Add the "Give Card Access" button
        addGiveCardAccessButton();
    }

    /**
     * Adds a back button to the page.
     */
    private void addBackButton() {
        System.out.println("[DEBUG] Adding back button...");

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 10, 100, 40); // Top-left corner
        backButton.setContentAreaFilled(true);
        backButton.setBackground(Color.LIGHT_GRAY); // Set a visible background color
        backButton.setBorderPainted(true);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Back to OwnerDashboard");
            appFrame.switchTo("OwnerDashboard");
        });
        add(backButton);

        System.out.println("[DEBUG] Back button added.");
    }

    /**
     * Adds the "Add Employee" button to the page.
     */
    private void addAddEmployeeButton() {
        System.out.println("[DEBUG] Adding Add Employee button...");

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addEmployeeButton.setBounds(100, 100, 150, 40); // Position the button
        addEmployeeButton.setContentAreaFilled(true);
        addEmployeeButton.setBackground(Color.LIGHT_GRAY); // Set a visible background color
        addEmployeeButton.setBorderPainted(true);
        addEmployeeButton.setFocusPainted(false);
        addEmployeeButton.setOpaque(true);
        addEmployeeButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Switching to AddEmpPage");
            appFrame.switchTo("AddEmpPage");
        });
        add(addEmployeeButton);

        System.out.println("[DEBUG] Add Employee button added.");
    }

    /**
     * Adds the "Remove Employee" button to the page.
     */
    private void addRemoveEmployeeButton() {
        System.out.println("[DEBUG] Adding Remove Employee button...");

        JButton removeEmployeeButton = new JButton("Remove Employee");
        removeEmployeeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        removeEmployeeButton.setBounds(100, 160, 150, 40); // Position below the Add Employee button
        removeEmployeeButton.setContentAreaFilled(true);
        removeEmployeeButton.setBackground(Color.LIGHT_GRAY);
        removeEmployeeButton.setBorderPainted(true);
        removeEmployeeButton.setFocusPainted(false);
        removeEmployeeButton.setOpaque(true);
        removeEmployeeButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Switching to RemoveEmp");
            appFrame.switchTo("RemoveEmp");
        });
        add(removeEmployeeButton);

        System.out.println("[DEBUG] Remove Employee button added.");
    }

    /**
     * Adds the "Give Card Access" button to the page.
     */
    private void addGiveCardAccessButton() {
        System.out.println("[DEBUG] Adding Give Card Access button...");

        JButton giveCardAccessButton = new JButton("Give Card Access");
        giveCardAccessButton.setFont(new Font("Arial", Font.PLAIN, 16));
        giveCardAccessButton.setBounds(100, 220, 150, 40); // Position below the Remove Employee button
        giveCardAccessButton.setContentAreaFilled(true);
        giveCardAccessButton.setBackground(Color.LIGHT_GRAY);
        giveCardAccessButton.setBorderPainted(true);
        giveCardAccessButton.setFocusPainted(false);
        giveCardAccessButton.setOpaque(true);
        giveCardAccessButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Switching to GIVEcardacc");
            appFrame.switchTo("GIVEcardacc");
        });
        add(giveCardAccessButton);

        System.out.println("[DEBUG] Give Card Access button added.");
    }

    /**
     * Paints the background (optional, can be customized later).
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensure existing components are painted first
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(Color.WHITE); // Set a default background color
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}

