package Gui_View.PopUpWindows;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Player.ActiveGameState;
import Player.NetworkLogger;

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

/** ConnectionFailed creates a PopUp that informs the user that it was not possible to establish a connection with the
 *  remote partner and lets him choose whether he wants to go back to the Main Menu oder Exit the Game
 */
public class ConnectionFailed {

    static Scene conFailed;
    static int width = 300;
    static int height = 100;

    public static void display() {
        Stage failed = new Stage();
        failed.initModality(Modality.APPLICATION_MODAL);

        Label showError = new Label();
        Button backToMainMenu = new Button();
        Button endGame = new Button();
        backToMainMenu.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            Parent mainMenu;
            try {
                mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                failed.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endGame.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            failed.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToMainMenu, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);


        conFailed = new Scene(layout1, width, height);
        conFailed.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            showError.setText("establishing connection failed");
            backToMainMenu.setText("Main Menu");
            endGame.setText("Exit Game");
        } else {
            showError.setText("Verbindungsaufbau fehlgeschlagen");
            backToMainMenu.setText("Hauptmenü");
            endGame.setText("Spiel beenden");
        }


        HelpMethods.alignStageCenter(failed, width, height);
        failed.setScene(conFailed);
        failed.setResizable(false);
        failed.showAndWait();
    }
}
