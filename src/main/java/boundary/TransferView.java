package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Account;

public class TransferView {
    @FXML private ComboBox<String> fromBox;
    @FXML private Label balanceLabel;
    @FXML private RadioButton ownRadio;
    @FXML private RadioButton otherRadio;
    @FXML private VBox ownBox;
    @FXML private VBox otherBox;
    @FXML private ComboBox<String> toOwnBox;
    @FXML private TextField recipientField;
    @FXML private TextField amountField;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        for (Account account : BankData.getCurrentAccounts()) {
            fromBox.getItems().add(account.getAccountNumber());
            toOwnBox.getItems().add(account.getAccountNumber());
        }
        if (!fromBox.getItems().isEmpty()) {
            fromBox.getSelectionModel().selectFirst();
            updateBalance();
        }
        fromBox.setOnAction(e -> updateBalance());
        ownRadio.selectedProperty().addListener((obs, oldValue, selected) -> updateMode());
        updateMode();
    }

    private void updateMode() {
        boolean own = ownRadio.isSelected();
        ownBox.setVisible(own);
        ownBox.setManaged(own);
        otherBox.setVisible(!own);
        otherBox.setManaged(!own);
    }

    private Account fromAccount() {
        return BankData.getAccount(fromBox.getValue());
    }

    private void updateBalance() {
        Account account = fromAccount();
        balanceLabel.setText(account == null ? "BWP 0.00"
                : String.format("BWP %.2f", account.getBalance()));
    }

    @FXML
    private void onConfirmClicked() {
        Account source = fromAccount();
        if (source == null) {
            messageLabel.setText("Select a source account.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0 || amount > source.getBalance()) {
                throw new IllegalArgumentException("Enter a positive amount within your available balance.");
            }

            Account target;
            if (ownRadio.isSelected()) {
                target = BankData.getAccount(toOwnBox.getValue());
            } else {
                target = BankData.getAccount(recipientField.getText().trim());
            }

            if (target == null) {
                messageLabel.setText("Recipient account was not found.");
                return;
            }
            if (source == target) {
                messageLabel.setText("You cannot transfer money to the same account.");
                return;
            }

            source.withdraw(amount);
            target.deposit(amount);
            BankData.addHistory(source, "Transfer to " + target.getAccountNumber(), amount, "TRANSFER OUT");
            BankData.addHistory(target, "Transfer from " + source.getAccountNumber(), amount, "TRANSFER IN");
            updateBalance();
            messageLabel.setText("Transfer successful.");
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
