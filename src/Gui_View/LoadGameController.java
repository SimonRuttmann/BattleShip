package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoadGameController {

    @FXML private Button deleteGames;
    @FXML private Button loadGame;

    public void delete() {

        // delete ausgewählte datein todo robin link skype
    }

    public void load() throws IOException{
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
