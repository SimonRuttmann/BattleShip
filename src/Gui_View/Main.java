// todo: important -> use -ea to enable assertions when testing project code - assertions where used

// todo set min Height: so, dass Festerrahmen egal welches BS berücksichtigt wird??

package Gui_View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {
/*
ZU GIT:
Guppe 14 Yannick$ git rm --cached -r .savedGames
git rm --cached -r .savedGames
--> Cache löschen, wenn ausversehen Dateien hinzugefügt worden sind (z.b. der out-Ordner)
--> Danach normal Comitten
 */

    public static Stage primaryStage;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    CancelGame cancelGame = new CancelGame();
    WinLose winLose = new WinLose();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        //todo MainMenu 1 vs 2
        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/MainMenu2.fxml"));
        primaryStage.setTitle("Schiffe versenken");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        HelpMethods.alignStageCenter(primaryStage, WIDTH, HEIGHT);
        //primaryStage.setMinWidth(width);
        //primaryStage.setMinHeight(height);
        primaryStage.show();

    }
}
