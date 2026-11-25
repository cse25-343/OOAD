
package model;

import java.time.LocalDate;

public class Transaction {

    private String transactional;
    private LocalDate date;
    private double amount;

    public Transaction(String transactional, LocalDate date, double amount) {
        this.transactional = transactional;
        this.date = date;
        this.amount = amount;
    }

    public void execute() {
        System.out.println("Transaction executed.");
    }

    // Getters
    public String getTransactional() {
        return transactional;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}
