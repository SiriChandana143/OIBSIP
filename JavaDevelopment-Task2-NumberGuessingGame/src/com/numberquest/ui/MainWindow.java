package com.numberquest.ui;

import com.numberquest.model.Difficulty;
import com.numberquest.model.GameEngine;
import com.numberquest.model.Player;
import com.numberquest.model.ScoreRecord;
import com.numberquest.service.ScoreManager;
import com.numberquest.service.SoundManager;
import com.numberquest.ui.components.GradientPanel;
import com.numberquest.ui.components.ModernButton;
import com.numberquest.ui.components.ModernTextField;
import com.numberquest.ui.theme.Theme;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Main application Swing JFrame window orchestration controller.
 */
public class MainWindow extends JFrame {

    private final GameEngine gameEngine;
    private final Player player;
    private final ScoreManager scoreManager;
    private final SoundManager soundManager;

    // UI Components
    private JComboBox<Difficulty> difficultyComboBox;
    private ModernTextField guessInputField;
    private ModernButton guessBtn;
    private ModernButton newGameBtn;
    private ModernButton soundToggleBtn;
    private ModernButton leaderboardBtn;
    private ModernButton statsBtn;
    private ModernButton changeNameBtn;

    private JLabel playerBadgeLabel;
    private JLabel totalScoreLabel;
    private JLabel roundBadgeLabel;
    private JLabel rangeInstructionLabel;
    private JLabel attemptCounterLabel;
    private JProgressBar attemptProgressBar;

    private GradientPanel feedbackBannerPanel;
    private JLabel feedbackBannerLabel;
    private JLabel correctNumberRevealLabel;

    public MainWindow() {
        this.gameEngine = new GameEngine();
        this.scoreManager = new ScoreManager();
        this.soundManager = new SoundManager();
        this.player = new Player("Player 1");

        initializeFrame();
        promptPlayerNameOnStartup();
        buildUI();
        startNewGame();
    }

