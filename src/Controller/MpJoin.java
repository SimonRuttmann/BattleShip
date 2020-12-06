package Controller;

import Gui_View.Main;
import Network.Client;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MpJoin {

    @FXML private Button backButton;
    @FXML private TextField playerName;
    @FXML private TextField hostIP;
    @FXML private Button connectButton;
    @FXML private ProgressIndicator loadingIndicator;


    public void backToLastScene() throws IOException{
        Parent mpSelect = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }

    // todo -> read from text field, make connect in bg, show spinner
    public void connect() throws IOException {
        loadingIndicator.setVisible(true);
        connectButton.setVisible(false);
        System.out.println("connectButton pressed");

        //todo schauen ob korrekte ip adresse
        ActiveGameState.setClient(new Client(hostIP.getText()));

        //todo das erste wenn connected
        Parent start = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/chooseSelfOrKi.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }
}
