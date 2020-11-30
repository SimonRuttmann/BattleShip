// todo Styling, Aufruf case win or lose: automatisch am Spielende, boolean true false muss uebergeben werden
// todo bei neues Spiel starten, neue Objekte etc. ???

package Gui_View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinLose {

    static Scene win, lose;
    static int width = 300;
    static int height = 100;

    public static void display(boolean winTrue) {
        Stage winLose = new Stage();
        winLose.initModality(Modality.APPLICATION_MODAL);

        Button startAgain = new Button("Neues Spiel");
        startAgain.setOnAction(f -> {
            // Versehentliches Schließen des Spiels verhindern - zurück zu ohne Speicheraufforderung
            Main.primaryStage.setOnCloseRequest(e -> {
                e.consume();
                HelpMethods.closeProgramm();
            });
            // todo neues Spiel starten - im Hintergrund??
            // am einfachsten wohl Programm neu starten
        });

        Button end = new Button("Spiel beenden");
        end.setOnAction(e -> {
            winLose.close();
            Main.primaryStage.close();
        });

        if (winTrue) {
            Label winner = new Label("Gewonnen !");
            VBox layout1 = new VBox(15);
            layout1.getChildren().addAll(winner, startAgain, end);
            layout1.setAlignment(Pos.CENTER);
            win = new Scene(layout1, width, height);
            win.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
            winLose.setScene(win);
        } else {
            // Scene 2 - lose
            Label loser = new Label("Verloren :(");
            VBox layout2 = new VBox(15);
            layout2.getChildren().addAll(loser, startAgain, end);
            layout2.setAlignment(Pos.CENTER);
            lose = new Scene(layout2, width, height);
            lose.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
            winLose.setScene(lose);
        }
        HelpMethods.alignStageCenter(winLose, width, height);
        winLose.setResizable(false);
        winLose.showAndWait();
    }
}
