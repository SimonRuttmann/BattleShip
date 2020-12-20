// todo add stylesheets to classes

package Gui_View;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpMethods {

    // method to align the stage in center of the screen
    // has to be called before stage is shown
    public static void alignStageCenter(Stage stage, int width, int height) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - width) / 2);
        stage.setY((bounds.getHeight() - height) / 2);
    }

    // do you really want to exit the Game?
    public static void closeProgramm() {
        CancelGame.exit();
    }

    // do you really want to exit the Game - special for in-game: with saving
    public static void closeProgrammSaveGame() {
        CancelGame.save();
    }

    // connectionFailed
    public static void connectionFailed() {
        Platform.runLater(ConnectionFailed::display);
    }

    // connection lost
    public static void connectionLost() {
        Platform.runLater(ConnectionLost::display);
    }

    // win or lose - new game or exit
    public static void winOrLose(boolean win) {
        Platform.runLater(() -> WinLose.display(win));
        //todo -> only one is showed: when won - no response from client??
        // when client wins: both displayed,
        // when server wins: game does not stop for server, only for client - abfrage needed if all ships sunk in main??
        // @Yannick @Simon Fehler finden

        // todo beim popup -> systemleiste ausblenden -> soll nicht schließbar sein
    }

    // todo: evtl better, does work but really slow
    /*public void backToMainMenu() {
        Parent mainMenu = null;
        try {
            mainMenu = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.primaryStage.setScene(new Scene(mainMenu));
        Main.primaryStage.show();
    }*/
}