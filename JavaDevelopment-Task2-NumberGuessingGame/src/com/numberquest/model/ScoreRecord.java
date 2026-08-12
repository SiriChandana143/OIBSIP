package com.numberquest.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates a score record for leaderboard storage.
 */
public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String playerName;
    private final Difficulty difficulty;
    private final int score;
    private final int attemptsUsed;
    private final String timestamp;

    public ScoreRecord(String playerName, Difficulty difficulty, int score, int attemptsUsed) {
        this.playerName = (playerName == null || playerName.trim().isEmpty()) ? "Anonymous" : playerName.trim();
        this.difficulty = difficulty;
        this.score = score;
        this.attemptsUsed = attemptsUsed;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public ScoreRecord(String playerName, Difficulty difficulty, int score, int attemptsUsed, String timestamp) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.score = score;
        this.attemptsUsed = attemptsUsed;
        this.timestamp = timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getScore() {
        return score;
    }

    public int getAttemptsUsed() {
        return attemptsUsed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Serializes record to a CSV line format for file persistence.
     */
    public String toCsv() {
        return String.format("%s,%s,%d,%d,%s", 
                playerName.replace(",", " "), 
                difficulty.name(), 
                score, 
                attemptsUsed, 
                timestamp);
    }

    /**
     * Parses CSV line into ScoreRecord.
     */
    public static ScoreRecord fromCsv(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",");
        if (parts.length < 5) return null;
        try {
            String name = parts[0].trim();
            Difficulty diff = Difficulty.valueOf(parts[1].trim());
            int score = Integer.parseInt(parts[2].trim());
            int attempts = Integer.parseInt(parts[3].trim());
            String time = parts[4].trim();
            return new ScoreRecord(name, diff, score, attempts, time);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int compareTo(ScoreRecord other) {
        // Higher scores come first
        return Integer.compare(other.score, this.score);
    }
}
