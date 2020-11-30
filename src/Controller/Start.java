package Controller;

import Gui_View.Main;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class Start {

    @FXML public Button playBySelf;
    @FXML public Button letKIplay;

    public void playSelf() throws IOException {
        Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
        ActiveGameState.setSelfOrKi(true);
        Main.primaryStage.setScene(new Scene(placeShips));
        Main.primaryStage.show();
    }

    public void playKI() throws IOException {
        Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
        ActiveGameState.setSelfOrKi(false);
        Main.primaryStage.setScene(new Scene(placeShips));
        Main.primaryStage.show();
    }


}
