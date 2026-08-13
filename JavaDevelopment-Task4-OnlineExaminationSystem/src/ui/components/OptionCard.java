package ui.components;

import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clickable option row for MCQ questions.
 * The entire row (including label) is clickable.
 * Shows hover and selected states with blue border + light-blue background.
 */
public class OptionCard extends JPanel {

    private final JRadioButton radio;
    private final JTextArea    textArea;
    private boolean selected = false;
    private boolean hovered  = false;
    private Runnable onSelected;

    private static final Color BG_NORMAL   = UIUtils.SURFACE_COLOR;
    private static final Color BG_HOVER    = new Color(239, 246, 255);   // light blue tint
    private static final Color BG_SELECTED = new Color(219, 234, 254);   // slightly stronger blue tint
    private static final Color BD_NORMAL   = UIUtils.BORDER_COLOR;
    private static final Color BD_HOVER    = new Color(147, 197, 253);
    private static final Color BD_SELECTED = UIUtils.SECONDARY_BLUE;

    public OptionCard(String text) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        radio = new JRadioButton();
        radio.setOpaque(false);
        radio.setFocusPainted(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));

        textArea = new JTextArea(text);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UIUtils.FONT_BODY);
        textArea.setForeground(UIUtils.TEXT_PRIMARY);
        textArea.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(radio, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);
        refreshAppearance();

        // Mouse on panel background
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  refreshAppearance(); }
            @Override public void mouseExited (MouseEvent e) { hovered = false; refreshAppearance(); }
            @Override public void mousePressed(MouseEvent e) {
                radio.setSelected(true);
                if (onSelected != null) onSelected.run();
            }
        });

        // Direct click on radio button
        radio.addActionListener(e -> {
            if (onSelected != null) onSelected.run();
        });
        radio.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  refreshAppearance(); }
            @Override public void mouseExited (MouseEvent e) { hovered = false; refreshAppearance(); }
        });
        
        // Clicks on text area
        textArea.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  refreshAppearance(); }
            @Override public void mouseExited (MouseEvent e) { hovered = false; refreshAppearance(); }
            @Override public void mousePressed(MouseEvent e) {
                radio.setSelected(true);
                if (onSelected != null) onSelected.run();
            }
        });
    }

    public void setSelectedState(boolean sel) {
        this.selected = sel;
        radio.setSelected(sel);
        refreshAppearance();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public boolean isOptionSelected() { return radio.isSelected(); }
    public JRadioButton getRadioButton() { return radio; }
    public void setOnSelected(Runnable r) { this.onSelected = r; }

    private void refreshAppearance() {
        Color bg, bd;
        if (selected) {
            bg = BG_SELECTED; bd = BD_SELECTED;
        } else if (hovered) {
            bg = BG_HOVER; bd = BD_HOVER;
        } else {
            bg = BG_NORMAL; bd = BD_NORMAL;
        }
        setBackground(bg);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bd, selected ? 2 : 1, true),
            BorderFactory.createEmptyBorder(
                selected ? 11 : 12, 16, selected ? 11 : 12, 16)));
        repaint();
    }

    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.max(48, d.height);
        return d;
    }
    @Override public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
