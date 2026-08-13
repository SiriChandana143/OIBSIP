import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Scanner scanner;
    private Account currentAccount;

    // Receipt width is computed dynamically at print-time — no hardcoded constants.

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }

    public void start() {
        showStartupScreen();
        if (!insertCardSimulation()) {
            return;
        }

        if (!authenticateUserSession()) {
            return;
        }

        showMainMenuLoop();
        terminateSession();
    }

    private void showStartupScreen() {
        System.out.println("============================================================");
        System.out.println("                    JAVA ATM");
        System.out.println("               SECURE BANKING SYSTEM");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Initializing ATM...");
        sleep(300);
        System.out.println("Checking system...");
        sleep(300);
        System.out.println("Connecting to banking network...");
        sleep(400);
        System.out.println();
        System.out.println("SYSTEM READY");
        System.out.println();
        System.out.println("============================================================");
        System.out.println();
    }

    private boolean insertCardSimulation() {
        System.out.println("Please press ENTER to insert your card...");
        scanner.nextLine();

        System.out.println("------------------------------------------------------------");
        System.out.println("CARD DETECTED");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Reading card...");
        sleep(300);
        System.out.println("Verifying card...");
        sleep(300);
        System.out.println();
        System.out.println("Card accepted.");
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        return true;
    }

    private boolean authenticateUserSession() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.println("============================================================");
            System.out.println("                     USER LOGIN");
            System.out.println("============================================================");
            System.out.println();
            System.out.print("Enter User ID:\n> ");
            String userId = scanner.nextLine().trim();

            System.out.print("\nEnter PIN:\n> ");
            String pin;
            if (System.console() != null) {
                char[] pinChars = System.console().readPassword();
                pin = new String(pinChars).trim();
            } else {
                pin = scanner.nextLine().trim();
            }

            System.out.println("\nAuthenticating...");
            sleep(400);

            currentAccount = bank.authenticateUser(userId, pin);

            if (currentAccount != null) {
                System.out.println();
                System.out.println("============================================================");
                System.out.println("             AUTHENTICATION SUCCESSFUL");
                System.out.println("============================================================");
                System.out.println();
                System.out.println("Account verified.");
                System.out.println("Secure session established.");
                System.out.println();
                System.out.println("Welcome to Java ATM.");
                System.out.println();
                System.out.println("============================================================");
                sleep(400);
                return true;
            } else {
                attempts++;
                System.out.println("\nInvalid User ID or PIN.");
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                    System.out.println();
                }
            }
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    ACCESS DENIED");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Maximum authentication attempts exceeded.");
        System.out.println();
        System.out.println("Your ATM session has been terminated.");
        System.out.println();
        System.out.println("Please contact your bank if you require assistance.");
        System.out.println();
        System.out.println("============================================================");
        return false;
    }

    private void showMainMenuLoop() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                    JAVA ATM");
            System.out.println("               SECURE BANKING SYSTEM");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("                    MAIN MENU");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("   [1]  Cash Withdrawal");
            System.out.println("   [2]  Cash Deposit");
            System.out.println("   [3]  Fund Transfer");
            System.out.println("   [4]  Transaction History");
            System.out.println("   [5]  Balance Inquiry");
            System.out.println("   [6]  Exit / Eject Card");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.print("Please select an option:\n> ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    handleCashWithdrawal();
                    running = askAnotherTransaction();
                    break;
                case "2":
                    handleCashDeposit();
                    running = askAnotherTransaction();
                    break;
                case "3":
                    handleFundTransfer();
                    running = askAnotherTransaction();
                    break;
                case "4":
                    handleTransactionHistory();
                    running = askAnotherTransaction();
                    break;
                case "5":
                    handleBalanceInquiry();
                    running = askAnotherTransaction();
                    break;
                case "6":
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid selection. Please choose an available option.");
                    sleep(300);
            }
        }
    }

    private void handleBalanceInquiry() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    BALANCE INQUIRY");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Processing request...");
        sleep(300);
        System.out.println("Verifying account...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.printf("Account Number    : %s%n", currentAccount.getMaskedAccountId());
        System.out.printf("Available Balance : ₹%,.2f%n", currentAccount.getBalance());
        System.out.println();
        System.out.println("------------------------------------------------------------");

        // Record balance inquiry transaction for receipt tracking
        currentAccount.addTransaction(new Transaction("BALANCE ENQUIRY", currentAccount.getBalance(), "Balance Inquiry", currentAccount.getBalance(), "SUCCESS"));
    }

    private void handleCashWithdrawal() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                  CASH WITHDRAWAL");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please enter the amount you would like to withdraw.");
        System.out.println();
        System.out.println("Enter 0 to cancel.");
        System.out.println();
        System.out.print("Amount:\n₹ > ");

        double amount = parseAmountInput();
        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            System.out.println("\nTransaction cancelled.");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING REQUEST");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Checking available funds...");
        sleep(300);
        System.out.println("Verifying account...");
        sleep(300);
        System.out.println("Authorizing transaction...");
        sleep(300);
        System.out.println("Processing cash...");
        sleep(300);
        System.out.println();
        System.out.println("Please wait...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.getBalance() < amount) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("               TRANSACTION DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Insufficient Funds.");
            System.out.println();
            System.out.println("The requested amount cannot be withdrawn.");
            System.out.println();
            System.out.println("No money has been deducted.");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            return;
        }

        if (currentAccount.withdraw(amount)) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("              TRANSACTION APPROVED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Please collect your cash.");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.printf("Amount Withdrawn : ₹%,.2f%n", amount);
            System.out.printf("Remaining Balance: ₹%,.2f%n", currentAccount.getBalance());
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("Transaction completed successfully.");
        }
    }

    private void handleCashDeposit() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                   CASH DEPOSIT");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please enter the amount you would like to deposit.");
        System.out.println();
        System.out.println("Enter 0 to cancel.");
        System.out.println();
        System.out.print("Amount:\n₹ > ");

        double amount = parseAmountInput();
        if (amount < 0) {
            return;
        }

        if (amount == 0) {
            System.out.println("\nTransaction cancelled.");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING DEPOSIT");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Verifying amount...");
        sleep(300);
        System.out.println("Updating account...");
        sleep(300);
        System.out.println("Updating balance...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.deposit(amount)) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                DEPOSIT SUCCESSFUL");
            System.out.println("============================================================");
            System.out.println();
            System.out.printf("Amount Deposited : ₹%,.2f%n", amount);
            System.out.printf("Updated Balance  : ₹%,.2f%n", currentAccount.getBalance());
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("Transaction completed successfully.");
        }
    }

    private void handleFundTransfer() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                    FUND TRANSFER");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.print("Enter recipient User ID or Account Number:\n> ");
        String recipientId = scanner.nextLine().trim();

        if (recipientId.isEmpty()) {
            System.out.println("\nPlease enter a valid User ID or Account Number.");
            return;
        }

        if (recipientId.equals("0")) {
            System.out.println("\nTransfer cancelled.");
            return;
        }

        Account recipient = bank.findAccountByUserId(recipientId);
        if (recipient == null) {
            recipient = bank.findAccountByNumber(recipientId);
        }

        if (recipient == null) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Recipient account not found.");
            System.out.println();
            System.out.println("Please verify the account number and try again.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        if (recipient.getAccountId().equalsIgnoreCase(currentAccount.getAccountId())) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                  INVALID TRANSFER");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("You cannot transfer money to your own account.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        System.out.println();
        System.out.println("✓ Recipient Account Found");
        System.out.printf("  Recipient      : %s%n", recipient.getUserId());
        System.out.printf("  Account Number : %s%n", recipient.getMaskedAccountId());
        System.out.println();
        System.out.print("Enter amount to transfer: ₹\n> ");

        double amount = parseTransferAmountInput();
        if (amount <= 0) {
            return;
        }

        if (currentAccount.getBalance() < amount) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER DECLINED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("Insufficient balance.");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        // Transfer Confirmation Screen
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 TRANSFER CONFIRMATION");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.printf("From Account : %s%n", currentAccount.getMaskedAccountId());
        System.out.printf("To Account   : %s%n", recipient.getMaskedAccountId());
        System.out.printf("Amount       : ₹%,.2f%n", amount);
        System.out.println();
        System.out.print("Confirm transfer? (Y/N):\n> ");

        String confirmChoice = scanner.nextLine().trim();
        if (!confirmChoice.equalsIgnoreCase("Y") && !confirmChoice.equalsIgnoreCase("YES") && !confirmChoice.equals("1")) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("                 TRANSFER CANCELLED");
            System.out.println("============================================================");
            System.out.println();
            System.out.println("No money has been transferred.");
            System.out.println();
            System.out.println("============================================================");
            return;
        }

        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                 PROCESSING TRANSFER");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Verifying recipient...");
        sleep(300);
        System.out.println("Checking available funds...");
        sleep(300);
        System.out.println("Authorizing transaction...");
        sleep(300);
        System.out.println("Transferring funds...");
        sleep(300);
        System.out.println();
        System.out.println("Please wait...");
        sleep(300);
        System.out.println();
        System.out.println("------------------------------------------------------------");

        if (currentAccount.transfer(recipient, amount)) {
            System.out.println();
            System.out.println("TRANSFER SUCCESSFUL");
            System.out.println("------------------------------------------------------------");
        }
    }

    private void handleTransactionHistory() {
        System.out.println();
        currentAccount.printTransactionHistory();
    }

    private double parseAmountInput() {
        try {
            String input = scanner.nextLine().trim();
            double amount = Double.parseDouble(input);
            if (amount < 0) {
                System.out.println("\nInvalid input. Amount cannot be negative.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid numerical amount.");
            return -1;
        }
    }

    private double parseTransferAmountInput() {
        try {
            String input = scanner.nextLine().trim();
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                System.out.println("\nPlease enter a valid transfer amount.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid transfer amount.");
            return -1;
        }
    }

    private boolean askAnotherTransaction() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Transaction completed.");
        System.out.println();
        System.out.println("Would you like to perform another transaction?");
        System.out.println();
        System.out.println("[1] Yes");
        System.out.println("[2] No");
        System.out.println();
        System.out.print("Select:\n> ");

        String choice = scanner.nextLine().trim();
        return choice.equals("1") || choice.equalsIgnoreCase("Y");
    }

    private void terminateSession() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    PRINT RECEIPT?");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("[1] Yes");
        System.out.println("[2] No");
        System.out.println();
        System.out.print("Select:\n> ");

        String receiptChoice = scanner.nextLine().trim();
        if (receiptChoice.equals("1") || receiptChoice.equalsIgnoreCase("Y")) {
            printReceipt();
        }

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 ENDING ATM SESSION");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Please wait...");
        sleep(400);
        System.out.println();
        System.out.println("Preparing card ejection...");
        sleep(400);
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("PLEASE TAKE YOUR CARD.");
        System.out.println();
        System.out.println("CARD EJECTED SUCCESSFULLY.");
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Thank you for using Java ATM.");
        System.out.println();
        System.out.println("Please make sure you have collected your card and cash.");
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                 SESSION TERMINATED");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Goodbye!");
    }

    // =========================================================================
    // RESPONSIVE BOX-DRAWING RECEIPT  —  adapts to the visible terminal width
    // =========================================================================

    /**
     * Returns the terminal's visible column count.
     * Priority: $COLUMNS env-var (set by most Unix/VS Code terminals)
     *           → safe default of 100 for Windows VS Code terminals.
     */
    private int getTerminalWidth() {
        try {
            String cols = System.getenv("COLUMNS");
            if (cols != null && !cols.isEmpty()) {
                int w = Integer.parseInt(cols.trim());
                if (w > 40) return w;
            }
        } catch (Exception ignored) {}
        return 100; // safe default — fits standard VS Code terminal on Windows
    }

    /**
     * Receipt total width:
     *   - At most termWidth - 2  (1-char margin each side → never overflows)
     *   - At most 100            (avoid absurdly wide receipts on huge terminals)
     *   - At least 72            (minimum to render the 5-column table)
     */
    private int calculateReceiptWidth(int termWidth) {
        int w = Math.min(termWidth - 2, 100);
        return Math.max(w, 72);
    }

    /**
     * Compute the five table column widths so they fit exactly inside innerWidth.
     *
     * Row layout:  │ col1 │ col2 │ col3 │ col4 │ col5 │
     * Width math:  1 + col1 + 1 + col2 + 1 + col3 + 1 + col4 + 1 + col5 + 1
     *            = col1+col2+col3+col4+col5 + 6  ==  innerWidth + 2  (receiptWidth)
     *   ∴  col1+col2+col3+col4+col5 = innerWidth - 4
     *
     * Returns int[]{ col1, col2, col3, col4, col5 }
     *   col1 = S.No  |  col2 = Transaction  |  col3 = Amount
     *   col4 = Status  |  col5 = Date & Time
     */
    private int[] calculateColumnWidths(int innerWidth) {
        int available = innerWidth - 4;  // 4 internal │ separators
        int col1 = 6;   // S.No      — "  1  " needs 5, give 6
        int col3 = 16;  // Amount    — "INR 99,999.00" = 13 chars + padding
        int col4 = 12;  // Status    — "APPROVED" = 8 chars + padding
        int col5 = 22;  // Date&Time — "12-Aug-2026 20:23:53" = 20 + padding
        int col2 = available - col1 - col3 - col4 - col5;
        if (col2 < 12) {
            // Terminal narrower than ideal — give back space from Date&Time
            col5 = 20;
            col2 = available - col1 - col3 - col4 - col5;
        }
        if (col2 < 12) col2 = 12; // hard floor for Transaction column
        return new int[]{col1, col2, col3, col4, col5};
    }

    // ── Public entry point ───────────────────────────────────────────────────

    public void printReceipt() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String currentDate = now.format(dateFormatter);
        String currentTime = now.format(timeFormatter);
        String maskedAcc   = currentAccount != null ? currentAccount.getMaskedAccountId() : "****0000";

        // ── Compute dimensions ────────────────────────────────────────────────
        int termWidth    = getTerminalWidth();
        int receiptWidth = calculateReceiptWidth(termWidth);
        int innerWidth   = receiptWidth - 2;          // space between │ and │
        int[] cols       = calculateColumnWidths(innerWidth);

        System.out.println();

        // Top border  ┌────────────────────────────────────────────────────────┐
        System.out.println("┌" + repeatChar('─', innerWidth) + "┐");

        // Header — mathematically centered inside innerWidth
        printBoxLine(centerText("JAVA ATM", innerWidth), innerWidth);
        printBoxLine(centerText("SECURE BANKING SYSTEM", innerWidth), innerWidth);
        printBoxLine(centerText("ATM TRANSACTION RECEIPT", innerWidth), innerWidth);

        // ├────────────────────────────────────────────────────────────────────┤
        printFullSeparator(innerWidth);

        // Account / Date / Time  (adaptive: 1 line if it fits, else 2 lines)
        printMetaRow(maskedAcc, currentDate, currentTime, innerWidth);

        // ├──────┬───────────────────┬────────────────┬────────────┬──────────┤
        printTableHeaderSeparator(cols, innerWidth);

        // │ S.No │ Transaction │ Amount │ Status │ Date & Time │
        printTableHeader(cols);

        // ├──────┼───────────────────┼────────────────┼────────────┼──────────┤
        printTableRowSeparator(cols, innerWidth);

        // Transaction rows
        ArrayList<Transaction> txList = currentAccount != null
                ? currentAccount.getTransactions() : new ArrayList<>();
        Transaction lastTransferTx = null;

        if (txList.isEmpty()) {
            double bal = currentAccount != null ? currentAccount.getBalance() : 0.0;
            printTransactionRow(1, "BALANCE ENQUIRY", formatAmount(bal),
                    "SUCCESS", currentDate + " " + currentTime, cols);
            printTableBottomSeparator(cols, innerWidth);
        } else {
            int sno = 1;
            for (Transaction t : txList) {
                printTransactionRow(sno++, t.getType(), formatAmount(t.getAmount()),
                        t.getStatus(), t.getFormattedDateTime(), cols);
                if (t.getFromAccountMasked() != null && t.getToAccountMasked() != null) {
                    lastTransferTx = t;
                }
            }
            printTableBottomSeparator(cols, innerWidth);
        }

        // Transfer details (only when a fund transfer was performed)
        if (lastTransferTx != null) {
            printBoxLine(padRight(" From Account : " + lastTransferTx.getFromAccountMasked(), innerWidth), innerWidth);
            printBoxLine(padRight(" To Account   : " + lastTransferTx.getToAccountMasked(),   innerWidth), innerWidth);
            printFullSeparator(innerWidth);
        }

        // Footer — centered
        printBoxLine(centerText("TRANSACTION COMPLETED SUCCESSFULLY", innerWidth), innerWidth);
        printBoxLine(centerText("THANK YOU FOR USING JAVA ATM",       innerWidth), innerWidth);

        // Bottom border  └────────────────────────────────────────────────────┘
        System.out.println("└" + repeatChar('─', innerWidth) + "┘");
        System.out.println();
    }

    // ── Receipt row printers ─────────────────────────────────────────────────

    /** Print a normal bordered row: │ content (padded to innerWidth) │ */
    private void printBoxLine(String content, int innerWidth) {
        System.out.println("│" + padRight(content, innerWidth) + "│");
    }

    /** ├──────────────────────────────────────────────────────────────────────┤ */
    private void printFullSeparator(int innerWidth) {
        System.out.println("├" + repeatChar('─', innerWidth) + "┤");
    }

    /**
     * Account / Date / Time meta row.
     * Fits on one line when the receipt is wide enough, splits to two lines
     * on narrower terminals so the right border is never clipped.
     */
    private void printMetaRow(String maskedAcc, String date, String time, int innerWidth) {
        String accPart  = " Account : " + maskedAcc;
        String datePart = "Date : " + date;
        String timePart = "Time : " + time + " ";
        // Attempt single-line layout with 3-space padding between fields
        String oneLine  = accPart + "   " + datePart + "   " + timePart;
        if (oneLine.length() <= innerWidth) {
            printBoxLine(padRight(oneLine, innerWidth), innerWidth);
        } else {
            // Two-line fallback: account on line 1, date+time on line 2
            printBoxLine(padRight(accPart, innerWidth), innerWidth);
            printBoxLine(padRight(" " + datePart + "   " + timePart, innerWidth), innerWidth);
        }
    }

    /** ├──col1──┬──col2──┬──col3──┬──col4──┬──col5──┤ */
    private void printTableHeaderSeparator(int[] cols, int innerWidth) {
        System.out.println("├" + repeatChar('─', cols[0])
                + "┬" + repeatChar('─', cols[1])
                + "┬" + repeatChar('─', cols[2])
                + "┬" + repeatChar('─', cols[3])
                + "┬" + repeatChar('─', cols[4]) + "┤");
    }

    /** │ S.No │ Transaction │ Amount │ Status │ Date & Time │ */
    private void printTableHeader(int[] cols) {
        System.out.println("│" + centerText("S.No",        cols[0])
                + "│" + centerText("Transaction",  cols[1])
                + "│" + centerText("Amount",        cols[2])
                + "│" + centerText("Status",        cols[3])
                + "│" + centerText("Date & Time",   cols[4]) + "│");
    }

    /** ├──col1──┼──col2──┼──col3──┼──col4──┼──col5──┤ */
    private void printTableRowSeparator(int[] cols, int innerWidth) {
        System.out.println("├" + repeatChar('─', cols[0])
                + "┼" + repeatChar('─', cols[1])
                + "┼" + repeatChar('─', cols[2])
                + "┼" + repeatChar('─', cols[3])
                + "┼" + repeatChar('─', cols[4]) + "┤");
    }

    /** ├──col1──┴──col2──┴──col3──┴──col4──┴──col5──┤ */
    private void printTableBottomSeparator(int[] cols, int innerWidth) {
        System.out.println("├" + repeatChar('─', cols[0])
                + "┴" + repeatChar('─', cols[1])
                + "┴" + repeatChar('─', cols[2])
                + "┴" + repeatChar('─', cols[3])
                + "┴" + repeatChar('─', cols[4]) + "┤");
    }

    /**
     * Print one transaction data row.
     * col1 — S.No  : centered
     * col2 — Type  : left-aligned, leading space, truncated if too long
     * col3 — Amount: right-aligned, trailing space, truncated if too long
     * col4 — Status: centered, truncated if too long
     * col5 — Dt/Tm : centered, truncated if too long
     */
    private void printTransactionRow(int sno, String type, String amount,
                                     String status, String dateTimeStr, int[] cols) {
        String c1 = centerText(String.valueOf(sno),                   cols[0]);
        String c2 = padRight(" " + truncateText(type,   cols[1] - 1), cols[1]);
        String c3 = padLeft(truncateText(amount, cols[2] - 1) + " ",  cols[2]);
        String c4 = centerText(truncateText(status,      cols[3]),     cols[3]);
        String c5 = centerText(truncateText(dateTimeStr, cols[4]),     cols[4]);
        System.out.println("│" + c1 + "│" + c2 + "│" + c3 + "│" + c4 + "│" + c5 + "│");
    }

    // ── Formatting helpers ───────────────────────────────────────────────────

    /**
     * Format a monetary amount for the receipt.
     * Uses "INR" prefix to avoid the ₹ → '?' encoding issue on Windows terminals.
     * Example: 2690.00 → "INR 2,690.00"
     */
    private String formatAmount(double amount) {
        return String.format("INR %,.2f", amount);
    }

    // ── String utilities ─────────────────────────────────────────────────────

    /** Center {@code text} inside a field of {@code width} characters. */
    private String centerText(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        int leftPad  = (width - text.length()) / 2;
        int rightPad = width - text.length() - leftPad;
        return repeatChar(' ', leftPad) + text + repeatChar(' ', rightPad);
    }

    /** Pad {@code text} with spaces on the right to exactly {@code width} chars. */
    private String padRight(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        return text + repeatChar(' ', width - text.length());
    }

    /** Pad {@code text} with spaces on the left to exactly {@code width} chars. */
    private String padLeft(String text, int width) {
        if (text == null) text = "";
        if (text.length() >= width) return text.substring(0, width);
        return repeatChar(' ', width - text.length()) + text;
    }

    /**
     * Truncate {@code text} to at most {@code maxLen} characters.
     * Appends '~' to indicate truncation when text was longer.
     * Never pushes a column border outward.
     */
    private String truncateText(String text, int maxLen) {
        if (text == null) return "";
        if (maxLen <= 0)  return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, Math.max(0, maxLen - 1)) + "~";
    }

    /** Build a string of {@code count} repetitions of {@code ch}. */
    private String repeatChar(char ch, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(ch);
        return sb.toString();
    }
}
