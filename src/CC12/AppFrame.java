package CC12;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserManager userManager;
    private CardManager cardManager;
    private CompanyCardManager companyCardManager; // Add this line

    /**
     * Constructor for the AppFrame.
     * Initializes the managers, configures the window, sets up UI components, and adds pages.
     */
    public AppFrame() {
        // Initialize managers
        cardManager = new CardManager(); // Ensure CardManager is initialized
        companyCardManager = new CompanyCardManager(); // Initialize CompanyCardManager
        userManager = new UserManager(cardManager); // Pass CardManager to UserManager

        // Configure the main application window
        configureWindow();
        initializeUIComponents();
        setupPages();

        // Make the frame visible
        setVisible(true);
    }

    /**
     * Gets the CompanyCardManager instance.
     *
     * @return The CompanyCardManager instance.
     */
    public CompanyCardManager getCompanyCardManager() {
        return companyCardManager;
    }
    

    /**
     * Configures the main application window.
     */
    private void configureWindow() {
        setTitle("GASTO");
        setSize(340, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Initializes the main UI components.
     * Sets up the CardLayout and the main panel.
     */
    private void initializeUIComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    /**
     * Sets up the pages for the application.
     * Adds all the pages to the CardLayout with their respective names.
     */
    private void setupPages() {
        // Create the PersonalDashboard instance
        PersonalDashboard personalDashboard = new PersonalDashboard(this);


        // Add pages to the main panel
        mainPanel.add(new HomePage(this), "HomePage");
        mainPanel.add(new ProfileSelectionPage(this), "ProfileSelectionPage");
        mainPanel.add(new EmployeeLoginPage(this), "EmployeeLoginPage");
        mainPanel.add(new OwnerLoginPage(this), "OwnerLoginPage");
        mainPanel.add(new PersonalLoginPage(this), "PersonalLoginPage");
        mainPanel.add(new RoleSelectionPage(this), "RoleSelectionPage");
        mainPanel.add(new OwnerRegisterPage(this), "OwnerRegisterPage");
        mainPanel.add(new PersonalRegisterPage(this), "PersonalRegisterPage");
        mainPanel.add(new OwnerDashboard(this), "OwnerDashboard");
        mainPanel.add(new PersonalDashboard(this), "PersonalDashboard");
        mainPanel.add(new PersonalFinancial(this, personalDashboard), "PersonalFinancial");
        mainPanel.add(new EmpmanagerPage(this), "EmployeeManagementPage");
        mainPanel.add(new AddEmpPage(this), "AddEmpPage");
        mainPanel.add(new RemoveEmp(this), "RemoveEmp");
        mainPanel.add(new EMPdash(this), "EMPdash");
        mainPanel.add(new GIVEcardacc(this), "GIVEcardacc");
        mainPanel.add(new EMPcardview(this), "EMPcardview");
        mainPanel.add(new EMPreimburse(this), "EMPreimburse");
        mainPanel.add(new OWNERrimbursement(this), "OWNERrimbursement");
        mainPanel.add(new OWNERfinancials(this),"OWNERfinancials");
        mainPanel.add(new EMPfinancial(this), "EMPfinancial");

        // For PersonalCardPage, let's add an instance once so that getComponentByName can find it initially
        PersonalCardPage personalCardPage = new PersonalCardPage(cardManager, this);
        personalCardPage.putClientProperty("name", "PersonalCardPage");
        mainPanel.add(personalCardPage, "PersonalCardPage");
    }

    /**
     * Retrieves a component from the main panel by its name.
     *
     * @param name The name of the component to retrieve.
     * @return The component with the specified name, or null if not found.
     */
    private Component getComponentByName(String name) {
        for (Component component : mainPanel.getComponents()) {
            if (component instanceof JComponent && name.equals(((JComponent) component).getClientProperty("name"))) {
                return component;
            }
        }
        return null;
    }



public void switchToEMPfinancial() {
    System.out.println("[NAVIGATION] Switching to EMPfinancial");

    // Remove the existing EMPfinancial page (if any)
    Component oldPage = getComponentByName("EMPfinancial");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new EMPfinancial instance with the current session
    EMPfinancial empFinancialPage = new EMPfinancial(this);
    empFinancialPage.putClientProperty("name", "EMPfinancial");
    mainPanel.add(empFinancialPage, "EMPfinancial");

    // Show the EMPfinancial page
    cardLayout.show(mainPanel, "EMPfinancial");
    mainPanel.revalidate();
    mainPanel.repaint();
}







public void switchToOWNERfinancials() {
    System.out.println("[NAVIGATION] Switching to OWNERfinancials");

    // Get the logged-in company ID
    String companyId = getUserManager().getLoggedInCompanyId();
    if (companyId == null || companyId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No company is logged in. Please log in as an owner first.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Remove the existing OWNERfinancials page (if any) to refresh the data
    Component oldPage = getComponentByName("OWNERfinancials");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new OWNERfinancials instance and add it to the main panel
    OWNERfinancials ownerFinancialsPage = new OWNERfinancials(this);
    ownerFinancialsPage.putClientProperty("name", "OWNERfinancials");
    mainPanel.add(ownerFinancialsPage, "OWNERfinancials");

    // Show the OWNERfinancials page
    cardLayout.show(mainPanel, "OWNERfinancials");
    mainPanel.revalidate();
    mainPanel.repaint();
}







public void switchToOWNERrimbursement() {
    System.out.println("[NAVIGATION] Switching to OWNERrimbursement");

    // Remove the existing OWNERrimbursement page (if any) to refresh the queue
    Component oldPage = getComponentByName("OWNERrimbursement");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new OWNERrimbursement instance and add it to the main panel
    OWNERrimbursement ownerRimbursementPage = new OWNERrimbursement(this);
    ownerRimbursementPage.putClientProperty("name", "OWNERrimbursement");
    mainPanel.add(ownerRimbursementPage, "OWNERrimbursement");

    // Show the OWNERrimbursement page
    cardLayout.show(mainPanel, "OWNERrimbursement");
    mainPanel.revalidate();
    mainPanel.repaint();
}






    public void switchToEMPcardview() {
    System.out.println("[NAVIGATION] Switching to EMPcardview");

    // Get the company ID of the logged-in employee
    String companyId = getUserManager().getLoggedInCompanyId();
    if (companyId == null || companyId.isEmpty()) {
        System.out.println("[ERROR] No company ID found for the logged-in employee.");
        return;
    }

    // Remove the existing EMPcardview page (if any) to refresh the details
    Component oldPage = getComponentByName("EMPcardview");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new EMPcardview instance and add it to the main panel
    EMPcardview empCardViewPage = new EMPcardview(this);
    empCardViewPage.putClientProperty("name", "EMPcardview");
    mainPanel.add(empCardViewPage, "EMPcardview");

    // Show the EMPcardview page
    cardLayout.show(mainPanel, "EMPcardview");
    mainPanel.revalidate();
    mainPanel.repaint();
}





public void switchToGiveCardAcc() {
    System.out.println("[NAVIGATION] Switching to GIVEcardacc");

    // Get the logged-in company ID
    String companyId = getUserManager().getLoggedInCompanyId();
    if (companyId == null || companyId.isEmpty()) {
        System.out.println("[ERROR] No logged-in company. Cannot load employees.");
        return;
    }

    // Remove the existing GIVEcardacc page (if any) to refresh the list
    Component oldPage = getComponentByName("GIVEcardacc");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new GIVEcardacc instance and add it to the main panel
    GIVEcardacc giveCardAccPage = new GIVEcardacc(this);
    giveCardAccPage.putClientProperty("name", "GIVEcardacc");
    mainPanel.add(giveCardAccPage, "GIVEcardacc");

    // Show the GIVEcardacc page
    cardLayout.show(mainPanel, "GIVEcardacc");
    mainPanel.revalidate();
    mainPanel.repaint();
}







  public void switchToRemoveEmp() {
    System.out.println("[NAVIGATION] Switching to RemoveEmp");

    // Get the logged-in company ID
    String companyId = getUserManager().getLoggedInCompanyId();
    if (companyId == null || companyId.isEmpty()) {
        System.out.println("[ERROR] No logged-in company. Cannot load employees.");
        return;
    }

    // Remove the existing RemoveEmp page (if any) to refresh the list
    Component oldPage = getComponentByName("RemoveEmp");
    if (oldPage != null) {
        mainPanel.remove(oldPage);
    }

    // Create a new RemoveEmp instance and add it to the main panel
    RemoveEmp removeEmpPage = new RemoveEmp(this);
    removeEmpPage.putClientProperty("name", "RemoveEmp");
    mainPanel.add(removeEmpPage, "RemoveEmp");

    // Show the RemoveEmp page
    cardLayout.show(mainPanel, "RemoveEmp");
    mainPanel.revalidate();
    mainPanel.repaint();
}






public void switchToCompanyCardPage() {
    System.out.println("[NAVIGATION] Switching to CompanyCardPage");

    // Load the card data for the logged-in company
    String loggedInCompanyId = getUserManager().getLoggedInCompanyId();
    if (loggedInCompanyId == null || loggedInCompanyId.isEmpty()) {
        System.out.println("[ERROR] No logged-in company. Cannot load card data.");
        return;
    }

    getCompanyCardManager().loadCompanyCardData(loggedInCompanyId); // Ensure card data is loaded

    // Check if the CompanyCardPage already exists
    Component oldPage = getComponentByName("CompanyCardPage");
    if (oldPage != null && oldPage instanceof CompanyCardPage) {
        // Refresh the existing page
        CompanyCardPage existingPage = (CompanyCardPage) oldPage;
        existingPage.refresh();
    } else {
        // Create and name the new CompanyCardPage if it doesn't exist
        CompanyCardPage newCardPage = new CompanyCardPage(getCompanyCardManager(), this);
        newCardPage.putClientProperty("name", "CompanyCardPage");
        mainPanel.add(newCardPage, "CompanyCardPage");
    }

    // Switch to the CompanyCardPage
    cardLayout.show(mainPanel, "CompanyCardPage");
    mainPanel.revalidate();
    mainPanel.repaint();
}



    public void switchToPersonalCardPage() {
        System.out.println("[NAVIGATION] Switching to PersonalCardPage");

        // Load the card data for the logged-in user
        String username = getUserManager().getLoggedInUsername();
        if (username == null || username.isEmpty()) {
            System.out.println("[ERROR] No logged-in user. Cannot load card data.");
            return;
        }

        getCardManager().loadCardData(username); // Ensure card data is loaded

        // Check if the PersonalCardPage already exists
        Component oldPage = getComponentByName("PersonalCardPage");
        if (oldPage != null && oldPage instanceof PersonalCardPage) {
            // Refresh the existing page
            PersonalCardPage existingPage = (PersonalCardPage) oldPage;
            existingPage.refresh();
        } else {
            // Create and name the new PersonalCardPage if it doesn't exist
            PersonalCardPage newCardPage = new PersonalCardPage(getCardManager(), this);
            newCardPage.putClientProperty("name", "PersonalCardPage");
            mainPanel.add(newCardPage, "PersonalCardPage");
        }

        // Switch to the PersonalCardPage
        cardLayout.show(mainPanel, "PersonalCardPage");
        mainPanel.revalidate();
        mainPanel.repaint();
    }





    public void switchToPersonalFinancial() {
        System.out.println("[NAVIGATION] Switching to PersonalFinancial");

        // Get the existing PersonalDashboard (should have setName earlier)
        PersonalDashboard personalDashboard = (PersonalDashboard) getComponentByName("PersonalDashboard");

        // Remove the existing PersonalFinancial page (if any)
        Component oldPage = getComponentByName("PersonalFinancial");
        if (oldPage != null) {
            mainPanel.remove(oldPage);
        }

        // Create a new PersonalFinancial instance and set its name
        PersonalFinancial personalFinancial = new PersonalFinancial(this, personalDashboard);
        personalFinancial.setName("PersonalFinancial");
        mainPanel.add(personalFinancial, "PersonalFinancial");

        // Show the new page
        cardLayout.show(mainPanel, "PersonalFinancial");
        mainPanel.revalidate();
        mainPanel.repaint();
    }













    public void switchTo(String pageName) {
        System.out.println("[NAVIGATION] Switching to page: " + pageName);
        if ("PersonalCardPage".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToPersonalCardPage");
            switchToPersonalCardPage();
            return; // avoid running cardLayout.show twice
        
        }

        if ("CompanyCardPage".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToCompanyCardPage");
            switchToCompanyCardPage();
            return; // avoid running cardLayout.show twice
        }
        
        if("EmpmanagerPage".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToEmpmanagerPage");
            switchTo("EmployeeManagementPage");
            return; // avoid running cardLayout.show twice
        }
        if ("RemoveEmp".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToRemoveEmp");
            switchToRemoveEmp();
            return; // avoid running cardLayout.show twice
        }
        if ("GIVEcardacc".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToGiveCardAcc");
            switchToGiveCardAcc();
            return; // avoid running cardLayout.show twice
        }
        if ("EMPcardview".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToEMPcardview");
            switchToEMPcardview();
            return; // avoid running cardLayout.show twice
        }
        if ("OWNERrimbursement".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToOWNERrimbursement");
            switchToOWNERrimbursement();
            return; // avoid running cardLayout.show twice
        }
        if ("OWNERfinancials".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToOWNERfinancials");
            switchToOWNERfinancials();
            return; // avoid running cardLayout.show twice
        }
        if ("EMPfinancial".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToEMPfinancial");
            switchToEMPfinancial();
            return; // avoid running cardLayout.show twice
        }
        




        if ("PersonalFinancial".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToPersonalFinancial");
            switchToPersonalFinancial();
            return; // avoid running cardLayout.show twice
        }



        if ("PersonalDashboard".equals(pageName)) {
            System.out.println("[INFO] Redirecting switchTo to switchToPersonalDashboard");
            switchToPersonalDashboard();
            return; // avoid running cardLayout.show twice
        }
        cardLayout.show(mainPanel, pageName);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

   public void switchToPersonalDashboard() {
    System.out.println("[NAVIGATION] Switching to PersonalDashboard");

    // Load the user data
    String username = getUserManager().getLoggedInUsername();
    if (username == null || username.isEmpty()) {
        System.out.println("[ERROR] No logged-in user. Cannot load user data.");
        username = "Guest"; // Default to Guest if no user is logged in
    }

    double totalBalance = loadUserBalance(username);
    if (totalBalance < 0) {
        System.out.println("[ERROR] Failed to load balance for user: " + username);
        return;
    }

    // Check if the PersonalDashboard already exists
    Component oldPage = getComponentByName("PersonalDashboard");
    if (oldPage != null && oldPage instanceof PersonalDashboard) {
        // Refresh the existing page
        PersonalDashboard existingDashboard = (PersonalDashboard) oldPage;
        existingDashboard.updateWelcomeMessage(username); // Update the welcome message
        existingDashboard.updateBalance(totalBalance);    // Update the balance
    } else {
        // Create and name the new PersonalDashboard if it doesn't exist
        PersonalDashboard newDashboard = new PersonalDashboard(this);
        newDashboard.updateWelcomeMessage(username);
        newDashboard.updateBalance(totalBalance);
        newDashboard.putClientProperty("name", "PersonalDashboard");
        mainPanel.add(newDashboard, "PersonalDashboard");
    }

    // Switch to the PersonalDashboard
    cardLayout.show(mainPanel, "PersonalDashboard");
    mainPanel.revalidate();
    mainPanel.repaint();
}


/**
 * Loads the user's balance from the statements.txt file.
 *
 * @param username The username of the logged-in user.
 * @return The total balance of the user, or -1 if an error occurs.
 */
private double loadUserBalance(String username) {
    double totalBalance = 0.0;

    File file = new File("data/" + username + "_statements.txt");
    if (!file.exists()) {
        System.out.println("[DEBUG] No statements file found for user: " + username);
        return 0.0; // Default balance if no file exists
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Total Balance:")) {
                String balanceString = line.replace("Total Balance:", "").replace("PHP", "").trim();
                totalBalance = Double.parseDouble(balanceString);
                System.out.println("[DEBUG] Total balance loaded: " + totalBalance + " PHP");
                break;
            }
        }
    } catch (IOException e) {
        System.out.println("[ERROR] Failed to load user balance: " + e.getMessage());
        e.printStackTrace();
        return -1; // Indicate an error occurred
    } catch (NumberFormatException e) {
        System.out.println("[ERROR] Failed to parse balance: " + e.getMessage());
        e.printStackTrace();
        return -1; // Indicate an error occurred
    }

    return totalBalance;
}
    /**
     * Gets the UserManager instance.
     *
     * @return The UserManager instance.
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets the CardManager instance.
     *
     * @return The CardManager instance.
     */
    public CardManager getCardManager() {
        return cardManager;
    }
}

