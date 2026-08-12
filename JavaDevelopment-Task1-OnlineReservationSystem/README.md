# Online Reservation System (Java Swing + SQLite)

A clean, beginner-friendly desktop application for train ticket reservation built using pure **Java Swing** and **SQLite JDBC**. Designed specifically for 2nd-year B.Tech Computer Science / Information Technology students.

---

## Features

1. **User Login**:
   - Authenticates against the `users` SQLite database table.
   - Default credentials pre-loaded:
     - **Username**: `admin`
     - **Password**: `admin123`
   - Input validation with user-friendly error alerts.

2. **Train Reservation**:
   - Passenger Name, Train Number, Train Name, Class Type, Journey Date, Source, and Destination.
   - Auto-populates train names based on train numbers (`12002` -> *Bhopal Shatabdi*, `12301` -> *Rajdhani Express*, etc.).
   - Validates non-empty fields, numeric train numbers, and `dd-MM-yyyy` date formats.
   - Automatically generates a unique 6-digit PNR.
   - Displays booking confirmation dialog with full summary.

3. **Ticket Cancellation**:
   - Search booking by PNR number.
   - Displays passenger details, train name, route, and journey date.
   - Deletes booking from SQLite database upon confirmation.

4. **Database & Storage**:
   - SQLite relational database (`reservation.db`).
   - Auto-initializes tables and default admin credentials on first startup.

---

## How to Run in Visual Studio Code (Zero Configuration Required!)

### Option A: Direct VS Code Run Button
1. Open [Main.java](file:///c:/Users/ASUS/OneDrive/Desktop/java%20project%201/Main.java) in Visual Studio Code.
2. Click the **Run** button above `public static void main(String[] args)` or press **F5**.

---

### Option B: Terminal / Command Prompt
Simply type:

```cmd
java Main
```

Or to recompile and run:
```cmd
javac *.java
java Main
```

---

## Login Credentials
* **Username**: `admin`
* **Password**: `admin123`

---

## College Viva Q&A Guide

1. **Q: What architecture or framework is used for the GUI?**  
   *A:* The application uses **Java Swing**, which is part of Java Foundation Classes (JFC). Swing components such as `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JButton`, and `JOptionPane` are used to build the user interface.

2. **Q: How does the application connect to SQLite?**  
   *A:* Connection is established using the JDBC API via `DriverManager.getConnection("jdbc:sqlite:reservation.db")`. The `Class.forName("org.sqlite.JDBC")` loads the driver into memory.

3. **Q: Why did you use `PreparedStatement` instead of standard `Statement`?**  
   *A:* `PreparedStatement` pre-compiles SQL queries, improves execution efficiency for repetitive queries, and prevents **SQL Injection attacks** by escaping input parameters automatically using placeholders (`?`).

4. **Q: How is table initialization handled?**  
   *A:* `DBConnection.createTables()` uses `CREATE TABLE IF NOT EXISTS` SQL statements. It runs at startup in `Main.java` to ensure database tables are created automatically if missing.

5. **Q: How is the PNR generated?**  
   *A:* `PNRGenerator.java` uses `(int)(Math.random() * 900000) + 100000` to generate a random 6-digit integer formatted as a String.

6. **Q: How are Swing event actions handled?**  
   *A:* GUI classes implement the `ActionListener` interface and override the `actionPerformed(ActionEvent e)` method to handle button click events.

7. **Q: How is input validation performed?**  
   *A:* Inputs are checked before database queries: empty string validation using `.trim().isEmpty()`, numeric verification using `Character.isDigit()`, and date format validation by splitting strings by `-`.

8. **Q: What is the purpose of `SwingUtilities.invokeLater()` in `Main.java`?**  
   *A:* It ensures that the Swing GUI is created and updated on the **Event Dispatch Thread (EDT)**, which is the standard thread safety requirement for Swing applications.
