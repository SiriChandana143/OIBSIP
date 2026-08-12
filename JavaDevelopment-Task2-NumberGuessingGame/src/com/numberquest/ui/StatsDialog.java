package com.numberquest.ui;

import com.numberquest.model.Player;
import com.numberquest.ui.components.GradientPanel;
import com.numberquest.ui.components.ModernButton;
import com.numberquest.ui.theme.Theme;

import java.awt.*;
import javax.swing.*;

/**
 * Modal dialog displaying detailed session player statistics and analytics.
 */
public class StatsDialog extends JDialog {

    public StatsDialog(Frame owner, Player player) {
        super(owner, "Player Statistics - " + player.getName(), true);
        setSize(480, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_DARK);

        // Header Panel
        GradientPanel headerPanel = new GradientPanel(0);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel titleLabel = new JLabel("📊 Session Analytics");
        titleLabel.setFont(Theme.FONT_HEADER_TITLE);
        titleLabel.setForeground(Theme.TEXT_MAIN);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Stats Grid Panel
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        gridPanel.setBackground(Theme.BG_DARK);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        gridPanel.add(createStatCard("Rounds Played", String.valueOf(player.getTotalRoundsPlayed()), Theme.SECONDARY_ACCENT));
        gridPanel.add(createStatCard("Total Score", player.getTotalScore() + " pts", Theme.COLOR_CORRECT));
        gridPanel.add(createStatCard("Games Won", String.valueOf(player.getGamesWon()), Theme.COLOR_CORRECT));
        gridPanel.add(createStatCard("Games Lost", String.valueOf(player.getGamesLost()), Theme.COLOR_HIGH));
        gridPanel.add(createStatCard("Win Rate", String.format("%.1f%%", player.getWinRatePercentage()), Theme.PRIMARY_ACCENT));
        gridPanel.add(createStatCard("Avg Attempts", String.format("%.2f", player.getAverageAttemptsPerRound()), Theme.COLOR_LOW));

        add(gridPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        footerPanel.setBackground(Theme.BG_DARK);

        ModernButton resetBtn = new ModernButton("Reset Stats", Theme.COLOR_HIGH, Theme.COLOR_HIGH.brighter());
        resetBtn.setPreferredSize(new Dimension(130, 38));
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "Are you sure you want to reset your session statistics?", 
                    "Confirm Reset", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                player.resetStats();
                dispose();
            }
        });

        ModernButton closeBtn = new ModernButton("Close Analytics", Theme.PRIMARY_ACCENT, Theme.PRIMARY_HOVER);
        closeBtn.setPreferredSize(new Dimension(150, 38));
        closeBtn.addActionListener(e -> dispose());

        footerPanel.add(resetBtn);
        footerPanel.add(closeBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String label, String value, Color accentColor) {
        GradientPanel card = new GradientPanel(12);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblText = new JLabel(label);
        lblText.setFont(Theme.FONT_SUBTITLE);
        lblText.setForeground(Theme.TEXT_MUTED);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(Theme.FONT_STAT_VAL);
        lblVal.setForeground(accentColor);

        card.add(lblText, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);

        return card;
    }
}
