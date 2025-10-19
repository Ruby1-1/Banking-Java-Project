package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class EMPcardview extends JPanel {

    private AppFrame appFrame;
    private Image bgImage;

    public EMPcardview(AppFrame appFrame) {
        this.appFrame = appFrame;

        // Load the background image
        bgImage = new ImageIcon("src/CC12/assets/EMPcardview.png").getImage();

        setLayout(null);

        // Add a back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBounds(10, 10, 100, 40);
        backButton.addActionListener(e -> appFrame.switchTo("EMPdash"));
        add(backButton);

        // Display card details
        displayCardDetails();
    }

    private void displayCardDetails() {
        String empId = appFrame.getUserManager().getLoggedInEmployeeId();
        if (empId == null) {
            JLabel errorLabel = new JLabel("Employee ID not found.", SwingConstants.CENTER);
            errorLabel.setBounds(50, 100, 400, 30);
            add(errorLabel);
            return;
        }

        // Format: name,empid,password,companyid,cardnumber,expdate,cvv,YES/NO
        String cardNumber = "N/A";
        String expDate = "N/A";
        String cvv = "N/A";
        String access = "NO";
        boolean found = false;

        try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",", -1);
                if (data.length >= 8 && data[1].trim().equals(empId)) {
                    cardNumber = data[4].trim();
                    expDate = data[5].trim();
                    cvv = data[6].trim();
                    access = data[7].trim();
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error loading card details.", SwingConstants.CENTER);
            errorLabel.setBounds(50, 100, 400, 30);
            add(errorLabel);
            return;
        }

        if (!found) {
            JLabel errorLabel = new JLabel("No card details found for this employee.", SwingConstants.CENTER);
            errorLabel.setBounds(50, 100, 400, 30);
            add(errorLabel);
            return;
        }

        if (!access.equalsIgnoreCase("YES")) {
            JLabel errorLabel = new JLabel("You do not have access to view the company card.", SwingConstants.CENTER);
            errorLabel.setBounds(50, 100, 400, 30);
            add(errorLabel);
            return;
        }

        JLabel cardNumberLabel = new JLabel("Card Number: " + cardNumber, SwingConstants.CENTER);
        cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cardNumberLabel.setBounds(50, 100, 400, 30);
        add(cardNumberLabel);

        JLabel expDateLabel = new JLabel("Expiration Date: " + expDate, SwingConstants.CENTER);
        expDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        expDateLabel.setBounds(50, 140, 400, 30);
        add(expDateLabel);

        JLabel cvvLabel = new JLabel("CVV: " + cvv, SwingConstants.CENTER);
        cvvLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        cvvLabel.setBounds(50, 180, 400, 30);
        add(cvvLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}

