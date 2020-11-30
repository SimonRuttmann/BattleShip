// todo: richtig Speichern mit Robin - evtl sogar mit success, Styling

package Gui_View;

import Player.SaveAndLoad;
import Player.Savegame;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CancelGame {

    static Scene choose, success;
    static int width = 300;
    static int height = 100;

    public static void exit() {
        Stage exit = new Stage();
        exit.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Wollen Sie das Spiel wirklich beenden?");
        Button yes = new Button("Ja");
        yes.setOnAction(e -> {
            exit.close();
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
        Stage save = new Stage();
        save.initModality(Modality.APPLICATION_MODAL);

        // Scene 1 - save or don't save
        Button saveGame = new Button("Spiel speichern");
        saveGame.setOnAction(e -> {
            //todo get current Gamestats into Savegame Object -> gamemode in Dateiname - Multiplayer vs Singeplayer
            Savegame toSave = new Savegame();
            String time = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            String temp = "Spiel am: " + time; // todo besserer Name
            if(SaveAndLoad.save(toSave, temp))
                save.setScene(success);
            //else
            //    save.setScene(failure);//todo create scene failure
        });
        Button noSave = new Button("Beenden ohne Speichern");
        noSave.setOnAction(e -> {
            save.close();
            Main.primaryStage.close();
        });

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(saveGame, noSave);
        layout1.setAlignment(Pos.CENTER);
        choose = new Scene(layout1, width, height);
        choose.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");


        // Scene 2 - successfully saved
        Label successfull = new Label("Speichern erfolgreich!"); // todo: wir gehen aktuell davon aus, das Speichern immer erfolgreich
        Button endGame = new Button("Spiel beenden"); // todo: eventuell rückkehr ins startmenü: not so easy
        endGame.setOnAction(e -> {
            save.close();
            Main.primaryStage.close();
        });
        /* todo evlt
        Button backToMainManu = new Button("Zurück ins Hauptmenü");
        backToMainManu.setOnAction(e -> {
            HelpMethods backto = new HelpMethods();
            backto.backToMainMenu();
        });*/

        VBox layout2 = new VBox(15);
        layout2.getChildren().addAll(successfull, endGame);
        layout2.setAlignment(Pos.CENTER);
        success = new Scene(layout2, width, height);
        success.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");

        save.setScene(choose);
        HelpMethods.alignStageCenter(save, width, height);
        save.setResizable(false);
        save.showAndWait();
    }
}
