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


/** CancelGame creates Pop-Ups that are displayed when the user clicks on the red "Exit-Button" of the window:
 *  - a normal Pop-Up that is displayed in every Scene of the Game except the GamePlayground Scene. This one does only
 *    ask if the user is sure if he is sure to close the game
 *  - a extra Pop-Up that is only displayed when the current Scene is the GamePlayground Scene. This Pop-Up asks, if the
 *    user wants to quit with or without saving and if so, gives the ability to enter a name for the saved file.
 *    -> if the user has saved over the playgroundGui it only gives the possibility to head back to Main Menu or Exit
 *       the game
 */
public class CancelGame {

    static Scene choose, save, success;
    static int width = 300;
    static int height = 100;


    /** a normal Pop-Up that is displayed in every Scene of the Game except the GamePlayground Scene. This one does only
     *  ask if the user is sure if he is sure to close the game
     */
    public static void exit() {
        Stage exit = new Stage();
        exit.initModality(Modality.APPLICATION_MODAL);


        Label label = new Label();
        Button yes = new Button();
        yes.setOnAction(e -> {
            HelpMethods.closeMPSockets();
            exit.close();
            NetworkLogger.terminateLogging();
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



    /** a extra Pop-Up that is only displayed when the current Scene is the GamePlayground Scene. This Pop-Up asks, if the
     *  user wants to quit with or without saving and if so, gives the ability to enter a name for the saved file.
     *
     *  This pop-up also appears when the game is saved over the playground gui, but then only screen3 is displayed
     */
    public static void save(boolean alreadySaved) {
        Stage saveStage = new Stage();
        saveStage.initModality(Modality.APPLICATION_MODAL);


        // Scene 1 - asks the user: save or don't save?
        Button saveGame = new Button();
        Button noSave = new Button();
        saveGame.setOnAction(e -> saveStage.setScene(save));
        noSave.setOnAction(e -> {
            HelpMethods.closeMPSockets();
            saveStage.close();
            NetworkLogger.terminateLogging();

            Main.primaryStage.close();
        });

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(saveGame, noSave);
        layout1.setAlignment(Pos.CENTER);
        choose = new Scene(layout1, width, height);
        choose.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");



        // Scene 2 - appears when user wants to save - enter name for saveGame
        // Label + Button
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

            //Create an ID and save it to the ActiveGameState, necessary for loading (Savegame will contain the load id by loading)
            long id = System.currentTimeMillis();
            ActiveGameState.setLoadId(id);

            //The name the user put in -> Create a file with this text as name
            String savegamename = textField.getText();


            boolean saveSuccess;
            //Save the game with ID, when multiplayer is selected
            if (ActiveGameState.isMultiplayer()) {
                saveSuccess = SaveAndLoad.save(savegamename, id);
            }
            else{
                saveSuccess = SaveAndLoad.save(savegamename);
            }


            if(saveSuccess)
                saveStage.setScene(success);
            else {
                final Logger logClient = Logger.getLogger("parent.client");
                logClient.log(Level.SEVERE,"Error at Saving.");
            }
        });

        VBox textAndButton = new VBox(15);
        textAndButton.getChildren().addAll(textField, confirmSave);
        textAndButton.setAlignment(Pos.CENTER);

        save = new Scene(textAndButton, width, height);
        save.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");



        // Scene 3 - successfully saved
        Label successfull = new Label();
        Button endGame = new Button();
        Button backToMainManu = new Button();
        endGame.setOnAction(e -> {
            HelpMethods.closeMPSockets();
            saveStage.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });
        backToMainManu.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            Parent mainMenu;
            try {
                mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                saveStage.close();
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
            saveGame.setText("save game");
            noSave.setText("exit without saving");
            textField.setPromptText("enter file name");
            confirmSave.setText("Save");
            successfull.setText("saving successfull");
            endGame.setText("exit game");
            backToMainManu.setText("Main Menu");
        } else {
            saveGame.setText("Spiel speichern");
            noSave.setText("Beenden ohne Speichern");
            textField.setPromptText("Dateinamen eingeben");
            confirmSave.setText("Speichern");
            successfull.setText("Speichern erfolgreich");
            endGame.setText("Spiel beenden");
            backToMainManu.setText("Hauptmenü");
        }


        if(alreadySaved)
            saveStage.setScene(success);
        else
            saveStage.setScene(choose);
        HelpMethods.alignStageCenter(saveStage, width, height);
        saveStage.setResizable(false);
        saveStage.showAndWait();
    }
}
