package Gui_View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class MpHostController {

    public void establishConnection () throws IOException, InterruptedException {

        wait(20); // todo: statt wait nat. ins nachste Fenster wenn connected
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }
}
