package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class NewOrLoad {

    @FXML Button newGame;
    @FXML Button loadGame;

    public void createGame() throws IOException {
        Parent gameConfig = FXMLLoader.load(getClass().getResource("/Gui_View/gameConfig.fxml"));
        Main.primaryStage.setScene(new Scene(gameConfig));
        Main.primaryStage.show();
    }

    public void loadOldGame() throws IOException {
        Parent loadGame = FXMLLoader.load(getClass().getResource("/Gui_View/loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }
}
