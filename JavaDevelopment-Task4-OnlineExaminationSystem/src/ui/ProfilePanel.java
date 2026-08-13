package ui;

import service.AuthenticationService;
import ui.components.CustomPasswordField;
import ui.components.CustomTextField;
import ui.components.ModernButton;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private final MainFrame           mainFrame;
    private final AuthenticationService authService;

    private JLabel              welcomeLabel;
    private CustomTextField     displayNameField;
    private CustomPasswordField newPassField;
    private CustomPasswordField confirmPassField;
    private JLabel              nameError;
    private JLabel              passError;
    private JCheckBox           showNew;
    private JCheckBox           showConfirm;

    public ProfilePanel(MainFrame mainFrame, AuthenticationService authService) {
        this.mainFrame   = mainFrame;
        this.authService = authService;
        setLayout(new GridBagLayout());   // centres card
        setBackground(UIUtils.BACKGROUND_COLOR);
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIUtils.SURFACE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(36, 48, 36, 48)));

        int fieldW = 380;

        // ── Header ──────────────────────────────────────────────
        welcomeLabel = UIUtils.label("Welcome back, Student",
                                     UIUtils.FONT_PAGE_TITLE, UIUtils.PRIMARY_COLOR);
        welcomeLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = UIUtils.label("Set up your profile before the examination.",
                                   UIUtils.FONT_BODY, UIUtils.TEXT_SECONDARY);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        // ── Display Name ─────────────────────────────────────────
        JLabel nameLbl = UIUtils.fieldLabel("Display Name");
        nameLbl.setAlignmentX(LEFT_ALIGNMENT);

        displayNameField = new CustomTextField("Enter your display name", 0);
        displayNameField.setPreferredSize(new Dimension(fieldW, 42));
        displayNameField.setMaximumSize(new Dimension(fieldW, 42));
        displayNameField.setAlignmentX(LEFT_ALIGNMENT);

        nameError = UIUtils.errorLabel();
        nameError.setAlignmentX(LEFT_ALIGNMENT);

        // ── New Password ─────────────────────────────────────────
        JLabel newPassLbl = UIUtils.fieldLabel("New Password");
        newPassLbl.setAlignmentX(LEFT_ALIGNMENT);

        newPassField = new CustomPasswordField("Enter new password (optional)", 0);
        newPassField.setPreferredSize(new Dimension(fieldW, 42));
        newPassField.setMaximumSize(new Dimension(fieldW, 42));
        newPassField.setAlignmentX(LEFT_ALIGNMENT);

        showNew = showHideCheck(newPassField);
        showNew.setAlignmentX(LEFT_ALIGNMENT);

        // ── Confirm Password ─────────────────────────────────────
        JLabel confirmLbl = UIUtils.fieldLabel("Confirm Password");
        confirmLbl.setAlignmentX(LEFT_ALIGNMENT);

        confirmPassField = new CustomPasswordField("Confirm your password", 0);
        confirmPassField.setPreferredSize(new Dimension(fieldW, 42));
        confirmPassField.setMaximumSize(new Dimension(fieldW, 42));
        confirmPassField.setAlignmentX(LEFT_ALIGNMENT);

        showConfirm = showHideCheck(confirmPassField);
        showConfirm.setAlignmentX(LEFT_ALIGNMENT);

        passError = UIUtils.errorLabel();
        passError.setAlignmentX(LEFT_ALIGNMENT);

        // ── Buttons ───────────────────────────────────────────────
        ModernButton saveBtn = new ModernButton("SAVE & CONTINUE", ModernButton.ButtonStyle.PRIMARY);
        ModernButton logoutBtn= new ModernButton("BACK / LOGOUT", ModernButton.ButtonStyle.SECONDARY);

        saveBtn.setPreferredSize(new Dimension(fieldW, 42));
        saveBtn.setMaximumSize(new Dimension(fieldW, 42));
        saveBtn.setAlignmentX(LEFT_ALIGNMENT);
        logoutBtn.setPreferredSize(new Dimension(fieldW, 42));
        logoutBtn.setMaximumSize(new Dimension(fieldW, 42));
        logoutBtn.setAlignmentX(LEFT_ALIGNMENT);

        // ── Assembly ──────────────────────────────────────────────
        card.add(welcomeLabel);
        card.add(UIUtils.vgap(4));
        card.add(sub);
        card.add(UIUtils.vgap(24));
        card.add(UIUtils.separator());
        card.add(UIUtils.vgap(24));

        card.add(nameLbl);
        card.add(UIUtils.vgap(6));
        card.add(displayNameField);
        card.add(UIUtils.vgap(4));
        card.add(nameError);
        card.add(UIUtils.vgap(16));

        card.add(newPassLbl);
        card.add(UIUtils.vgap(6));
        card.add(newPassField);
        card.add(UIUtils.vgap(6));
        card.add(showNew);
        card.add(UIUtils.vgap(16));

        card.add(confirmLbl);
        card.add(UIUtils.vgap(6));
        card.add(confirmPassField);
        card.add(UIUtils.vgap(6));
        card.add(showConfirm);
        card.add(UIUtils.vgap(4));
        card.add(passError);
        card.add(UIUtils.vgap(28));

        card.add(saveBtn);
        card.add(UIUtils.vgap(10));
        card.add(logoutBtn);

        // ── Events ───────────────────────────────────────────────
        saveBtn.addActionListener(e -> save());
        logoutBtn.addActionListener(e -> {
            authService.logout();
            mainFrame.showCard("LOGIN");
        });

        return card;
    }

    // ─────────────────────────────────────────────────────────────
    private JCheckBox showHideCheck(CustomPasswordField field) {
        JCheckBox cb = new JCheckBox("Show password");
        cb.setFont(UIUtils.FONT_HELPER);
        cb.setForeground(UIUtils.TEXT_SECONDARY);
        cb.setBackground(UIUtils.SURFACE_COLOR);
        cb.setFocusPainted(false);
        cb.addActionListener(e -> {
            field.setEchoChar(cb.isSelected() ? (char) 0 : '•');
            field.repaint();
        });
        return cb;
    }

    private void save() {
        boolean ok = true;
        nameError.setText(" ");
        passError.setText(" ");

        String name    = displayNameField.getText().trim();
        String newP    = new String(newPassField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        if (name.isEmpty()) {
            nameError.setText("Display name cannot be empty.");
            ok = false;
        }
        if (!newP.isEmpty() && !newP.equals(confirm)) {
            passError.setText("Passwords do not match.");
            ok = false;
        }
        if (!ok) return;

        authService.updateProfile(name, newP.isEmpty() ? null : newP);
        JOptionPane.showMessageDialog(this,
            "Profile updated successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.showCard("INSTRUCTIONS");
    }

    public void refresh() {
        if (authService.getCurrentUser() != null) {
            welcomeLabel.setText("Welcome back, " +
                authService.getCurrentUser().getDisplayName());
            displayNameField.setText(authService.getCurrentUser().getDisplayName());
            displayNameField.repaint();
        }
        newPassField.setText("");     newPassField.repaint();
        confirmPassField.setText(""); confirmPassField.repaint();
        nameError.setText(" ");       passError.setText(" ");
        showNew.setSelected(false);   newPassField.setEchoChar('•');
        showConfirm.setSelected(false); confirmPassField.setEchoChar('•');
    }
}
