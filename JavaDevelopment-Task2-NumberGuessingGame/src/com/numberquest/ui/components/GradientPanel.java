package com.numberquest.ui.components;

import com.numberquest.ui.theme.Theme;

import java.awt.*;
import javax.swing.*;

/**
 * Custom JPanel providing a smooth background linear gradient with rounded corners.
 */
public class GradientPanel extends JPanel {
    private Color colorStart;
    private Color colorEnd;
    private int cornerRadius;

    public GradientPanel(Color colorStart, Color colorEnd, int cornerRadius) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.cornerRadius = cornerRadius;
        setOpaque(false);
    }

    public GradientPanel(int cornerRadius) {
        this(Theme.CARD_BG, Theme.CARD_HEADER_BG, cornerRadius);
    }

    public void setGradientColors(Color start, Color end) {
        this.colorStart = start;
        this.colorEnd = end;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.setupGraphicsQuality(g2);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, colorStart, 0, height, colorEnd);
        g2.setPaint(gp);

        if (cornerRadius > 0) {
            g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
            g2.setColor(Theme.BORDER_COLOR);
            g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        } else {
            g2.fillRect(0, 0, width, height);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