    private void initializeFrame() {
        setTitle("Number Quest – Interactive Java Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 620);
        setMinimumSize(new Dimension(680, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 0));
    }

    private void promptPlayerNameOnStartup() {
        String name = JOptionPane.showInputDialog(
                this,
                "Enter your Player Name for the Leaderboard:",
                "Welcome to Number Quest",
                JOptionPane.QUESTION_MESSAGE
        );
        if (name != null && !name.trim().isEmpty()) {
            player.setName(name);
        }
    }

    private void buildUI() {
        // --- 1. Top Header Panel ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(Theme.BG_DARK);

        GradientPanel titlePanel = new GradientPanel(Theme.CARD_BG, Theme.CARD_HEADER_BG, 0);
        titlePanel.setLayout(new BorderLayout(15, 10));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title and Tagline
        JPanel textGroup = new JPanel(new GridLayout(2, 1, 2, 2));
        textGroup.setOpaque(false);
        JLabel titleLabel = new JLabel("⚡ NUMBER QUEST");
        titleLabel.setFont(Theme.FONT_HEADER_TITLE);
        titleLabel.setForeground(Theme.TEXT_MAIN);

        JLabel subtitleLabel = new JLabel("Interactive Java Guessing Game • Portfolio Edition");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        textGroup.add(titleLabel);
        textGroup.add(subtitleLabel);

        // Header Right: Player Profile Badge & Sound Toggle
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRightPanel.setOpaque(false);

        playerBadgeLabel = new JLabel("👤 " + player.getName());
        playerBadgeLabel.setFont(Theme.FONT_LABEL_BOLD);
        playerBadgeLabel.setForeground(Theme.SECONDARY_ACCENT);

        changeNameBtn = new ModernButton("Edit Name", Theme.CARD_BG, Theme.BORDER_COLOR);
        changeNameBtn.setPreferredSize(new Dimension(90, 30));
        changeNameBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        changeNameBtn.addActionListener(e -> changePlayerName());

        soundToggleBtn = new ModernButton("🔊 Sound", Theme.CARD_BG, Theme.BORDER_COLOR);
        soundToggleBtn.setPreferredSize(new Dimension(90, 30));
        soundToggleBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        soundToggleBtn.addActionListener(e -> toggleAudio());

        headerRightPanel.add(playerBadgeLabel);
        headerRightPanel.add(changeNameBtn);
        headerRightPanel.add(soundToggleBtn);

        titlePanel.add(textGroup, BorderLayout.WEST);
        titlePanel.add(headerRightPanel, BorderLayout.EAST);
        topContainer.add(titlePanel, BorderLayout.NORTH);

        // --- 2. Control Bar (Difficulty Selector & Toolbar Buttons) ---
        JPanel controlBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlBarPanel.setBackground(Theme.BG_DARK);

        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(Theme.FONT_LABEL_BOLD);
        diffLabel.setForeground(Theme.TEXT_MAIN);

        difficultyComboBox = new JComboBox<>(Difficulty.values());
        difficultyComboBox.setSelectedItem(Difficulty.MEDIUM);
        difficultyComboBox.setFont(Theme.FONT_LABEL_BOLD);
        difficultyComboBox.setBackground(Theme.CARD_HEADER_BG);
        difficultyComboBox.setForeground(Theme.TEXT_MAIN);
        difficultyComboBox.setFocusable(false);
        difficultyComboBox.setPreferredSize(new Dimension(280, 36));

        // Custom Renderer to ensure crystal clear high-contrast text rendering across all OS LookAndFeels
        difficultyComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setFont(Theme.FONT_LABEL_BOLD);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                if (list != null) {
                    list.setBackground(new Color(15, 23, 42)); // Deep slate background for popup menu
                    list.setSelectionBackground(Theme.PRIMARY_ACCENT);
                    list.setSelectionForeground(Color.WHITE);
                }

                if (isSelected) {
                    label.setBackground(Theme.PRIMARY_ACCENT); // Indigo selection accent
                    label.setForeground(Color.WHITE);          // Pure white text
                } else {
                    label.setBackground(new Color(15, 23, 42)); // Deep slate background
                    label.setForeground(Color.WHITE);          // Pure white text on dark background
                }

                if (value instanceof Difficulty) {
                    label.setText(((Difficulty) value).toString());
                }
                return label;
            }
        });

        difficultyComboBox.addActionListener(e -> {
            soundManager.playClickSound();
            startNewGame();
        });

        newGameBtn = new ModernButton("🔄 New Game", Theme.SECONDARY_ACCENT, Theme.SECONDARY_ACCENT.brighter());
        newGameBtn.setPreferredSize(new Dimension(120, 34));
        newGameBtn.addActionListener(e -> {
            soundManager.playClickSound();
            startNewGame();
        });

        leaderboardBtn = new ModernButton("🏆 Leaderboard", Theme.CARD_BG, Theme.BORDER_COLOR);
        leaderboardBtn.setPreferredSize(new Dimension(130, 34));
        leaderboardBtn.addActionListener(e -> {
            soundManager.playClickSound();
            new LeaderboardDialog(this, scoreManager).setVisible(true);
        });

        statsBtn = new ModernButton("📊 Stats", Theme.CARD_BG, Theme.BORDER_COLOR);
        statsBtn.setPreferredSize(new Dimension(90, 34));
        statsBtn.addActionListener(e -> {
            soundManager.playClickSound();
            new StatsDialog(this, player).setVisible(true);
        });

        controlBarPanel.add(diffLabel);
        controlBarPanel.add(difficultyComboBox);
        controlBarPanel.add(newGameBtn);
        controlBarPanel.add(leaderboardBtn);
        controlBarPanel.add(statsBtn);

        topContainer.add(controlBarPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        // --- 3. Center Game Card Panel ---
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Theme.BG_DARK);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        GradientPanel gameCard = new GradientPanel(16);
        gameCard.setLayout(new BoxLayout(gameCard, BoxLayout.Y_AXIS));
        gameCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Sub Header Info: Round Badge & Total Score
        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);

        roundBadgeLabel = new JLabel("Round #1");
        roundBadgeLabel.setFont(Theme.FONT_LABEL_BOLD);
        roundBadgeLabel.setForeground(Theme.TEXT_MUTED);

        totalScoreLabel = new JLabel("Total Score: 0 pts");
        totalScoreLabel.setFont(Theme.FONT_LABEL_BOLD);
        totalScoreLabel.setForeground(Theme.COLOR_CORRECT);

        infoRow.add(roundBadgeLabel, BorderLayout.WEST);
        infoRow.add(totalScoreLabel, BorderLayout.EAST);
        gameCard.add(infoRow);
        gameCard.add(Box.createVerticalStrut(15));

        // Range Instruction
        rangeInstructionLabel = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        rangeInstructionLabel.setFont(Theme.FONT_SUBTITLE);
        rangeInstructionLabel.setForeground(Theme.TEXT_MAIN);
        rangeInstructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(rangeInstructionLabel);
        gameCard.add(Box.createVerticalStrut(10));

        // Attempt Counter & Visual Progress Bar
        attemptCounterLabel = new JLabel("Attempts: 0 / 7", SwingConstants.CENTER);
        attemptCounterLabel.setFont(Theme.FONT_LABEL_BOLD);
        attemptCounterLabel.setForeground(Theme.SECONDARY_ACCENT);
        attemptCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(attemptCounterLabel);
        gameCard.add(Box.createVerticalStrut(6));

        attemptProgressBar = new JProgressBar(0, 100);
        attemptProgressBar.setValue(100);
        attemptProgressBar.setMaximumSize(new Dimension(400, 10));
        attemptProgressBar.setForeground(Theme.PRIMARY_ACCENT);
        attemptProgressBar.setBackground(Theme.INPUT_BG);
        attemptProgressBar.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1));
        gameCard.add(attemptProgressBar);
        gameCard.add(Box.createVerticalStrut(20));

        // Feedback Banner
        feedbackBannerPanel = new GradientPanel(Theme.CARD_HEADER_BG, Theme.CARD_BG, 12);
        feedbackBannerPanel.setLayout(new BorderLayout());
        feedbackBannerPanel.setMaximumSize(new Dimension(500, 50));
        feedbackBannerPanel.setPreferredSize(new Dimension(500, 50));

        feedbackBannerLabel = new JLabel("Make your first guess below!", SwingConstants.CENTER);
        feedbackBannerLabel.setFont(Theme.FONT_FEEDBACK_BANNER);
        feedbackBannerLabel.setForeground(Theme.TEXT_MAIN);
        feedbackBannerPanel.add(feedbackBannerLabel, BorderLayout.CENTER);
        gameCard.add(feedbackBannerPanel);

        correctNumberRevealLabel = new JLabel("", SwingConstants.CENTER);
        correctNumberRevealLabel.setFont(Theme.FONT_LABEL_BOLD);
        correctNumberRevealLabel.setForeground(Theme.COLOR_HIGH);
        correctNumberRevealLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameCard.add(Box.createVerticalStrut(5));
        gameCard.add(correctNumberRevealLabel);

        gameCard.add(Box.createVerticalStrut(15));

        // Input Controls Row (TextField & Guess Button)
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        inputRow.setOpaque(false);

        guessInputField = new ModernTextField("Enter guess", 8);
        guessInputField.setPreferredSize(new Dimension(180, 50));
        guessInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && guessBtn.isEnabled()) {
                    handleGuessSubmitted();
                }
            }
        });

        guessBtn = new ModernButton("🎯 Guess", Theme.PRIMARY_ACCENT, Theme.PRIMARY_HOVER);
        guessBtn.setPreferredSize(new Dimension(140, 50));
        guessBtn.setFont(Theme.FONT_LABEL_BOLD);
        guessBtn.addActionListener(e -> handleGuessSubmitted());

        inputRow.add(guessInputField);
        inputRow.add(guessBtn);
        gameCard.add(inputRow);

        centerContainer.add(gameCard, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);
    }

    private void startNewGame() {
        Difficulty diff = (Difficulty) difficultyComboBox.getSelectedItem();
        if (diff == null) diff = Difficulty.MEDIUM;

        gameEngine.startNewRound(diff);

        // Reset UI Components
        rangeInstructionLabel.setText(String.format("Guess a number between %d and %d", diff.getMinRange(), diff.getMaxRange()));
        guessInputField.setText("");
        guessInputField.setEnabled(true);
        guessInputField.requestFocusInWindow();
        guessBtn.setEnabled(true);

        feedbackBannerPanel.setGradientColors(Theme.CARD_HEADER_BG, Theme.CARD_BG);
        feedbackBannerLabel.setText("Make your first guess!");
        feedbackBannerLabel.setForeground(Theme.TEXT_MAIN);
        correctNumberRevealLabel.setText("");

        updateStatsHeader();
        updateAttemptProgress();
    }

    private void handleGuessSubmitted() {
        String inputStr = guessInputField.getText().trim();
        if (inputStr.isEmpty()) {
            showFeedback("Please enter a valid number!", Theme.COLOR_LOW);
            soundManager.playClickSound();
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(inputStr);
        } catch (NumberFormatException ex) {
            showFeedback("Invalid input! Digits only.", Theme.COLOR_HIGH);
            soundManager.playClickSound();
            return;
        }

        GameEngine.GuessResult result = gameEngine.processGuess(guess);
        updateAttemptProgress();

        switch (result) {
            case TOO_HIGH:
                showFeedback("🔥 " + guess + " is Too High!", Theme.COLOR_HIGH);
                soundManager.playTooHighSound();
                guessInputField.setText("");
                guessInputField.requestFocusInWindow();
                break;

            case TOO_LOW:
                showFeedback("❄️ " + guess + " is Too Low!", Theme.COLOR_LOW);
                soundManager.playTooLowSound();
                guessInputField.setText("");
                guessInputField.requestFocusInWindow();
                break;

            case CORRECT:
                handleWin();
                break;

            case GAME_OVER_LOST:
                handleLoss();
                break;

            case INVALID_INPUT:
                Difficulty d = gameEngine.getDifficulty();
                showFeedback(String.format("Out of range! (%d - %d)", d.getMinRange(), d.getMaxRange()), Theme.COLOR_HIGH);
                soundManager.playClickSound();
                break;
        }
    }

    private void handleWin() {
        int earnedScore = gameEngine.getCurrentRoundScore();
        player.recordRound(true, earnedScore, gameEngine.getAttemptsUsed());

        // Save score record to leaderboard
        ScoreRecord record = new ScoreRecord(
                player.getName(),
                gameEngine.getDifficulty(),
                earnedScore,
                gameEngine.getAttemptsUsed()
        );
        scoreManager.addScore(record);

        showFeedback("🎉 Correct! You won " + earnedScore + " pts!", Theme.COLOR_CORRECT);
        soundManager.playVictorySound();
        endRoundControls();
        updateStatsHeader();

        // Prompt Play Again Option
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Congratulations " + player.getName() + "! You guessed the number in " + gameEngine.getAttemptsUsed() + " attempts.\n" +
                        "Score Earned: " + earnedScore + " pts\n\nWould you like to play another round?",
                "Victory!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private void handleLoss() {
        player.recordRound(false, 0, gameEngine.getAttemptsUsed());

        int target = gameEngine.getTargetNumber();
        showFeedback("💀 You Lost!", Theme.COLOR_HIGH);
        correctNumberRevealLabel.setText("The correct number was: " + target);
        soundManager.playDefeatSound();
        endRoundControls();
        updateStatsHeader();

        // Prompt Play Again Option
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Game Over! Out of attempts.\nThe correct number was: " + target + "\n\nWould you like to try again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private void endRoundControls() {
        guessInputField.setEnabled(false);
        guessBtn.setEnabled(false);
    }

    private void showFeedback(String message, Color accentColor) {
        feedbackBannerLabel.setText(message);
        feedbackBannerLabel.setForeground(accentColor);
        feedbackBannerPanel.setGradientColors(Theme.CARD_BG, accentColor.darker().darker());
    }

    private void updateAttemptProgress() {
        int used = gameEngine.getAttemptsUsed();
        int max = gameEngine.getDifficulty().getMaxAttempts();
        int remaining = gameEngine.getRemainingAttempts();

        attemptCounterLabel.setText(String.format("Attempts: %d / %d  (Remaining: %d)", used, max, remaining));

        int percentage = (int) (((double) remaining / max) * 100);
        attemptProgressBar.setValue(percentage);

        if (percentage > 50) {
            attemptProgressBar.setForeground(Theme.COLOR_CORRECT);
        } else if (percentage > 25) {
            attemptProgressBar.setForeground(Theme.COLOR_LOW);
        } else {
            attemptProgressBar.setForeground(Theme.COLOR_HIGH);
        }
    }

    private void updateStatsHeader() {
        roundBadgeLabel.setText("Round #" + (player.getTotalRoundsPlayed() + 1));
        totalScoreLabel.setText("Total Score: " + player.getTotalScore() + " pts");
        playerBadgeLabel.setText("👤 " + player.getName());
    }

    private void changePlayerName() {
        String newName = JOptionPane.showInputDialog(
                this,
                "Enter new Player Name:",
                player.getName()
        );
        if (newName != null && !newName.trim().isEmpty()) {
            player.setName(newName);
            updateStatsHeader();
        }
    }

    private void toggleAudio() {
        soundManager.toggleSound();
        if (soundManager.isSoundEnabled()) {
            soundToggleBtn.setText("🔊 Sound");
            soundManager.playClickSound();
        } else {
            soundToggleBtn.setText("🔇 Muted");
        }
    }
}
