package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginView {
    @FXML private TextField accountNumberField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void onLoginClicked() {
        String account = accountNumberField.getText().trim();
        String password = passwordField.getText();

        if (account.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter your account number and password.");
            return;
        }

        if (BankData.login(account, password) == null) {
            messageLabel.setText("Incorrect account number or password.");
            return;
        }

        Main.show("dashboard.fxml", "Seabank - Dashboard", 900, 700);
    }

    @FXML
    private void onRegisterClicked() {
        Main.show("register.fxml", "Seabank - Register", 780, 640);
    }
}
