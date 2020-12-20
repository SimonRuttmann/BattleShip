package Controller;

import Gui_View.HelpMethods;
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

public class MainMenu2 implements Initializable {    //todo: ordentliche Namen für Scenes - alle verweise überprüfen - duplikate entfernen
    //todo -> acitveGameState hier alles auf null setzten (evtl. -> wenn rückkehr aus spiel)

    // views
    @FXML private Button singleplayerButton;
    @FXML private Button mulitplayerButton;
    @FXML private Button endButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActiveGameState.setSceneIsPlaceShips(false);

        // Versehentliches Schließen des Spiels verhindern
        Main.primaryStage.setOnCloseRequest(e -> {
            // e.consume catches closeWindowEvent, which would otherwise be sent to OS
            // -> stage would be closed even when "do you want to close -> no" is selected
            e.consume();
            HelpMethods.closeProgramm();
        });
    }


    public void startSingleplayer() throws IOException{
        ActiveGameState.setMultiplayer(false);
        // change Scene
        Parent newOrLoadParent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoadParent));
        Main.primaryStage.show();
    }

    public void startMultiplayer() throws IOException{
        ActiveGameState.setMultiplayer(true);
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
