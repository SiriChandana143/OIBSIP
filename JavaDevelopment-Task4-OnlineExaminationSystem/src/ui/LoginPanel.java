package ui;

import service.AuthenticationService;
import ui.components.CustomPasswordField;
import ui.components.CustomTextField;
import ui.components.ModernButton;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AuthenticationService authService;

    private CustomTextField      usernameField;
    private CustomPasswordField  passwordField;
    private JLabel               errorLabel;
    private JCheckBox            showPassCheck;

    public LoginPanel(MainFrame mainFrame, AuthenticationService authService) {
        this.mainFrame   = mainFrame;
        this.authService = authService;
        // Use GridBagLayout so the card is perfectly centred both horizontally
        // and vertically regardless of the JFrame size.
        setLayout(new GridBagLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);

        GridBagConstraints outer = new GridBagConstraints();
        outer.gridx   = 0;
        outer.gridy   = 0;
        outer.weightx = 1.0;
        outer.weighty = 1.0;
        outer.anchor  = GridBagConstraints.CENTER;
        outer.fill    = GridBagConstraints.NONE;   // card stays at its preferred size
        add(buildCard(), outer);
    }

    private JPanel buildCard() {
        // All interactive elements share this exact width so every edge lines up.
        final int fieldW = 340;
        final int fieldH = 42;

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIUtils.SURFACE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(40, 48, 40, 48)));

        // ── Title & subtitle ─────────────────────────────────────────
        JLabel title = UIUtils.label("ONLINE EXAMINATION SYSTEM",
                                     UIUtils.FONT_APP_TITLE, UIUtils.PRIMARY_COLOR);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = UIUtils.label("Secure  •  Simple  •  Smart Assessment",
                                        UIUtils.FONT_HELPER, UIUtils.TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // ── Fields ───────────────────────────────────────────────────
        usernameField = new CustomTextField("Enter your username", 0);
        passwordField = new CustomPasswordField("Enter your password", 0);

        // ── Labels ───────────────────────────────────────────────────
        JLabel userLbl = UIUtils.fieldLabel("Username");
        JLabel passLbl = UIUtils.fieldLabel("Password");

        // ── Show password ────────────────────────────────────────────
        showPassCheck = new JCheckBox("Show password");
        showPassCheck.setFont(UIUtils.FONT_HELPER);
        showPassCheck.setForeground(UIUtils.TEXT_SECONDARY);
        showPassCheck.setBackground(UIUtils.SURFACE_COLOR);
        showPassCheck.setFocusPainted(false);
        showPassCheck.addActionListener(e -> {
            passwordField.setEchoChar(showPassCheck.isSelected() ? (char) 0 : '•');
            passwordField.repaint();
        });

        // ── Error ─────────────────────────────────────────────────────
        errorLabel = UIUtils.errorLabel();

        // ── Buttons ───────────────────────────────────────────────────
        ModernButton loginBtn = new ModernButton("LOGIN", ModernButton.ButtonStyle.PRIMARY);
        ModernButton clearBtn = new ModernButton("CLEAR", ModernButton.ButtonStyle.SECONDARY);

        loginBtn.setPreferredSize(new Dimension(fieldW, fieldH));
        loginBtn.setMinimumSize(new Dimension(fieldW, fieldH));
        loginBtn.setMaximumSize(new Dimension(fieldW, fieldH));
        clearBtn.setPreferredSize(new Dimension(fieldW, fieldH));
        clearBtn.setMinimumSize(new Dimension(fieldW, fieldH));
        clearBtn.setMaximumSize(new Dimension(fieldW, fieldH));

        // ── GridBagConstraints: single column, all items fill fieldW ──
        // Each row uses the same GBC template; only gridy and insets change.
        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx   = 0;
        fc.weightx = 1.0;
        fc.fill    = GridBagConstraints.HORIZONTAL;
        fc.anchor  = GridBagConstraints.CENTER;

        // Row 0 – Title (centred text, constrained to fieldW)
        fc.gridy  = 0;
        fc.insets = new Insets(0, 0, 4, 0);
        card.add(constrainedLabel(title, fieldW), fc);

        // Row 1 – Subtitle
        fc.gridy  = 1;
        fc.insets = new Insets(0, 0, 32, 0);
        card.add(constrainedLabel(subtitle, fieldW), fc);

        // Row 2 – Username label
        fc.gridy  = 2;
        fc.insets = new Insets(0, 0, 6, 0);
        card.add(constrainedLabel(userLbl, fieldW), fc);

        // Row 3 – Username field
        fc.gridy  = 3;
        fc.insets = new Insets(0, 0, 18, 0);
        card.add(fieldWrap(usernameField, fieldW, fieldH), fc);

        // Row 4 – Password label
        fc.gridy  = 4;
        fc.insets = new Insets(0, 0, 6, 0);
        card.add(constrainedLabel(passLbl, fieldW), fc);

        // Row 5 – Password field
        fc.gridy  = 5;
        fc.insets = new Insets(0, 0, 8, 0);
        card.add(fieldWrap(passwordField, fieldW, fieldH), fc);

        // Row 6 – Show password checkbox (left-aligned within fieldW)
        fc.gridy  = 6;
        fc.insets = new Insets(0, 0, 6, 0);
        card.add(alignedCheckbox(showPassCheck, fieldW), fc);

        // Row 7 – Error label
        fc.gridy  = 7;
        fc.insets = new Insets(0, 0, 20, 0);
        card.add(constrainedLabel(errorLabel, fieldW), fc);

        // Row 8 – Login button
        fc.gridy  = 8;
        fc.insets = new Insets(0, 0, 10, 0);
        card.add(loginBtn, fc);

        // Row 9 – Clear button
        fc.gridy  = 9;
        fc.insets = new Insets(0, 0, 0, 0);
        card.add(clearBtn, fc);

        // ── Events ────────────────────────────────────────────────────
        loginBtn.addActionListener(e -> attemptLogin());
        clearBtn.addActionListener(e -> clearFields());
        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) attemptLogin();
            }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);

        return card;
    }

    /**
     * Wraps a JLabel in a transparent fixed-width panel so that every label
     * in the form shares the same left and right edges as the input fields.
     */
    private JPanel constrainedLabel(JLabel label, int width) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(width, label.getPreferredSize().height + 2));
        wrap.setMinimumSize(new Dimension(width, label.getPreferredSize().height + 2));
        wrap.setMaximumSize(new Dimension(width, Short.MAX_VALUE));
        wrap.add(label, BorderLayout.CENTER);
        return wrap;
    }

    /** Wraps an input field in a transparent fixed-size panel. */
    private JPanel fieldWrap(JComponent field, int width, int height) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(width, height));
        wrap.setMinimumSize(new Dimension(width, height));
        wrap.setMaximumSize(new Dimension(width, height));
        wrap.add(field, BorderLayout.CENTER);
        return wrap;
    }

    /** Wraps a checkbox in a panel whose left edge is flush with the fields. */
    private JPanel alignedCheckbox(JCheckBox cb, int width) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.setPreferredSize(new Dimension(width, cb.getPreferredSize().height));
        wrap.setMinimumSize(new Dimension(width, cb.getPreferredSize().height));
        wrap.setMaximumSize(new Dimension(width, cb.getPreferredSize().height));
        wrap.add(cb);
        return wrap;
    }

    // ────────────────────────────────────────────────────────────────
    private void attemptLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        errorLabel.setForeground(UIUtils.ERROR_COLOR);

        if (user.isEmpty()) { errorLabel.setText("Please enter your username."); return; }
        if (pass.isEmpty()) { errorLabel.setText("Please enter your password."); return; }

        if (authService.login(user, pass)) {
            errorLabel.setText(" ");
            mainFrame.showCard("PROFILE");
        } else {
            errorLabel.setText("Invalid username or password. Please try again.");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
        showPassCheck.setSelected(false);
        passwordField.setEchoChar('•');
        usernameField.repaint();
        passwordField.repaint();
    }

    public void reset() { clearFields(); }
}
