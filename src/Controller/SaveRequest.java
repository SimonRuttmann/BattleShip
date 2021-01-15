package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;

import Gui_View.PopUpWindows.unexpectedMessageFromRemote;

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
import javafx.stage.StageStyle;

import java.io.IOException;

public class SaveRequest {

    static Scene save;
    static int width = 300;
    static int height = 100;

    public static void display(long id) {

        Stage saveRequest = new Stage();
        saveRequest.initModality(Modality.APPLICATION_MODAL);

        // kein Stage Style -> kann nicht abgebrochen werden
        saveRequest.initStyle(StageStyle.UNDECORATED);

        // Label + Button
        Label remoteSaveRequest = new Label("Speicheranfrage vom Spielpartner!");
        TextField textField = new TextField();
        textField.setPromptText("Speichernamen eingeben");
        textField.setMaxWidth(250);
        Button saveAndMain = new Button("Speichern & Hauptmenü");
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
                Parent mainMenu = null;
                try {
                    mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                    Main.primaryStage.setScene(new Scene(mainMenu));
                    saveRequest.close();
                    Main.primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TODO YANNICK Display -> Spiel wurde gespeichert -> Zum Hauptmenü zurückkehren (Am besten anch 3-5 sekunden, kein button benötigt) Dabei alle Threads beenden (interrupt)

            }
            else {
                //TODO LoggingNetwork Level SEVERE -> Error at saving
            }
        });

        HBox textAndButton = new HBox(15);
        textAndButton.getChildren().addAll(textField, saveAndMain);
        textAndButton.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(remoteSaveRequest, textAndButton);
        layout1.setAlignment(Pos.CENTER);

        save = new Scene(layout1, width, height);
        save.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");

        saveRequest.setScene(save);
        HelpMethods.alignStageCenter(saveRequest, width, height);
        saveRequest.setResizable(false);
        saveRequest.showAndWait();
    }
}