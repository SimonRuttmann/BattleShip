// todo set min Height: so, dass Festerrahmen egal welches BS berücksichtigt wird??

package Gui_View;

import Player.SaveAndLoad;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    public static Stage primaryStage;
    int width = 600;
    int height = 400;
    CancelGame cancelGame = new CancelGame();
    WinLose winLose = new WinLose();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        // Versehentliches Schließen des Spiels verhindern
        primaryStage.setOnCloseRequest(e -> {
            // e.consume catches closeWindowEvent, which would otherwise be sent to OS
            // -> stage would be closed even when "do you want to close -> no" is selected
            e.consume();
            HelpMethods.closeProgramm();
        });

        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/MainMenu.fxml"));
        primaryStage.setTitle("Schiffe versenken");
        primaryStage.setScene(new Scene(root, width, height));
        HelpMethods.alignStageCenter(primaryStage, width, height);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.show();

    }
}