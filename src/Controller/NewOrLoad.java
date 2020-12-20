package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewOrLoad implements Initializable {

    @FXML Button backButton;
    @FXML Button newGame;
    @FXML Button loadGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(false) // todo get gamemode - only back if singleplayer
        backButton.setVisible(false);
    }

    public void backToLastScene() throws IOException{
        Parent hello = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu2.fxml"));
        Main.primaryStage.setScene(new Scene(hello));
        Main.primaryStage.show();
    }

    public void createGame() throws IOException {
        Parent gameConfig = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gameConfig.fxml"));
        Main.primaryStage.setScene(new Scene(gameConfig));
        Main.primaryStage.show();
    }

    public void loadOldGame() throws IOException {
        Parent loadGame = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }
}
