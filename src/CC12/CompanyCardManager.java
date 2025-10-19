package CC12;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CompanyCardManager {
    private CompanyCard companyCard;
    private final String USERS_FILE = "users.txt";

    /**
     * Gets the current company card.
     *
     * @return The company card, or null if no card is set.
     */
    public CompanyCard getCompanyCard() {
        return companyCard;
    }

    /**
     * Sets the company card for the company.
     *
     * @param companyCard The company card to set.
     */
    public void setCompanyCard(CompanyCard companyCard) {
        this.companyCard = companyCard;
        System.out.println("[CARD] Company card set: " + companyCard.getCardNumber());
    }

    /**
     * Checks if the company already has a card in the system.
     *
     * @param companyId The company ID.
     * @return True if the company has a card, false otherwise.
     */
    public boolean companyHasCardInSystem(String companyId) {
        try (Scanner scanner = new Scanner(new File(USERS_FILE))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split(",", -1);
                if (userData.length >= 8 && userData[6].trim().equals(companyId.trim())) {
                    return userData[3] != null && !userData[3].trim().isEmpty() &&
                           userData[4] != null && !userData[4].trim().isEmpty() &&
                           userData[5] != null && !userData[5].trim().isEmpty();
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to check company card: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves the company card data for the company to a file.
     * Also updates all employees for that company in emp_users.txt.
     *
     * @param companyId The company ID.
     */
    public void saveCompanyCardData(String companyId) {
        if (companyCard == null) {
            System.out.println("[ERROR] No card to save for company: " + companyId);
            return;
        }

        File tempFile = new File("users_temp.txt");
        File originalFile = new File(USERS_FILE);

        try (Scanner scanner = new Scanner(originalFile);
             PrintWriter writer = new PrintWriter(tempFile)) {

            boolean companyFound = false;

            // Copy existing data, updating the company's card if it exists
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userData = line.split(",", -1);

                if (userData.length >= 8 && userData[6].trim().equals(companyId.trim())) {
                    // Update the existing company entry with card details
                    userData[3] = companyCard.getCardNumber();
                    userData[4] = companyCard.getExpirationDate();
                    userData[5] = companyCard.getCVV();
                    writer.println(String.join(",", userData));
                    companyFound = true;
                    System.out.println("[CARD] Updated card for company ID: " + companyId);
                } else {
                    // Write the line as is for other users/companies
                    writer.println(line);
                }
            }

            if (!companyFound) {
                System.out.println("[CARD] Company not found, cannot add card.");
            }

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save company card data: " + e.getMessage());
            e.printStackTrace();
        }

        // Replace the original file with the updated file
        if (originalFile.delete() && tempFile.renameTo(originalFile)) {
            System.out.println("[CARD] Company card data file updated successfully.");
            // --- Update all employees for this company in emp_users.txt ---
            AddEmpPage.updateEmployeeCardInfoForCompany(
                companyId,
                companyCard.getCardNumber(),
                companyCard.getExpirationDate(),
                companyCard.getCVV()
            );
        } else {
            System.out.println("[ERROR] Failed to update company card data file.");
        }
    }

    /**
     * Loads the company card data from the users.txt file.
     *
     * @param companyId The company ID.
     */
    public void loadCompanyCardData(String companyId) {
        this.companyCard = null;

        try (Scanner scanner = new Scanner(new File(USERS_FILE))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split(",", -1);
                if (userData.length >= 8 && userData[6].trim().equals(companyId.trim())) {
                    if (userData[3] != null && !userData[3].trim().isEmpty() &&
                        userData[4] != null && !userData[4].trim().isEmpty() &&
                        userData[5] != null && !userData[5].trim().isEmpty()) {
                        companyCard = new CompanyCard(
                            userData[3].trim(),
                            userData[4].trim(),
                            userData[5].trim()
                        );
                        System.out.println("[DEBUG] Company card data loaded for company ID: " + companyId);
                    } else {
                        System.out.println("[DEBUG] No complete card data for company ID: " + companyId);
                    }
                    return;
                }
            }
            System.out.println("[DEBUG] No card data found for company ID: " + companyId);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to load company card data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
