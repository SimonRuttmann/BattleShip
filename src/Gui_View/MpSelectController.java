package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MpSelectController {

    @FXML private Button hostGame;
    @FXML private Button joinGame;

    public void offerConnection() throws IOException {
        Parent mpHost = FXMLLoader.load(getClass().getResource("mpHost.fxml"));
        Main.primaryStage.setScene(new Scene(mpHost));
        Main.primaryStage.show();
    }

    public void acceptConnection() throws IOException{
        Parent mpJoin = FXMLLoader.load(getClass().getResource("mpJoin.fxml"));
        Main.primaryStage.setScene(new Scene(mpJoin));
        Main.primaryStage.show();
    }
}
