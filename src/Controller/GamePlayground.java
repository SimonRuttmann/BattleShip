package Controller;

import Controller.Handler.MultiplayerControlThreadPerformEnemyAction;
import Gui_View.Main;
import Model.Playground.IEnemyPlayground;
import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;
import Player.Savegame;
import Controller.Handler.GameShootEnemy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Handler;

public class GamePlayground implements Initializable {

    @FXML
    private Button cancleGame;
    @FXML
    private GridPane ownField;
    @FXML
    private GridPane enemyField;
    @FXML
    private Label ownFieldLabel;
    @FXML
    private Label enemyFieldLabel;

    // todo: Feld zusammenhängend machen + Window size so, dass ganzes Feld passt aber nicht kleiner
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Gui_View.HelpMethods.closeProgrammSaveGame();
        });


        ActiveGameState.setSceneIsGamePlayground(true);
        ActiveGameState.setSceneIsPlaceShips(false);
        // set Labels to Player Names
        ownFieldLabel.setText(ActiveGameState.getOwnPlayerName() + "'s Spielfeld");
        // todo evtl für Gegner - bekommen wir einen Namen?????
        if(ActiveGameState.isMultiplayer())
            System.out.println("gegenername");/// name from gegner todo
        else
            System.out.println("bot name");// name für ki überlegen todo

        // todo: Scene builder -> langer Name + kleines Feld: Felder not in Mitte -- verbessern
        // todo -> oder eben kein name- einfach eigenes feld gegnerisches feld, name nur fürs abspeichern

        // 2D fields for Labels:
        int gamesize = ActiveGameState.getPlaygroundSize();
        ownField.setHgap(1);
        ownField.setVgap(1);
        enemyField.setHgap(1);
        enemyField.setVgap(1);

        // own Playground
        for (int h = 0; h < gamesize; h++) {
            for (int v = 0; v < gamesize; v++) {
                Label label = new Label();

                //fliegt später raus
                label.setStyle("-fx-background-color: lightblue");

                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);
                GridPane.setConstraints(label, h, v);
                ownField.getChildren().addAll(label);
            }
        }

        // enemy Playground
        for (int h = 0; h < gamesize; h++) {
            for (int v = 0; v < gamesize; v++) {
                Label label= new Label();


                // every labels gets it's handler: GameShootEnemy -> activated on mouse click: fire shot
                label.setOnMouseClicked(new GameShootEnemy());

                //todo fliegt später raus
                label.setStyle("-fx-background-color: lightblue");

                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);
                GridPane.setConstraints(label, h, v);
                enemyField.getChildren().addAll(label);
            }
        }

        /* the elements of a grid-pane can be returned as an array of Objects - cast Objetct to Label, than it is possible
           to change the properties of the Label, e.g. the color
           ! important: Objects of grid pane are stored "vertically"*/

        // connect Labels to Playground - labels are saved in arrays
        Object[] ownFieldArray = new Object[gamesize*gamesize];
        ownFieldArray = ownField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();

        Object[] enemyFieldArray = new Object[gamesize*gamesize];
        enemyFieldArray = enemyField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIEnemyPlayground().setLabels(enemyFieldArray);
        ActiveGameState.getOwnPlayerIEnemyPlayground().drawPlayground();

        //Client -> Zuerst ist der Server dran -> Setze alle Labels im gegnerischen Spielfeld nicht klickbar
        // Starte den Perform Enemy Action Thread um auf die Eingaben des Servers zu reagieren -> Danach PingPong Prinzip
        if ( ActiveGameState.isMultiplayer() && ! ActiveGameState.isAmIServer()){
            MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
            multiplayerControlThreadPerformEnemyAction.start();
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
        }
    }

    // when Button cancleGame is pressed - save or no saving?
    public void cancleGameMethod() {
        Gui_View.HelpMethods.closeProgrammSaveGame();
    }
}