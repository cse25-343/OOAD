
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("BANKING MANAGEMENT SYSTEM");
        title.setFont(new Font("Arial", 22));
        title.setTextFill(Color.DARKBLUE);

        Label subtitle = new Label("Please Login to Continue");

        TextField username = new TextField();
        username.setPromptText("Enter Username");
        username.setMaxWidth(250);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter Password");
        password.setMaxWidth(250);

        Button loginButton = new Button("Login");

        Label message = new Label();

       loginButton.setOnAction(event -> {
    if (username.getText().isEmpty() || password.getText().isEmpty()) {
        message.setText("Please enter your username and password.");
    } else {
        DashboardView dashboard = new DashboardView();
        dashboard.start(new Stage());
        stage.close();
    }
});

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        layout.getChildren().addAll(
            title, subtitle, username, password,
            loginButton, message
        );

        Scene scene = new Scene(layout, 600, 450);

        stage.setTitle("Banking System Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

