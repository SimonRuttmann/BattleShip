package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class GameConfig {

    @FXML private Button startButton;

    public void startGame() throws IOException {
        Parent start = FXMLLoader.load(getClass().getResource("/Gui_View/start.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }

}
