package com.numberquest.ui.theme;

import java.awt.*;
import javax.swing.*;

/**
 * Design system tokens containing colors, fonts, margins, 
 * and styling helpers for the Number Quest application interface.
 */
public class Theme {
    // Dark Palette Tokens
    public static final Color BG_DARK = new Color(18, 22, 33);
    public static final Color CARD_BG = new Color(28, 34, 49);
    public static final Color CARD_HEADER_BG = new Color(36, 44, 63);
    public static final Color INPUT_BG = new Color(22, 27, 39);

    // Accent Colors
    public static final Color PRIMARY_ACCENT = new Color(99, 102, 241);     // Vibrant Indigo
    public static final Color PRIMARY_HOVER = new Color(129, 140, 248);
    public static final Color SECONDARY_ACCENT = new Color(14, 165, 233);   // Sky Blue
    
    // Status Colors
    public static final Color COLOR_HIGH = new Color(244, 63, 94);          // Rose Red ("Too High")
    public static final Color COLOR_LOW = new Color(245, 158, 11);          // Amber Amber ("Too Low")
    public static final Color COLOR_CORRECT = new Color(16, 185, 129);       // Emerald Green ("Correct!")
    public static final Color COLOR_INFO = new Color(99, 102, 241);          // Info Indigo

    // Text Colors
    public static final Color TEXT_MAIN = new Color(243, 244, 246);
    public static final Color TEXT_MUTED = new Color(156, 163, 175);
    public static final Color BORDER_COLOR = new Color(55, 65, 81);

    // Fonts
    public static final Font FONT_HEADER_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_LABEL_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_GUESS_INPUT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_FEEDBACK_BANNER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_STAT_VAL = new Font("Segoe UI", Font.BOLD, 22);

    /**
     * Enables anti-aliasing graphics hints for smooth rendering.
     */
    public static void setupGraphicsQuality(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * Configures global UIManager dark theme properties so all Swing dialogs 
     * and JComboBox dropdowns display crisp, high-contrast text across all operating systems.
     */
    public static void applyGlobalThemeDefaults() {
        Color darkPopupBg = new Color(15, 23, 42);       // Deep Slate Dark
        Color cardHeaderBg = new Color(30, 41, 59);      // Slate Header
        Color primaryAccent = new Color(79, 70, 229);    // Vibrant Indigo
        Color pureWhite = Color.WHITE;

        UIManager.put("ComboBox.background", cardHeaderBg);
        UIManager.put("ComboBox.foreground", pureWhite);
        UIManager.put("ComboBox.selectionBackground", primaryAccent);
        UIManager.put("ComboBox.selectionForeground", pureWhite);
        UIManager.put("ComboBox.buttonBackground", cardHeaderBg);

        UIManager.put("PopupMenu.background", darkPopupBg);
        UIManager.put("PopupMenu.foreground", pureWhite);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(BORDER_COLOR, 1));

        UIManager.put("List.background", darkPopupBg);
        UIManager.put("List.foreground", pureWhite);
        UIManager.put("List.selectionBackground", primaryAccent);
        UIManager.put("List.selectionForeground", pureWhite);

        UIManager.put("OptionPane.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", pureWhite);
        UIManager.put("Panel.background", BG_DARK);
    }
}
