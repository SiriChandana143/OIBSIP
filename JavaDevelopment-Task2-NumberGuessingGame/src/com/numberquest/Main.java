package com.numberquest;

import com.numberquest.ui.MainWindow;

import javax.swing.*;

/**
 * Application Entry Point for Number Quest.
 */
public class Main {
    public static void main(String[] args) {
        // Set cross-platform look and feel for consistent modern visual rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default Swing Look & Feel if system LAF unavailable
        }

        // Configure global Swing UIManager dark theme defaults for high contrast JComboBox visibility
        com.numberquest.ui.theme.Theme.applyGlobalThemeDefaults();

        // Launch GUI safely on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
