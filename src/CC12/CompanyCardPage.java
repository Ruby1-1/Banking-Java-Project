package CC12;

import javax.swing.*;
import java.awt.*;

public class CompanyCardPage extends JPanel {

    private AppFrame appFrame; // Store the AppFrame instance
    private CompanyCardManager companyCardManager; // Store the CompanyCardManager instance
    private Image bgImage; // Background image

    public CompanyCardPage(CompanyCardManager companyCardManager, AppFrame appFrame) {
        this.appFrame = appFrame; // Initialize the AppFrame instance
        this.companyCardManager = companyCardManager; // Initialize the CompanyCardManager instance

        // Load the background image
        bgImage = new ImageIcon("src/CC12/assets/companycardpage.png").getImage();

        // Use null layout for absolute positioning
        setLayout(null);

        // Display card details
        displayCardDetails();

        // Add the back button
        addBackButton();
    }

    /**
     * Refreshes the page with updated card details.
     */
    public void refresh() {
        System.out.println("[DEBUG] Refreshing CompanyCardPage with updated card details.");

        removeAll(); // Clear existing components
        setLayout(null); // Reset layout

        // Reload the card data using the current company ID
        String loggedInCompanyId = appFrame.getUserManager().getLoggedInCompanyId();
        companyCardManager.loadCompanyCardData(loggedInCompanyId);

        // Display card details
        System.out.println("[DEBUG] Calling displayCardDetails from refresh...");
        displayCardDetails();

        // Add the back button
        addBackButton();

        // Refresh the layout
        revalidate();
        repaint();
    }

    /**
     * Displays the company card details.
     */
    private void displayCardDetails() {
        System.out.println("[DEBUG] Entering displayCardDetails...");

        CompanyCard card = companyCardManager.getCompanyCard();
        if (card == null) {
            System.out.println("[ERROR] No CompanyCard found for the company.");
            JLabel errorLabel = new JLabel("No card details available.", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            errorLabel.setBounds(50, 100, 240, 30); // x, y, width, height
            add(errorLabel);
            return;
        }

        System.out.println("[DEBUG] Card details found. Displaying...");
        System.out.println("[DEBUG] Card Number: " + card.getCardNumber());
        System.out.println("[DEBUG] Expiration Date: " + card.getExpirationDate());
        System.out.println("[DEBUG] CVV: " + card.getCVV());

        // Card Number
        JLabel cardNumberLabel = new JLabel(formatCardNumber(card.getCardNumber()), SwingConstants.LEFT);
        cardNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        cardNumberLabel.setBounds(50, 245, 300, 30); // x, y, width, height
        add(cardNumberLabel);

        // Expiration Date
        JLabel expLabel = new JLabel(card.getExpirationDate(), SwingConstants.LEFT);
        expLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        expLabel.setBounds(139, 280, 300, 30); // x, y, width, height
        add(expLabel);

        // CVV
        JLabel cvvLabel = new JLabel(card.getCVV(), SwingConstants.LEFT);
        cvvLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        cvvLabel.setBounds(234, 448, 300, 30); // x, y, width, height
        add(cvvLabel);

        System.out.println("[DEBUG] Exiting displayCardDetails...");
    }

    /**
     * Adds a back button to the page.
     */
    private void addBackButton() {
        System.out.println("[DEBUG] Adding back button...");

        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(115, 550, 105, 40); // Top-left corner
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Back to OwnerDashboard");
            appFrame.switchTo("OwnerDashboard");
        });
        add(backButton);

        System.out.println("[DEBUG] Back button added.");
    }

    /**
     * Paints the background image.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensure existing components are painted first
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Formats the card number as XXXX-XXXX-XXXX-XXXX if it is 16 digits long.
     *
     * @param number The card number to format.
     * @return The formatted card number or the original number if not 16 digits.
     */
    private String formatCardNumber(String number) {
        if (number != null && number.length() == 16) {
            return number.replaceAll("(.{4})(?!$)", "$1-");
        }
        return "Invalid Card Number";
    }
}
