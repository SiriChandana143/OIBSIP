# 🎯 Number Quest – Interactive Java Guessing Game

> **OIBSIP - Oasis InfoByte Java Development Internship | Task Project**  
> A polished, Object-Oriented Desktop Application built with Java & Swing GUI featuring multi-difficulty levels, persistent leaderboard scoring, session analytics, custom UI components, and synthesized audio feedback.

---

## 📌 Project Overview

**Number Quest** is a feature-rich desktop guessing game developed in Java for the **OIBSIP (Oasis InfoByte)** Java Development Internship. Unlike basic command-line guessing tutorials, **Number Quest** offers a complete desktop application experience with custom modern dark-mode rendering, dynamic attempt progress indicators, file-based leaderboard persistence, player analytics, and zero-dependency procedural audio synthesis using the Java Sound API.

This project demonstrates strong software engineering practices, clean Object-Oriented Design (OOP), separation of concerns (MVC architecture), event-driven Swing programming, file stream I/O, and UI component styling.

---

## ✨ Features

- 🎮 **Core Gameplay Mechanics**:
  - Dynamic random target generation (`java.util.Random`).
  - Real-time feedback alerts: **"Too High!"**, **"Too Low!"**, and **"Correct!"**.
  - Attempt counter with color-coded visual progress bar (`JProgressBar`).
  - Number range validation & edge case input handling.
- ⚙️ **Multi-Difficulty Levels**:
  - **Easy**: Range `1–50`, `10 attempts`, `1.0x score multiplier`.
  - **Medium**: Range `1–100`, `7 attempts`, `1.5x score multiplier`.
  - **Hard**: Range `1–200`, `5 attempts`, `2.0x score multiplier`.
- 🧮 **Dynamic Scoring Algorithm**:
  - Calculates score based on difficulty base points, remaining attempts, and speed bonuses (bonus points for fast solving under 15 seconds).
- 🏆 **Local Leaderboard & File Persistence**:
  - Saves top 10 player scores locally using standard Java File I/O (`leaderboard.txt`).
  - Displays rank badges (🥇 Gold, 🥈 Silver, 🥉 Bronze) inside a customized `JTable` modal dialog.
- 📊 **Detailed Player Session Analytics**:
  - Tracks total rounds played, games won, games lost, overall win rate (`%`), total score, and average attempts per round.
  - Reset stats option with user confirmation prompts.
- 🔊 **Procedural Audio Synthesis**:
  - Built-in audio generator (`SoundManager`) using `javax.sound.sampled` to synthesize custom frequency tones for guesses, wins, losses, and clicks—requiring zero external `.wav` asset files.
- 🎨 **Modern Dark UI Design System**:
  - Styled with custom color tokens, anti-aliased graphics rendering (`Graphics2D`), custom rounded button highlights (`ModernButton`), focus glow inputs (`ModernTextField`), and linear gradient panels (`GradientPanel`).

---

## 🛠️ Technologies & Concepts Used

- **Programming Language**: Java (JDK 8 or higher)
- **GUI Framework**: Java Swing (`javax.swing.*`), Java AWT (`java.awt.*`)
- **Data Structures & Collections**: Java Collections Framework (`List`, `ArrayList`, `Collections.sort`)
- **File I/O & Persistence**: File streams (`BufferedReader`, `PrintWriter`, `File`), CSV serialization
- **Audio Processing**: Java Sound API (`javax.sound.sampled`)
- **Architecture**: Object-Oriented Programming (OOP), MVC-pattern separation, Custom Swing component painting

---

## 📁 Project Structure

