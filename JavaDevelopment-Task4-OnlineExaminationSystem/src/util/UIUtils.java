package util;

import javax.swing.*;
import java.awt.*;

public class UIUtils {

    // ── Color Palette ─────────────────────────────────────────────
    public static final Color PRIMARY_COLOR   = new Color(30,  58, 138);   // #1E3A8A deep blue
    public static final Color SECONDARY_BLUE  = new Color(37,  99, 235);   // #2563EB
    public static final Color LIGHT_BLUE_BG   = new Color(239,246,255);    // #EFF6FF
    public static final Color BACKGROUND_COLOR= new Color(248,250,252);    // #F8FAFC
    public static final Color SURFACE_COLOR   = Color.WHITE;
    public static final Color TEXT_PRIMARY    = new Color(17,  24, 39);    // #111827
    public static final Color TEXT_SECONDARY  = new Color(100,116,139);    // #64748B
    public static final Color BORDER_COLOR    = new Color(217,225,236);    // #D9E1EC
    public static final Color BORDER_FOCUS    = new Color(59, 130,246);    // #3B82F6
    public static final Color SUCCESS_COLOR   = new Color(16, 185,129);    // #10B981
    public static final Color WARNING_COLOR   = new Color(245,158, 11);    // #F59E0B
    public static final Color ERROR_COLOR     = new Color(239, 68, 68);    // #EF4444

    // ── Fonts ────────────────────────────────────────────────────
    public static final Font FONT_APP_TITLE   = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font FONT_PAGE_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_SECTION     = new Font("Segoe UI", Font.BOLD,  16);
    public static final Font FONT_BODY        = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD   = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_INPUT       = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON      = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_HELPER      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TIMER       = new Font("Segoe UI", Font.BOLD,  28);
    public static final Font FONT_QUESTION    = new Font("Segoe UI", Font.BOLD,  17);
    public static final Font FONT_STAT_VALUE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_STAT_LABEL  = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Keep old aliases so nothing breaks ───────────────────────
    public static final Font APP_TITLE_FONT   = FONT_APP_TITLE;
    public static final Font PAGE_TITLE_FONT  = FONT_PAGE_TITLE;
    public static final Font SECTION_HEADER_FONT = FONT_SECTION;
    public static final Font BODY_FONT        = FONT_BODY;
    public static final Font INPUT_FONT       = FONT_INPUT;
    public static final Font BUTTON_FONT      = FONT_BUTTON;
    public static final Font SMALL_HELPER_FONT= FONT_HELPER;
    // Old color aliases
    public static final Color TEXT_COLOR      = TEXT_PRIMARY;
    public static final Color WHITE           = Color.WHITE;
    public static final Color BORDER_FOCUS_COLOR = BORDER_FOCUS;

    // ── Helpers ──────────────────────────────────────────────────
    public static JLabel label(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    public static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY_BOLD);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel errorLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(FONT_HELPER);
        lbl.setForeground(ERROR_COLOR);
        return lbl;
    }

    /** Thin separator line. */
    public static JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    /** Vertical gap component. */
    public static Component vgap(int h) {
        return Box.createRigidArea(new Dimension(0, h));
    }

    /** Horizontal gap component. */
    public static Component hgap(int w) {
        return Box.createRigidArea(new Dimension(w, 0));
    }

    // ── Deprecated helpers kept for compatibility ─────────────────
    public static void configureLabel(JLabel label, Font font, Color color) {
        label.setFont(font); label.setForeground(color);
    }
    public static JPanel createPaddedPanel(int t, int l, int b, int r) {
        JPanel p = new JPanel(); p.setBackground(BACKGROUND_COLOR);
        p.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
        return p;
    }
    public static JPanel createSurfaceCard() {
        JPanel p = new JPanel(); p.setBackground(SURFACE_COLOR);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)));
        return p;
    }
}
