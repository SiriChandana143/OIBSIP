# Java ATM Interface Simulation

A complete, working, interactive console-based **ATM Simulation** built using Java. This application simulates a realistic real-world ATM machine workflow—including card insertion prompts, user authentication with attempt limits, masked balance display, fund transfers with confirmation screens, dynamic transaction history tables, receipt printing, and simulated card ejection routines.

---

## Features

- **Realistic ATM Workflow**: Simulated ATM startup, system checks, interactive card insertion, user login, main menu navigation, confirmation prompts, receipt generation, and card ejection.
- **Authentication Security**: 3-attempt login limit with failure countdown and generic security responses.
- **Privacy Design**: Account numbers are masked (`****1001`), PINs are masked/hidden, and available balance is hidden from the main menu (only viewable upon explicit inquiry or post-transaction receipts).
- **Cash Operations**: Cash Withdrawal and Cash Deposit with input validation (numeric checks, non-negative amounts, overdraft prevention).
- **Fund Transfer**: Transfer funds between registered bank accounts with recipient account verification, self-transfer prevention, and an interactive confirmation screen (`[1] Confirm` / `[2] Cancel`).
- **Transaction History**: Dynamic formatted tabular record using Java `LocalDateTime` and `ArrayList<Transaction>`.
- **Receipt & Session Wrap-up**: Option to generate and print formatted ATM receipts upon session completion followed by an animated card ejection routine.

---

## Technology Restrictions & Stack

- **Language**: Standard Java (JDK 8+)
- **Architecture**: Object-Oriented Programming (OOP)
- **Input/Output**: `java.util.Scanner`, `System.out`
- **Data Structures**: `java.util.ArrayList`
- **Date/Time**: `java.time.LocalDateTime` & `java.time.format.DateTimeFormatter`

---

## Project Structure

```
ATM-Interface/
│
├── src/
│   ├── Main.java          # Entry point and sample account initializer
│   ├── ATM.java           # Console UI, menu flow, authentication, workflows
│   ├── Account.java       # Encapsulated account info, balance & transaction history
│   ├── Bank.java          # Account collection & authentication logic
│   └── Transaction.java   # Data model for individual financial transactions
│
├── README.md              # Project documentation
└── .vscode/
    └── settings.json      # VS Code configuration ("code-runner.runInTerminal": true)
```

---

## OOP Concepts Used

- **Encapsulation**: Private fields for sensitive state (`pin`, `balance`, `transactions`) exposed via validated methods.
- **Abstraction & Class Design**: 5 dedicated classes (`Main`, `ATM`, `Account`, `Bank`, `Transaction`) separating UI, business rules, and storage.
- **Collections**: `ArrayList<Account>` for multi-account support and `ArrayList<Transaction>` for dynamic activity tracking.

---

## How to Compile & Run

### Running from Project Root (`ATM-Interface/`)

1. Open VS Code Integrated Terminal, PowerShell, or Command Prompt.
2. Navigate to the project folder:
   ```bash
   cd ATM-Interface
   ```
3. Compile all Java source files:
   ```bash
   javac src/*.java
   ```
4. Run the application:
   ```bash
   java -cp src Main
   ```

### Running from `src/` directory

```bash
cd src
javac *.java
java Main
```

---

## Sample Test Credentials

| Account ID | User ID | PIN | Initial Balance |
| :--- | :--- | :--- | :--- |
| **ACC1001** | `user123` | `1234` | ₹10,000.00 |
| **ACC1002** | `user456` | `5678` | ₹15,000.00 |

---

## Example ATM Main Menu

```
============================================================
                    JAVA ATM
               SECURE BANKING SYSTEM
============================================================

                    MAIN MENU

------------------------------------------------------------

   [1]  Cash Withdrawal
   [2]  Cash Deposit
   [3]  Fund Transfer
   [4]  Transaction History
   [5]  Balance Inquiry
   [6]  Exit / Eject Card

------------------------------------------------------------

Please select an option:
> 
```
