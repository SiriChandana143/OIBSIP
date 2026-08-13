package model;

public class ExamResult {
    private int score;
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private int unansweredQuestions;
    private int timeTakenSeconds;
    private String submissionType; // "Manual" or "Automatic — Time Expired"

    public ExamResult(int score, int totalQuestions, int correctAnswers, int incorrectAnswers, 
                      int unansweredQuestions, int timeTakenSeconds, String submissionType) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.unansweredQuestions = unansweredQuestions;
        this.timeTakenSeconds = timeTakenSeconds;
        this.submissionType = submissionType;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public int getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public int getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public double getPercentage() {
        if (totalQuestions == 0) return 0;
        return ((double) score / totalQuestions) * 100;
    }

    public String getFormattedTimeTaken() {
        int minutes = timeTakenSeconds / 60;
        int seconds = timeTakenSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
