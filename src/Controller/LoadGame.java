package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Player.ActiveGameState;
import Player.SaveAndLoad;
import Player.Savegame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadGame implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private ListView<File> gameList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // create observable list of the Game Files
        ObservableList<File> observableList = FXCollections.observableArrayList();
        // dir is the folder that contains our saved game files - different for singleplayer and multiplayer
        File dir;
        if(ActiveGameState.isMultiplayer())         //TODO AN DER DATEIENDUNG HERUMZUPFUSCHEN IST MURKS HOCH 10 -> ORDNER SINGLEPLAYER UND ORDNER MULTIPLAYER -> SAVEGAME DARAUS LADEN -> DIE DATEIENDUNG BLEIBT NATÜRLICH .json, DA ES OFFENSICHTLICHERWEISE EINE JSON DATEI IST...
            dir = new File(".savedGames"); //todo mit simon: dateiendung -> besser .svsingle .svmulti ?? und laden only multiplayer with ip????
        else
            dir = new File(".savedGames"); // todo: dann statt über ordner über filename schauen???

        File[] savedGames = dir.listFiles((directory, filename) -> filename.endsWith(".json"));
        // add files to observable List and furthermore to gameList
        observableList.clear();
        observableList.addAll(savedGames);
        gameList.setItems(observableList);


        // use Cell Factory to display only the Name of the Files + make ContextMenu for loading/deleting gameFile
        gameList.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> gameList) {

                // change updateItem method - display name of file
                ListCell<File> cell = new ListCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null)
                            setText("");
                        else
                            setText(item.getName());
                    }
                };


                // create context menu + function for its items
                ContextMenu contextMenu = new ContextMenu();

                // load -> changes scene to game, loads gamestats from saved game into gameObject
                // -> gameObject into ???? todo
                MenuItem load = new MenuItem("Spielstand laden");
                load.setOnAction(e -> {
                    try {
                        // loading .json file from memory: .savedGames
                        System.out.println(cell.getItem()); //todo savegame not needed
                        Savegame gameObject = new Savegame();
                        String temp = cell.getItem().toString();
                        gameObject = SaveAndLoad.load(gameObject, temp);
                        // todo test if loading was successful -> not correct at the moment
                        if (gameObject != null) {
                            System.out.println("loading successful");
                        }
                        // load Savedgame object into ???????? todo
                        // todo now we have a Savegameobject: gameObject -> todo -> load game


                        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
                        Main.primaryStage.setOnCloseRequest(j -> {
                            j.consume();
                            HelpMethods.closeProgrammSaveGame();
                        });
                        // Change scene to game Playground
                        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                        Main.primaryStage.setScene(new Scene(game));
                        Main.primaryStage.show();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }); // todo evtl own thread

                // delete -> deltes game file from list and also from system
                MenuItem delete = new MenuItem("Spielstand löschen");
                delete.setOnAction(e -> {
                    System.out.println("Delete Item" + cell.getItem().toString()); // todo löschen funktioniert noch nicht
                    gameList.getItems().remove(cell.getItem());
                });
                contextMenu.getItems().addAll(load, delete);

                // display context menu only for cells that contain file - not for empty cells
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });

                return cell;
            }
        });
    }

    public void backToLastScene() throws IOException {
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }
}
