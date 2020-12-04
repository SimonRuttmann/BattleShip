// todo add stylesheets to classes

package Gui_View;

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

    // connection lost
    public static void connectionLost() {ConnectionLost.display(); }

    // win or lose - new game or exit
    public static void winOrlose(boolean win) {
        WinLose.display(win);
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