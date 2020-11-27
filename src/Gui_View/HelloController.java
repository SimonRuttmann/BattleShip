package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class HelloController {

    // views
    @FXML private Button singleplayerButton;
    @FXML private Button mulitplayerButton;
    @FXML private Button endButton;


    public void startSingleplayer() throws IOException{
        // change Scene
        Parent newOrLoadParent = FXMLLoader.load(getClass().getResource("newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoadParent));
        Main.primaryStage.show();
    }

    public void startMultiplayer() throws IOException{
        // change Scene
        Parent mpSelect = FXMLLoader.load(getClass().getResource("mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }

    public void endGame(){
        // close primary Stage
        Main.primaryStage.close();
    }
}
