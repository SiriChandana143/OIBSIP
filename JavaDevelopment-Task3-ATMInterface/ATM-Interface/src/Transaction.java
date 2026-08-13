import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String type;
    private double amount;
    private LocalDateTime dateTime;
    private String description;
    private double balanceAfterTransaction;
    private String status;
    private String fromAccountMasked;
    private String toAccountMasked;

    public Transaction(String type, double amount, String description, double balanceAfterTransaction) {
        this(type, amount, description, balanceAfterTransaction, "APPROVED", null, null);
    }

    public Transaction(String type, double amount, String description, double balanceAfterTransaction, String status) {
        this(type, amount, description, balanceAfterTransaction, status, null, null);
    }

    public Transaction(String type, double amount, String description, double balanceAfterTransaction, String status, String fromAccountMasked, String toAccountMasked) {
        this.type = type;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.description = description;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.status = status != null ? status : "APPROVED";
        this.fromAccountMasked = fromAccountMasked;
        this.toAccountMasked = toAccountMasked;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return dateTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public String getDescription() {
        return description;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public String getStatus() {
        return status;
    }

    public String getFromAccountMasked() {
        return fromAccountMasked;
    }

    public String getToAccountMasked() {
        return toAccountMasked;
    }
}
