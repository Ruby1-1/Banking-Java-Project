package CC12;

import javax.swing.*;
import java.awt.*;

public class OwnerDashboard extends JPanel {
    private AppFrame appFrame;

    // UI components for displaying owner data
    private JLabel companyNameLabel;
    private JLabel cardNumberLabel;
    private JLabel expirationDateLabel;
    private JLabel cvvLabel;

    /**
     * Constructor for the OwnerDashboard.
     *
     * @param appFrame The main application frame.
     */
    public OwnerDashboard(AppFrame appFrame) {
        this.appFrame = appFrame;
        initializeUI();
    }

    /**
     * Initializes the UI components for the OwnerDashboard.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Owner Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Labels for displaying owner data
        companyNameLabel = new JLabel("Company: N/A");
        cardNumberLabel = new JLabel("Card Number: N/A");
        expirationDateLabel = new JLabel("Expiration Date: N/A");
        cvvLabel = new JLabel("CVV: N/A");

        // Add labels to the info panel
        infoPanel.add(companyNameLabel);
        infoPanel.add(cardNumberLabel);
        infoPanel.add(expirationDateLabel);
        infoPanel.add(cvvLabel);

        // Add the info panel to the center
        add(infoPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10));

        // Add buttons for each feature
        JButton employeePageButton = new JButton("Manage Employees");
        JButton reimbursementButton = new JButton("Reimbursement Requests");
        JButton createCardButton = new JButton("Create Company Card");
        JButton viewCardButton = new JButton("View Company Cards");
        JButton incomeStatementButton = new JButton("Income Statement");
        JButton logoutButton = new JButton("Logout");

        // Add action listeners
        employeePageButton.addActionListener(e -> appFrame.switchTo("EmployeeManagementPage"));
        reimbursementButton.addActionListener(e -> appFrame.switchTo("OWNERrimbursement"));
        createCardButton.addActionListener(e -> handleCompanyCardCreation(appFrame.getCompanyCardManager(), appFrame.getUserManager()));
        viewCardButton.addActionListener(e -> handleViewCompanyCardDetails(appFrame.getCompanyCardManager(), appFrame, appFrame.getUserManager()));
        incomeStatementButton.addActionListener(e -> {
            // Only allow access if the company has a card
            CompanyCardManager companyCardManager = appFrame.getCompanyCardManager();
            UserManager userManager = appFrame.getUserManager();
            String companyId = userManager.getLoggedInCompanyId();
            companyCardManager.loadCompanyCardData(companyId);

            if (companyCardManager.getCompanyCard() == null) {
                showErrorDialog("No Card Found",
                    "Your company doesn't have a card yet.<br>Please create one first to access income statements.");
                return;
            }
            appFrame.switchTo("OWNERfinancials");
        });
        logoutButton.addActionListener(e -> appFrame.switchTo("HomePage"));

        // Add buttons to the panel
        buttonPanel.add(employeePageButton);
        buttonPanel.add(reimbursementButton);
        buttonPanel.add(createCardButton);
        buttonPanel.add(viewCardButton);
        buttonPanel.add(incomeStatementButton);
        buttonPanel.add(logoutButton);

        // Add the button panel to the south
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates the owner-specific data in the dashboard.
     *
     * @param username       The username of the owner.
     * @param companyName    The name of the company.
     * @param cardNumber     The company card number.
     * @param expirationDate The expiration date of the card.
     * @param cvv            The CVV of the card.
     */
    public void updateOwnerData(String username, String companyName, String cardNumber, String expirationDate, String cvv) {
        // Update the labels with the provided data
        companyNameLabel.setText("Company: " + companyName);
        cardNumberLabel.setText("Card Number: " + cardNumber);
        expirationDateLabel.setText("Expiration Date: " + expirationDate);
        cvvLabel.setText("CVV: " + cvv);

        // Debugging: Log the updated data
        System.out.println("[DEBUG] OwnerDashboard updated with:");
        System.out.println("[DEBUG] Username: " + username);
        System.out.println("[DEBUG] Company Name: " + companyName);
        System.out.println("[DEBUG] Card Number: " + cardNumber);
        System.out.println("[DEBUG] Expiration Date: " + expirationDate);
        System.out.println("[DEBUG] CVV: " + cvv);
    }

    private void handleCompanyCardCreation(CompanyCardManager companyCardManager, UserManager userManager) {
        String companyId = userManager.getLoggedInCompanyId();

        // Always reload from file to check if a card exists
        companyCardManager.loadCompanyCardData(companyId);

        if (companyCardManager.getCompanyCard() != null && companyCardManager.companyHasCardInSystem(companyId)) {
            showErrorDialog("Card Already Exists",
                "Your company already has a registered card.<br>Each company can only have one card.");
            return;
        }

        try {
            // Generate a new company card
            CompanyCard newCard = new CompanyCard();
            companyCardManager.setCompanyCard(newCard);

            // Save the card data
            companyCardManager.saveCompanyCardData(companyId);

            // Show success message
            showCompanyCardCreationSuccess(newCard);
        } catch (Exception e) {
            showErrorDialog("Creation Failed",
                "Failed to create company card: " + e.getMessage());
        }
    }

    private void handleViewCompanyCardDetails(CompanyCardManager companyCardManager, AppFrame appFrame, UserManager userManager) {
        // Always reload the card data from file to ensure up-to-date info
        companyCardManager.loadCompanyCardData(userManager.getLoggedInCompanyId());
        if (companyCardManager.getCompanyCard() == null) {
            showErrorDialog("No Card Found",
                "Your company doesn't have a card yet.<br>Please create one first.");
            return;
        }
        appFrame.switchTo("CompanyCardPage"); // Or whatever your company card details page is called
    }

    private void showCompanyCardCreationSuccess(CompanyCard card) {
        String message = String.format(
            "<html><div style='text-align:center;'>" +
            "<b>Company Card Created Successfully!</b><br><br>" +
            "Card Number: %s<br>" +
            "Expiration Date: %s<br>" +
            "CVV: %s<br><br>" +
            "<font color='red'>Please save these details securely!</font>" +
            " ",
            card.getCardNumber(),
            card.getExpirationDate(),
            card.getCVV()
        );

        JOptionPane.showMessageDialog(this, message, "Company Card Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:center;'>" + message + "",
            title,
            JOptionPane.ERROR_MESSAGE);
    }
}
