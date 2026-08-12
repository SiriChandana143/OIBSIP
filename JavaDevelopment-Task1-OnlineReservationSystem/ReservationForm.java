import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ReservationForm.java
 * Swing GUI window for booking train tickets.
 * Collects passenger details with side hint formats, validates inputs,
 * generates a PNR, and saves reservation into SQLite database.
 */
public class ReservationForm extends JFrame implements ActionListener {

    // GUI Components
    private JLabel labelTitle;
    private JLabel labelPassengerName, labelTrainNo, labelTrainName, labelClassType, labelDate, labelSource, labelDestination;
    private JTextField textPassengerName, textTrainNo, textTrainName, textDate, textSource, textDestination;
    private JComboBox<String> comboClassType;
    private JButton buttonBook, buttonClear, buttonCancellation, buttonExit;
    private JPanel panelForm;

    /**
     * Constructor to initialize and layout components of Reservation Form.
     */
    public ReservationForm() {
        setTitle("Online Reservation System - Book Ticket");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(null);
        setResizable(false);

        // Header Title
        labelTitle = new JLabel("Train Ticket Reservation", SwingConstants.CENTER);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelTitle.setForeground(new Color(25, 118, 210));
        labelTitle.setBounds(20, 15, 650, 35);
        add(labelTitle);

        // Form Panel Setup
        panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(20, 60, 645, 360);

        int yOffset = 10;
        int labelWidth = 140;
        int fieldWidth = 220;
        int hintWidth = 260;
        int height = 28;
        int gap = 42;

        Font hintFont = new Font("Segoe UI", Font.ITALIC, 12);
        Color hintColor = new Color(108, 117, 125);

        // 1. Passenger Name Field & Example Hint
        labelPassengerName = new JLabel("Passenger Name:");
        labelPassengerName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPassengerName.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelPassengerName);

        textPassengerName = new JTextField();
        textPassengerName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textPassengerName.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(textPassengerName);

