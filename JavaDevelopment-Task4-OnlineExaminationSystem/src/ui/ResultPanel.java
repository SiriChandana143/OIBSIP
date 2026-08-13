package ui;

import model.ExamResult;
import model.Question;
import service.AuthenticationService;
import service.ExamService;
import service.ResultService;
import ui.components.ModernButton;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;

public class ResultPanel extends JPanel {

    private static final int REVIEW_MARGIN = 24;

    private final MainFrame             mainFrame;
    private final AuthenticationService authService;
    private final ExamService           examService;
    private final ResultService         resultService;

    private JLabel congratsLabel;
    private JLabel subtitleLabel;

    // Stat labels
    private JLabel scoreVal;
    private JLabel pctVal;
    private JLabel correctVal;
    private JLabel incorrectVal;
    private JLabel unansweredVal;
    private JLabel timeVal;
    private JLabel submissionLbl;

    // The scrollable review area
    private JPanel       reviewPanel;
    private JScrollPane  reviewScroll;
    private ScrollablePanel scrollBody;

    // Track the current review row count for GridBagLayout
    private int reviewRowIndex = 0;

    public ResultPanel(MainFrame mainFrame, AuthenticationService authService,
                       ExamService examService, ResultService resultService) {
        this.mainFrame     = mainFrame;
        this.authService   = authService;
        this.examService   = examService;
        this.resultService = resultService;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);
        initComponents();
    }

    private void initComponents() {

        // ── Compact header ───────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 3));
        headerText.setOpaque(false);

        congratsLabel = UIUtils.label("Examination Result",
                                      UIUtils.FONT_PAGE_TITLE, Color.WHITE);
        subtitleLabel = UIUtils.label("You have completed the examination.",
                                      UIUtils.FONT_BODY, new Color(191, 219, 254));
        headerText.add(congratsLabel);
        headerText.add(subtitleLabel);
        header.add(headerText, BorderLayout.WEST);

        // ── Stats bar ─────────────────────────────────────────────────
        // Use a panel that fills its parent width.  GridBagLayout with
        // weightx=1.0 on every card makes all 5 cards share available space
        // equally and shrink/grow without forcing a fixed minimum width.
        JPanel statsBar = new JPanel(new GridBagLayout());
        statsBar.setBackground(UIUtils.BACKGROUND_COLOR);
        statsBar.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        scoreVal      = new JLabel("—"); pctVal = new JLabel("—");
        correctVal    = new JLabel("—");
        incorrectVal  = new JLabel("—");
        unansweredVal = new JLabel("—");
        timeVal       = new JLabel("—");

        addStatCard(statsBar, 0, compactCard("SCORE",      scoreVal,      UIUtils.PRIMARY_COLOR,  Color.WHITE));
        addStatCard(statsBar, 1, compactCard("CORRECT",    correctVal,    UIUtils.SUCCESS_COLOR,  Color.WHITE));
        addStatCard(statsBar, 2, compactCard("INCORRECT",  incorrectVal,  UIUtils.ERROR_COLOR,    Color.WHITE));
        addStatCard(statsBar, 3, compactCard("UNANSWERED", unansweredVal, new Color(100,116,139), Color.WHITE));
        addStatCard(statsBar, 4, compactCard("TIME TAKEN", timeVal,       UIUtils.WARNING_COLOR,  Color.WHITE));

        // Submission + percentage row
        submissionLbl = UIUtils.label(" ", UIUtils.FONT_HELPER, UIUtils.TEXT_SECONDARY);
        JPanel subRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        subRow.setBackground(UIUtils.BACKGROUND_COLOR);
        subRow.add(submissionLbl);
        subRow.add(pctVal);
        pctVal.setFont(UIUtils.FONT_BODY_BOLD);
        pctVal.setForeground(UIUtils.TEXT_SECONDARY);

        // ── Fixed top block: header + stats (no scroll, fixed to frame width) ──
        JPanel fixedTop = new JPanel(new BorderLayout());
        fixedTop.setBackground(UIUtils.BACKGROUND_COLOR);

        JPanel statsBlock = new JPanel(new BorderLayout());
        statsBlock.setBackground(UIUtils.BACKGROUND_COLOR);
        statsBlock.add(statsBar, BorderLayout.NORTH);
        statsBlock.add(subRow,   BorderLayout.SOUTH);

        fixedTop.add(header,     BorderLayout.NORTH);
        fixedTop.add(statsBlock, BorderLayout.CENTER);

        // ── Answer review (ONLY this section scrolls — vertically only) ──
        JLabel reviewTitle = UIUtils.label("Answer Review",
                                           UIUtils.FONT_SECTION, UIUtils.TEXT_PRIMARY);
        reviewTitle.setHorizontalAlignment(SwingConstants.CENTER);
        reviewTitle.setBorder(BorderFactory.createEmptyBorder(8, REVIEW_MARGIN, 8, REVIEW_MARGIN));

        // Use GridBagLayout so each row gets fill=HORIZONTAL automatically —
        // this eliminates the BoxLayout alignment issues that caused H-scroll.
        reviewPanel = new JPanel(new GridBagLayout());
        reviewPanel.setBackground(UIUtils.BACKGROUND_COLOR);

        // ScrollablePanel wraps reviewPanel and enforces viewport-width tracking.
        scrollBody = new ScrollablePanel();
        scrollBody.setLayout(new BorderLayout());
        scrollBody.setBackground(UIUtils.BACKGROUND_COLOR);

        // Inner wrapper adds the margin around reviewPanel.
        JPanel reviewWrapper = new JPanel(new GridBagLayout());
        reviewWrapper.setBackground(UIUtils.BACKGROUND_COLOR);

        GridBagConstraints bodyGbc = new GridBagConstraints();
        bodyGbc.gridx   = 0;
        bodyGbc.gridy   = 0;
        bodyGbc.weightx = 1.0;
        bodyGbc.weighty = 0;
        bodyGbc.anchor  = GridBagConstraints.NORTH;
        bodyGbc.fill    = GridBagConstraints.HORIZONTAL;
        bodyGbc.insets  = new Insets(0, REVIEW_MARGIN, 0, REVIEW_MARGIN);
        reviewWrapper.add(reviewPanel, bodyGbc);

        // Filler row to push content to the top
        GridBagConstraints fillerGbc = new GridBagConstraints();
        fillerGbc.gridx   = 0;
        fillerGbc.gridy   = 1;
        fillerGbc.weightx = 1.0;
        fillerGbc.weighty = 1.0;
        fillerGbc.fill    = GridBagConstraints.BOTH;
        reviewWrapper.add(Box.createGlue(), fillerGbc);

        scrollBody.add(reviewWrapper, BorderLayout.CENTER);

        reviewScroll = new JScrollPane(scrollBody);
        reviewScroll.setBorder(null);
        reviewScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        reviewScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        reviewScroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel reviewSection = new JPanel(new BorderLayout());
        reviewSection.setBackground(UIUtils.BACKGROUND_COLOR);
        reviewSection.add(reviewTitle,  BorderLayout.NORTH);
        reviewSection.add(reviewScroll, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────
        ModernButton logoutBtn = new ModernButton("LOGOUT", ModernButton.ButtonStyle.DANGER);
        logoutBtn.setPreferredSize(new Dimension(130, 38));
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(UIUtils.SURFACE_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtils.BORDER_COLOR));
        footer.add(logoutBtn);

        add(fixedTop,      BorderLayout.NORTH);
        add(reviewSection, BorderLayout.CENTER);
        add(footer,        BorderLayout.SOUTH);

        logoutBtn.addActionListener(e -> mainFrame.resetApplication());
    }

    // ── Called from MainFrame ──────────────────────────────────────────
    public void displayResult() {
        String submissionType = (examService.getRemainingSeconds() <= 0)
            ? "Automatic" : "Manual";
        ExamResult r = resultService.calculateResult(examService, submissionType);

        String name = authService.getCurrentUser() != null
            ? authService.getCurrentUser().getDisplayName() : "Student";

        congratsLabel.setText("Congratulations, " + name + "!");
        subtitleLabel.setText("You have completed the examination successfully.");
        scoreVal.setText(r.getScore() + " / " + r.getTotalQuestions());
        pctVal.setText(String.format("Score: %.0f%%", r.getPercentage()));
        correctVal.setText(String.valueOf(r.getCorrectAnswers()));
        incorrectVal.setText(String.valueOf(r.getIncorrectAnswers()));
        unansweredVal.setText(String.valueOf(r.getUnansweredQuestions()));
        timeVal.setText(r.getFormattedTimeTaken());
        submissionLbl.setText("Submission: " + r.getSubmissionType());

        populateReview();

        SwingUtilities.invokeLater(() -> {
            scrollBody.revalidate();
            scrollBody.repaint();
        });
    }

    private void populateReview() {
        reviewPanel.removeAll();
        reviewRowIndex = 0;

        List<Question>       questions = examService.getQuestions();
        Map<Integer, Integer> answers  = examService.getAllSelectedAnswers();

        GridBagConstraints rowGbc = new GridBagConstraints();
        rowGbc.gridx   = 0;
        rowGbc.weightx = 1.0;
        rowGbc.fill    = GridBagConstraints.HORIZONTAL;
        rowGbc.anchor  = GridBagConstraints.NORTHWEST;

        for (int i = 0; i < questions.size(); i++) {
            Question q          = questions.get(i);
            Integer  userAnswer = answers.get(i);
            int      correct    = q.getCorrectOptionIndex();

            String status; Color accentColor; Color rowBg;
            if (userAnswer == null) {
                status = "—  Unanswered"; accentColor = new Color(100,116,139); rowBg = new Color(248,250,252);
            } else if (userAnswer == correct) {
                status = "✓  Correct";    accentColor = UIUtils.SUCCESS_COLOR;  rowBg = new Color(240,253,244);
            } else {
                status = "✗  Incorrect";  accentColor = UIUtils.ERROR_COLOR;    rowBg = new Color(254,242,242);
            }

            rowGbc.gridy  = reviewRowIndex++;
            rowGbc.insets = new Insets(0, 0, 8, 0);
            reviewPanel.add(reviewRow(i + 1, q, userAnswer, correct, status, accentColor, rowBg), rowGbc);
        }

        reviewPanel.revalidate();
        reviewPanel.repaint();
    }

    /**
     * Builds one answer-review card.
     *
     * Layout:
     *   [4px accent strip] | [content: Q-header / question text / your answer / correct answer]
     *
     * The row fills its parent width (enforced by GridBagLayout fill=HORIZONTAL in
     * reviewPanel), and the JTextArea components wrap their text to avoid horizontal overflow.
     */
    private JPanel reviewRow(int num, Question q, Integer userAnswer,
                             int correct, String status, Color accent, Color bg) {

        // Outer row – BorderLayout so the accent strip sits flush on the left
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(bg);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(Math.min(accent.getRed()   + 60, 255),
                          Math.min(accent.getGreen() + 60, 255),
                          Math.min(accent.getBlue()  + 60, 255), 80), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        // Accent strip on the left
        JPanel strip = new JPanel();
        strip.setPreferredSize(new Dimension(4, 0));
        strip.setBackground(accent);
        row.add(strip, BorderLayout.WEST);

        // Content area – GridBagLayout so every child fills the available width
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(bg);

        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx   = 0;
        cc.weightx = 1.0;
        cc.fill    = GridBagConstraints.HORIZONTAL;
        cc.anchor  = GridBagConstraints.NORTHWEST;

        // Q-number + status header
        JLabel statusLbl = UIUtils.label("Q" + num + "  " + status, UIUtils.FONT_BODY_BOLD, accent);
        cc.gridy  = 0;
        cc.insets = new Insets(0, 0, 4, 0);
        content.add(statusLbl, cc);

        // Question text – wraps to content width
        JTextArea qText = wrappingArea(q.getQuestionText(), UIUtils.FONT_BODY, UIUtils.TEXT_PRIMARY, bg);
        cc.gridy  = 1;
        cc.insets = new Insets(0, 0, 4, 0);
        content.add(qText, cc);

        // Your answer
        JTextArea yourAns = wrappingArea(
            "Your answer: " + ((userAnswer == null) ? "Not answered" : q.getOptions()[userAnswer]),
            UIUtils.FONT_HELPER,
            userAnswer == null ? new Color(100,116,139)
                : (userAnswer == correct ? UIUtils.SUCCESS_COLOR : UIUtils.ERROR_COLOR),
            bg);
        cc.gridy  = 2;
        cc.insets = new Insets(0, 0, 2, 0);
        content.add(yourAns, cc);

        // Correct answer
        JTextArea correctAns = wrappingArea(
            "Correct answer: " + q.getOptions()[correct],
            new Font("Segoe UI", Font.BOLD, 12),
            UIUtils.SUCCESS_COLOR,
            bg);
        cc.gridy  = 3;
        cc.insets = new Insets(0, 0, 0, 0);
        content.add(correctAns, cc);

        row.add(content, BorderLayout.CENTER);
        return row;
    }

    /**
     * Creates a non-editable, line-wrapping JTextArea styled as a label.
     * Setting columns=1 ensures the preferred width is minimal so the
     * GridBagLayout (fill=HORIZONTAL) — not the text content — controls width.
     */
    private JTextArea wrappingArea(String text, Font font, Color fg, Color bg) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(font);
        area.setForeground(fg);
        area.setBackground(bg);
        area.setBorder(null);
        area.setColumns(1);   // minimal preferred width – layout supplies actual width
        return area;
    }

    private void addStatCard(JPanel statsBar, int column, JPanel card) {
        GridBagConstraints sc = new GridBagConstraints();
        sc.gridx   = column;
        sc.gridy   = 0;
        sc.weightx = 1.0;
        sc.weighty = 1.0;
        sc.fill    = GridBagConstraints.BOTH;
        sc.insets  = new Insets(0, column == 0 ? 0 : 5, 0, column == 4 ? 0 : 5);
        statsBar.add(card, sc);
    }

    // ── Compact stat card ──────────────────────────────────────────────
    private JPanel compactCard(String label, JLabel valueLabel, Color bg, Color fg) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
        card.setBackground(bg);
        // No fixed minimum – card shrinks with the window.
        card.setMinimumSize(new Dimension(0, 0));
        card.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        JLabel lbl = UIUtils.label(label, UIUtils.FONT_STAT_LABEL,
                                   new Color(Math.min(fg.getRed()   + 40, 255),
                                             Math.min(fg.getGreen() + 40, 255),
                                             Math.min(fg.getBlue()  + 40, 255)));
        valueLabel.setFont(UIUtils.FONT_STAT_VALUE);
        valueLabel.setForeground(fg);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lbl);
        card.add(valueLabel);
        return card;
    }

    // ── ScrollablePanel: tracks viewport width, never scrolls horizontally ──
    private class ScrollablePanel extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            int vw = viewportWidth();
            if (vw > 0) d.width = vw;
            return d;
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension d = super.getMinimumSize();
            int vw = viewportWidth();
            if (vw > 0) d.width = vw;
            return d;
        }

        private int viewportWidth() {
            Container parent = getParent();
            return (parent instanceof JViewport) ? parent.getWidth() : 0;
        }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle v, int o, int d) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle v, int o, int d) { return 64; }
        @Override public boolean getScrollableTracksViewportWidth()  { return true;  }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }
}
