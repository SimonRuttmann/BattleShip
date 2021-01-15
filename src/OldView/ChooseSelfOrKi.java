package OldView;

import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.Main;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseSelfOrKi implements Initializable {

    @FXML public Button playBySelf;
    @FXML public Button letKiPlay;

    //TODO von Simon
    public void initialize(URL location, ResourceBundle resources) {
        MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
        multiplayerControlThreadConfigCommunication.start();
    }
    public void playSelf() throws IOException {

        Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
        ActiveGameState.setSelfOrKi(true);
        Main.primaryStage.setScene(new Scene(placeShips));
        Main.primaryStage.show();
    }

    public void playKi() throws IOException {
        Parent gamePlayground = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
        ActiveGameState.setSelfOrKi(false);
        Main.primaryStage.setScene(new Scene(gamePlayground));
        Main.primaryStage.show();
    }
}
