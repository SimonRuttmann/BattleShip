// todo add stylesheets to classes

package Gui_View;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

    // win or lose - new game or exit
    public static void winOrlose(boolean win) {
        WinLose.display(win);
    }
}
