package com.numberquest.ui.components;

import com.numberquest.ui.theme.Theme;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Custom painted modern button with hover micro-animations and sleek dark styling.
 */
public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 12;

    public ModernButton(String text, Color normalColor, Color hoverColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressedColor = normalColor.darker();

        setFont(Theme.FONT_LABEL_BOLD);
        setForeground(Theme.TEXT_MAIN);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public ModernButton(String text) {
        this(text, Theme.PRIMARY_ACCENT, Theme.PRIMARY_HOVER);
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.setupGraphicsQuality(g2);

        Color currentBg = normalColor;
        if (!isEnabled()) {
            currentBg = Theme.CARD_BG;
        } else if (isPressed) {
            currentBg = pressedColor;
        } else if (isHovered) {
            currentBg = hoverColor;
        }

        g2.setColor(currentBg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border if disabled
        if (!isEnabled()) {
            g2.setColor(Theme.BORDER_COLOR);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        // Draw centered button label
        FontMetrics fm = g2.getFontMetrics(getFont());
        Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

        g2.setFont(getFont());
        g2.setColor(isEnabled() ? getForeground() : Theme.TEXT_MUTED);
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
