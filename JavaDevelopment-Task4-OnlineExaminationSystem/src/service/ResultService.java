package service;

import model.ExamResult;
import model.Question;

import java.util.List;
import java.util.Map;

public class ResultService {
    
    public ExamResult calculateResult(ExamService examService, String submissionType) {
        List<Question> questions = examService.getQuestions();
        Map<Integer, Integer> answers = examService.getAllSelectedAnswers();
        
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        int incorrectAnswers = 0;
        int unansweredQuestions = 0;
        
        for (int i = 0; i < totalQuestions; i++) {
            Question q = questions.get(i);
            Integer selectedAnswer = answers.get(i);
            
            if (selectedAnswer == null) {
                unansweredQuestions++;
            } else if (selectedAnswer == q.getCorrectOptionIndex()) {
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }
        }
        
        int score = correctAnswers; // 1 point per correct answer
        int timeTaken = examService.getExamDuration() - examService.getRemainingSeconds();
        
        return new ExamResult(score, totalQuestions, correctAnswers, incorrectAnswers, 
                              unansweredQuestions, timeTaken, submissionType);
    }
}
