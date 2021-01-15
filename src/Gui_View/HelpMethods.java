// todo add style to all pop-up windows

package Gui_View;

import Controller.SaveRequest;
import Gui_View.PopUpWindows.*;
import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;


/** HelpMethods contains useful methods that are used in different places:
 *  method to align a Stage in the center of a screen, methods to create pop-up-windows that inform the user about
 *  different states of the game and a method that tries to close a MP SERVER/CLIENT SOCKET
 */
public class HelpMethods {

    // method to align the stage in center of the screen
    // has to be called before stage is shown
    public static void alignStageCenter(Stage stage, int width, int height) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - width) / 2);
        stage.setY((bounds.getHeight() - height) / 2);
    }

    // Pop-Up: do you really want to exit the Game? - all scenes except GamePlayground
    public static void closeProgramm() {
        CancelGame.exit();
    }

    // Pop-Up: do you really want to exit the Game - special for in-game: with saving - only in GamePlayground
    public static void closeProgrammSaveGame(boolean alreadySaved) {
        CancelGame.save(alreadySaved);
    }

    // connectionFailed
    public static void connectionFailed() {
        Platform.runLater(ConnectionFailed::display);
    }

    // connection lost
    public static void connectionLost() {
        Platform.runLater(ConnectionLost::display);
    }

    // save request -> remote wants to save
    public static void saveRequest(long id) { Platform.runLater( () -> SaveRequest.display(id));}

    // load request -> remote wants to load, but no matching file
    public static void noGameFile() { Platform.runLater(noMatchingSavedGame::display);}

    // unexpectecd message from remote
    public static void unexceptedMessage() {Platform.runLater(unexpectedMessageFromRemote::display);}

    // win or lose - new game or exit
    public static void winOrLose(boolean win) {
        Platform.runLater(() -> WinLose.display(win));
    }

    // method that closes MP SERVER/CLIENT SOCKETS - if there are existing ones
    public static void closeMPSockets() {
        if(ActiveGameState.isMultiplayer()){
            if (ActiveGameState.isAmIServer()) {
                ActiveGameState.getServer().closeConnection();
            } else {
                ActiveGameState.getClient().closeConnection();
            }
        }
    }
}