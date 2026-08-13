package service;

import model.Question;
import util.QuestionBank;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamService {
    
    private List<Question> questions;
    private Map<Integer, Integer> selectedAnswers; // Question index -> Selected option index
    private int currentQuestionIndex;
    
    private int remainingSeconds;
    private final int EXAM_DURATION = 5 * 60; // 5 minutes in seconds
    
    private boolean examStarted;
    private boolean examSubmitted;
    
    private Timer timer;
    @SuppressWarnings("unused")
    private Runnable onTick;
    private Runnable onTimeUp;

    public ExamService() {
        resetExam();
    }
    
    public void resetExam() {
        stopTimer();
        this.questions = QuestionBank.getQuestions();
        this.selectedAnswers = new HashMap<>();
        this.currentQuestionIndex = 0;
        this.remainingSeconds = EXAM_DURATION;
        this.examStarted = false;
        this.examSubmitted = false;
    }
    
    public void startExam(Runnable onTick, Runnable onTimeUp) {
        if (examStarted) return;
        
        this.onTick = onTick;
        this.onTimeUp = onTimeUp;
        this.examStarted = true;
        this.examSubmitted = false;
        this.remainingSeconds = EXAM_DURATION;
        
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;
                if (onTick != null) {
                    onTick.run();
                }
                if (remainingSeconds <= 0) {
                    stopTimer();
                    examSubmitted = true;
                    if (onTimeUp != null) {
                        onTimeUp.run();
                    }
                }
            }
        });
        timer.start();
    }
    
    public void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public Question getCurrentQuestion() {
        if (questions == null || questions.isEmpty()) return null;
        return questions.get(currentQuestionIndex);
    }
    
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    public void setCurrentQuestionIndex(int idx) {
        if (idx >= 0 && idx < questions.size()) {
            currentQuestionIndex = idx;
        }
    }

    
    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        }
    }
    
    public void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        }
    }
    
    public void saveAnswer(int optionIndex) {
        selectedAnswers.put(currentQuestionIndex, optionIndex);
    }
    
    public Integer getSavedAnswer(int questionIndex) {
        return selectedAnswers.get(questionIndex);
    }
    
    public Map<Integer, Integer> getAllSelectedAnswers() {
        return selectedAnswers;
    }
    
    public int getAnsweredCount() {
        return selectedAnswers.size();
    }
    
    public int getRemainingSeconds() {
        return remainingSeconds;
    }
    
    public int getExamDuration() {
        return EXAM_DURATION;
    }
    
    public boolean isExamStarted() {
        return examStarted;
    }
    
    public boolean isExamSubmitted() {
        return examSubmitted;
    }
    
    public void setExamSubmitted(boolean submitted) {
        this.examSubmitted = submitted;
    }
}
