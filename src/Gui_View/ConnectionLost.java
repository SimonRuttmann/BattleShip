package Gui_View;

import Player.NetworkLogger;
import Player.SaveAndLoad;
import Player.Savegame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConnectionLost {

    static Scene conLost;
    static int width = 300;
    static int height = 100;

    public static void display() {
        Stage lost = new Stage();
        lost.initModality(Modality.APPLICATION_MODAL);

        // todo implement funktion + speicheranfrage ?
        Label showError = new Label("Verbindung verloren");
        Button backToStart = new Button("Hauptmenü");
        Button endGame = new Button("Spiel beenden");
        backToStart.setOnAction(event -> {
            // todo -> go back. how??? static???
        });
        endGame.setOnAction(event -> {
            lost.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToStart, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);


        conLost = new Scene(layout1, width, height);
        conLost.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");

        lost.setScene(conLost);
        HelpMethods.alignStageCenter(lost, width, height);
        lost.setResizable(false);
        lost.showAndWait();
    }
}
