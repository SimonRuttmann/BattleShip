package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

public class PlaceShips {

    @FXML
    private Button readyButton;

    public void startGame() throws IOException {
        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Gui_View.HelpMethods.closeProgrammSaveGame();
        });
        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/game.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }

}
