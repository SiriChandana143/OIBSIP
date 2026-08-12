import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Login.java
 * Swing GUI window for user login.
 * Validates credentials against SQLite database table 'users'.
 */
public class Login extends JFrame implements ActionListener {

    // GUI Components
    private JLabel labelTitle, labelUsername, labelPassword;
    private JTextField textUsername;
    private JPasswordField textPassword;
    private JButton buttonLogin, buttonReset, buttonExit;
    private JPanel panelForm, panelButtons;

    /**
     * Constructor to setup the Login Window components and layouts.
     */
    public Login() {
        // Set Frame Title
        setTitle("Online Reservation System - Login");
        setSize(450, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen
        setLayout(null); // Absolute layout for simplicity
        setResizable(false);

        // Header Title Label
        labelTitle = new JLabel("Online Reservation System", SwingConstants.CENTER);
        labelTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelTitle.setForeground(new Color(25, 118, 210)); // Deep blue color
        labelTitle.setBounds(20, 20, 400, 30);
        add(labelTitle);

        // Subtitle Label
        JLabel labelSubtitle = new JLabel("Please enter your login details", SwingConstants.CENTER);
        labelSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelSubtitle.setForeground(Color.GRAY);
        labelSubtitle.setBounds(20, 50, 400, 20);
        add(labelSubtitle);

        // Form Panel setup
        panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(30, 85, 380, 110);

        // Username Label and Field
        labelUsername = new JLabel("Username:");
        labelUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelUsername.setBounds(20, 15, 100, 25);
        panelForm.add(labelUsername);

        textUsername = new JTextField();
        textUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textUsername.setBounds(130, 15, 220, 28);
        panelForm.add(textUsername);

        // Password Label and Field
        labelPassword = new JLabel("Password:");
        labelPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPassword.setBounds(20, 60, 100, 25);
        panelForm.add(labelPassword);

        textPassword = new JPasswordField();
        textPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textPassword.setBounds(130, 60, 220, 28);
        panelForm.add(textPassword);

        add(panelForm);

        // Buttons Panel setup
        panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(1, 3, 10, 0));
        panelButtons.setBounds(40, 215, 360, 35);

        // Login Button
        buttonLogin = new JButton("Login");
        buttonLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonLogin.setBackground(new Color(25, 118, 210));
        buttonLogin.setForeground(Color.WHITE);
        buttonLogin.setFocusable(false);
        buttonLogin.addActionListener(this);
        panelButtons.add(buttonLogin);

        // Reset Button
        buttonReset = new JButton("Reset");
        buttonReset.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonReset.setBackground(new Color(240, 240, 240));
        buttonReset.setFocusable(false);
        buttonReset.addActionListener(this);
        panelButtons.add(buttonReset);

        // Exit Button
        buttonExit = new JButton("Exit");
        buttonExit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        buttonExit.setBackground(new Color(220, 53, 69));
        buttonExit.setForeground(Color.WHITE);
        buttonExit.setFocusable(false);
        buttonExit.addActionListener(this);
        panelButtons.add(buttonExit);

        add(panelButtons);
    }

    /**
     * Handles button click actions.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLogin) {
            handleLogin();
        } else if (e.getSource() == buttonReset) {
            textUsername.setText("");
            textPassword.setText("");
        } else if (e.getSource() == buttonExit) {
            System.exit(0);
        }
    }

    /**
     * Helper method to process login authentication.
     */
    private void handleLogin() {
        String username = textUsername.getText().trim();
        String password = new String(textPassword.getPassword()).trim();

        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both Username and Password.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate credentials against SQLite database
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed! Check SQLite setup.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Prepared Statement query to prevent SQL injection
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // If result set contains a row, login is successful
            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Login Successful! Welcome, " + username + ".",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Close login window and open Reservation Form
                this.dispose();
                ReservationForm reservationForm = new ReservationForm();
                reservationForm.setVisible(true);
            } else {
                // If credentials do not match
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }

            // Close database resources
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
}
