package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Account;

public class DepositView {
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
            accountBox.getItems().add(account.getAccountNumber());
        }
        if (!accountBox.getItems().isEmpty()) {
            accountBox.getSelectionModel().selectFirst();
            updateBalance();
        }
        accountBox.setOnAction(e -> updateBalance());
    }

    private Account selectedAccount() {
        return BankData.getAccount(accountBox.getValue());
    }

    private void updateBalance() {
        Account account = selectedAccount();
        balanceLabel.setText(account == null ? "BWP 0.00"
                : String.format("BWP %.2f", account.getBalance()));
    }

    @FXML
    private void onConfirmClicked() {
        Account account = selectedAccount();
        if (account == null) {
            messageLabel.setText("No account is available.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            account.deposit(amount);
            BankData.addHistory(account, "Deposit", amount, "DEPOSIT");
            updateBalance();
            messageLabel.setText("Deposit successful.");
            amountField.clear();
        } catch (NumberFormatException ex) {
            messageLabel.setText("Enter a valid amount.");
        } catch (RuntimeException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void onBackClicked() {
        Main.show("dashboard.fxml", "Seabank - Dashboard", 900, 700);
    }
}
