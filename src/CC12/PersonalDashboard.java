package CC12;

import javax.swing.*;
import java.awt.*;

public class PersonalDashboard extends JPanel {
    private JLabel balanceLabel;
    private JLabel welcomeLabel;
    private Image bgImage;

    public PersonalDashboard(AppFrame appFrame) {
        // Load the background image
        bgImage = new ImageIcon("src/CC12/assets/personald.png").getImage();

        // Use null layout for absolute positioning
        setLayout(null);
        setOpaque(true);

        // Balance Display
        balanceLabel = new JLabel("Balance: PHP 0.00", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 40));
        balanceLabel.setBackground(new Color(0, 0, 0, 0));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder());
        balanceLabel.setBounds(-35, 140, 400, 30); // x, y, width, height
        balanceLabel.setForeground(Color.WHITE);

        add(balanceLabel);

        

        // Welcome Message
        String username = appFrame.getUserManager().getLoggedInUsername();
        welcomeLabel = new JLabel((username != null ? username : "Guest"), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD |Font.ITALIC, 20));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setOpaque(false);
        welcomeLabel.setBounds(55, 324, 240, 30); // x, y, width, height
        add(welcomeLabel);

        // Create Personal Card Button
        JButton createCardButton = new JButton();
        createCardButton.setFont(new Font("Arial", Font.PLAIN, 16));
        createCardButton.setBounds(30, 480, 107, 90); // x, y, width, height
        createCardButton.setContentAreaFilled(false);
        createCardButton.setBorderPainted(false);
        createCardButton.setFocusPainted(false);
        createCardButton.setOpaque(false);
        createCardButton.addActionListener(e -> handleCardCreation(appFrame.getCardManager(), appFrame.getUserManager()));
        add(createCardButton);

        // View Card Details Button
        JButton viewCardDetailsButton = new JButton();
        viewCardDetailsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewCardDetailsButton.setBounds(30, 365, 107, 90); // x, y, width, height
        viewCardDetailsButton.setContentAreaFilled(false);
        viewCardDetailsButton.setBorderPainted(false);
        viewCardDetailsButton.setFocusPainted(false);
        viewCardDetailsButton.setOpaque(false);
        viewCardDetailsButton.addActionListener(e -> handleViewCardDetails(appFrame.getCardManager(), appFrame));
        add(viewCardDetailsButton);

       // Financial Statements Button
        JButton financialStatementsButton = new JButton();
        financialStatementsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        financialStatementsButton.setBounds(205, 365, 100, 90); // x, y, width, height
        financialStatementsButton.setContentAreaFilled(false);
        financialStatementsButton.setBorderPainted(false);
        financialStatementsButton.setFocusPainted(false);
        financialStatementsButton.setOpaque(false);
        financialStatementsButton.addActionListener(e -> {
            if (appFrame.getCardManager().getPersonalCard() == null) {
                showErrorDialog("No Card Found","You don't have a card yet.\nPlease create one first to access financial statements.");
        return;
    }
    appFrame.switchTo("PersonalFinancial");
});
add(financialStatementsButton);


        // Logout Button
        JButton logoutButton = new JButton();
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setBounds(205, 480, 100, 90); // x, y, width, height
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(false);
        logoutButton.addActionListener(e -> appFrame.switchTo("HomePage"));
        add(logoutButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering
        Graphics2D g2 = (Graphics2D) g;
        // Apply bilinear interpolation for smoother scaling
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Draw the background image scaled to the panel's size
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
/*POTANG INAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAA
UNSA MANEEEEEEEEEEE!!!!
YAWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA */
   
    private void handleCardCreation(CardManager cardManager, UserManager userManager) {
        if (cardManager.getPersonalCard() != null) {
            showErrorDialog("Card Already Exists",
                "You already have a registered card.\n" +
                "Each account can only have one card.");
            return;
        }

        try {
            // Generate a new card
            PersonalCard newCard = new PersonalCard();
            cardManager.setPersonalCard(newCard);

            // Save the card data
            String username = userManager.getLoggedInUsername();
            cardManager.saveCardData(username);

            // Show success message
            showCardCreationSuccess(newCard);
        } catch (Exception e) {
            showErrorDialog("Creation Failed",
                "Failed to create card: " + e.getMessage());}
        }
        

public void updateWelcomeMessage(String username) {
    welcomeLabel.setText( (username != null ? username : "Guest"));
}

    /**
     * Handles viewing card details.
     */
    private void handleViewCardDetails(CardManager cardManager, AppFrame appFrame) {
        if (cardManager.getPersonalCard() == null) {
            showErrorDialog("No Card Found",
                "You don't have a card yet.\n" +
                "Please create one first.");
            return;
        }
        appFrame.switchTo("PersonalCardPage");
    }

    /**
     * Displays a success dialog with the card details.
     */
    private void showCardCreationSuccess(PersonalCard card) {
        String message = String.format(
            "<html><div style='text-align:center;'>" +
            "<b>Card Created Successfully!</b><br><br>" +
            "Card Number: %s<br>" +
            "Expiration Date: %s<br>" +
            "CVV: %s<br><br>" +
            "<font color='red'>Please save these details securely!</font>" +
            " ",
            card.getCardNumber(),
            card.getExpirationDate(),
            card.getCVV()
        );

        JOptionPane.showMessageDialog(this, message, "Card Details", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays an error dialog with the specified title and message.
     */
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'>" + message + "",
            title,
            JOptionPane.ERROR_MESSAGE);
    }

    public void updateBalance(double totalBalance) {
    balanceLabel.setText("PHP " + String.format("%.2f", totalBalance));
}

}
