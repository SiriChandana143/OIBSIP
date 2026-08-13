package ui;

import model.Question;
import service.ExamService;
import service.ResultService;
import ui.components.ModernButton;
import ui.components.OptionCard;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExamPanel extends JPanel {

    private final MainFrame      mainFrame;
    private final ExamService    examService;
    private final ResultService  resultService;

    // Header
    private JLabel studentLabel;
    private JLabel timerLabel;
    private JLabel timerCaption;

    // Navigator
    private JButton[] navBtns;

    // Question
    private JLabel    questionNumLabel;
    private JTextArea questionTextArea;
    private OptionCard[] optionCards;
    private ButtonGroup  optionsGroup;

    // Footer
    private JLabel      progressLabel;
    private JProgressBar progressBar;
    private ModernButton prevBtn;
    private ModernButton nextBtn;
    private ModernButton submitBtn;

    // Nav btn colours
    private static final Color NAV_ANSWERED = new Color(16, 185, 129);   // green
    private static final Color NAV_CURRENT  = UIUtils.PRIMARY_COLOR;     // blue
    private static final Color NAV_EMPTY    = new Color(226, 232, 240);  // light grey

    public ExamPanel(MainFrame mainFrame, ExamService examService, ResultService resultService) {
        this.mainFrame    = mainFrame;
        this.examService  = examService;
        this.resultService = resultService;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BACKGROUND_COLOR);
        initComponents();
    }

    private void initComponents() {

        // ── TOP HEADER ──────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JPanel headerLeft = new JPanel(new GridLayout(2, 1, 0, 2));
        headerLeft.setOpaque(false);
        JLabel appTitle = UIUtils.label("ONLINE EXAMINATION",
                                        new Font("Segoe UI", Font.BOLD, 17), Color.WHITE);
        studentLabel = UIUtils.label("Student: —",
                                     UIUtils.FONT_HELPER, new Color(191, 219, 254));
        headerLeft.add(appTitle);
        headerLeft.add(studentLabel);

        // Timer box
        JPanel timerBox = new JPanel();
        timerBox.setLayout(new BoxLayout(timerBox, BoxLayout.Y_AXIS));
        timerBox.setOpaque(false);
        timerBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)));

        timerCaption = UIUtils.label("TIME REMAINING",
                                     new Font("Segoe UI", Font.BOLD, 10),
                                     new Color(191, 219, 254));
        timerCaption.setAlignmentX(CENTER_ALIGNMENT);

        timerLabel = new JLabel("05:00");
        timerLabel.setFont(UIUtils.FONT_TIMER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setAlignmentX(CENTER_ALIGNMENT);

        timerBox.add(timerCaption);
        timerBox.add(UIUtils.vgap(2));
        timerBox.add(timerLabel);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(timerBox,   BorderLayout.EAST);

        // ── BODY: left navigator + right question ────────────────
        JPanel body = new JPanel(new BorderLayout(12, 0));
        body.setBackground(UIUtils.BACKGROUND_COLOR);
        body.setBorder(BorderFactory.createEmptyBorder(18, 20, 12, 20));

        // LEFT: Question navigator
        JPanel navPanel = buildNavigator();

        // RIGHT: Question card
        JPanel qCard = buildQuestionCard();

        body.add(navPanel, BorderLayout.WEST);
        body.add(qCard,    BorderLayout.CENTER);

        // ── FOOTER ───────────────────────────────────────────────
        JPanel footer = buildFooter();

        add(header, BorderLayout.NORTH);
        add(body,   BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    // ── Navigator panel (left sidebar) ───────────────────────────
    private JPanel buildNavigator() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(148, 0));
        panel.setBackground(UIUtils.SURFACE_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(16, 12, 16, 12)));

        JLabel navTitle = UIUtils.label("NAVIGATOR",
                                        new Font("Segoe UI", Font.BOLD, 11),
                                        UIUtils.TEXT_SECONDARY);

        JPanel grid = new JPanel(new GridLayout(0, 2, 6, 6));
        grid.setBackground(UIUtils.SURFACE_COLOR);
        navBtns = new JButton[10];
        for (int i = 0; i < 10; i++) {
            JButton btn = makeNavBtn(i + 1, NAV_EMPTY);
            navBtns[i] = btn;
            final int idx = i;
            btn.addActionListener(e -> jumpTo(idx));
            grid.add(btn);
        }

        // Legend
        JPanel legend = buildLegend();

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setBackground(UIUtils.SURFACE_COLOR);
        top.add(navTitle, BorderLayout.NORTH);
        top.add(grid,     BorderLayout.CENTER);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(legend, BorderLayout.SOUTH);
        return panel;
    }

    private JButton makeNavBtn(int num, Color bg) {
        JButton btn = new JButton(String.valueOf(num));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(bg == NAV_EMPTY ? UIUtils.TEXT_SECONDARY : Color.WHITE);
        btn.setBackground(bg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(52, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIUtils.SURFACE_COLOR);
        p.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        p.add(legendRow(NAV_ANSWERED, "Answered"));
        p.add(UIUtils.vgap(4));
        p.add(legendRow(NAV_CURRENT,  "Current"));
        p.add(UIUtils.vgap(4));
        p.add(legendRow(NAV_EMPTY,    "Unanswered"));
        return p;
    }

    private JPanel legendRow(Color c, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setBackground(UIUtils.SURFACE_COLOR);
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c == NAV_EMPTY ? UIUtils.BORDER_COLOR : c);
                g2.fillOval(0, 0, 10, 10);
                g2.dispose();
            }
        };
        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(10, 10));
        JLabel lbl = UIUtils.label(text, UIUtils.FONT_HELPER, UIUtils.TEXT_SECONDARY);
        row.add(dot);
        row.add(lbl);
        return row;
    }

    // ── Question card (right) ─────────────────────────────────────
    private JPanel buildQuestionCard() {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(UIUtils.SURFACE_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(24, 28, 20, 28)));

        // Question num + text
        questionNumLabel = UIUtils.label("Question 1 of 10",
                                         UIUtils.FONT_BODY_BOLD, UIUtils.TEXT_SECONDARY);

        questionTextArea = new JTextArea();
        questionTextArea.setEditable(false);
        questionTextArea.setFocusable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(UIUtils.FONT_QUESTION);
        questionTextArea.setForeground(UIUtils.TEXT_PRIMARY);
        questionTextArea.setBackground(UIUtils.SURFACE_COLOR);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 18, 0));

        JPanel qTop = new JPanel(new BorderLayout());
        qTop.setBackground(UIUtils.SURFACE_COLOR);
        qTop.add(questionNumLabel,  BorderLayout.NORTH);
        qTop.add(questionTextArea,  BorderLayout.CENTER);

        // Options
        optionsGroup = new ButtonGroup();
        optionCards  = new OptionCard[4];
        JPanel optPanel = new JPanel();
        optPanel.setLayout(new BoxLayout(optPanel, BoxLayout.Y_AXIS));
        optPanel.setBackground(UIUtils.SURFACE_COLOR);

        String[] prefixes = {"A. ", "B. ", "C. ", "D. "};
        for (int i = 0; i < 4; i++) {
            optionCards[i] = new OptionCard(prefixes[i]);
            optionCards[i].setAlignmentX(LEFT_ALIGNMENT);
            optionsGroup.add(optionCards[i].getRadioButton());

            final int idx = i;
            optionCards[i].setOnSelected(() -> {
                for (int j = 0; j < 4; j++)
                    optionCards[j].setSelectedState(j == idx);
                examService.saveAnswer(idx);
                updateProgress();
                refreshNavigator();
            });
            optPanel.add(optionCards[i]);
            optPanel.add(UIUtils.vgap(8));
        }

        card.add(qTop,    BorderLayout.NORTH);
        card.add(optPanel, BorderLayout.CENTER);
        return card;
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIUtils.SURFACE_COLOR);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtils.BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        // Progress left
        JPanel progSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        progSection.setBackground(UIUtils.SURFACE_COLOR);
        progressLabel = UIUtils.label("Answered: 0 / 10", UIUtils.FONT_BODY_BOLD, UIUtils.TEXT_PRIMARY);
        progressBar = new JProgressBar(0, 10);
        progressBar.setPreferredSize(new Dimension(140, 6));
        progressBar.setForeground(UIUtils.SUCCESS_COLOR);
        progressBar.setBackground(UIUtils.BORDER_COLOR);
        progressBar.setBorderPainted(false);
        progSection.add(progressLabel);
        progSection.add(UIUtils.hgap(12));
        progSection.add(progressBar);

        // Buttons right
        JPanel navSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navSection.setBackground(UIUtils.SURFACE_COLOR);
        prevBtn   = new ModernButton("← PREVIOUS", ModernButton.ButtonStyle.SECONDARY);
        nextBtn   = new ModernButton("NEXT →",     ModernButton.ButtonStyle.PRIMARY);
        submitBtn = new ModernButton("SUBMIT EXAM", ModernButton.ButtonStyle.SUCCESS);


        prevBtn.setPreferredSize(new Dimension(130, 38));
        nextBtn.setPreferredSize(new Dimension(110, 38));
        submitBtn.setPreferredSize(new Dimension(140, 38));


        navSection.add(prevBtn);
        navSection.add(nextBtn);
        navSection.add(submitBtn);

        footer.add(progSection, BorderLayout.WEST);
        footer.add(navSection,  BorderLayout.EAST);

        prevBtn.addActionListener(e -> {
            examService.previousQuestion();
            loadCurrentQuestion();
        });
        nextBtn.addActionListener(e -> {
            examService.nextQuestion();
            loadCurrentQuestion();
        });
        submitBtn.addActionListener(e -> manualSubmit());

        return footer;
    }

    // ── Public API ────────────────────────────────────────────────
    public void startExam() {
        examService.resetExam();
        examService.startExam(this::onTick, this::autoSubmit);
        loadCurrentQuestion();
        updateProgress();
        updateTimerDisplay();
        refreshNavigator();
    }

    public void setStudentName(String name) {
        studentLabel.setText("Student: " + name);
    }

    // ── Internals ─────────────────────────────────────────────────
    private void onTick() { updateTimerDisplay(); }

    private void updateTimerDisplay() {
        int rem = examService.getRemainingSeconds();
        timerLabel.setText(String.format("%02d:%02d", rem / 60, rem % 60));
        if      (rem <= 30) { timerLabel.setForeground(UIUtils.ERROR_COLOR);   timerCaption.setText("⚠ TIME REMAINING"); }
        else if (rem <= 60) { timerLabel.setForeground(UIUtils.WARNING_COLOR); timerCaption.setText("⚠ TIME REMAINING"); }
        else                { timerLabel.setForeground(Color.WHITE);            timerCaption.setText("TIME REMAINING");   }
    }

    private void loadCurrentQuestion() {
        Question q    = examService.getCurrentQuestion();
        if (q == null) return;

        int idx   = examService.getCurrentQuestionIndex();
        int total = examService.getQuestions().size();

        questionNumLabel.setText("Question " + (idx + 1) + " of " + total);
        questionTextArea.setText(q.getQuestionText());

        String[]  opts  = q.getOptions();
        Integer   saved = examService.getSavedAnswer(idx);
        String[]  pfx   = {"A. ", "B. ", "C. ", "D. "};

        optionsGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionCards[i].setText(pfx[i] + opts[i]);
            boolean sel = saved != null && saved == i;
            optionCards[i].setSelectedState(sel);
        }
        if (saved != null) optionCards[saved].getRadioButton().setSelected(true);

        prevBtn.setEnabled(idx > 0);
        nextBtn.setVisible(idx < total - 1);
        submitBtn.setVisible(idx == total - 1);
        refreshNavigator();
    }

    private void jumpTo(int idx) {
        // Save current index, jump to target
        examService.setCurrentQuestionIndex(idx);
        loadCurrentQuestion();
    }

    private void updateProgress() {
        int answered = examService.getAnsweredCount();
        int total    = examService.getQuestions().size();
        progressLabel.setText("Answered: " + answered + " / " + total);
        progressBar.setMaximum(total);
        progressBar.setValue(answered);
    }

    private void refreshNavigator() {
        int cur   = examService.getCurrentQuestionIndex();
        int total = examService.getQuestions().size();
        for (int i = 0; i < total && i < navBtns.length; i++) {
            JButton btn = navBtns[i];
            if (i == cur) {
                btn.setBackground(NAV_CURRENT);
                btn.setForeground(Color.WHITE);
            } else if (examService.getSavedAnswer(i) != null) {
                btn.setBackground(NAV_ANSWERED);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(NAV_EMPTY);
                btn.setForeground(UIUtils.TEXT_SECONDARY);
            }
        }
    }

    private void manualSubmit() {
        int answered  = examService.getAnsweredCount();
        int total     = examService.getQuestions().size();
        int unanswered = total - answered;

        String msg;
        Object[] opts;
        if (unanswered > 0) {
            msg  = "You have " + unanswered + " unanswered question(s).\n"
                 + "Unanswered questions will be marked incorrect.\n\n"
                 + "Are you sure you want to submit?";
            opts = new Object[]{"Go Back", "Submit Anyway"};
        } else {
            msg  = "You have answered all " + total + " questions.\nAre you sure you want to submit?";
            opts = new Object[]{"Cancel", "Submit"};
        }

        int choice = JOptionPane.showOptionDialog(this, msg, "Submit Examination?",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opts, opts[0]);

        if (choice == 1) {
            examService.stopTimer();
            examService.setExamSubmitted(true);
            mainFrame.showCard("RESULT");
        }
    }

    private void autoSubmit() {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("00:00");
            timerLabel.setForeground(UIUtils.ERROR_COLOR);
            examService.setExamSubmitted(true);
            JOptionPane.showMessageDialog(this,
                "Time is up!\nYour examination has been automatically submitted.",
                "Time Expired", JOptionPane.WARNING_MESSAGE);
            mainFrame.showCard("RESULT");
        });
    }
}
