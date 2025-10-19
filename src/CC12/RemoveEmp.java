package CC12;

import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RemoveEmp extends JPanel {
    private AppFrame appFrame;
    private DefaultListModel<String> employeeListModel;
    private JList<String> employeeJList;
    private java.util.List<String[]> employeeDataList; // To keep full employee data for removal

   public RemoveEmp(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        addBackButton();
        addEmployeeList();
        addRemoveButton();
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

    private void addRemoveButton() {
        JButton removeButton = new JButton("Remove Selected Employee");
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setBounds(80, 340, 240, 40);
        removeButton.setBackground(Color.PINK);
        removeButton.addActionListener(e -> removeSelectedEmployee());
        add(removeButton);
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
            // Format: empname,empid,password,companyid,cardnumber,expdate,cvv
            if (empData.length >= 4 && empData[3].trim().equals(companyId)) {
                employeeListModel.addElement(empData[0] + " (ID: " + empData[1] + ")");
                employeeDataList.add(empData);
            }
        }
    } catch (IOException e) {
        System.out.println("Failed to load employees.");
    }
}


    private void removeSelectedEmployee() {
        int selectedIdx = employeeJList.getSelectedIndex();
        if (selectedIdx == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] empToRemove = employeeDataList.get(selectedIdx);
        String empIdToRemove = empToRemove[1];

        // Remove from file
        File originalFile = new File("emp_users.txt");
        File tempFile = new File("emp_users_temp.txt");

        boolean removed = false;

        try (Scanner scanner = new Scanner(originalFile);
             PrintWriter writer = new PrintWriter(tempFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] empData = line.split(",", -1);
                if (empData.length >= 2 && empData[1].trim().equals(empIdToRemove)) {
                    removed = true;
                    continue; // Skip writing this employee
                }
                writer.println(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to remove employee.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (originalFile.delete() && tempFile.renameTo(originalFile)) {
            if (removed) {
                JOptionPane.showMessageDialog(this, "Employee removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update employee file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        loadEmployees(); // Refresh list
    }
}