```
OIBSIP/
└── Number-Quest-Java-Game/
    ├── src/
    │   └── com/
    │       └── numberquest/
    │           ├── Main.java                        # Application launcher & EDT initialization
    │           ├── model/
    │           │   ├── Difficulty.java              # Enum for range, attempts, and multipliers
    │           │   ├── GameEngine.java              # Gameplay rules, state, and score math
    │           │   ├── Player.java                  # Player profile & aggregate statistics
    │           │   └── ScoreRecord.java             # Data model & CSV parser for leaderboard entries
    │           ├── service/
    │           │   ├── ScoreManager.java            # Local score persistence file handler
    │           │   └── SoundManager.java            # Pure Java procedural tone synthesizer
    │           └── ui/
    │               ├── MainWindow.java              # Main application frame & controller
    │               ├── LeaderboardDialog.java       # Custom modal for top scores table
    │               ├── StatsDialog.java             # Custom modal for session analytics grid
    │               ├── theme/
    │               │   └── Theme.java               # Design tokens, color palette & typography
    │               └── components/
    │                   ├── ModernButton.java        # Custom painted hover-animated button
    │                   ├── ModernTextField.java     # Custom painted input field with glow effect
    │                   └── GradientPanel.java       # Linear gradient background panel
    ├── screenshots/
    │   └── README.md                                # Screenshot guidelines
    ├── compile_and_run.bat                          # Windows build script
    ├── compile_and_run.sh                           # Linux/macOS build script
    ├── .gitignore                                   # Git ignore rules for Java builds
    ├── OIBSIP_GitHub_and_LinkedIn_Guide.md          # GitHub repository & LinkedIn showcase guide
    └── README.md                                    # Comprehensive portfolio documentation
```

---

## 🚀 How To Run

### Prerequisites
- Install **Java Development Kit (JDK 8 or higher)**.
- Verify installation by running:
  ```bash
  java -version
  javac -version
  ```

### Method 1: Using Automatic Build Scripts

- **Windows**:
  Double-click `compile_and_run.bat` or run in terminal:
  ```cmd
  compile_and_run.bat
  ```

- **Linux / macOS**:
  Make script executable and run:
  ```bash
  chmod +x compile_and_run.sh
  ./compile_and_run.sh
  ```

### Method 2: Manual Terminal Compilation

1. Navigate to the project directory:
   ```bash
   cd Number-Quest-Java-Game
   ```
2. Compile Java source code into the `bin` output folder:
   ```bash
   javac -d bin src/com/numberquest/Main.java src/com/numberquest/model/*.java src/com/numberquest/service/*.java src/com/numberquest/ui/*.java src/com/numberquest/ui/theme/*.java src/com/numberquest/ui/components/*.java
   ```
3. Execute the compiled application:
   ```bash
   java -cp bin com.numberquest.Main
   ```

---

## 📸 Screenshots

*(Place application UI screenshots in the `screenshots/` directory)*

| Main Gameplay Window | Hall of Fame Leaderboard | Session Analytics |
|:---:|:---:|:---:|
| ![Gameplay](screenshots/main_gameplay.png) | ![Leaderboard](screenshots/leaderboard_dialog.png) | ![Stats](screenshots/session_stats.png) |

---

## 🎓 Learning Outcomes & Concepts Mastered

1. **Object-Oriented Programming (OOP)**: Applied principles of Encapsulation, Abstraction, and Polymorphism across modular packages (`model`, `service`, `ui`).
2. **Event-Driven Swing UI Programming**: Handled mouse listeners, key press events, custom component painting (`paintComponent`), and thread safety (`SwingUtilities.invokeLater`).
3. **File Handling & Serialization**: Structured lightweight CSV reading/writing with exception handling to achieve cross-session state persistence.
4. **Custom Audio Synthesis**: Understood digital audio signals by generating sine-wave byte buffers directly fed to `SourceDataLine`.
5. **UI/UX Styling in Java**: Created a dark design system with custom borders, rounded edges, focus highlights, and color tokens.

---

## 💡 Future Enhancements

- 🌐 Add Network Multiplayer / Global REST API Leaderboard backend.
- 💡 Add Smart AI Hint System (e.g., "Divisible by 5", "Is Prime", "Binary Search Range").
- 🏆 Add Achievements & Unlockable Badges ("Speed Demon", "First Try Wonder").

---

## 📝 GitHub Repository Setup Suggestions

- **Repository Name**: `OIBSIP`
- **Project Folder**: `Task-2_Number-Quest-Java-Game`
- **Short Description**: *"A polished, Object-Oriented Java Swing guessing game featuring multi-difficulty modes, local leaderboard persistence, procedural audio synthesis, and session analytics. Developed for OIBSIP."*
- **Topics**: `java`, `java-swing`, `oibsip`, `oasis-infobyte`, `guessing-game`, `object-oriented-programming`, `file-handling`, `desktop-app`
