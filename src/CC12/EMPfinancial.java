package CC12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EMPfinancial extends JPanel {

    private AppFrame appFrame;
    private JLabel balanceLabel;
    private JPanel transactionsPanel;
    private JScrollPane scrollPane;
    private List<String> transactions;
    private String empId;
    private String compId;
    private File dataFolder;
    private File financialFile;

    public EMPfinancial(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        // Get the currently logged-in employee ID and company ID
        empId = appFrame.getUserManager().getLoggedInEmployeeId();
        compId = appFrame.getUserManager().getLoggedInCompanyId();

        // Defensive: If empId or compId is null, do not proceed
        if (empId == null || compId == null) {
            JLabel errorLabel = new JLabel("No employee or company selected. Please log in.");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
            errorLabel.setBounds(30, 100, 400, 30);
            add(errorLabel);
            return;
        }

        // Ensure empdata folder exists
        dataFolder = new File("empdata");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        // File format: empdata/[empid,compid].txt
        financialFile = new File(dataFolder, "[" + empId + "," + compId + "].txt");

        JLabel titleLabel = new JLabel("Employee Financials");
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

        // Subtract 562 PHP button
        JButton subtractButton = new JButton("Subtract 562 PHP");
        subtractButton.setFont(new Font("Arial", Font.PLAIN, 16));
        subtractButton.setBounds(30, 320, 180, 40);
        subtractButton.addActionListener(this::handleSubtractFunds);
        add(subtractButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 370, 100, 40);
        backButton.addActionListener(e -> appFrame.switchTo("EMPdash"));
        add(backButton);

        // Defensive: Initialize transactions list
        transactions = new ArrayList<>();
        loadFinancials();
        updateUIData();
    }

    // Always get the company balance for display
    private int getCompanyBalance() {
        int companyBalance = 0;
        if (compId != null) {
            File companyFile = new File("compdata", compId + "_financial.txt");
            if (companyFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(companyFile))) {
                    String line = reader.readLine();
                    if (line != null) {
                        try {
                            companyBalance = Integer.parseInt(line.trim());
                        } catch (NumberFormatException ex) {
                            companyBalance = 0;
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Failed to load company financials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return companyBalance;
    }

    private void handleSubtractFunds(ActionEvent e) {
        int companyBalance = getCompanyBalance();
        if (companyBalance < 562) {
            JOptionPane.showMessageDialog(this, "Insufficient funds in company balance.", "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Log transaction in employee file
        String transaction = "Subtracted 562 PHP";
        transactions.add(transaction);
        saveFinancials();

        // Subtract from the company's total balance
        if (compId != null) {
            File compDataFolder = new File("compdata");
            if (!compDataFolder.exists()) {
                compDataFolder.mkdir();
            }
            File companyFile = new File(compDataFolder, compId + "_financial.txt");
            int companyBalanceNew = 0;
            List<String> companyTransactions = new ArrayList<>();
            // Load company balance and transactions
            if (companyFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(companyFile))) {
                    String line;
                    // First line: total balance
                    if ((line = reader.readLine()) != null) {
                        try {
                            companyBalanceNew = Integer.parseInt(line.trim());
                        } catch (NumberFormatException ex) {
                            companyBalanceNew = 0;
                        }
                    }
                    // Next lines: transactions
                    while ((line = reader.readLine()) != null) {
                        companyTransactions.add(line);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load company financials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Subtract and save
            companyBalanceNew -= 562;
            companyTransactions.add("Subtracted 562 PHP by employee " + empId);
            try (PrintWriter writer = new PrintWriter(new FileWriter(companyFile))) {
                writer.println(companyBalanceNew);
                for (String t : companyTransactions) {
                    writer.println(t);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save company financials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        updateUIData();
    }

    private void loadFinancials() {
        transactions = new ArrayList<>();
        if (!financialFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(financialFile))) {
            reader.readLine(); // First line: employee name (not displayed)
            String line;
            // Second line: (old) total balance, skip
            reader.readLine();
            // Next lines: transactions
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        } catch (IOException e) {
            // Only show a popup if the file exists but can't be read
            if (financialFile.exists()) {
                JOptionPane.showMessageDialog(this, "Failed to load financials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // If the file doesn't exist, just start with an empty list (no popup)
        }
    }

    private void saveFinancials() {
        String empName = appFrame.getUserManager().getLoggedInEmployeeName();
        try (PrintWriter writer = new PrintWriter(new FileWriter(financialFile))) {
            writer.println(empName != null ? empName : "Unknown Employee"); // First line: employee name
            writer.println("0"); // Placeholder for balance, not used
            for (String t : transactions) {
                writer.println(t);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save financials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUIData() {
        int companyBalance = getCompanyBalance();
        balanceLabel.setText("Total Balance: PHP " + companyBalance);
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
