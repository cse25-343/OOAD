import java.time.LocalDate;

public class InvestmentAccount extends Account {

    private double monthlyInterestRate = 0.05;
    private double minimumOpeningDeposit = 500.0;

    public InvestmentAccount(String number, double deposit, LocalDate dateOpened,
                             String branch) {
        super(number, deposit, dateOpened, branch );

        if (deposit < minimumOpeningDeposit) {
            throw new IllegalArgumentException(
                "Minimum opening deposit is BWP 500."
            );
        }

        this.balance = deposit;
    }

    public double calculateInterest() {
        return balance * monthlyInterestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }
}
