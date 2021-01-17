package Gui_View;


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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CancelGame {

    static Scene choose, save, success;
    static int width = 300;
    static int height = 100;

    public static void exit() {
        Stage exit = new Stage();
        exit.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Wollen Sie das Spiel wirklich beenden?");
        Button yes = new Button("Ja");
        yes.setOnAction(e -> {
            exit.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });
        Button no = new Button("Nein");
        no.setOnAction(e -> {
            exit.close();
        });

        VBox exitBox = new VBox(15);
        HBox yesNo = new HBox(15);
        yesNo.getChildren().addAll(yes, no);
        yesNo.setAlignment(Pos.CENTER);
        exitBox.getChildren().addAll(label, yesNo);
        exitBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(exitBox, width, height);
        exit.setScene(scene);
        scene.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
        HelpMethods.alignStageCenter(exit, width, height);
        exit.setResizable(false);
        exit.showAndWait();
    }



    public static void save() {
        Stage saveStage = new Stage();
        saveStage.initModality(Modality.APPLICATION_MODAL);

        // Scene 1 - save or don't save
        Button saveGame = new Button("Spiel speichern");
        saveGame.setOnAction(e -> {

            saveStage.setScene(save);
        });
        Button noSave = new Button("Beenden ohne Speichern");
        noSave.setOnAction(e -> {
            saveStage.close();
            NetworkLogger.terminateLogging();

            Main.primaryStage.close();
        });

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(saveGame, noSave);
        layout1.setAlignment(Pos.CENTER);
        choose = new Scene(layout1, width, height);
        choose.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");



        // Scene 2 - enter name for saveGame
        // Label + Button
        TextField textField = new TextField();
        textField.setPromptText("Speichernamen eingeben");
        textField.setMaxWidth(250);
        Button saveAndMain = new Button("Speichern");
        saveAndMain.setDisable(true);

        // saveAndMain is only clickable, when save name is valid
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            //The name the user put in -> Create a file with this text as name
            String savegamename = textField.getText();

            //Check for not allowed characters -> no File creation possible
            char[] notAllowedCharacters = {'\\','/',':','!','?','*','"','|','<','>'};
            for ( char character : notAllowedCharacters){
                if (savegamename.contains(String.valueOf(character)) || savegamename.isEmpty()) {
                    saveAndMain.setDisable(true);
                    return;
                }
            }
            saveAndMain.setDisable(false);
        });



        saveAndMain.setOnAction(event -> {

            //todo this is copied from game Playground
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


            if(saveSuccess){
                saveStage.setScene(success);
                //TODO YANNICK Display -> Spiel wurde gespeichert -> Zum Hauptmenü zurückkehren (Am besten anch 3-5 sekunden, kein button benötigt) Dabei alle Threads beenden (interrupt)

            }
            else {
                //TODO LoggingNetwork Level SEVERE -> Error at saving
            }
        });

        VBox textAndButton = new VBox(15);
        textAndButton.getChildren().addAll(textField, saveAndMain);
        textAndButton.setAlignment(Pos.CENTER);

        save = new Scene(textAndButton, width, height);
        save.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");



        // Scene 3 - successfully saved
        Label successfull = new Label("Speichern erfolgreich!");
        Button endGame = new Button("Spiel beenden");
        endGame.setOnAction(e -> {

            //save.close();
            NetworkLogger.terminateLogging();

            Main.primaryStage.close();
        });
        Button backToMainManu = new Button("Hauptmenü");
        backToMainManu.setOnAction(event -> {
            Parent mainMenu = null;
            try {
                mainMenu = FXMLLoader.load(unexceptedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
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

        saveStage.setScene(choose);
        HelpMethods.alignStageCenter(saveStage, width, height);
        saveStage.setResizable(false);
        saveStage.showAndWait();
    }
}
