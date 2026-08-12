import javax.swing.JButton;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CancellationForm.java
 * Swing GUI window for searching and cancelling booked tickets by PNR.
 */
public class CancellationForm extends JFrame implements ActionListener {

    // GUI Components
    private JLabel labelTitle, labelPnr, hintPnr;
    private JTextField textPnr;
    private JButton buttonFetch, buttonCancel, buttonBack;

    // Display fields for fetched booking details
    private JLabel labelNameVal, labelTrainVal, labelClassVal, labelDateVal, labelRouteVal;
    private JPanel panelDetails;

    // Currently loaded PNR
    private String currentPnr = "";

    /**
     * Constructor to initialize and layout components of Cancellation Form.
     */
    public CancellationForm() {
        setTitle("Online Reservation System - Ticket Cancellation");
        setSize(580, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // Header Title
        labelTitle = new JLabel("Cancel Reservation", SwingConstants.CENTER);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelTitle.setForeground(new Color(220, 53, 69)); // Red header
        labelTitle.setBounds(20, 15, 520, 35);
        add(labelTitle);

        // PNR Input Section
        labelPnr = new JLabel("Enter PNR Number:");
        labelPnr.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPnr.setBounds(30, 65, 130, 28);
        add(labelPnr);

        textPnr = new JTextField();
        textPnr.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textPnr.setBounds(165, 65, 130, 28);
        add(textPnr);

        buttonFetch = new JButton("Fetch Booking");
        buttonFetch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        buttonFetch.setBackground(new Color(25, 118, 210));
        buttonFetch.setForeground(Color.WHITE);
        buttonFetch.setBounds(305, 65, 120, 28);
        buttonFetch.setFocusable(false);
        buttonFetch.addActionListener(this);
        add(buttonFetch);

        hintPnr = new JLabel("e.g. 654321");
        hintPnr.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hintPnr.setForeground(new Color(108, 117, 125));
        hintPnr.setBounds(435, 65, 110, 28);
        add(hintPnr);

        // Booking Details Panel (Initially empty)
        panelDetails = new JPanel();
        panelDetails.setLayout(null);
        panelDetails.setBounds(30, 110, 505, 200);
        panelDetails.setBackground(new Color(248, 249, 250));

        JLabel headerDetails = new JLabel("--- Booking Details ---", SwingConstants.CENTER);
        headerDetails.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerDetails.setForeground(Color.DARK_GRAY);
        headerDetails.setBounds(10, 10, 485, 25);
        panelDetails.add(headerDetails);

        int y = 45;
        int gap = 30;

        // Passenger Name Label
        JLabel lbl1 = new JLabel("Passenger Name:");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl1.setBounds(20, y, 130, 25);
        panelDetails.add(lbl1);

        labelNameVal = new JLabel("-");
        labelNameVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelNameVal.setBounds(150, y, 335, 25);
        panelDetails.add(labelNameVal);

        // Train Details Label
        y += gap;
        JLabel lbl2 = new JLabel("Train:");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl2.setBounds(20, y, 130, 25);
        panelDetails.add(lbl2);

        labelTrainVal = new JLabel("-");
        labelTrainVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelTrainVal.setBounds(150, y, 335, 25);
        panelDetails.add(labelTrainVal);

        // Class Label
        y += gap;
        JLabel lbl3 = new JLabel("Class & Date:");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl3.setBounds(20, y, 130, 25);
        panelDetails.add(lbl3);

        labelClassVal = new JLabel("-");
        labelClassVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelClassVal.setBounds(150, y, 335, 25);
        panelDetails.add(labelClassVal);

        // Route Label
        y += gap;
        JLabel lbl4 = new JLabel("Route (From -> To):");
        lbl4.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl4.setBounds(20, y, 130, 25);
        panelDetails.add(lbl4);

        labelRouteVal = new JLabel("-");
        labelRouteVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelRouteVal.setBounds(150, y, 335, 25);
        panelDetails.add(labelRouteVal);

        add(panelDetails);

        // Action Buttons Setup
        buttonCancel = new JButton("Cancel Booking");
        buttonCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonCancel.setBackground(new Color(220, 53, 69)); // Red button
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setBounds(130, 340, 150, 35);
        buttonCancel.setEnabled(false); // Disabled until a booking is fetched
        buttonCancel.setFocusable(false);
        buttonCancel.addActionListener(this);
        add(buttonCancel);

        buttonBack = new JButton("Back");
        buttonBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonBack.setBackground(new Color(108, 117, 125)); // Gray button
        buttonBack.setForeground(Color.WHITE);
        buttonBack.setBounds(300, 340, 130, 35);
        buttonBack.setFocusable(false);
        buttonBack.addActionListener(this);
        add(buttonBack);
    }

    /**
     * Handles button action events.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonFetch) {
            handleFetchBooking();
        } else if (e.getSource() == buttonCancel) {
            handleCancelBooking();
        } else if (e.getSource() == buttonBack) {
            this.dispose();
            ReservationForm reservationForm = new ReservationForm();
            reservationForm.setVisible(true);
        }
    }

    /**
     * Fetches booking details from SQLite database using PreparedStatement.
     */
    private void handleFetchBooking() {
        String pnrInput = textPnr.getText().trim();

        if (pnrInput.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a PNR Number to fetch details.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Database Connection Failed!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT * FROM reservations WHERE pnr = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pnrInput);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String passengerName = rs.getString("passenger_name");
                String trainNo = rs.getString("train_no");
                String trainName = rs.getString("train_name");
                String classType = rs.getString("class_type");
                String journeyDate = rs.getString("journey_date");
                String source = rs.getString("source");
                String destination = rs.getString("destination");

                labelNameVal.setText(passengerName);
                labelTrainVal.setText(trainName + " (" + trainNo + ")");
                labelClassVal.setText(classType + " | Date: " + journeyDate);
                labelRouteVal.setText(source + " --> " + destination);

                currentPnr = pnrInput;
                buttonCancel.setEnabled(true);

                JOptionPane.showMessageDialog(this,
                        "Booking Record Found for PNR: " + pnrInput,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                resetDetails();
                JOptionPane.showMessageDialog(this,
                        "No reservation found with PNR: " + pnrInput,
                        "Record Not Found",
                        JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Query Error: " + ex.getMessage(),
                    "SQL Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancels the currently loaded booking after user confirmation.
     */
    private void handleCancelBooking() {
        if (currentPnr.isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this ticket (PNR: " + currentPnr + ")?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = DBConnection.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Database Connection Failed!",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String sql = "DELETE FROM reservations WHERE pnr = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, currentPnr);

                int rowsDeleted = pstmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Reservation Cancelled Successfully.",
                            "Cancellation Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    textPnr.setText("");
                    resetDetails();
                }

                pstmt.close();
                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error cancelling reservation: " + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Helper method to reset detail labels.
     */
    private void resetDetails() {
        labelNameVal.setText("-");
        labelTrainVal.setText("-");
        labelClassVal.setText("-");
        labelRouteVal.setText("-");
        currentPnr = "";
        buttonCancel.setEnabled(false);
    }
}
