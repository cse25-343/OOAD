
package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;

public class DashboardController {

    // Dashboard FXML fields
    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;

    // Existing account fields
    @FXML private TextField txtAccountNumber;
    @FXML private TextField txtBalance;
    @FXML private TextField txtBranch;
    @FXML private ComboBox<String> cmbAccountType;
    @FXML private TextField txtCompanyName;
    @FXML private TextField txtCompanyAddress;
    @FXML private TextField txtAmount;
    @FXML private Label lblBalance;
    @FXML private Label lblInterest;

    // Initialize dashboard
    @FXML
    private void initialize() {

        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome to Seabank");
        }

        if (cmbAccountType != null) {
            cmbAccountType.getItems().setAll(
                "Savings", "Cheque", "Investment"
            );
        }

        Account account = currentAccount();

        if (account != null) {

            if (txtAccountNumber != null) {
                txtAccountNumber.setText(account.getAccountNumber());
            }

            if (txtBranch != null) {
                txtBranch.setText(account.getBranch());
            }

            if (txtBalance != null) {
                txtBalance.setText(
                    String.format("%.2f", account.getBalance())
                );
            }
        }

        refreshBalance();
    }

    // LOG OUT
    @FXML
    private void onLogoutClicked() {
        Main.show("login.fxml", "Seabank - Login", 380, 560);
    }

    // DEPOSIT SCREEN
    @FXML
    private void onDepositClicked() {
        Main.show("deposit.fxml", "Seabank - Deposit", 820, 620);
    }

    // WITHDRAW SCREEN
    @FXML
    private void onWithdrawClicked() {
        Main.show("withdraw.fxml", "Seabank - Withdraw", 820, 620);
    }

    // TRANSFER SCREEN
    @FXML
    private void onWireClicked() {
        Main.show("transfer.fxml", "Seabank - Transfer", 820, 620);
    }

    // ACCOUNT HISTORY SCREEN
    @FXML
    private void onHistoryClicked() {
        Main.show("history.fxml", "Seabank - Account History", 820, 620);
    }

    // CREATE ACCOUNT
    @FXML
    private void createAccount() {
        message(
            "An account is created during registration. "
            + "Additional account creation can be added later."
        );
    }

    // DEPOSIT
    @FXML
    private void deposit() {
        performAmountAction(true);
    }

    // WITHDRAW
    @FXML
    private void withdraw() {
        performAmountAction(false);
    }

    private void performAmountAction(boolean isDeposit) {

        Account account = currentAccount();

        if (account == null) {
            return;
        }

        if (txtAmount == null || txtAmount.getText().trim().isEmpty()) {
            message("Please enter an amount.");
            return;
        }

        try {

            double amount = Double.parseDouble(
                txtAmount.getText().trim()
            );

            if (isDeposit) {

                account.deposit(amount);

                BankData.addHistory(
                    account, "Cash deposit", amount, "DEPOSIT"
                );

            } else {

                account.withdraw(amount);

                BankData.addHistory(
                    account, "Cash withdrawal", amount, "WITHDRAWAL"
                );
            }

            refreshBalance();

            if (txtBalance != null) {
                txtBalance.setText(
                    String.format("%.2f", account.getBalance())
                );
            }

            txtAmount.clear();

            message(
                (isDeposit ? "Deposit" : "Withdrawal")
                + " successful."
            );

        } catch (NumberFormatException ex) {

            message("Please enter a valid amount.");

        } catch (RuntimeException ex) {

            message(ex.getMessage() == null
                    ? "Transaction could not be completed."
                    : ex.getMessage());
        }
    }

    // CALCULATE INTEREST
    @FXML
    private void calculateInterest() {

        Account account = currentAccount();

        if (account == null) {
            return;
        }

        if (account instanceof InterestCalculable) {

            InterestCalculable interestAccount =
                (InterestCalculable) account;

            double interest = interestAccount.calculateInterest();

            if (lblInterest != null) {
                lblInterest.setText(
                    String.format("BWP %.2f", interest)
                );
            }

        } else {

            if (lblInterest != null) {
                lblInterest.setText("Not available");
            }
        }
    }

    // GET CURRENT ACCOUNT
    private Account currentAccount() {

        if (BankData.getCurrentUser() == null) {
            return null;
        }

        return BankData.getCurrentUser()
                .getCustomer().getAccount();
    }

    // REFRESH ACCOUNT BALANCE
    private void refreshBalance() {

        Account account = currentAccount();

        String formattedBalance = account == null
                ? "P 0.00"
                : String.format("P %,.2f", account.getBalance());

        if (balanceLabel != null) {
            balanceLabel.setText(formattedBalance);
        }

        if (lblBalance != null) {
            lblBalance.setText(formattedBalance);
        }
    }

    // DISPLAY MESSAGE
    private void message(String text) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("SeaBank");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
