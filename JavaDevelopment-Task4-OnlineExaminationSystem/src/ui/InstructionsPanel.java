package ui;

import ui.components.ModernButton;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;

public class InstructionsPanel extends JPanel {

    private final MainFrame mainFrame;

    public InstructionsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());   // centres card
        setBackground(UIUtils.BACKGROUND_COLOR);
        add(buildCard());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIUtils.SURFACE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(36, 48, 36, 48)));

        // ── Title ───────────────────────────────────────────────
        JLabel title = UIUtils.label("EXAM INSTRUCTIONS",
                                     UIUtils.FONT_PAGE_TITLE, UIUtils.PRIMARY_COLOR);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = UIUtils.label("Please read the following instructions carefully.",
                                   UIUtils.FONT_BODY, UIUtils.TEXT_SECONDARY);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        // ── Stats row (compact) ──────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        statsRow.setAlignmentX(LEFT_ALIGNMENT);
        statsRow.add(statCard("10", "QUESTIONS"));
        statsRow.add(statCard("05:00", "DURATION"));
        statsRow.add(statCard("MCQ", "FORMAT"));

        // ── Rules ────────────────────────────────────────────────
        JLabel rulesTitle = UIUtils.label("General Instructions",
                                          UIUtils.FONT_SECTION, UIUtils.TEXT_PRIMARY);
        rulesTitle.setAlignmentX(LEFT_ALIGNMENT);

        String[] rules = {
            "Each question has exactly one correct answer.",
            "You can navigate freely using Previous and Next.",
            "Your answers are automatically saved as you move.",
            "You can change your answers before submission.",
            "The exam automatically submits when time expires.",
            "Closing the window during the exam shows a confirmation.",
            "Answers cannot be changed after submission."
        };

        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setOpaque(false);
        rulesPanel.setAlignmentX(LEFT_ALIGNMENT);
        for (String rule : rules) {
            JLabel lbl = UIUtils.label("  ✓  " + rule,
                                       UIUtils.FONT_BODY, UIUtils.TEXT_PRIMARY);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            rulesPanel.add(lbl);
            rulesPanel.add(UIUtils.vgap(8));
        }

        // ── Start button ─────────────────────────────────────────
        ModernButton startBtn = new ModernButton("START EXAM →", ModernButton.ButtonStyle.PRIMARY);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startBtn.setAlignmentX(LEFT_ALIGNMENT);
        startBtn.setPreferredSize(new Dimension(200, 44));
        startBtn.setMaximumSize(new Dimension(200, 44));

        // ── Assemble ─────────────────────────────────────────────
        card.add(title);
        card.add(UIUtils.vgap(6));
        card.add(sub);
        card.add(UIUtils.vgap(24));
        card.add(statsRow);
        card.add(UIUtils.vgap(24));
        card.add(UIUtils.separator());
        card.add(UIUtils.vgap(20));
        card.add(rulesTitle);
        card.add(UIUtils.vgap(14));
        card.add(rulesPanel);
        card.add(UIUtils.vgap(32));
        card.add(startBtn);

        startBtn.addActionListener(e -> mainFrame.showCard("EXAM"));
        return card;
    }

    private JPanel statCard(String value, String label) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIUtils.LIGHT_BLUE_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)));

        JLabel val = UIUtils.label(value, UIUtils.FONT_STAT_VALUE, UIUtils.PRIMARY_COLOR);
        val.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbl = UIUtils.label(label, UIUtils.FONT_STAT_LABEL, UIUtils.TEXT_SECONDARY);
        lbl.setAlignmentX(CENTER_ALIGNMENT);

        p.add(val);
        p.add(UIUtils.vgap(4));
        p.add(lbl);
        return p;
    }
}
