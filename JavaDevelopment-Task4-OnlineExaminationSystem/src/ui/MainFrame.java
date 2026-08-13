package ui;

import service.AuthenticationService;
import service.ExamService;
import service.ResultService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private AuthenticationService authService;
    private ExamService examService;
    private ResultService resultService;

    private LoginPanel loginPanel;
    private ProfilePanel profilePanel;
    private InstructionsPanel instructionsPanel;
    private ExamPanel examPanel;
    private ResultPanel resultPanel;

    public MainFrame() {
        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(960, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        authService   = new AuthenticationService();
        examService   = new ExamService();
        resultService = new ResultService();

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        loginPanel        = new LoginPanel(this, authService);
        profilePanel      = new ProfilePanel(this, authService);
        instructionsPanel = new InstructionsPanel(this);
        examPanel         = new ExamPanel(this, examService, resultService);
        resultPanel       = new ResultPanel(this, authService, examService, resultService);

        mainPanel.add(loginPanel,        "LOGIN");
        mainPanel.add(profilePanel,      "PROFILE");
        mainPanel.add(instructionsPanel, "INSTRUCTIONS");
        mainPanel.add(examPanel,         "EXAM");
        mainPanel.add(resultPanel,       "RESULT");

        add(mainPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        showCard("LOGIN");
    }

    public void showCard(String cardName) {
        switch (cardName) {
            case "PROFILE":
                profilePanel.refresh();
                break;
            case "EXAM":
                // Inject current student display name into exam header
                if (authService.getCurrentUser() != null) {
                    examPanel.setStudentName(authService.getCurrentUser().getDisplayName());
                }
                examPanel.startExam();
                break;
            case "RESULT":
                resultPanel.displayResult();
                break;
            case "LOGIN":
                loginPanel.reset();
                break;
        }
        cardLayout.show(mainPanel, cardName);
    }

    private void handleWindowClosing() {
        if (examService.isExamStarted() && !examService.isExamSubmitted()) {
            Object[] opts = {"Continue Exam", "Quit"};
            int choice = JOptionPane.showOptionDialog(this,
                "Your examination is currently in progress.\nIf you exit now, your exam will end.",
                "Are you sure you want to quit?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, opts, opts[0]);

            if (choice == 1) { // Quit
                examService.stopTimer();
                System.exit(0);
            }
            // choice == 0 → continue exam, do nothing
        } else {
            System.exit(0);
        }
    }

    public void resetApplication() {
        examService.resetExam();
        showCard("LOGIN");
    }
}
