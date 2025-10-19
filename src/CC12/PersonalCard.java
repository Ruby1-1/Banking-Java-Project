package CC12;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PersonalCard {
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    /**
     * Default constructor that generates a new card with random details.
     */
    public PersonalCard() {
        this.cardNumber = generateCardNumber();
        this.expirationDate = generateExpirationDate();
        this.cvv = generateCVV();
    }

    /**
     * Constructor to create a card with specific details.
     *
     * @param cardNumber      The card number.
     * @param expirationDate  The expiration date.
     * @param cvv             The CVV.
     */
    public PersonalCard(String cardNumber, String expirationDate, String cvv) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    /**
     * Generates a random 16-digit card number.
     *
     * @return The generated card number.
     */
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumberBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumberBuilder.append(random.nextInt(10));
        }
        return cardNumberBuilder.toString();
    }

    /**
     * Generates an expiration date 2 years from the current date.
     *
     * @return The generated expiration date in MM/yy format.
     */
    private String generateExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = currentDate.plusYears(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return expirationDate.format(formatter);
    }

    /**
     * Generates a random 3-digit CVV.
     *
     * @return The generated CVV.
     */
    private String generateCVV() {
        Random random = new Random();
        int cvv = random.nextInt(900) + 100; // Ensures a 3-digit number
        return String.valueOf(cvv);
    }

    // Getter and Setter for Card Number
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    // Getter and Setter for Expiration Date
    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    // Getter and Setter for CVV
    public String getCVV() {
        return cvv;
    }

    public void setCVV(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "PersonalCard{" +
               "cardNumber='" + cardNumber + '\'' +
               ", expirationDate='" + expirationDate + '\'' +
               ", cvv='" + cvv + '\'' +
               '}';
    }
}
