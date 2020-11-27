package Gui_View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MpJoinController {

    @FXML private Button connectButton;

    public void connect() throws IOException {
        Parent start = FXMLLoader.load(getClass().getResource("start.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }
}
