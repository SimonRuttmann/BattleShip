package Controller;

import Gui_View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class MpHost {

    public void establishConnection () throws IOException, InterruptedException {

        wait(20); // todo: statt wait nat. ins nachste Fenster wenn connected
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }
}
