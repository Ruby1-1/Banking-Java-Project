package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AddEmpPage extends JPanel {

    private AppFrame appFrame; // Reference to the main application frame

    public AddEmpPage(AppFrame appFrame) {
        this.appFrame = appFrame;

        setLayout(null);

        addBackButton();
        addInputFields();
    }

    /**
     * Adds a back button to the page.
     */
    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 10, 100, 40);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setBorderPainted(true);
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.addActionListener(e -> appFrame.switchTo("EmpmanagerPage"));
        add(backButton);
    }

    /**
     * Adds input fields for employee details and a save button.
     */
    private void addInputFields() {
        // Employee Name
        JLabel nameLabel = new JLabel("Employee Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBounds(50, 100, 120, 30);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(180, 100, 150, 30);
        add(nameField);

        // Employee ID
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        idLabel.setBounds(50, 150, 120, 30);
        add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(180, 150, 150, 30);
        add(idField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(50, 200, 120, 30);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(180, 200, 150, 30);
        add(passwordField);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
        saveButton.setBounds(120, 260, 100, 40);
        saveButton.setBackground(Color.LIGHT_GRAY);
        saveButton.setBorderPainted(true);
        saveButton.setFocusPainted(false);
        saveButton.setOpaque(true);
        saveButton.addActionListener(e -> {
            String empName = nameField.getText().trim();
            String empId = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (empName.isEmpty() || empId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saveEmployee(empName, empId, password);
        });
        add(saveButton);
    }

    /**
     * Saves the employee details to the emp_users.txt file.
     * If the company does not have a card, card fields are left empty.
     */
    private void saveEmployee(String empName, String empId, String password) {
        String companyId = appFrame.getUserManager().getLoggedInCompanyId();
        if (companyId == null || companyId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No company ID found. Please log in as an owner.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the employee ID already exists
        try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] empData = scanner.nextLine().split(",", -1);
                if (empData.length >= 2 && empData[1].trim().equals(empId)) {
                    JOptionPane.showMessageDialog(this, "Employee ID already exists. Please use a unique ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (IOException e) {
            // If file doesn't exist, it's fine (first employee)
        }

        // Check if the company has a card
        String cardNumber = "";
        String expDate = "";
        String cvv = "";

        // If you have a CompanyCardManager, use it to get card info for this company
        try {
            CompanyCardManager companyCardManager = appFrame.getCompanyCardManager();
            companyCardManager.loadCompanyCardData(companyId);
            CompanyCard companyCard = companyCardManager.getCompanyCard();

            if (companyCard != null) {
                cardNumber = companyCard.getCardNumber();
                expDate = companyCard.getExpirationDate();
                cvv = companyCard.getCVV();
            }
        } catch (Exception e) {
            // If you don't have a card manager or card, just leave fields empty
        }

        // Format: empname,empid,password,companyid,cardnumber,expdate,cvv,NO
        String employeeData = String.format("%s,%s,%s,%s,%s,%s,%s,NO", empName, empId, password, companyId, cardNumber, expDate, cvv);

        try (FileWriter writer = new FileWriter("emp_users.txt", true)) {
            writer.write(employeeData + System.lineSeparator());
            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("[DEBUG] Employee saved: " + employeeData);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save employee. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("[ERROR] Failed to save employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Paints the background (optional, can be customized later).
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Utility: Call this after a company card is created/updated to update all employees for that company.
     */
    public static void updateEmployeeCardInfoForCompany(String companyId, String cardNumber, String expDate, String cvv) {
    File inputFile = new File("emp_users.txt");
    File tempFile = new File("emp_users_temp.txt");

    try (
        Scanner scanner = new Scanner(inputFile);
        FileWriter writer = new FileWriter(tempFile)
    ) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] empData = line.split(",", -1);
            System.out.println("[DEBUG] Checking line: " + line);
            if (empData.length >= 8 && empData[3].trim().equals(companyId)) {
                System.out.println("[DEBUG] Updating employee: " + empData[1]);
                empData[4] = cardNumber;
                empData[5] = expDate;
                empData[6] = cvv;
                writer.write(String.join(",", empData) + System.lineSeparator());
            } else {
                writer.write(line + System.lineSeparator());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to update employee card info.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Replace the old file with the updated file
    if (inputFile.delete()) {
        boolean renamed = tempFile.renameTo(inputFile);
        System.out.println("[DEBUG] Renamed temp file: " + renamed);
        if (!renamed) {
            JOptionPane.showMessageDialog(null, "Failed to rename temp file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        System.out.println("[DEBUG] Failed to delete original emp_users.txt");
        JOptionPane.showMessageDialog(null, "Failed to delete original emp_users.txt.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}}
