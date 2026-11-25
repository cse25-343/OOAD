package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.*;

import java.io.File;
import java.time.LocalDate;

public class RegisterView {
    @FXML private TextField fullNameField;
    @FXML private TextField nationalIdField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> accountTypeBox;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label fileLabel;
    @FXML private Label messageLabel;

    private File employmentFile;

    @FXML
    private void initialize() {
        accountTypeBox.getItems().setAll("Savings", "Cheque", "Investment");
    }

    @FXML
    private void onChooseFileClicked() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose proof of employment");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.png", "*.jpg", "*.jpeg"));
        employmentFile = chooser.showOpenDialog(fullNameField.getScene().getWindow());
        if (employmentFile != null) {
            fileLabel.setText(employmentFile.getName());
        }
    }

    @FXML
    private void onRegisterClicked() {
        String fullName = fullNameField.getText().trim();
        String nationalId = nationalIdField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String type = accountTypeBox.getValue();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (fullName.isEmpty() || nationalId.isEmpty() || phone.isEmpty()
                || email.isEmpty() || type == null || password.isEmpty() || confirm.isEmpty()) {
            messageLabel.setText("Please complete all required fields.");
            return;
        }
        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        if (password.length() < 4) {
            messageLabel.setText("Password must contain at least 4 characters.");
            return;
        }

        try {
            String[] names = fullName.trim().split("\\s+", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "";
            String accountNumber = BankData.generateAccountNumber();
            double openingBalance = "Investment".equals(type) ? 500.0 : 0.0;

            Customer customer = new Person(nationalId, email, phone, "", firstName, lastName);
            Account account;
            switch (type) {
                case "Cheque":
                    account = new ChequeAccount(accountNumber, openingBalance,
                            LocalDate.now(), "Gaborone", employmentFile == null
                            ? "Not supplied" : employmentFile.getName());
                    break;
                case "Investment":
                    account = new InvestmentAccount(accountNumber, openingBalance,
                            LocalDate.now(), "Gaborone");
                    break;
                default:
                    account = new SavingsAccount(accountNumber, openingBalance,
                            LocalDate.now(), "Gaborone");
                    break;
            }

            BankData.register(customer, account, password);
            BankData.addHistory(account, "Account opened", openingBalance, "OPENING");
            messageLabel.setStyle("-fx-text-fill: #1e8449;");
            messageLabel.setText("Registration successful. Your account number is " + accountNumber
                    + ". Save it to log in.");
        } catch (RuntimeException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    private void onBackClicked() {
        Main.show("login.fxml", "Seabank - Login", 780, 640);
    }
}
