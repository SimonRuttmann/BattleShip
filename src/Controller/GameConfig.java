package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GameConfig {

    @FXML private Button backButton;
    @FXML private TextField playerName;
    @FXML private Spinner<Integer> fieldSize;
    @FXML private Spinner<Integer> numberOfShips;
    @FXML private Spinner<Integer> number2Ships;
    @FXML private Spinner<Integer> number3Ships;
    @FXML private Spinner<Integer> number4Ships;
    @FXML private Spinner<Integer> number5Ships;
    @FXML private Button startButton;


    public void backToLastScene() throws IOException{
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }

    public void startGame() throws IOException {
        Parent start = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/start.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }

}
