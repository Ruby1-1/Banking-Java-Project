package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class EMPdash extends JPanel {

    private JButton logoutButton;
    private JButton viewCardButton;
    private JButton reimbursementButton;
    private JButton financialStatementButton;

    public EMPdash(AppFrame appFrame) {
        setLayout(null);

        // Initialize and add buttons
        addLogoutButton(appFrame);
        addViewCardButton(appFrame);
        addReimbursementButton(appFrame);
        addFinancialStatementButton(appFrame);
    }

    private void addLogoutButton(AppFrame appFrame) {
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.setBounds(10, 10, 100, 40);
        logoutButton.addActionListener(e -> handleLogout(appFrame));
        add(logoutButton);
    }

    private void addViewCardButton(AppFrame appFrame) {
        viewCardButton = new JButton("View Card Details");
        viewCardButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewCardButton.setBounds(80, 80, 200, 50);
        viewCardButton.addActionListener(e -> handleViewCardDetails(appFrame));
        add(viewCardButton);
    }

    private void addReimbursementButton(AppFrame appFrame) {
        reimbursementButton = new JButton("Reimbursement Request");
        reimbursementButton.setFont(new Font("Arial", Font.PLAIN, 16));
        reimbursementButton.setBounds(80, 150, 200, 50);
        reimbursementButton.addActionListener(e -> appFrame.switchTo("EMPreimburse"));
        add(reimbursementButton);
    }

    private void addFinancialStatementButton(AppFrame appFrame) {
        financialStatementButton = new JButton("Financial Statement");
        financialStatementButton.setFont(new Font("Arial", Font.PLAIN, 16));
        financialStatementButton.setBounds(80, 220, 200, 50);
        financialStatementButton.addActionListener(e -> {
            String empId = appFrame.getUserManager().getLoggedInEmployeeId();
            boolean hasAccess = false;

            // Check card access in emp_users.txt
            try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
                while (scanner.hasNextLine()) {
                    String[] empData = scanner.nextLine().split(",", -1);
                    if (empData.length < 8) continue;
                    if (empData[1].trim().equals(empId) && empData[7].trim().equalsIgnoreCase("YES")) {
                        hasAccess = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading employee data!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (hasAccess) {
                appFrame.switchTo("EMPfinancial");
            } else {
                JOptionPane.showMessageDialog(this, "You do not have access to the company card. Financial statement is unavailable.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(financialStatementButton);
    }

    // --- Action Listeners ---

    private void handleLogout(AppFrame appFrame) {
        appFrame.getUserManager().logout();
        appFrame.switchTo("EmployeeLoginPage");
    }

    private void handleViewCardDetails(AppFrame appFrame) {
        String empId = appFrame.getUserManager().getLoggedInEmployeeId();
        System.out.println("[DEBUG] empId: " + empId);
        if (empId == null) {
            JOptionPane.showMessageDialog(this, "Session error: Employee ID not set.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean hasAccess = false;
        String cardNumber = "";
        String expDate = "";
        String cvv = "";

        try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] empData = scanner.nextLine().split(",", -1);
                if (empData.length < 8) {
                    System.out.println("[DEBUG] Skipping malformed line: " + String.join(",", empData));
                    continue;
                }
                System.out.println("[DEBUG] Comparing empData[1]: '" + empData[1].trim() + "' with empId: '" + empId + "'");
                System.out.println("[DEBUG] Access field: '" + empData[7].trim() + "'");
                if (empData[1].trim().equals(empId)) {
                    cardNumber = empData[4].trim();
                    expDate = empData[5].trim();
                    cvv = empData[6].trim();
                    if (empData[7].trim().equalsIgnoreCase("YES")) {
                        hasAccess = true;
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading employee data!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (hasAccess) {
            // Check if card details are empty
            if (cardNumber.isEmpty() || expDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No card details found for your company.", "No Card Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            appFrame.switchTo("EMPcardview");
        } else {
            JOptionPane.showMessageDialog(this, "You do not have access to view the company card.", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }
}
