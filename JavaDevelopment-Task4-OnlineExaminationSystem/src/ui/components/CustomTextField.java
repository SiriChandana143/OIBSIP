package ui.components;

import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/** Text field with placeholder text, proper focus border, and comfortable height. */
public class CustomTextField extends JTextField {

    private final String placeholder;

    public CustomTextField(String placeholder, int cols) {
        super(cols);
        this.placeholder = placeholder;
        setup();
    }

    private void setup() {
        setFont(UIUtils.FONT_INPUT);
        setForeground(UIUtils.TEXT_PRIMARY);
        setCaretColor(UIUtils.TEXT_PRIMARY);
        setBackground(UIUtils.SURFACE_COLOR);
        applyNormalBorder();

        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { applyFocusBorder(); repaint(); }
            @Override public void focusLost (FocusEvent e) { applyNormalBorder(); repaint(); }
        });
    }

    private void applyNormalBorder() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));
    }
    private void applyFocusBorder() {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_FOCUS, 2),
            BorderFactory.createEmptyBorder(9, 11, 9, 11)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getText().isEmpty() && placeholder != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont());
            g2.setColor(UIUtils.TEXT_SECONDARY);
            Insets ins = getInsets();
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, ins.left, y);
            g2.dispose();
        }
    }

    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize(); d.height = 42; return d;
    }
}
