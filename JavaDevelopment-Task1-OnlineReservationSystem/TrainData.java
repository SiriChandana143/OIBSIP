/**
 * TrainData.java
 * This helper class provides a simple method to get the train name 
 * based on a given train number using a switch statement.
 */
public class TrainData {

    /**
     * Given a train number string, returns the corresponding Train Name.
     * Uses simple string comparison and switch statement.
     * 
     * @param trainNumber String representing train number
     * @return Train name string
     */
    public static String getTrainName(String trainNumber) {
        if (trainNumber == null || trainNumber.trim().isEmpty()) {
            return "";
        }

        // Clean up input whitespace
        String cleanedNumber = trainNumber.trim();

        // Switch statement to map train numbers to train names
        switch (cleanedNumber) {
            case "12002":
                return "Bhopal Shatabdi Express";
            case "12301":
                return "Howrah Rajdhani Express";
            case "12626":
                return "Kerala Express";
            case "12723":
                return "Telangana Express";
            case "12951":
                return "Mumbai Rajdhani Express";
            case "12650":
                return "Karnataka Sampark Kranti Express";
            case "12801":
                return "Purushottam Express";
            case "12260":
                return "Sealdah Duronto Express";
            default:
                return "Superfast Express Train";
        }
    }
}
