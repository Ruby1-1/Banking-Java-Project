package CC12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;

public class EMPreimburse extends JPanel {

    private AppFrame appFrame;

    public EMPreimburse(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(null);

        // Label
        JLabel label = new JLabel("Enter Reimbursement Amount:");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(60, 60, 250, 30);
        add(label);

        // Input field (only positive integers)
        JTextField amountField = new JTextField();
        amountField.setBounds(60, 100, 200, 35);
        amountField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(amountField);

        // Restrict input to positive integers only
        amountField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        // Submit button
        JButton submitButton = new JButton("Submit Reimbursement Request");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        submitButton.setBounds(60, 160, 250, 40);
        submitButton.addActionListener(e -> {
            String input = amountField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int amount = Integer.parseInt(input);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Get employee and company ID and employee name
                String empId = appFrame.getUserManager().getLoggedInEmployeeId();
                String compId = appFrame.getUserManager().getLoggedInCompanyId();
                String empName = appFrame.getUserManager().getLoggedInEmployeeName(); // Ensure this method exists
                if (empId == null || compId == null || empName == null) {
                    JOptionPane.showMessageDialog(this, "Session error: Employee, Company ID, or Name not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Save to reimbursements.txt with new format
                try (FileWriter writer = new FileWriter("reimbursements.txt", true)) {
                    writer.write(empId + "," + compId + "," + amount + "," + empName + System.lineSeparator());
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(this, "Failed to save reimbursement request.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Reimbursement request submitted for amount: " + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(submitButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 10, 100, 40);
        backButton.addActionListener(e -> appFrame.switchTo("EMPdash"));
        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optional: custom background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
