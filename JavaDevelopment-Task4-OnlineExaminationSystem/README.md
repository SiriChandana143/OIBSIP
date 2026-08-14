# Online Examination System

## Oasis Infobyte Java Development Internship – Task 4

### Project Title

**Online Examination System**

### Internship Domain

**Java Development**

### Task Number

**Task 4**

---

## 1. Objective

The objective of this project is to develop a desktop-based **Online Examination System** using Java. The application allows a student to log in, view examination instructions, attend a timed multiple-choice examination, navigate between questions, submit answers, and view the examination result.

The project demonstrates the use of **Java Swing, Object-Oriented Programming, event handling, collections, and timer-based functionality** to create an interactive examination application.

---

## 2. Project Description

The Online Examination System is a Java Swing desktop application designed to simulate an online examination environment.

The application provides:

* Student login
* Student profile management
* Examination instructions
* Multiple-choice questions
* Four options for each question
* Question navigation
* Answer tracking
* Five-minute examination timer
* Automatic submission when the time expires
* Manual examination submission
* Answered and unanswered question tracking
* Result calculation
* Score and percentage display
* Correct, incorrect, and unanswered answer statistics
* Time taken display
* Modern graphical user interface

---

## 3. Features

### Student Login

The system provides a login screen for the student.

**Default test credentials:**

* **Username:** `student`
* **Password:** `student123`

### Profile Management

The student can view and update the display name and password during the application session.

### Examination Instructions

Before starting the examination, the student can view the examination instructions.

### Timed Examination

* Examination duration: **5 minutes**
* A countdown timer is displayed during the examination.
* The examination is automatically submitted when the timer reaches zero.

### Multiple-Choice Questions

* The examination contains **10 questions**.
* Each question contains **4 options**.
* The student can select one answer for each question.

### Question Navigator

A question navigator allows the student to:

* Move between questions
* Jump directly to a question
* Identify answered questions
* Identify the current question
* Identify unanswered questions

### Result Calculation

After submission, the system calculates:

* Total questions
* Correct answers
* Incorrect answers
* Unanswered questions
* Score
* Percentage
* Time taken
* Submission type

The submission type can be:

* Manual
* Automatic – Time Expired

---

## 4. Technologies Used

* **Java**
* **Java Swing**
* **Java AWT**
* **Object-Oriented Programming (OOP)**
* **Java Collections Framework**
* **Java Swing Timer**
* **Event Handling**
* **Visual Studio Code**
* **JDK**

---

## 5. Project Structure

```text
JavaDevelopment-Task4-OnlineExaminationSystem/
│
├── src/
│   ├── Main.java
│   │
│   ├── model/
│   │   ├── User.java
│   │   ├── Question.java
│   │   └── ExamResult.java
│   │
│   ├── service/
│   │   ├── AuthenticationService.java
│   │   ├── ExamService.java
│   │   └── ResultService.java
│   │
│   ├── ui/
│   │   ├── MainFrame.java
│   │   ├── LoginPanel.java
│   │   ├── ProfilePanel.java
│   │   ├── InstructionsPanel.java
│   │   ├── ExamPanel.java
│   │   ├── ResultPanel.java
│   │   └── components/
│   │       ├── CustomPasswordField.java
│   │       ├── CustomTextField.java
│   │       ├── ModernButton.java
│   │       └── OptionCard.java
│   │
│   └── util/
│       ├── QuestionBank.java
│       └── UIUtils.java
│
├── bin/
├── .vscode/
├── run.bat
└── README.md
```

---

## 6. Main Components

### Model

The `model` package contains the data classes used by the application.

* `User.java` – Stores student information.
* `Question.java` – Stores question text, options, and the correct answer.
* `ExamResult.java` – Stores and calculates examination results.

### Service

The `service` package contains the main application logic.

* `AuthenticationService.java` – Handles student authentication and profile updates.
* `ExamService.java` – Handles questions, selected answers, navigation, and the examination timer.
* `ResultService.java` – Calculates the final examination result.

### UI

The `ui` package contains the graphical user interface.

* `LoginPanel.java` – Student login interface.
* `ProfilePanel.java` – Student profile interface.
* `InstructionsPanel.java` – Examination instructions.
* `ExamPanel.java` – Main examination interface.
* `ResultPanel.java` – Displays examination results.
* `MainFrame.java` – Controls the application's main window and screen navigation.

### Utilities

The `util` package contains supporting classes.

* `QuestionBank.java` – Stores the examination questions.
* `UIUtils.java` – Provides common UI styles, fonts, colors, and components.

---

## 7. Steps Performed

1. Designed the application structure using Java.
2. Created model classes for users, questions, and examination results.
3. Implemented student authentication.
4. Created the graphical user interface using Java Swing.
5. Added examination instructions.
6. Created a question bank containing multiple-choice questions.
7. Implemented question navigation.
8. Implemented answer selection and tracking.
9. Added a five-minute countdown timer.
10. Implemented automatic submission when the examination time expires.
11. Implemented manual examination submission.
12. Added result calculation for correct, incorrect, and unanswered questions.
13. Added percentage and time-taken calculations.
14. Added a result display screen.
15. Added a batch file for compiling and running the application.

---

## 8. How to Run the Project

### Prerequisites

Make sure Java JDK is installed and configured on the system.

Check the Java version using:

```bash
java -version
```

Also verify the Java compiler:

```bash
javac -version
```

### Method 1 – Using `run.bat`

On Windows:

1. Open the project folder.
2. Double-click `run.bat`.
3. The project will be compiled automatically.
4. After successful compilation, the application will start.

### Method 2 – Using Command Prompt

Open Command Prompt inside the project folder and run:

```bash
javac -d bin src\model\*.java src\util\*.java src\service\*.java src\ui\components\*.java src\ui\*.java src\Main.java
```

Then run:

```bash
java -cp bin Main
```

---

## 9. Application Flow

```text
Start Application
       ↓
   Student Login
       ↓
   Student Profile
       ↓
Examination Instructions
       ↓
   Start Examination
       ↓
Answer MCQ Questions
       ↓
Question Navigation
       ↓
Manual Submit / Time Expires
       ↓
 Calculate Result
       ↓
   Display Result
```

---

## 10. Outcome

The project successfully implements a functional desktop-based Online Examination System using Java Swing.

It demonstrates practical implementation of:

* Java OOP concepts
* GUI development
* Event-driven programming
* Collections
* Authentication
* Timer-based functionality
* Multiple-choice examination logic
* Result calculation
* User interaction and navigation

The application provides an interactive examination experience with automatic time management and detailed result reporting.

---

## 11. Demo Video

**Demo Video:**
*Add your YouTube or LinkedIn demo video link here.*

The demonstration video covers the main application workflow, including login, instructions, examination, timer, question navigation, submission, and result display.

---

## 12. Internship Submission

**Organization:** Oasis Infobyte
**Program:** AICTE Oasis Infobyte Internship Program
**Domain:** Java Development
**Task:** Task 4 – Online Examination System
**Student:** Sadula Siri Chandana

---

## 13. GitHub Repository

**GitHub Repository:**
*Add your GitHub repository link here.*

---

## 14. LinkedIn Post

**LinkedIn Project Post:**
*Add your LinkedIn post link here.*

---

## 15. Conclusion

The Online Examination System was developed as part of the Oasis Infobyte Java Development Internship. The project provided practical experience in developing a Java desktop application, designing graphical user interfaces, handling user interaction, implementing timers, managing examination data, and calculating results.
