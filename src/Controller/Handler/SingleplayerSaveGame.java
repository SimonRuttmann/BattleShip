package Controller.Handler;

import Player.SaveAndLoad;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SingleplayerSaveGame {


    public void save(long id) {
        SaveAndLoad.save(String.valueOf(id));
        Platform.runLater( () -> {
            //TODO Display speichern erfolgreich...
        });
    }
}
