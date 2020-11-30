package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
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
        //todo das erste wenn connected
        Parent start = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/start.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }
}
