package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class NewOrLoadController {

    @FXML Button newGame;
    @FXML Button loadGame;

    public void createGame() throws IOException {
        Parent gameConfig = FXMLLoader.load(getClass().getResource("gameConfig.fxml"));
        Main.primaryStage.setScene(new Scene(gameConfig));
        Main.primaryStage.show();
    }

    public void loadOldGame() throws IOException {
        Parent loadGame = FXMLLoader.load(getClass().getResource("loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }
}
