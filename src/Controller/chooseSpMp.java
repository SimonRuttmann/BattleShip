package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class chooseSpMp {

    // views
    @FXML private Button singleplayerButton;
    @FXML private Button mulitplayerButton;
    @FXML private Button endButton;


    public void startSingleplayer() throws IOException{
        // change Scene
        Parent newOrLoadParent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoadParent));
        Main.primaryStage.show();
    }

    public void startMultiplayer() throws IOException{
        // change Scene
        Parent mpSelect = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }

    public void endGame(){
        // close primary Stage
        Main.primaryStage.close();
    }
}
