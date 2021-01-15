// todo Styling, Aufruf case win or lose: automatisch am Spielende, boolean true false muss uebergeben werden
// todo bei neues Spiel starten, neue Objekte etc. ???

package Gui_View.PopUpWindows;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Player.ActiveGameState;
import Player.NetworkLogger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


/** WinLose creates PopUps that inform the user whether he has won or lost the game
 *  these let him also decide whether he wants to head back to the Main Menu or to completely close the game
 */
public class WinLose {

    static Scene win, lose;
    static int width = 300;
    static int height = 150;

    public static void display(boolean winTrue) {
        Stage winLose = new Stage();
        winLose.initModality(Modality.APPLICATION_MODAL);

        // window decoration not shown -> this pop-Up can not be closed
        winLose.initStyle(StageStyle.UNDECORATED);


        Button backToMainMenu = new Button();
        Button end = new Button();
        backToMainMenu.setOnAction(event -> {
            HelpMethods.closeMPSockets();
            Parent mainMenu;
            try {
                HelpMethods.closeMPSockets();
                mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                winLose.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        end.setOnAction(e -> {
            HelpMethods.closeMPSockets();
            winLose.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });


        Label winner = new Label();
        Label loser = new Label();
        if (winTrue) {
            // Scene 1 - win
            VBox layout1 = new VBox(15);
            layout1.getChildren().addAll(winner, backToMainMenu, end);
            layout1.setAlignment(Pos.CENTER);
            win = new Scene(layout1, width, height);
            win.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
            winLose.setScene(win);
        } else {
            // Scene 2 - lose
            VBox layout2 = new VBox(15);
            layout2.getChildren().addAll(loser, backToMainMenu, end);
            layout2.setAlignment(Pos.CENTER);
            lose = new Scene(layout2, width, height);
            lose.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");
            winLose.setScene(lose);
        }


        // language settings
        if(ActiveGameState.getLanguage() == ActiveGameState.Language.english) {
            backToMainMenu.setText("Main Menu");
            end.setText("Exit Game");
            winner.setText("You've won!");
            loser.setText("You've lost :(");
        } else {
            backToMainMenu.setText("Hauptmenü");
            end.setText("Spiel beenden");
            winner.setText("Gewonnen!");
            loser.setText("Verloren :(");
        }


        HelpMethods.alignStageCenter(winLose, width, height);
        winLose.setResizable(false);
        winLose.showAndWait();
    }
}
