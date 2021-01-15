package Gui_View.PopUpWindows;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Player.ActiveGameState;
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


/** unexpectedMessageFromRemote informs the user that there was an unexpected message send by the remote
 *  -> failure of remote programm
 *  -> user can go back to Main Menu or Exit the Game
 */

public class unexpectedMessageFromRemote {

    static Scene wrongMessage;
    static int width = 300;
    static int height = 100;

    public static void display() {

        Stage unexceptedMessage = new Stage();
        unexceptedMessage.initModality(Modality.APPLICATION_MODAL);


        // Label + Button
        Label showError = new Label();
        Button backToMainMenu = new Button();

        backToMainMenu.setOnAction(event -> {
            Parent mainMenu;
            try {

                HelpMethods.closeMPSockets();

                mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                unexceptedMessage.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                HelpMethods.closeMPSockets();
                e.printStackTrace();
            }
        });
        Button endGame = new Button();
        endGame.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            unexceptedMessage.close();
            Main.primaryStage.close();
        });


        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToMainMenu, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);

        wrongMessage = new Scene(layout1, width, height);
        wrongMessage.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            showError.setText("unexpected message from remote");
            endGame.setText("exit game");
            backToMainMenu.setText("Main Menu");
        } else {
            showError.setText("Unerwartete Nachricht vom Spielpartner");
            endGame.setText("Spiel beenden");
            backToMainMenu.setText("Hauptmenü");
        }


        unexceptedMessage.setScene(wrongMessage);
        HelpMethods.alignStageCenter(unexceptedMessage, width, height);
        unexceptedMessage.setResizable(false);
        unexceptedMessage.showAndWait();
    }
}
