package ui.components;

import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A fully custom-painted button with reliable hover/pressed/disabled states.
 * Use ButtonStyle.PRIMARY for blue background + white text.
 * Use ButtonStyle.SECONDARY for white background + blue border + blue text.
 * Use ButtonStyle.DANGER for red background + white text.
 */
public class ModernButton extends JButton {

    public enum ButtonStyle { PRIMARY, SECONDARY, DANGER, GRAY, SUCCESS }

    private final ButtonStyle style;
    private final Color baseColor;
    private boolean hovered = false;
    private boolean pressed = false;

    // PRIMARY / DANGER / GRAY style colors
    private static final int ARC = 6;

    public ModernButton(String text, ButtonStyle style) {
        super(text);
        this.style = style;
        this.baseColor = resolveBase(style);
        init();
    }

    /** Convenience: creates a PRIMARY button */
    public ModernButton(String text) {
        this(text, ButtonStyle.PRIMARY);
    }

    private Color resolveBase(ButtonStyle s) {
        switch (s) {
            case DANGER:    return UIUtils.ERROR_COLOR;
            case GRAY:      return new Color(100, 116, 139);
            case SUCCESS:   return UIUtils.SUCCESS_COLOR;
            case SECONDARY: return UIUtils.PRIMARY_COLOR;
            default:        return UIUtils.PRIMARY_COLOR;
        }
    }

    private void init() {
        setFont(UIUtils.BUTTON_FONT);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { if (isEnabled()) { hovered = true;  repaint(); } }
            @Override public void mouseExited(MouseEvent e)   { hovered = false; pressed = false; repaint(); }
            @Override public void mousePressed(MouseEvent e)  { if (isEnabled()) { pressed = true;  repaint(); } }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        if (style == ButtonStyle.SECONDARY) {
            paintSecondary(g2, w, h);
        } else {
            paintSolid(g2, w, h);
        }
        g2.dispose();
    }

    private void paintSolid(Graphics2D g2, int w, int h) {
        Color bg;
        Color fg;

        if (!isEnabled()) {
            bg = new Color(203, 213, 225); // slate-300
            fg = new Color(148, 163, 184); // slate-400
        } else if (pressed) {
            bg = darken(baseColor, 0.18f);
            fg = Color.WHITE;
        } else if (hovered) {
            bg = lighten(baseColor, 0.15f);
            fg = Color.WHITE;
        } else {
            bg = baseColor;
            fg = Color.WHITE;
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, ARC, ARC);

        drawCenteredText(g2, fg, w, h);
    }

    private void paintSecondary(Graphics2D g2, int w, int h) {
        Color border;
        Color bg;
        Color fg;

        if (!isEnabled()) {
            bg     = new Color(248, 250, 252);
            border = new Color(203, 213, 225);
            fg     = new Color(148, 163, 184);
        } else if (pressed) {
            bg     = new Color(226, 232, 240); // slate-200
            border = new Color(148, 163, 184);
            fg     = new Color(30, 41, 59);
        } else if (hovered) {
            bg     = new Color(241, 245, 249); // slate-100
            border = new Color(148, 163, 184);
            fg     = new Color(30, 41, 59);
        } else {
            bg     = Color.WHITE;
            border = new Color(148, 163, 184);
            fg     = new Color(51, 65, 85);
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, ARC, ARC);

        g2.setColor(border);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, ARC, ARC);

        drawCenteredText(g2, fg, w, h);
    }

    private void drawCenteredText(Graphics2D g2, Color fg, int w, int h) {
        g2.setColor(fg);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int w = fm.stringWidth(getText()) + 48;
        int h = 42;
        return new Dimension(w, h);
    }

    private Color darken(Color c, float f) {
        return new Color(
            Math.max(0, (int)(c.getRed()   * (1.0 - f))),
            Math.max(0, (int)(c.getGreen() * (1.0 - f))),
            Math.max(0, (int)(c.getBlue()  * (1.0 - f))),
            c.getAlpha()
        );
    }

    private Color lighten(Color c, float f) {
        return new Color(
            Math.min(255, (int)(c.getRed() + (255 - c.getRed()) * f)),
            Math.min(255, (int)(c.getGreen() + (255 - c.getGreen()) * f)),
            Math.min(255, (int)(c.getBlue() + (255 - c.getBlue()) * f)),
            c.getAlpha()
        );
    }
}