        JLabel hintName = new JLabel("e.g. Rahul Sharma");
        hintName.setFont(hintFont);
        hintName.setForeground(hintColor);
        hintName.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintName);

        // 2. Train Number Field & Example Hint
        yOffset += gap;
        labelTrainNo = new JLabel("Train Number:");
        labelTrainNo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelTrainNo.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelTrainNo);

        textTrainNo = new JTextField();
        textTrainNo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textTrainNo.setBounds(155, yOffset, fieldWidth, height);
        textTrainNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String trainNo = textTrainNo.getText().trim();
                if (!trainNo.isEmpty()) {
                    String trainName = TrainData.getTrainName(trainNo);
                    textTrainName.setText(trainName);
                } else {
                    textTrainName.setText("");
                }
            }
        });
        panelForm.add(textTrainNo);

        JLabel hintTrainNo = new JLabel("e.g. 12002, 12301, 12626");
        hintTrainNo.setFont(hintFont);
        hintTrainNo.setForeground(hintColor);
        hintTrainNo.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintTrainNo);

        // 3. Train Name Field (Auto-populated hint)
        yOffset += gap;
        labelTrainName = new JLabel("Train Name:");
        labelTrainName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelTrainName.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelTrainName);

        textTrainName = new JTextField();
        textTrainName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textTrainName.setEditable(false);
        textTrainName.setBackground(new Color(245, 245, 245));
        textTrainName.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(textTrainName);

        JLabel hintTrainName = new JLabel("(Auto-filled based on Train No)");
        hintTrainName.setFont(hintFont);
        hintTrainName.setForeground(hintColor);
        hintTrainName.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintTrainName);

        // 4. Class Type JComboBox & Hint
        yOffset += gap;
        labelClassType = new JLabel("Class Type:");
        labelClassType.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelClassType.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelClassType);

        String[] classOptions = {"Select Class", "AC 1st Class (1A)", "AC 2 Tier (2A)", "AC 3 Tier (3A)", "Sleeper (SL)", "Executive Chair Car (EC)"};
        comboClassType = new JComboBox<>(classOptions);
        comboClassType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboClassType.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(comboClassType);

        JLabel hintClass = new JLabel("e.g. Sleeper (SL) or AC 3 Tier");
        hintClass.setFont(hintFont);
        hintClass.setForeground(hintColor);
        hintClass.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintClass);

        // 5. Date of Journey Field & Format Hint
        yOffset += gap;
        labelDate = new JLabel("Date of Journey:");
        labelDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelDate.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelDate);

        textDate = new JTextField();
        textDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textDate.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(textDate);

        JLabel hintDate = new JLabel("Format: dd-MM-yyyy (e.g. 25-12-2026)");
        hintDate.setFont(hintFont);
        hintDate.setForeground(hintColor);
        hintDate.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintDate);

        // 6. Source Station Field & Hint
        yOffset += gap;
        labelSource = new JLabel("Source Station:");
        labelSource.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelSource.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelSource);

        textSource = new JTextField();
        textSource.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textSource.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(textSource);

        JLabel hintSource = new JLabel("e.g. New Delhi");
        hintSource.setFont(hintFont);
        hintSource.setForeground(hintColor);
        hintSource.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintSource);

        // 7. Destination Station Field & Hint
        yOffset += gap;
        labelDestination = new JLabel("Destination:");
        labelDestination.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelDestination.setBounds(10, yOffset, labelWidth, height);
        panelForm.add(labelDestination);

        textDestination = new JTextField();
        textDestination.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textDestination.setBounds(155, yOffset, fieldWidth, height);
        panelForm.add(textDestination);

        JLabel hintDestination = new JLabel("e.g. Mumbai Central");
        hintDestination.setFont(hintFont);
        hintDestination.setForeground(hintColor);
        hintDestination.setBounds(385, yOffset, hintWidth, height);
        panelForm.add(hintDestination);

        add(panelForm);

        // Action Buttons Setup
        buttonBook = new JButton("Book Ticket");
        buttonBook.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonBook.setBackground(new Color(40, 167, 69));
        buttonBook.setForeground(Color.WHITE);
        buttonBook.setBounds(80, 440, 130, 35);
        buttonBook.setFocusable(false);
        buttonBook.addActionListener(this);
        add(buttonBook);

        buttonCancellation = new JButton("Cancellation");
        buttonCancellation.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonCancellation.setBackground(new Color(255, 193, 7));
        buttonCancellation.setBounds(225, 440, 130, 35);
        buttonCancellation.setFocusable(false);
        buttonCancellation.addActionListener(this);
        add(buttonCancellation);

        buttonClear = new JButton("Clear");
        buttonClear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonClear.setBackground(new Color(108, 117, 125));
        buttonClear.setForeground(Color.WHITE);
        buttonClear.setBounds(370, 440, 110, 35);
        buttonClear.setFocusable(false);
        buttonClear.addActionListener(this);
        add(buttonClear);

        buttonExit = new JButton("Exit");
        buttonExit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonExit.setBackground(new Color(220, 53, 69));
        buttonExit.setForeground(Color.WHITE);
        buttonExit.setBounds(495, 440, 110, 35);
        buttonExit.setFocusable(false);
        buttonExit.addActionListener(this);
        add(buttonExit);
    }

    /**
     * Action Listener implementation for button clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonBook) {
            handleBooking();
        } else if (e.getSource() == buttonCancellation) {
            this.dispose();
            CancellationForm cancelForm = new CancellationForm();
            cancelForm.setVisible(true);
        } else if (e.getSource() == buttonClear) {
            clearFields();
        } else if (e.getSource() == buttonExit) {
            System.exit(0);
        }
    }

    /**
     * Handles ticket booking logic, validation, PNR generation, and SQLite insertion.
     */
    private void handleBooking() {
        String passengerName = textPassengerName.getText().trim();
        String trainNo = textTrainNo.getText().trim();
        String trainName = textTrainName.getText().trim();
        String classType = (String) comboClassType.getSelectedItem();
        String journeyDate = textDate.getText().trim();
        String source = textSource.getText().trim();
        String destination = textDestination.getText().trim();

        // 1. Validation: No empty fields
        if (passengerName.isEmpty() || trainNo.isEmpty() || classType.equals("Select Class")
                || journeyDate.isEmpty() || source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required! Please complete all information.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validation: Train number must be numeric
        if (!isNumeric(trainNo)) {
            JOptionPane.showMessageDialog(this,
                    "Train Number must contain digits only!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validation: Date format dd-MM-yyyy (e.g. 25-12-2026)
        if (!isValidDateFormat(journeyDate)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Date! Please use format dd-MM-yyyy (Day: 01-31, Month: 01-12, Year: 2024-2100).\nExample: 25-12-2026",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If train name is empty, auto-populate using TrainData
        if (trainName.isEmpty()) {
            trainName = TrainData.getTrainName(trainNo);
            textTrainName.setText(trainName);
        }

        // 4. Generate unique 6-digit PNR
        String pnr = PNRGenerator.generatePNR();

        // 5. Save reservation to SQLite Database
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Database Connection Failed!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO reservations (pnr, passenger_name, train_no, train_name, class_type, journey_date, source, destination) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pnr);
            pstmt.setString(2, passengerName);
            pstmt.setString(3, trainNo);
            pstmt.setString(4, trainName);
            pstmt.setString(5, classType);
            pstmt.setString(6, journeyDate);
            pstmt.setString(7, source);
            pstmt.setString(8, destination);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                // Display confirmation dialog with details
                String confirmationMessage = "Reservation Successful!\n\n"
                        + "PNR Number: " + pnr + "\n"
                        + "Passenger Name: " + passengerName + "\n"
                        + "Train Name: " + trainName + " (" + trainNo + ")\n"
                        + "Class: " + classType + "\n"
                        + "Date of Journey: " + journeyDate + "\n"
                        + "Source: " + source + "\n"
                        + "Destination: " + destination;

                JOptionPane.showMessageDialog(this,
                        confirmationMessage,
                        "Booking Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear fields after successful booking
                clearFields();
            }

            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving reservation: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Helper method to clear all text input fields.
     */
    private void clearFields() {
        textPassengerName.setText("");
        textTrainNo.setText("");
        textTrainName.setText("");
        comboClassType.setSelectedIndex(0);
        textDate.setText("");
        textSource.setText("");
        textDestination.setText("");
    }

    /**
     * Helper method to check if a string contains only numeric digits.
     */
    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method to validate dd-MM-yyyy date format strictly.
     * Enforces Day (1-31), Month (1-12), Year (2024-2100).
     */
    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.length() != 10) {
            return false;
        }

        // Date must have hyphens at position 2 and 5 (e.g. 25-12-2026)
        if (dateStr.charAt(2) != '-' || dateStr.charAt(5) != '-') {
            return false;
        }

        String[] parts = dateStr.split("-");
        if (parts.length != 3) {
            return false;
        }

        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Enforce Month range strictly 1 to 12
            if (month < 1 || month > 12) {
                return false;
            }

            // Enforce Day range strictly 1 to 31
            if (day < 1 || day > 31) {
                return false;
            }

            // Enforce Year range
            if (year < 2024 || year > 2100) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
