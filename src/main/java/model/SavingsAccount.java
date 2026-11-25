
package model;

import java.time.LocalDate;

public class SavingsAccount extends Account
        implements InterestCalculable {

    private double monthlyInterestRate = 0.0005;

    public SavingsAccount(String number, double balance,
                          LocalDate openedDate, String branch) {

        super(number, balance, openedDate, branch);
    }

    @Override
    public double calculateInterest() {
        return balance * monthlyInterestRate;
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException(
            "Withdrawals are not allowed on a savings account."
        );
    }

    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }
}
