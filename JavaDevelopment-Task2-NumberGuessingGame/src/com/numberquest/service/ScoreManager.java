package com.numberquest.service;

import com.numberquest.model.Difficulty;
import com.numberquest.model.ScoreRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles persistent storage of leaderboard score records to local disk.
 */
public class ScoreManager {
    private static final String FILE_NAME = "leaderboard.txt";
    private static final int MAX_TOP_SCORES = 10;
    private final List<ScoreRecord> leaderboard;

    public ScoreManager() {
        this.leaderboard = new ArrayList<>();
        loadScores();
    }

    /**
     * Loads saved score records from disk. If the file doesn't exist, initializes default sample scores.
     */
    private synchronized void loadScores() {
        leaderboard.clear();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            populateDefaultScores();
            saveScores();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ScoreRecord record = ScoreRecord.fromCsv(line);
                if (record != null) {
                    leaderboard.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not read leaderboard file: " + e.getMessage());
        }

        Collections.sort(leaderboard);
        trimLeaderboard();
    }

    /**
     * Adds a new score record and saves it if it qualifies for top scores.
     */
    public synchronized boolean addScore(ScoreRecord record) {
        if (record == null) return false;

        leaderboard.add(record);
        Collections.sort(leaderboard);
        trimLeaderboard();
        saveScores();

        // Check if the newly added score is present in the leaderboard
        return leaderboard.contains(record);
    }

    /**
     * Returns an unmodifiable list of top score records.
     */
    public synchronized List<ScoreRecord> getTopScores() {
        return Collections.unmodifiableList(new ArrayList<>(leaderboard));
    }

    /**
     * Saves leaderboard entries to local CSV file.
     */
    private synchronized void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (ScoreRecord record : leaderboard) {
                writer.println(record.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving leaderboard to file: " + e.getMessage());
        }
    }

    private void trimLeaderboard() {
        if (leaderboard.size() > MAX_TOP_SCORES) {
            leaderboard.subList(MAX_TOP_SCORES, leaderboard.size()).clear();
        }
    }

    /**
     * Seeds initial benchmark scores for demonstrating leaderboard functionality.
     */
    private void populateDefaultScores() {
        leaderboard.add(new ScoreRecord("Alex", Difficulty.HARD, 1050, 3, "2026-08-01 14:20"));
        leaderboard.add(new ScoreRecord("Sam", Difficulty.MEDIUM, 480, 2, "2026-08-01 16:45"));
        leaderboard.add(new ScoreRecord("Jordan", Difficulty.HARD, 420, 4, "2026-08-02 09:15"));
        leaderboard.add(new ScoreRecord("Morgan", Difficulty.EASY, 220, 1, "2026-08-02 10:30"));
        leaderboard.add(new ScoreRecord("Taylor", Difficulty.MEDIUM, 180, 5, "2026-08-02 11:00"));
        Collections.sort(leaderboard);
    }
}
