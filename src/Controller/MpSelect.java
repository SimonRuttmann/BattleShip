package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MpSelect {

    @FXML private Button hostGame;
    @FXML private Button joinGame;

    public void offerConnection() throws IOException {
        Parent mpHost = FXMLLoader.load(getClass().getResource("/Gui_View/mpHost.fxml"));
        Main.primaryStage.setScene(new Scene(mpHost));
        Main.primaryStage.show();
    }

    public void acceptConnection() throws IOException{
        Parent mpJoin = FXMLLoader.load(getClass().getResource("/Gui_View/mpJoin.fxml"));
        Main.primaryStage.setScene(new Scene(mpJoin));
        Main.primaryStage.show();
    }
}
