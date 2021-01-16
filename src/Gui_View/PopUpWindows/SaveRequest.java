package Gui_View.PopUpWindows;

import Gui_View.HelpMethods;
import Gui_View.Main;

import Gui_View.PopUpWindows.CancelGame;

import Player.ActiveGameState;
import Player.NetworkLogger;
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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/** SaveRequest creates Pop-Up with two Scenes:
 *  1. when remote Player in MP wants to save, the user has to enter a name for the gamefile.
 *  2. success: when successfully saved, this scene is shown. The user now can go back to Main Menu or Exit the Game.
 *     the pop-Up with this scene is also shown when the player wants to save by himself in GamePlayground
 */
public class SaveRequest {

    static Scene save, success;
    static int width = 300;
    static int height = 100;

    public static void display(long id, boolean alreadySaved) {

        Stage saveRequest = new Stage();
        saveRequest.initModality(Modality.APPLICATION_MODAL);

        // window decoration not shown -> this pop-Up can not be closed
        saveRequest.initStyle(StageStyle.UNDECORATED);


        // Scene 1: Enter Save Name
        // Label + Button
        Label remoteSaveRequest = new Label();
        TextField textField = new TextField();
        textField.setMaxWidth(250);
        Button confirmSave = new Button();
        confirmSave.setDisable(true);

        // confirmSave is only clickable, when save name is valid
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            //The name the user put in -> Create a file with this text as name
            String savegamename = textField.getText();

            //Check for not allowed characters -> no File creation possible
            char[] notAllowedCharacters = {'\\','/',':','!','?','*','"','|','<','>'};
            for ( char character : notAllowedCharacters){
                if (savegamename.contains(String.valueOf(character)) || savegamename.isEmpty()) {
                    confirmSave.setDisable(true);
                    return;
                }
            }
            confirmSave.setDisable(false);
        });


        confirmSave.setOnAction(event -> {

            //Take the received id and save it to the ActiveGameState, necessary for loading (SaveGame will contain the load id by loading)
            ActiveGameState.setLoadId(id);

            //The name the user put in -> Create a file with this text as name
            String savegamename = textField.getText();


            boolean saveSuccess;
            saveSuccess = SaveAndLoad.save(savegamename, id);


            if(saveSuccess)
                saveRequest.setScene(success);
            else {
                final Logger logClient = Logger.getLogger("parent.client");
                logClient.log(Level.SEVERE,"Error at Saving.");
            }
        });


        HBox textAndButton = new HBox(15);
        textAndButton.getChildren().addAll(textField, confirmSave);
        textAndButton.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(remoteSaveRequest, textAndButton);
        layout1.setAlignment(Pos.CENTER);

        save = new Scene(layout1, width, height);
        save.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");



        // Scene 2: successfully saved - called when save name entered in popUp or when saving from gameplayground
        Label successfull = new Label();
        Button endGame = new Button();
        Button backToMainManu = new Button();
        endGame.setOnAction(e -> {

            HelpMethods.closeMPSockets();
            saveRequest.close();
            ActiveGameState.setLogging(false);
            NetworkLogger.terminateLogging();

            Main.primaryStage.close();

        });
        backToMainManu.setOnAction(event -> {

            HelpMethods.closeMPSockets();

            Parent mainMenu;
            try {
                mainMenu = FXMLLoader.load(CancelGame.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                saveRequest.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToMainManu, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout2 = new VBox(15);
        layout2.getChildren().addAll(successfull, buttons);
        layout2.setAlignment(Pos.CENTER);

        success = new Scene(layout2, width, height);
        success.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            remoteSaveRequest.setText("Remote wants to save game");
            textField.setPromptText("Enter file name");
            confirmSave.setText("Save");
            successfull.setText("saving successfull");
            endGame.setText("Exit Game");
            backToMainManu.setText("Main Menu");
        } else {
            remoteSaveRequest.setText("Speicheranfrage vom Spielpartner");
            textField.setPromptText("Dateinamen eingeben");
            confirmSave.setText("Speichern");
            successfull.setText("Speichern erfolgreich");
            endGame.setText("Spiel beenden");
            backToMainManu.setText("Hauptmenü");
        }


        if(alreadySaved)
            saveRequest.setScene(success);
        else
            saveRequest.setScene(save);
        HelpMethods.alignStageCenter(saveRequest, width, height);
        saveRequest.setResizable(false);
        saveRequest.showAndWait();
    }
}