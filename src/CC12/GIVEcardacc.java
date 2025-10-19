package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GIVEcardacc extends JPanel {
    private AppFrame appFrame;
    private DefaultListModel<String> employeeListModel;
    private JList<String> employeeJList;
    private java.util.List<String[]> employeeDataList; // To keep full employee data for updating

    public GIVEcardacc(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        addBackButton();
        addEmployeeList();
        addGrantAccessButton();
        loadEmployees();
    }

    private void addBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 10, 100, 40);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.addActionListener(e -> appFrame.switchTo("EmpmanagerPage"));
        add(backButton);
    }

    private void addEmployeeList() {
        employeeListModel = new DefaultListModel<>();
        employeeJList = new JList<>(employeeListModel);
        employeeJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeJList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(employeeJList);
        scrollPane.setBounds(40, 70, 320, 250);
        add(scrollPane);
    }

    private void addGrantAccessButton() {
        JButton grantAccessButton = new JButton("Grant Card Access");
        grantAccessButton.setFont(new Font("Arial", Font.BOLD, 16));
        grantAccessButton.setBounds(80, 340, 240, 40);
        grantAccessButton.setBackground(Color.GREEN);
        grantAccessButton.addActionListener(e -> grantCardAccessToSelectedEmployee());
        add(grantAccessButton);
    }

    private void loadEmployees() {
        employeeListModel.clear();
        employeeDataList = new ArrayList<>();
        String companyId = appFrame.getUserManager().getLoggedInCompanyId();

        File empFile = new File("emp_users.txt");
        if (!empFile.exists()) {
            // No popup, just show an empty list
            return;
        }

        try (Scanner scanner = new Scanner(empFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] empData = line.split(",", -1);
                // Format: name,empid,password,companyid,companycard,expdate,cvv,YES/NO
                if (empData.length >= 8 && empData[3].trim().equals(companyId)) {
                    String access = empData[7].trim().equalsIgnoreCase("YES") ? "YES" : "NO";
                    employeeListModel.addElement(empData[0] + " (ID: " + empData[1] + ") - Access: " + access);
                    employeeDataList.add(empData);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load employees.");
        }
    }

    private void grantCardAccessToSelectedEmployee() {
        int selectedIdx = employeeJList.getSelectedIndex();
        if (selectedIdx == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to grant access.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] empToUpdate = employeeDataList.get(selectedIdx);
        if (empToUpdate[7].trim().equalsIgnoreCase("YES")) {
            JOptionPane.showMessageDialog(this, "This employee already has card access.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        empToUpdate[7] = "YES"; // Grant access

        // Update emp_users.txt
        File originalFile = new File("emp_users.txt");
        File tempFile = new File("emp_users_temp.txt");

        try (Scanner scanner = new Scanner(originalFile);
             PrintWriter writer = new PrintWriter(tempFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] empData = line.split(",", -1);
                if (empData.length >= 2 && empData[1].trim().equals(empToUpdate[1].trim())) {
                    // Write updated employee line
                    writer.println(String.join(",", empToUpdate));
                } else {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to update employee access.");
            return;
        }

        if (originalFile.delete() && tempFile.renameTo(originalFile)) {
            JOptionPane.showMessageDialog(this, "Card access granted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update employee file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadEmployees(); // Refresh list
    }
}

