package com.numberquest.model;

/**
 * Enum representing game difficulty levels with their corresponding 
 * number ranges, attempt limits, base points, and multipliers.
 */
public enum Difficulty {
    EASY("Easy", 1, 50, 10, 100, 1.0),
    MEDIUM("Medium", 1, 100, 7, 250, 1.5),
    HARD("Hard", 1, 200, 5, 500, 2.0);

    private final String displayName;
    private final int minRange;
    private final int maxRange;
    private final int maxAttempts;
    private final int baseScore;
    private final double scoreMultiplier;

    Difficulty(String displayName, int minRange, int maxRange, int maxAttempts, int baseScore, double scoreMultiplier) {
        this.displayName = displayName;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.maxAttempts = maxAttempts;
        this.baseScore = baseScore;
        this.scoreMultiplier = scoreMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    @Override
    public String toString() {
        return displayName + " (" + minRange + "-" + maxRange + ", " + maxAttempts + " attempts)";
    }
}
