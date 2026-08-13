import java.util.ArrayList;

public class Account {
    private String accountId;
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> transactions;

    public Account(String accountId, String userId, String pin, double initialBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMaskedAccountId() {
        if (accountId == null || accountId.length() <= 4) {
            return "****" + (accountId != null ? accountId : "");
        }
        return "****" + accountId.substring(accountId.length() - 4);
    }

    public String getUserId() {
        return userId;
    }

    public boolean validatePin(String inputPin) {
        return this.pin != null && this.pin.equals(inputPin);
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || this.balance < amount) {
            return false;
        }
        this.balance -= amount;
        transactions.add(new Transaction("WITHDRAWAL", amount, "Cash Withdrawal", this.balance, "APPROVED"));
        return true;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, "Cash Deposit", this.balance, "APPROVED"));
        return true;
    }

    public boolean transfer(Account recipient, double amount) {
        if (recipient == null || this.accountId.equalsIgnoreCase(recipient.getAccountId()) || amount <= 0 || this.balance < amount) {
            return false;
        }
        
        this.balance -= amount;
        this.transactions.add(new Transaction("FUND TRANSFER", amount, "To " + recipient.getMaskedAccountId(), this.balance, "APPROVED", this.getMaskedAccountId(), recipient.getMaskedAccountId()));
        
        recipient.receiveTransfer(this.getMaskedAccountId(), amount);
        
        return true;
    }
    
    private void receiveTransfer(String fromMaskedId, double amount) {
        this.balance += amount;
        this.transactions.add(new Transaction("FUND TRANSFER IN", amount, "From " + fromMaskedId, this.balance, "APPROVED", fromMaskedId, this.getMaskedAccountId()));
    }
    
    public void printTransactionHistory() {
        System.out.println("============================================================");
        System.out.println("                 TRANSACTION HISTORY");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Account: " + getMaskedAccountId());
        System.out.println();
        System.out.println("------------------------------------------------------------");
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions have been made during this session.");
            System.out.println("------------------------------------------------------------");
        } else {
            System.out.printf("%-5s %-18s %-13s %-20s%n", "No.", "Type", "Amount", "Date & Time");
            System.out.println("------------------------------------------------------------");
            
            int count = 1;
            for (Transaction t : transactions) {
                String numStr = String.format("%02d", count);
                String amountStr = String.format("₹%,.2f", t.getAmount());
                System.out.printf("%-5s %-18s %-13s %-20s%n", 
                    numStr, 
                    t.getType(), 
                    amountStr, 
                    t.getFormattedDateTime());
                count++;
            }
            
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("Total Transactions: " + transactions.size());
        }
        
        System.out.println("============================================================");
    }
}
