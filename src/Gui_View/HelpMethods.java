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
        System.out.println("a");
        CancelGame.exit(); // todo call align center
    }

    // do you really want to exit the Game - special for in-game: with saving
    public static void closeProgrammSaveGame() {
        System.out.println("b");
        CancelGame.save(); // todo call align center
    }

    // win or lose - new game or exit
    public static void winOrlose(boolean win) {
        System.out.println("c");
        WinLose.display(win); // todo call align centerZ
    }
}
