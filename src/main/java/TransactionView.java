import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TransactionView extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("BANK TRANSACTIONS");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        ComboBox<String> transactionType = new ComboBox<>();
        transactionType.getItems().addAll("Deposit", "Withdrawal", "Transfer");
        transactionType.setPromptText("Select Transaction Type");

        TextField amount = new TextField();
        amount.setPromptText("Enter Amount (P)");

        TextField accountNumber = new TextField();
        accountNumber.setPromptText("Enter Account Number");

        Button submitButton = new Button("Submit Transaction");
        Label message = new Label();

        submitButton.setOnAction(e -> {
            message.setText("Transaction details submitted for processing.");
        });

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            DashboardView dashboard = new DashboardView();
            dashboard.start(new Stage());
            stage.close();
        });

        VBox layout = new VBox(15, title, transactionType,
                amount, accountNumber, submitButton, message, backButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4F7FC;");

        stage.setTitle("Bank Transactions");
        stage.setScene(new Scene(layout, 600, 450));
        stage.show();
    }
}
