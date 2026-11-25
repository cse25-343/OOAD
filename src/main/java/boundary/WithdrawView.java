
package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Account;

public class WithdrawView {

    @FXML private ComboBox<String> accountBox;
    @FXML private Label balanceLabel;
    @FXML private TextField amountField;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        loadAccounts();
    }

    private void loadAccounts() {
        accountBox.getItems().clear();

        for (Account account : BankData.getCurrentAccounts()) {
            if (account != null) {
                accountBox.getItems().add(account.getAccountNumber());
            }
        }

        if (!accountBox.getItems().isEmpty()) {
            accountBox.getSelectionModel().selectFirst();
            updateBalance();
        } else {
            balanceLabel.setText("BWP 0.00");
            messageLabel.setText("No accounts available.");
        }

        accountBox.setOnAction(event -> updateBalance());
    }

    private Account selectedAccount() {
        String accountNumber = accountBox.getValue();

        if (accountNumber == null || accountNumber.isBlank()) {
            return null;
        }

        return BankData.getAccount(accountNumber);
    }

    private void updateBalance() {
        Account account = selectedAccount();

        if (account == null) {
            balanceLabel.setText("BWP 0.00");
        } else {
            balanceLabel.setText(
                String.format("BWP %.2f", account.getBalance())
            );
        }
    }

    @FXML
    private void onConfirmClicked() {

        Account account = selectedAccount();

        if (account == null) {
            messageLabel.setText("Please select an account.");
            return;
        }

        String input = amountField.getText().trim();

        if (input.isEmpty()) {
            messageLabel.setText("Please enter an amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(input);

            if (!Double.isFinite(amount) || amount <= 0) {
                messageLabel.setText("Enter an amount greater than zero.");
                return;
            }

            if (amount > account.getBalance()) {
                messageLabel.setText("Insufficient funds.");
                return;
            }

            account.withdraw(amount);

            BankData.addHistory(
                account,
                "Withdrawal",
                amount,
                "WITHDRAWAL"
            );

            updateBalance();
            messageLabel.setText("Withdrawal successful!");
            amountField.clear();

        } catch (NumberFormatException ex) {
            messageLabel.setText("Please enter a valid number.");
        } catch (RuntimeException ex) {
            messageLabel.setText(
                ex.getMessage() == null
                    ? "Withdrawal could not be completed."
                    : ex.getMessage()
            );
        }
    }

    @FXML
    private void onBackClicked() {
        Main.show(
            "dashboard.fxml",
            "Seabank - Dashboard",
            900,
            700
        );
    }
}
