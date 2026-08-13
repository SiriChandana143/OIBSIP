import java.util.ArrayList;

public class Bank {
    private ArrayList<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account authenticateUser(String userId, String pin) {
        for (Account account : accounts) {
            if (account.getUserId().equals(userId) && account.validatePin(pin)) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountById(String accountId) {
        if (accountId == null) return null;
        for (Account account : accounts) {
            if (account.getAccountId().equalsIgnoreCase(accountId.trim())) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountByUserId(String userId) {
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    public Account findAccountByNumber(String accountNumber) {
        return findAccountById(accountNumber);
    }
}
