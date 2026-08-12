package com.numberquest.ui.components;

import com.numberquest.ui.theme.Theme;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

/**
 * Custom painted text field with focus highlighting and placeholder text support.
 */
public class ModernTextField extends JTextField {
    private String placeholder;
    private boolean isFocused = false;
    private int cornerRadius = 12;

    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        setFont(Theme.FONT_GUESS_INPUT);
        setForeground(Theme.TEXT_MAIN);
        setCaretColor(Theme.PRIMARY_ACCENT);
        setBackground(Theme.INPUT_BG);
        setOpaque(false);
        setHorizontalAlignment(JTextField.CENTER);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.setupGraphicsQuality(g2);

        // Paint rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Paint border (focus glow or standard border)
        if (isFocused) {
            g2.setColor(Theme.PRIMARY_ACCENT);
            g2.setStroke(new BasicStroke(2.0f));
        } else {
            g2.setColor(Theme.BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);

        // Draw placeholder text if empty and unfocused
        if (getText().isEmpty() && !isFocused && placeholder != null) {
            Graphics2D gPlaceholder = (Graphics2D) g.create();
            Theme.setupGraphicsQuality(gPlaceholder);
            gPlaceholder.setFont(Theme.FONT_REGULAR);
            gPlaceholder.setColor(Theme.TEXT_MUTED);

            FontMetrics fm = gPlaceholder.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            gPlaceholder.drawString(placeholder, x, y);
            gPlaceholder.dispose();
        }
    }
}
