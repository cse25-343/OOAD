
package model;

import java.time.LocalDate;

public abstract class Account {

    protected String accountNumber;
    protected double balance;
    protected LocalDate dateOpened;
    protected String branch;

    // Constructor
    public Account(String accountNumber, double balance,
                   LocalDate dateOpened, String branch) {

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.dateOpened = dateOpened;
        this.branch = branch;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException(
                "Deposit amount must be positive."
            );
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive and less than or equal to the current balance."
            );
        }
    }
}
