package com.numberquest.ui;

import com.numberquest.model.ScoreRecord;
import com.numberquest.service.ScoreManager;
import com.numberquest.ui.components.GradientPanel;
import com.numberquest.ui.components.ModernButton;
import com.numberquest.ui.theme.Theme;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Modal dialog displaying the persistent top scores leaderboard.
 */
public class LeaderboardDialog extends JDialog {

    public LeaderboardDialog(Frame owner, ScoreManager scoreManager) {
        super(owner, "Hall of Fame - Top Scores", true);
        setSize(600, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_DARK);

        // Header Panel
        GradientPanel headerPanel = new GradientPanel(0);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel titleLabel = new JLabel("🏆 Leaderboard - Top Players");
        titleLabel.setFont(Theme.FONT_HEADER_TITLE);
        titleLabel.setForeground(Theme.TEXT_MAIN);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Rank", "Player", "Difficulty", "Score", "Attempts", "Date"};
        List<ScoreRecord> topScores = scoreManager.getTopScores();

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < topScores.size(); i++) {
            ScoreRecord record = topScores.get(i);
            String rankStr;
            switch (i) {
                case 0: rankStr = "🥇 1st"; break;
                case 1: rankStr = "🥈 2nd"; break;
                case 2: rankStr = "🥉 3rd"; break;
                default: rankStr = "  " + (i + 1) + "th"; break;
            }

            model.addRow(new Object[]{
                    rankStr,
                    record.getPlayerName(),
                    record.getDifficulty().getDisplayName(),
                    record.getScore() + " pts",
                    record.getAttemptsUsed() + " attempts",
                    record.getTimestamp()
            });
        }

        JTable table = new JTable(model);
        table.setBackground(Theme.CARD_BG);
        table.setForeground(Theme.TEXT_MAIN);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(36);
        table.setGridColor(Theme.BORDER_COLOR);
        table.setFillsViewportHeight(true);

        // Header Customization
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.CARD_HEADER_BG);
        header.setForeground(Theme.TEXT_MAIN);
        header.setFont(Theme.FONT_LABEL_BOLD);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        // Center align table contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.CARD_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        scrollPane.setBackground(Theme.BG_DARK);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Close Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        footerPanel.setBackground(Theme.BG_DARK);

        ModernButton closeBtn = new ModernButton("Close Leaderboard", Theme.PRIMARY_ACCENT, Theme.PRIMARY_HOVER);
        closeBtn.setPreferredSize(new Dimension(180, 40));
        closeBtn.addActionListener(e -> dispose());
        footerPanel.add(closeBtn);

        add(footerPanel, BorderLayout.SOUTH);
    }
}
