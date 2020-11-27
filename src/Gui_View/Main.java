package Gui_View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    public static Stage primaryStage;
    int width = 600;
    int height = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        // Versehentliches Schließen des Spiels verhindern
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            HelpMethods.closeProgramm();
        });

        Parent root = FXMLLoader.load(getClass().getResource("hello.fxml"));
        primaryStage.setTitle("Schiffe versenken");
        primaryStage.setScene(new Scene(root, width, height));
        HelpMethods.alignStageCenter(primaryStage, width, height);
        primaryStage.show();

    }
}