package CC12;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CardManager {
    private PersonalCard personalCard;

    /**
     * Gets the current personal card.
     *
     * @return The personal card, or null if no card is set.
     */
    public PersonalCard getPersonalCard() {
        return personalCard;
    }

    /**
     * Sets the personal card for the user.
     *
     * @param personalCard The personal card to set.
     */
    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
        System.out.println("[CARD] Card set: " + personalCard.getCardNumber());
    }

    

    /**
     * Checks if the user already has a card in the system.
     *
     * @param username The username of the user.
     * @return True if the user has a card, false otherwise.
     */
    public boolean userHasCardInSystem(String username) {
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] cardData = scanner.nextLine().split(",", -1);
                if (cardData.length >= 4 && cardData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to check card: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves the personal card data for the user to a file.
     *
     * @param username The username of the user.
     */
    public void saveCardData(String username) {
    if (personalCard == null) {
        System.out.println("[ERROR] No card to save for user: " + username);
        return;
    }

    File tempFile = new File("users_temp.txt");
    File originalFile = new File("users.txt");

    try (Scanner scanner = new Scanner(originalFile);
         PrintWriter writer = new PrintWriter(tempFile)) {

        boolean userFound = false;

        // Copy existing data, updating the user's card if it exists
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] userData = line.split(",", -1);

            if (userData.length >= 3 && userData[0].equals(username)) {
                // Update the existing user entry with card details
                writer.println(username + "," +
                               userData[1] + "," + // Keep the password
                               userData[2] + "," + // Keep the role
                               personalCard.getCardNumber() + "," +
                               personalCard.getExpirationDate() + "," +
                               personalCard.getCVV());
                userFound = true;
                System.out.println("[CARD] Updated card for user: " + username);
            } else {
                // Write the line as is for other users
                writer.println(line);
            }
        }

        // If the user was not found, add a new entry
        if (!userFound) {
            System.out.println("[CARD] User not found, adding new entry.");
            writer.println(username + ",<password>,personal," + // Replace <password> with actual password logic
                           personalCard.getCardNumber() + "," +
                           personalCard.getExpirationDate() + "," +
                           personalCard.getCVV());
        }

    } catch (IOException e) {
        System.out.println("[ERROR] Failed to save card data: " + e.getMessage());
        e.printStackTrace();
    }

    // Replace the original file with the updated file
    if (originalFile.delete() && tempFile.renameTo(originalFile)) {
        System.out.println("[CARD] Card data file updated successfully.");
    } else {
        System.out.println("[ERROR] Failed to update card data file.");
    }
}



 public void loadCardData(String username) {
    // Clear any existing card data first
    this.personalCard = null;
    
    try (Scanner scanner = new Scanner(new File("users.txt"))) {
        while (scanner.hasNextLine()) {
            String[] userData = scanner.nextLine().split(",", -1);
            if (userData.length >= 6 && userData[0].equals(username)) {
                // Check if the user has card details data; if not, personalCard remains null.
                if (!userData[3].isEmpty() && !userData[4].isEmpty() && !userData[5].isEmpty()) {
                    personalCard = new PersonalCard();
                    personalCard.setCardNumber(userData[3]);    // Card Number is at index 3
                    personalCard.setExpirationDate(userData[4]);  // Expiration Date is at index 4
                    personalCard.setCVV(userData[5]);             // CVV is at index 5
                    System.out.println("[DEBUG] Card data loaded for user: " + username);
                    System.out.println("[DEBUG] Card Number: " + personalCard.getCardNumber());
                    System.out.println("[DEBUG] Expiration Date: " + personalCard.getExpirationDate());
                    System.out.println("[DEBUG] CVV: " + personalCard.getCVV());
                } else {
                    System.out.println("[DEBUG] No complete card data for user: " + username);
                }
                return;
            }
        }
        System.out.println("[DEBUG] No card data found for user: " + username);
    } catch (IOException e) {
        System.out.println("[ERROR] Failed to load card data: " + e.getMessage());
        e.printStackTrace();
    }
}}