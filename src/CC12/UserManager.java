package CC12;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UserManager {
    private String loggedInUsername;
    private String loggedInCompanyId; // Added to track the logged-in user's company ID
    private double totalBalance;
    private CardManager cardManager;

    /**
     * Constructor for UserManager.
     * Accepts a CardManager instance to handle card-related operations.
     *
     * @param cardManager The CardManager instance.
     */
    public UserManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    /**
     * Gets the username of the currently logged-in user.
     *
     * @return The logged-in username.
     */
    public String getUsername() {
        return loggedInUsername;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    /**
     * Sets the username of the currently logged-in user.
     *
     * @param username The username to set.
     */
    public void setLoggedInUsername(String username) {
        System.out.println("[USER] Logging in user: " + username);
        this.loggedInUsername = username;

        // Load the user's balance when they log in
        this.totalBalance = loadUserBalance(username);

        // Load the user's card data
        if (cardManager != null) {
            cardManager.loadCardData(username);
        } else {
            System.out.println("[ERROR] CardManager is not initialized.");
        }
    }

    /**
     * Gets the company ID of the currently logged-in user.
     *
     * @return The logged-in company ID.
     */
    public String getLoggedInCompanyId() {
        return loggedInCompanyId;
    }



    private String loggedInEmployeeId;

public void setLoggedInEmployeeId(String empId) {
    this.loggedInEmployeeId = empId;
}

public String getLoggedInEmployeeId() {
    return this.loggedInEmployeeId;
}

public String getLoggedInEmployeeName() {
    if (loggedInEmployeeId == null) return null;
    try (Scanner scanner = new Scanner(new File("emp_users.txt"))) {
        while (scanner.hasNextLine()) {
            String[] empData = scanner.nextLine().split(",", -1);
            if (empData.length >= 2 && empData[1].trim().equals(loggedInEmployeeId)) {
                return empData[0].trim(); // Assuming name is the first column
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}




    // In UserManager.java
public void logout() {
    this.loggedInUsername = null;
    this.loggedInCompanyId = null;
    // Clear any other session/user fields as needed
}

    /**
     * Sets the company ID of the currently logged-in user.
     *
     * @param companyId The company ID to set.
     */
    public void setLoggedInCompanyId(String companyId) {
        System.out.println("[USER] Logging in company ID: " + companyId);
        this.loggedInCompanyId = companyId;
    }

    /**
     * Validates if a user exists with the given username, password, and role.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @param role     The role to validate.
     * @return True if the user is valid, false otherwise.
     */
    public boolean isValidUser(String username, String password, String role) {
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split(",", -1);
                if (userData.length >= 3 &&
                    userData[0].equals(username) &&
                    userData[1].equals(password) &&
                    userData[2].equals(role)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to validate user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the total balance for the logged-in user.
     *
     * @return The total balance as a double.
     */
    public double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Loads the user's balance from a file or database.
     *
     * @param username The username of the logged-in user.
     * @return The user's balance as a double.
     */
    private double loadUserBalance(String username) {
        // Simulate loading the balance from a file or database
        // Replace this with actual logic to load the balance
        try (Scanner scanner = new Scanner(new File("users.txt"))) {
            while (scanner.hasNextLine()) {
                String[] balanceData = scanner.nextLine().split(",", -1);
                if (balanceData.length >= 2 && balanceData[0].equals(username)) {
                    return Double.parseDouble(balanceData[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[ERROR] Failed to load balance for user: " + username);
            e.printStackTrace();
        }
        return 0.0; // Default balance if not found or an error occurs
    }
}
