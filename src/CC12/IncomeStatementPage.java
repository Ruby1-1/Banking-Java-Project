package CC12;



import javax.swing.*;
import java.awt.*;

public class IncomeStatementPage extends JPanel {
    public IncomeStatementPage(AppFrame appFrame, OwnerDashboard ownerDashboard) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Income Statement", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Income Statement Area
        JTextArea incomeStatementArea = new JTextArea(10, 30);
        incomeStatementArea.setText("Income Statement Data Here...");
        incomeStatementArea.setEditable(false);
        gbc.gridy = 1;
        add(new JScrollPane(incomeStatementArea), gbc);

        // Back Button
        JButton backButton = new JButton("BACK");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.addActionListener(e -> appFrame.switchTo("OwnerDashboard"));
        gbc.gridy = 2;
        add(backButton, gbc);
    }
}
