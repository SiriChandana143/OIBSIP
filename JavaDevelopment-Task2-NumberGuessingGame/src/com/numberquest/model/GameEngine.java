package com.numberquest.model;

import java.util.Random;

/**
 * Core game engine responsible for random target generation,
 * guess evaluation, state tracking, and score calculation logic.
 */
public class GameEngine {

    public enum GuessResult {
        TOO_HIGH,
        TOO_LOW,
        CORRECT,
        INVALID_INPUT,
        GAME_OVER_LOST
    }

    private final Random random;
    private Difficulty difficulty;
    private int targetNumber;
    private int attemptsUsed;
    private boolean isGameOver;
    private boolean isWon;
    private int currentRoundScore;
    private long roundStartTimeMs;

    public GameEngine() {
        this.random = new Random();
        this.difficulty = Difficulty.MEDIUM;
        startNewRound(this.difficulty);
    }

    /**
     * Initializes a new game round with target difficulty.
     */
    public void startNewRound(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.attemptsUsed = 0;
        this.isGameOver = false;
        this.isWon = false;
        this.currentRoundScore = 0;
        this.roundStartTimeMs = System.currentTimeMillis();

        // Generate target number within range [min, max]
        int range = difficulty.getMaxRange() - difficulty.getMinRange() + 1;
        this.targetNumber = difficulty.getMinRange() + random.nextInt(range);
    }

    /**
     * Processes a user guess and returns the resulting status feedback.
     */
    public GuessResult processGuess(int guess) {
        if (isGameOver) {
            return isWon ? GuessResult.CORRECT : GuessResult.GAME_OVER_LOST;
        }

        // Validate range
        if (guess < difficulty.getMinRange() || guess > difficulty.getMaxRange()) {
            return GuessResult.INVALID_INPUT;
        }

        attemptsUsed++;

        if (guess == targetNumber) {
            isWon = true;
            isGameOver = true;
            calculateScore();
            return GuessResult.CORRECT;
        }

        if (attemptsUsed >= difficulty.getMaxAttempts()) {
            isWon = false;
            isGameOver = true;
            currentRoundScore = 0;
            return GuessResult.GAME_OVER_LOST;
        }

        return (guess > targetNumber) ? GuessResult.TOO_HIGH : GuessResult.TOO_LOW;
    }

    /**
     * Calculates score based on difficulty base points, remaining attempts,
     * multiplier, and quick solve bonus.
     */
    private void calculateScore() {
        int remainingAttempts = getRemainingAttempts();
        long timeTakenSeconds = (System.currentTimeMillis() - roundStartTimeMs) / 1000;

        // Base calculation formula
        double rawScore = difficulty.getBaseScore() + (remainingAttempts * 50.0);
        rawScore *= difficulty.getScoreMultiplier();

        // Speed bonus: up to 100 bonus points if solved under 15 seconds
        if (timeTakenSeconds < 15) {
            rawScore += (15 - timeTakenSeconds) * 10;
        }

        this.currentRoundScore = Math.max(10, (int) Math.round(rawScore));
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public int getAttemptsUsed() {
        return attemptsUsed;
    }

    public int getRemainingAttempts() {
        return Math.max(0, difficulty.getMaxAttempts() - attemptsUsed);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isWon() {
        return isWon;
    }

    public int getCurrentRoundScore() {
        return currentRoundScore;
    }
}
