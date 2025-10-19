package CC12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OWNERfinancials extends JPanel {

    private AppFrame appFrame;
    private JLabel balanceLabel;
    private JPanel transactionsPanel;
    private JScrollPane scrollPane;
    private int totalBalance = 0;
    private List<String> transactions;
    private String companyId;
    private File dataFolder;
    private File financialFile;

    public OWNERfinancials(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        // Get the currently logged-in company ID
        companyId = appFrame.getUserManager().getLoggedInCompanyId();

        // Defensive: If companyId is null, do not proceed (do not show popup here)
        if (companyId == null) {
            // Optionally, you can display a label or leave the panel blank
            JLabel errorLabel = new JLabel("No company selected. Please log in as an owner.");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
            errorLabel.setBounds(30, 100, 400, 30);
            add(errorLabel);
            return;
        }

        // Ensure compdata folder exists
        dataFolder = new File("compdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        financialFile = new File(dataFolder, companyId + "_financial.txt");

        JLabel titleLabel = new JLabel("Company Financials");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBounds(30, 10, 300, 30);
        add(titleLabel);

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setBounds(30, 50, 300, 30);
        add(balanceLabel);

        // Transactions panel inside a scroll pane
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(transactionsPanel);
        scrollPane.setBounds(30, 90, 440, 220);
        add(scrollPane);

        // Add 10,000 PHP button
        JButton addButton = new JButton("Add 10,000 PHP");
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setBounds(30, 320, 180, 40);
        addButton.addActionListener(this::handleAddFunds);
        add(addButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 370, 100, 40);
        backButton.addActionListener(e -> appFrame.switchTo("OwnerDashboard"));
        add(backButton);

        loadFinancials();
        updateUIData();
    }

    private void handleAddFunds(ActionEvent e) {
        String transaction = "Added 10000 PHP";
        transactions.add(transaction);
        totalBalance += 10000;
        saveFinancials();
        updateUIData();
    }

    private void loadFinancials() {
        transactions = new ArrayList<>();
        totalBalance = 0;
        if (!financialFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(financialFile))) {
            String line;
            // First line: total balance
            if ((line = reader.readLine()) != null) {
                try {
                    totalBalance = Integer.parseInt(line.trim());
                } catch (NumberFormatException ex) {
                    totalBalance = 0;
                }
            }
            // Next lines: transactions
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load financials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFinancials() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(financialFile))) {
            writer.println(totalBalance);
            for (String t : transactions) {
                writer.println(t);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save financials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUIData() {
        balanceLabel.setText("Total Balance: PHP " + totalBalance);
        transactionsPanel.removeAll();
        for (String t : transactions) {
            JLabel label = new JLabel(t);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            transactionsPanel.add(label);
        }
        transactionsPanel.revalidate();
        transactionsPanel.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}