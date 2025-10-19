package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OWNERrimbursement extends JPanel {

    private AppFrame appFrame;
    private JList<String> requestList;
    private DefaultListModel<String> listModel;
    private List<String> reimbursements; // Stores the raw lines for file operations

    public OWNERrimbursement(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        JLabel titleLabel = new JLabel("Reimbursement Requests");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(60, 10, 300, 30);
        add(titleLabel);

        // List model and JList for displaying requests
        listModel = new DefaultListModel<>();
        requestList = new JList<>(listModel);
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestList.setFont(new Font("Arial", Font.PLAIN, 16));
        requestList.setFixedCellHeight(40);

        JScrollPane scrollPane = new JScrollPane(requestList);
        scrollPane.setBounds(30, 50, 440, 250);
        add(scrollPane);

        // Accept button
        JButton acceptBtn = new JButton("Accept");
        acceptBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        acceptBtn.setBounds(100, 320, 100, 40);
        acceptBtn.addActionListener(e -> handleDecision(true));
        add(acceptBtn);

        // Decline button
        JButton declineBtn = new JButton("Decline");
        declineBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        declineBtn.setBounds(250, 320, 100, 40);
        declineBtn.addActionListener(e -> handleDecision(false));
        add(declineBtn);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 370, 100, 40);
        backButton.addActionListener(e -> appFrame.switchTo("OwnerDashboard"));
        add(backButton);

        loadReimbursements();
        displayQueue();
    }

    private void loadReimbursements() {
        reimbursements = new ArrayList<>();
        File file = new File("reimbursements.txt");
        if (!file.exists()) return;
        String ownerCompanyId = appFrame.getUserManager().getLoggedInCompanyId();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                // Format: empid,compid,amount,employeename
                if (parts.length >= 4 && parts[1].trim().equals(ownerCompanyId)) {
                    reimbursements.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load reimbursements.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayQueue() {
        listModel.clear();
        for (String req : reimbursements) {
            String[] parts = req.split(",", -1);
            // Format: empid,compid,amount,employeename
            if (parts.length >= 4) {
                String display = parts[3].trim() + " - " + parts[2].trim();
                listModel.addElement(display);
            }
        }
    }

    private void handleDecision(boolean accepted) {
        int selectedIdx = requestList.getSelectedIndex();
        if (selectedIdx == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reimbursement request.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedReq = reimbursements.get(selectedIdx);
        String[] parts = selectedReq.split(",", -1);
        // Format: empid,compid,amount,employeename
        if (parts.length < 4) {
            JOptionPane.showMessageDialog(this, "Invalid reimbursement request format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String compId = parts[1].trim();
        String amountStr = parts[2].trim();
        int amount = 0;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid reimbursement amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (accepted) {
            // Deduct from company balance
            File compDataFolder = new File("compdata");
            File companyFile = new File(compDataFolder, compId + "_financial.txt");
            int companyBalance = 0;
            List<String> companyTransactions = new ArrayList<>();
            if (companyFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(companyFile))) {
                    String line;
                    // First line: total balance
                    if ((line = reader.readLine()) != null) {
                        try {
                            companyBalance = Integer.parseInt(line.trim());
                        } catch (NumberFormatException ex) {
                            companyBalance = 0;
                        }
                    }
                    // Next lines: transactions
                    while ((line = reader.readLine()) != null) {
                        companyTransactions.add(line);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load company financials.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (companyBalance < amount) {
                JOptionPane.showMessageDialog(this, "Insufficient funds in company balance.", "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                return;
            }

            companyBalance -= amount;
            companyTransactions.add("Reimbursement of " + amount + " PHP accepted for " + parts[3].trim());

            try (PrintWriter writer = new PrintWriter(new FileWriter(companyFile))) {
                writer.println(companyBalance);
                for (String t : companyTransactions) {
                    writer.println(t);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to update company financials.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Reimbursement request accepted and deducted from company balance.", "Decision", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Reimbursement request denied", "Decision", JOptionPane.INFORMATION_MESSAGE);
        }

        // Remove from list and file
        reimbursements.remove(selectedIdx);
        saveReimbursements();
        displayQueue();
    }

    private void saveReimbursements() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("reimbursements.txt"))) {
            for (String req : reimbursements) {
                writer.println(req);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to update reimbursements file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

