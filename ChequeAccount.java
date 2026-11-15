import java.time.LocalDate;

public class ChequeAccount extends Account {
    private String employerDetails;

    public ChequeAccount(String number, double balance, LocalDate dateOpened,
                         String branch, String employerDetails) {
        super(number, balance, dateOpened, branch);
        this.employerDetails = employerDetails;
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
