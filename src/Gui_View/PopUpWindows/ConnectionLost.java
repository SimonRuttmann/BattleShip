package Gui_View.PopUpWindows;


import Gui_View.HelpMethods;
import Gui_View.Main;
import GameData.ActiveGameState;
import LoggingNetwork.NetworkLogger;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


/** ConnectionLost creates a PopUp that informs the user that the connection to the remote partner was lost and lets him
 *  choose whether he wants to go back to the Main Menu oder Exit the Game
 */
public class ConnectionLost {

    static Scene conLost;
    static int width = 300;
    static int height = 100;

    public static void display() {
        Stage lost = new Stage();
        lost.initModality(Modality.APPLICATION_MODAL);


        Label showError = new Label();
        Button backToMainMenu = new Button();
        Button endGame = new Button();
        backToMainMenu.setOnAction(event -> {
            Parent mainMenu;
            try {

                HelpMethods.closeMPSockets();

                mainMenu = FXMLLoader.load(ConnectionLost.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                lost.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endGame.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            lost.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });


        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToMainMenu, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);


        conLost = new Scene(layout1, width, height);
        conLost.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            showError.setText("connection lost");
            backToMainMenu.setText("Main Menu");
            endGame.setText("Exit Game");
        } else {
            showError.setText("Verbindung verloren");
            backToMainMenu.setText("Hauptmenü");
            endGame.setText("Spiel beenden");
        }


        lost.setScene(conLost);
        HelpMethods.alignStageCenter(lost, width, height);
        lost.setResizable(false);
        lost.showAndWait();
    }
}
