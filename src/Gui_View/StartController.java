package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartController {

    @FXML public Button playBySelf;
    @FXML public Button letKIplay;

    public void playSelf() throws IOException {
        Parent placeShips = FXMLLoader.load(getClass().getResource("placeShips.fxml"));
        Main.primaryStage.setScene(new Scene(placeShips));
        Main.primaryStage.show();
    }

    public void playKI() throws IOException {
        Parent placeShips = FXMLLoader.load(getClass().getResource("placeShips.fxml"));
        Main.primaryStage.setScene(new Scene(placeShips));
        Main.primaryStage.show();
    }
}
