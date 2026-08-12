package com.numberquest.model;

/**
 * Represents the current player and maintains aggregate session statistics.
 */
public class Player {
    private String name;
    private int totalRoundsPlayed;
    private int gamesWon;
    private int gamesLost;
    private int totalScore;
    private int totalAttemptsMade;

    public Player(String name) {
        this.name = (name == null || name.trim().isEmpty()) ? "Player 1" : name.trim();
        this.totalRoundsPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.totalScore = 0;
        this.totalAttemptsMade = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public void recordRound(boolean won, int scoreEarned, int attemptsInRound) {
        this.totalRoundsPlayed++;
        if (won) {
            this.gamesWon++;
            this.totalScore += scoreEarned;
        } else {
            this.gamesLost++;
        }
        this.totalAttemptsMade += attemptsInRound;
    }

    public int getTotalRoundsPlayed() {
        return totalRoundsPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalAttemptsMade() {
        return totalAttemptsMade;
    }

    public double getWinRatePercentage() {
        if (totalRoundsPlayed == 0) return 0.0;
        return ((double) gamesWon / totalRoundsPlayed) * 100.0;
    }

    public double getAverageAttemptsPerRound() {
        if (totalRoundsPlayed == 0) return 0.0;
        return (double) totalAttemptsMade / totalRoundsPlayed;
    }

    public void resetStats() {
        this.totalRoundsPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.totalScore = 0;
        this.totalAttemptsMade = 0;
    }
}
