
class InnerMain {
 
    
 }

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Create sample accounts
        Account account1 = new Account("ACC1001", "user123", "1234", 10000.00);
        Account account2 = new Account("ACC1002", "user456", "5678", 15000.00);

        bank.addAccount(account1);
        bank.addAccount(account2);

        // Start the ATM application
        ATM atm = new ATM(bank);
        atm.start();
    }
}

