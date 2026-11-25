
package service;

import database.DatabaseManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BankingService {

    public BankingService() {
        DatabaseManager.initialize();
    }

    // Register a customer and open an account
    public Map<String, Object> registerCustomerAndOpenAccount(
            String firstName,
            String surname,
            String email,
            String phone,
            String accountNumber,
            String accountType,
            BigDecimal openingBalance,
            String branch) {

        try {
            long customerId = DatabaseManager.createCustomer(
                    firstName, surname, email, phone
            );

            DatabaseManager.createAccount(
                    accountNumber,
                    customerId,
                    accountType,
                    openingBalance,
                    branch
            );

            return DatabaseManager.findAccount(accountNumber);

        } catch (SQLException e) {
            throw new RuntimeException("Could not register customer.", e);
        }
    }

    // Find an account
    public Map<String, Object> findAccount(String accountNumber) {
        try {
            return DatabaseManager.findAccount(accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException("Could not find account.", e);
        }
    }

    // Get account transaction history
    public List<Map<String, Object>> getTransactionHistory(
            String accountNumber) {

        try {
            return DatabaseManager.getTransactionsForAccount(accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException("Could not get transaction history.", e);
        }
    }

    // Deposit money
    public Map<String, Object> deposit(
            String accountNumber, BigDecimal amount) {

        validateAmount(amount);

        try {
            Map<String, Object> account =
                    DatabaseManager.findAccount(accountNumber);

            if (account == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            BigDecimal balance = (BigDecimal) account.get("balance");
            BigDecimal newBalance = balance.add(amount);

            DatabaseManager.updateAccount(
                    accountNumber,
                    (String) account.get("account_type"),
                    newBalance,
                    (String) account.get("branch")
            );

            DatabaseManager.createTransaction(
                    accountNumber, "DEPOSIT", amount, "Cash deposit"
            );

            return DatabaseManager.findAccount(accountNumber);

        } catch (SQLException e) {
            throw new RuntimeException("Deposit failed.", e);
        }
    }

    // Withdraw money
    public Map<String, Object> withdraw(
            String accountNumber, BigDecimal amount) {

        validateAmount(amount);

        try {
            Map<String, Object> account =
                    DatabaseManager.findAccount(accountNumber);

            if (account == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            BigDecimal balance = (BigDecimal) account.get("balance");

            if (balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds.");
            }

            BigDecimal newBalance = balance.subtract(amount);

            DatabaseManager.updateAccount(
                    accountNumber,
                    (String) account.get("account_type"),
                    newBalance,
                    (String) account.get("branch")
            );

            DatabaseManager.createTransaction(
                    accountNumber, "WITHDRAWAL", amount, "Cash withdrawal"
            );

            return DatabaseManager.findAccount(accountNumber);

        } catch (SQLException e) {
            throw new RuntimeException("Withdrawal failed.", e);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }
    }
}
