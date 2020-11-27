package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

public class PlaceShipsController {

    @FXML
    private Button readyButton;

    public void startGame() throws IOException {
        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            HelpMethods.closeProgrammSaveGame();
        });
        Parent game = FXMLLoader.load(getClass().getResource("game.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }

}
