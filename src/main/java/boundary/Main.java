package boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        show("login.fxml", "Seabank - Login", 780, 640);
    }

    public static void show(String fxmlFile, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("/boundary/" + fxmlFile));
            Parent root = loader.load();
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root, width, height));
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(
                    "Could not load /boundary/" + fxmlFile
                    + ". Check that the FXML file is in src/main/resources/boundary/.", ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
