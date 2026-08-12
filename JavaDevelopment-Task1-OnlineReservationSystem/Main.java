import javax.swing.SwingUtilities;

/**
 * Main.java
 * Main entry point of the Online Reservation System.
 * Initializes the SQLite database and launches the Login GUI.
 */
public class Main {

    public static void main(String[] args) {
        // Step 1: Initialize Database and Tables
        System.out.println("Initializing Online Reservation System...");
        DBConnection.createTables();

        // Step 2: Launch the GUI application on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the Login Form
                Login loginForm = new Login();
                loginForm.setVisible(true);
            }
        });
    }
}
