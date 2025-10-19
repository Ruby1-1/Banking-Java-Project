package CC12;

import javax.swing.*;
import java.awt.*;

public class PersonalCardPage extends JPanel {

    private AppFrame appFrame; // Store the AppFrame instance
    private CardManager cardManager; // Store the CardManager instance
    private Image bgImage; // Background image

    public PersonalCardPage(CardManager cardManager, AppFrame appFrame) {
        this.appFrame = appFrame; // Initialize the AppFrame instance
        this.cardManager = cardManager; // Initialize the CardManager instance

        // Load the background image
        bgImage = new ImageIcon("src/CC12/assets/personalcardpage.png").getImage(); 

        // Use null layout for absolute positioning
        setLayout(null);

       

        // Display card details
        displayCardDetails();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw the background image scaled to the panel's size
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }











    

  public void refresh() {
    System.out.println("[DEBUG] Refreshing PersonalCardPage with updated card details.");

    removeAll(); // Clear existing components

    // Reload the card data using the current user
    String currentUser = appFrame.getUserManager().getLoggedInUsername();
    cardManager.loadCardData(currentUser);
    
    // (Optional) Set layout back to null if you're using absolute positioning
    setLayout(null);

    // Display card details
    System.out.println("[DEBUG] Calling displayCardDetails from refresh...");
    displayCardDetails();

    // Back Button
    JButton backButton = new JButton();
    backButton.setFont(new Font("Arial", Font.PLAIN, 16));
    backButton.setBounds(115, 550, 105, 40); // x, y, width, height
    backButton.setContentAreaFilled(false);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setOpaque(false);
    backButton.addActionListener(e -> {
        System.out.println("[NAVIGATION] Back to PersonalDashboard");
        appFrame.switchTo("PersonalDashboard");
    });
    add(backButton);

    revalidate(); // Refresh the layout
    repaint();    // Repaint the panel
}

private void displayCardDetails() {
    System.out.println("[DEBUG] Entering displayCardDetails...");

    setLayout(null); // Use null layout for absolute positioning

    PersonalCard card = cardManager.getPersonalCard();
    if (card == null) {
        System.out.println("[ERROR] No PersonalCard found for the user.");
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
    JLabel cardNumberLabel = new JLabel( formatCardNumber(card.getCardNumber()), SwingConstants.LEFT);
    cardNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    cardNumberLabel.setBounds(50, 245, 300, 30); // x, y, width, height
    add(cardNumberLabel);

    // Expiration Date
    JLabel expLabel = new JLabel( card.getExpirationDate(), SwingConstants.LEFT);
    expLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    expLabel.setBounds(139, 280, 300, 30); // x, y, width, height
    add(expLabel);

    // CVV
    JLabel cvvLabel = new JLabel( card.getCVV(), SwingConstants.LEFT);
    cvvLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    cvvLabel.setBounds(234, 448, 300, 30); // x, y, width, height
    add(cvvLabel);

    revalidate();
    repaint();
    System.out.println("[DEBUG] Exiting displayCardDetails...");
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
