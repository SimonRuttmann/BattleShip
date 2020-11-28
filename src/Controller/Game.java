package Controller;

import Model.Playground.IEnemyPlayground;
import Model.Util.UtilDataType.Point;
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

public class Game implements Initializable {

    @FXML
    private Button cancleGame;
    @FXML
    private GridPane ownField;
    @FXML
    private GridPane enemyField;

    // todo: Feld zusammenhängend machenn + Window size so, dass ganzes Feld passt aber nicht kleiner
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ownField.addRow(1); // todo add rows abhänging von Spielfeldgroesse
        // 2D Feld für Labels:
        ownField.setHgap(1);
        ownField.setVgap(1);
        enemyField.setHgap(1);
        enemyField.setVgap(1);

        int gamesize = 15;
        //ArrayList<ArrayList<Integer>> ownFieldList = new ArrayList<>(gamesize);
        // todo 2d array or arraylist??

        for (int h = 0; h < gamesize; h++) {
            for (int v = 0; v < gamesize; v++) {
                Label label = new Label();
                label.setStyle("-fx-background-color: lightblue");
                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);
                GridPane.setConstraints(label, h, v);
                //todo label in array oder so speichern, um dann darauf zugreifen zu können???
                ownField.getChildren().addAll(label);
            }
        }

        //enemyField.addRow(1); // todo add rows abhänging von Spielfeldgroesse
        // 2D Feld für Labels:
        for (int h = 0; h < gamesize; h++) {
            for (int v = 0; v < gamesize; v++) {
                Label label= new Label();
                // todo make button clickable
                label.setOnMouseClicked(new GameShootEnemy());
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
           ! important: Objects of grid pane are stored "vertically"

        Object[] enemyFieldArray = new Object[gamesize*gamesize];
        enemyFieldArray = enemyField.getChildren().toArray();
        System.out.println(enemyFieldArray[0].toString());

        Label a = (Label)enemyFieldArray[30];
        a.setStyle("-fx-background-color: red");
        a.setDisable(true);*/


    }

    public void cancleGameMethod() {
        Gui_View.HelpMethods.closeProgrammSaveGame();
    }


}