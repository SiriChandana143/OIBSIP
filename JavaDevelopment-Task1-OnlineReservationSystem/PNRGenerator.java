/**
 * PNRGenerator.java
 * Helper class to generate a unique 6-digit PNR (Passenger Name Record) number.
 */
public class PNRGenerator {

    /**
     * Generates a random 6-digit PNR number.
     * Uses Math.random() basic math function.
     * 
     * @return String representation of 6-digit PNR
     */
    public static String generatePNR() {
        // Math.random() produces a double between 0.0 and 1.0
        // Multiply by 900000 and add 100000 to get a number between 100000 and 999999
        int randomNumber = (int) (Math.random() * 900000) + 100000;
        
        // Convert integer to String
        return String.valueOf(randomNumber);
    }
}
