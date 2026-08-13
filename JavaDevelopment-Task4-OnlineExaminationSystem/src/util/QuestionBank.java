package util;

import model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question(
            "Which keyword is used to inherit a class in Java?",
            new String[]{"implements", "extends", "inherits", "super"},
            1 // extends
        ));

        questions.add(new Question(
            "Which of the following is not a primitive data type in Java?",
            new String[]{"int", "float", "String", "boolean"},
            2 // String
        ));

        questions.add(new Question(
            "What is the parent class of all classes in Java?",
            new String[]{"Main", "System", "Object", "Class"},
            2 // Object
        ));

        questions.add(new Question(
            "Which method is the entry point for any Java program?",
            new String[]{"start()", "run()", "main()", "init()"},
            2 // main()
        ));

        questions.add(new Question(
            "Which concept in Java is achieved by using interfaces?",
            new String[]{"Encapsulation", "Polymorphism", "Multiple Inheritance", "Abstraction"},
            2 // Multiple Inheritance (Context: Interfaces allow a form of multiple inheritance)
        ));

        questions.add(new Question(
            "What does the 'final' keyword signify when applied to a variable?",
            new String[]{"The variable can be modified.", "The variable's value cannot be changed.", "The variable is static.", "The variable is volatile."},
            1 // The variable's value cannot be changed.
        ));

        questions.add(new Question(
            "Which of these exceptions is checked?",
            new String[]{"NullPointerException", "ArrayIndexOutOfBoundsException", "IOException", "ArithmeticException"},
            2 // IOException
        ));

        questions.add(new Question(
            "What is the default value of a local variable in Java?",
            new String[]{"null", "0", "false", "No default value, it must be initialized"},
            3 // No default value
        ));

        questions.add(new Question(
            "Which collection class allows you to associate its elements with key-value pairs?",
            new String[]{"ArrayList", "LinkedList", "HashSet", "HashMap"},
            3 // HashMap
        ));

        questions.add(new Question(
            "Which Swing component is used to group radio buttons so only one can be selected?",
            new String[]{"JPanel", "ButtonGroup", "JRadioButtonGroup", "RadioGroup"},
            1 // ButtonGroup
        ));

        return questions;
    }
}
