package Gui_View.PopUpWindows;

import Gui_View.HelpMethods;
import Gui_View.Main;
import GameData.ActiveGameState;
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


/** noMatchingSavedGame informs the user that he does not have the saved game the host wants to load
 *  -> gives possibility to go back to Main Menu or Exit the Game
 */
public class noMatchingSavedGame {

    static Scene noGameFile;
    static int width = 300;
    static int height = 100;

    public static void display() {

        Stage noMatchingSavedGameFile = new Stage();
        noMatchingSavedGameFile.initModality(Modality.APPLICATION_MODAL);

        // Label + Button
        Label showError = new Label();

        Button backToMainMenu = new Button();
        Button endGame = new Button();
        backToMainMenu.setOnAction(event -> {
            Parent mainMenu;
            try {
                HelpMethods.closeMPSockets();
                mainMenu = FXMLLoader.load(noMatchingSavedGame.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                noMatchingSavedGameFile.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endGame.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            noMatchingSavedGameFile.close();
            Main.primaryStage.close();
        });


        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToMainMenu, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);

        noGameFile = new Scene(layout1, width, height);
        noGameFile.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            showError.setText("required game file not existing");
            endGame.setText("exit game");
            backToMainMenu.setText("Main Menu");
        } else {
            showError.setText("Zu ladender Spielstand nicht vorhanden");
            endGame.setText("Spiel beenden");
            backToMainMenu.setText("Hauptmenü");
        }


        noMatchingSavedGameFile.setScene(noGameFile);
        HelpMethods.alignStageCenter(noMatchingSavedGameFile, width, height);
        noMatchingSavedGameFile.setResizable(false);
        noMatchingSavedGameFile.showAndWait();
    }
}
