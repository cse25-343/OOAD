import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DashboardView extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("BANKING MANAGEMENT SYSTEM");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        Label welcome = new Label("Welcome to Your Banking Dashboard");
        welcome.setFont(Font.font("Arial", 18));

        Label balance = new Label("Account Overview");
        balance.setFont(Font.font("Arial", 20));

        Label amount = new Label("Available Balance: P 0.00");
        amount.setFont(Font.font("Arial", 18));
        amount.setTextFill(Color.GREEN);

        Button accountButton = new Button("View Account");
        Button transactionButton = new Button("Transactions");
        Button logoutButton = new Button("Logout");

        accountButton.setOnAction(e -> {
            AccountView view = new AccountView();
            view.start(new Stage());
            stage.close();
        });

        transactionButton.setOnAction(e -> {
            TransactionView view = new TransactionView();
            view.start(new Stage());
            stage.close();
        });

        logoutButton.setOnAction(e -> {
            LoginView view = new LoginView();
            view.start(new Stage());
            stage.close();
        });

        VBox layout = new VBox(20, title, welcome, balance, amount,
                accountButton, transactionButton, logoutButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4F7FC;");

        stage.setTitle("Banking Dashboard");
        stage.setScene(new Scene(layout, 650, 500));
        stage.show();
    }
}
