package Gui_View.PopUpWindows;


import Gui_View.HelpMethods;
import Gui_View.Main;
import Player.NetworkLogger;
import Player.ActiveGameState;

import Player.SaveAndLoad;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;


/** CancelGame create a Pop-Up that is displayed when the user clicks on the red "Exit-Button" of the window:
 *  this Pop-Up is displayed in every Scene of the Game and does only ask if the user is sure if he is sure to close the
 *  game
 */
public class CancelGame {

    static Scene choose, save, success;
    static int width = 300;
    static int height = 100;


    public static void exit() {
        Stage exit = new Stage();
        exit.initModality(Modality.APPLICATION_MODAL);


        Label label = new Label();
        Button yes = new Button();
        yes.setOnAction(e -> {
            ActiveGameState.setRunning(false);
            HelpMethods.closeMPSockets();
            exit.close();
            NetworkLogger.terminateLogging();
            ActiveGameState.setLogging(false);
            Main.primaryStage.close();
        });
        Button no = new Button();
        no.setOnAction(e -> exit.close());


        VBox exitBox = new VBox(15);
        HBox yesNo = new HBox(15);
        yesNo.getChildren().addAll(yes, no);
        yesNo.setAlignment(Pos.CENTER);
        exitBox.getChildren().addAll(label, yesNo);
        exitBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(exitBox, width, height);
        scene.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
        HelpMethods.alignStageCenter(exit, width, height);


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            label.setText("Do you really want to exit the game?");
            yes.setText("Yes");
            no.setText("No");
        } else {
            label.setText("Wollen Sie das Spiel wirklich beenden?");
            yes.setText("Ja");
            no.setText("Nein");
        }


        exit.setScene(scene);
        exit.setResizable(false);
        exit.showAndWait();
    }
}
