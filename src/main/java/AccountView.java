import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AccountView extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("ACCOUNT DETAILS");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.DARKBLUE);

        Label accountNumber = new Label("Account Number: Not Available");
        Label accountType = new Label("Account Type: Not Available");
        Label balance = new Label("Account Balance: P 0.00");

        Button backButton = new Button("Back to Dashboard");

        backButton.setOnAction(e -> {
            DashboardView dashboard = new DashboardView();
            dashboard.start(new Stage());
            stage.close();
        });

        VBox layout = new VBox(20, title, accountNumber,
                accountType, balance, backButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4F7FC;");

        stage.setTitle("Account Details");
        stage.setScene(new Scene(layout, 600, 400));
        stage.show();
    }
}
