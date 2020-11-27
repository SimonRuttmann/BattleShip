// todo: richtig Speichern mit Robin - evtl sogar mit success, Styling

package Gui_View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

        exit.setScene(new Scene(exitBox, width, height));
        HelpMethods.alignStageCenter(exit, width, height);
        exit.showAndWait();
    }

    public static void save() {
        Stage save = new Stage();
        save.initModality(Modality.APPLICATION_MODAL);

        // Scene 1 - save or don't save
        Button saveGame = new Button("Spiel speichern");
        saveGame.setOnAction(e -> {
            save.setScene(success);
        }); // todo save - hier dafür sorgen, dass gespeichert wird -> mit Robin
        Button noSave = new Button("Beenden ohne Speichern");
        noSave.setOnAction(e -> {
            save.close();
            Main.primaryStage.close();
        });

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(saveGame, noSave);
        layout1.setAlignment(Pos.CENTER);
        choose = new Scene(layout1, width, height);


        // Scene 2 - successfully saved
        Label successfull = new Label("Speichern erfolgreich!"); // todo: wir gehen aktuell davon aus, das Speichern immer erfolgreich
        Button endGame = new Button("Spiel beenden");
        endGame.setOnAction(e -> {
            save.close();
            Main.primaryStage.close();
        });

        VBox layout2 = new VBox(15);
        layout2.getChildren().addAll(successfull, endGame);
        layout2.setAlignment(Pos.CENTER);
        success = new Scene(layout2, width, height);

        save.setScene(choose);
        HelpMethods.alignStageCenter(save, width, height);
        save.showAndWait();
    }
}
