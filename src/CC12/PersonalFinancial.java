package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.LinkedList;

public class PersonalFinancial extends JPanel {
    private LinkedList<String> statements;
    private JTextArea statementsArea;
    private String username;
    private double totalBalance;
    private Image bgImage;

    public PersonalFinancial(AppFrame appFrame, PersonalDashboard dashboard) {
        this.statements = new LinkedList<>();
        this.username = appFrame.getUserManager().getLoggedInUsername();
        
    
        this.totalBalance = 0.0;

       
        bgImage = new ImageIcon("src/CC12/assets/personalfinanci.png").getImage();

     //  NULL LAYOUT
        setLayout(null);
        setOpaque(true);



        // ANG SCROLL PANE
        statementsArea = new JTextArea();
        statementsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statementsArea);
        scrollPane.setOpaque(false);
        statementsArea.setOpaque(false);
        statementsArea.setForeground(Color.WHITE);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        statementsArea.setBackground(new Color(0, 0, 0, 0)); // fully transparent background
        statementsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        scrollPane.setBounds(58, 145, 230, 280);
        add(scrollPane);

       
        loadStatements();

        // Back button
        JButton backButton = new JButton();
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(115, 550, 105, 40);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        backButton.addActionListener(e -> {
            System.out.println("[NAVIGATION] Back to PersonalDashboard");
            appFrame.switchTo("PersonalDashboard");
        });
        add(backButton);

        // Add +5000 PHP button
        JButton addButton = new JButton();
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setBounds(26, 485, 130, 40);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setOpaque(false);
        addButton.addActionListener(e -> {
            totalBalance += 5000;
            statements.add("Added +5000 PHP");
            System.out.println("[DEBUG] Added +5000 PHP");
            saveStatements();
            updateStatements();
        });
        add(addButton);

        // Subtract -270 PHP button
        JButton subtractButton = new JButton();
        subtractButton.setFont(new Font("Arial", Font.PLAIN, 16));
        subtractButton.setBounds(220, 485, 100, 40);
        subtractButton.setContentAreaFilled(false);
        subtractButton.setBorderPainted(false);
        subtractButton.setFocusPainted(false);
        subtractButton.setOpaque(false);

        subtractButton.addActionListener(e -> {
            if (totalBalance < 270) {
                System.out.println("[ERROR] Insufficient funds. Cannot subtract -270 PHP.");
                JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            totalBalance -= 270;
            statements.add("Subtracted -270 PHP");
            System.out.println("[DEBUG] Subtracted -270 PHP");
            saveStatements();
            updateStatements();
        });
        add(subtractButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // INTERPOLATION 
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void saveStatements() {
        if (username == null || username.isEmpty()) {
            System.out.println("[ERROR] Cannot save statements: username is null or empty.");
            return;
        }
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    System.out.println("[DEBUG] 'data' directory created.");
                } else {
                    System.out.println("[ERROR] Failed to create 'data' directory.");
                    return;
                }
            }
            File file = new File(dir, username + "_statements.txt");
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("[DEBUG] Statement file created: " + file.getAbsolutePath());
                } else {
                    System.out.println("[ERROR] Failed to create statement file.");
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String statement : statements) {
                    writer.write(statement);
                    writer.newLine();
                }
                writer.write("Total Balance: " + totalBalance + " PHP");
                writer.newLine();
                System.out.println("[DEBUG] Financial statements saved for user: " + username);
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save financial statements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStatements() {
        if (statementsArea == null) {
            System.out.println("[ERROR] statementsArea is not initialized.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = statements.size() - 1; i >= 0; i--) {
            sb.append(statements.get(i)).append("\n");
        }
        statementsArea.setText(sb.toString());
    }

    private void loadStatements() {
        if (username == null || username.isEmpty()) {
            System.out.println("[ERROR] Cannot load statements: username is null or empty.");
            return;
        }
        File file = new File("data/" + username + "_statements.txt");
        if (!file.exists()) {
            System.out.println("[DEBUG] No financial statements file found for user: " + username);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Total Balance:")) {
                    String balanceString = line.replace("Total Balance:", "").replace("PHP", "").trim();
                    try {
                        totalBalance = Double.parseDouble(balanceString);
                        System.out.println("[DEBUG] Total balance loaded: " + totalBalance + " PHP");
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Failed to parse total balance: " + balanceString);
                    }
                } else {
                    statements.add(line);
                }
            }
            updateStatements();
            System.out.println("[DEBUG] Financial statements loaded for user: " + username);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to load financial statements: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

