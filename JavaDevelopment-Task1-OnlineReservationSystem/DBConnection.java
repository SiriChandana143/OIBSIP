import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBConnection.java
 * This class handles database connection to SQLite
 * and initializes the database tables (users and reservations).
 */
public class DBConnection {

    // Database file location URL for SQLite
    private static final String DB_URL = "jdbc:sqlite:reservation.db";

    /**
     * Method to establish and return a Connection to the SQLite database.
     * @return Connection object
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load the SQLite JDBC driver class
            Class.forName("org.sqlite.JDBC");
            
            // Connect to SQLite database file named reservation.db
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Method to automatically create required database tables if they do not exist.
     * Also inserts the default admin account into the users table.
     */
    public static void createTables() {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Cannot initialize tables because connection failed.");
            return;
        }

        try {
            Statement stmt = conn.createStatement();

            // SQL query to create the users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "username TEXT PRIMARY KEY, "
                    + "password TEXT NOT NULL"
                    + ");";

            // SQL query to create the reservations table
            String createReservationsTable = "CREATE TABLE IF NOT EXISTS reservations ("
                    + "pnr TEXT PRIMARY KEY, "
                    + "passenger_name TEXT NOT NULL, "
                    + "train_no TEXT NOT NULL, "
                    + "train_name TEXT NOT NULL, "
                    + "class_type TEXT NOT NULL, "
                    + "journey_date TEXT NOT NULL, "
                    + "source TEXT NOT NULL, "
                    + "destination TEXT NOT NULL"
                    + ");";

            // Execute the table creation statements
            stmt.execute(createUsersTable);
            stmt.execute(createReservationsTable);

            stmt.close();

            // Check if the default admin user exists
            String checkAdminQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkAdminQuery);
            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();

            // If admin does not exist, insert default credentials
            if (!rs.next()) {
                String insertAdminQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertAdminQuery);
                insertStmt.setString(1, "admin");
                insertStmt.setString(2, "admin123");
                insertStmt.executeUpdate();
                insertStmt.close();
                System.out.println("Default admin user created successfully (username: admin, password: admin123).");
            }

            rs.close();
            checkStmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error initializing database tables: " + e.getMessage());
        }
    }
}
