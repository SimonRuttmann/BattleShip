package Controller;

import Controller.Handler.MultiplayerControlThreadKiShootsEnemy;
import Controller.Handler.MultiplayerControlThreadPerformEnemyAction;
import Controller.Handler.SingleplayerControlThreadKiVsKi;
import Gui_View.Main;
import KI.Ki;
import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Player.ActiveGameState;
import Player.GameMode;
import Controller.Handler.GameShootEnemy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GamePlayground implements Initializable {


    public Button saveGame;
    // import from FXML
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
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Group groupOwnP;
    @FXML
    private Group groupEnemP;


    int gamesize = ActiveGameState.getPlaygroundSize();


    /** initialize-method:
     * -----------------------------------------------------------------------------------------------------------------
     *  ->
     *----------------------------------------------------------------------------------------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ActiveGameState.setSceneIsGamePlayground(true);
        ActiveGameState.setSceneIsPlaceShips(false);


        // if ki is part of game, playgrounds are not created in placeShips -> done here
        initializeKiPlayground();
        /**
         * After playground is initialized the thread SingleplayerControlThreadKivsKi has to be started, if the Gamemode KivsKi is selected
         */
        if ( ActiveGameState.getModes() == GameMode.kiVsKi){
            SingleplayerControlThreadKiVsKi singleplayerControlThreadKiVsKi = new SingleplayerControlThreadKiVsKi();
            singleplayerControlThreadKiVsKi.start();
        }

        /**
         * Gamemode kiVsRemote -> Start Perform enemy action, if we are Client ( enemy´s Turn )
         *                      -> Start KI Shoots Enemy, if we are Server ( our Turn)
         */
        if ( ActiveGameState.getModes() == GameMode.kiVsRemote){
            if ( !ActiveGameState.isAmIServer()){
                MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
                multiplayerControlThreadPerformEnemyAction.start();
            }
            else{
                MultiplayerControlThreadKiShootsEnemy multiplayerControlThreadKiShootsEnemy = new MultiplayerControlThreadKiShootsEnemy();
                multiplayerControlThreadKiShootsEnemy.start();
            }
        }
        // initialize the static variable groupEnemyPS -> used in MultiplayerControlThreadShootEnemy
        groupEnemyPS = groupEnemP;


        //prevent the player from closing the game accidentally -> should be asked wether he wants to save or hard quit
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Gui_View.HelpMethods.closeProgrammSaveGame();
        });


        //The scale of one Field,   Ship size 2 -> Image: | 30px | 30px |
        //                          Ship size 3 -> Image: | 30px | 30px | 30px |
        // scale is depended on playground size
        int scale = 30;
        if (5 <= gamesize && gamesize <= 10) {
            scale = 45;
        } else if (11 <= gamesize && gamesize <= 15) {
            scale = 35;
        } else if (16 <= gamesize && gamesize <= 20) {
            scale = 25;
        } else if (21 <= gamesize && gamesize <= 25) {
            scale = 20;
        } else if (26 <= gamesize && gamesize <= 30) {
            scale = 15;
        }

        // finalscale is needed due to using scale in lambda expressions
        final int finalscale = scale;



        // playground field --------------------------------------------------------------------------------------------
        // set Labels for the Playgrounds
        ownFieldLabel.setText("Own Playground");
        enemyFieldLabel.setText("Enemy Playground");


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

                // set background for labels
                label.setStyle("-fx-background-color: lightblue");

                label.setMinSize(scale, scale);
                label.setPrefSize(scale, scale);
                label.setMaxSize(scale, scale);
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

                // set background for labels
                label.setStyle("-fx-background-color: lightblue");

                label.setMinSize(scale, scale);
                label.setPrefSize(scale, scale);
                label.setMaxSize(scale, scale);
                GridPane.setConstraints(label, h, v);
                enemyField.getChildren().addAll(label);
            }
        }

        //  the elements of a grid-pane can be returned as an array of Objects - cast Objetct to Label, than it is possible
        //  to change the properties of the Label, e.g. the color
        //  -> important: Objects of grid pane are stored "vertically"

        // connect Labels to Playground - labels are saved in arrays
        Object[] ownFieldArray = new Object[gamesize*gamesize];
        ownFieldArray = ownField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();

        Object[] enemyFieldArray = new Object[gamesize*gamesize];
        enemyFieldArray = enemyField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIEnemyPlayground().setLabels(enemyFieldArray);
        ActiveGameState.getOwnPlayerIEnemyPlayground().drawPlayground();


        //


        //Client -> Zuerst ist der Server dran -> Setze alle Labels im gegnerischen Spielfeld nicht klickbar
        // Starte den Perform Enemy Action Thread um auf die Eingaben des Servers zu reagieren -> Danach PingPong Prinzip
        //TODO SIMON GAMEMODE.PLAYERVSREMOTE -> KI vs Remote wird woanders gestartet
        if ( ActiveGameState.isMultiplayer() && ! ActiveGameState.isAmIServer() && ActiveGameState.getModes() == GameMode.playerVsRemote){
            MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
            multiplayerControlThreadPerformEnemyAction.start();
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
        }


        // setting the background image
        setBackground();
    }

    // Group -> Add Label (localX, localY, Schiffslabel)
    // -> localX und localY bekommen wir von Label.getLayoutX und Label.getLayoutY -> Von dem Label, das wir bei shoot mit Answer 2 zurückbekommen + Größe vom Schiff + Ausrichtung vom Schiff (also Vertikal oder Horizontal)



    // sets scene background
    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }



    // getter for EnemyP Group -> used in MultiplayerControlThreadShootEnemy
    public static Group groupEnemyPS;

    public static Group getGroupEnemP(){
        return groupEnemyPS;
    }


    /***/
    public void initializeKiPlayground() {

        // ki is the enemy player
        if(ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.kiVsKi) {

            ActiveGameState.setEnemyPlayerEnemyPlayground(new EnemyPlayground());
            ActiveGameState.getEnemyPlayerEnemyPlayground().buildPlayground();

            IOwnPlayground kiOwnPlayground = new OwnPlayground();
            ActiveGameState.setEnemyPlayerOwnPlayground(kiOwnPlayground);

            kiOwnPlayground.buildPlayground();

            //
            ActiveGameState.setPlacementKi(new Ki());


            ArrayList<IShip> newShips = ActiveGameState.getPlacementKi().placeships(kiOwnPlayground);
            kiOwnPlayground.setShipListOfThisPlayground( new ArrayList<IShip>()); //Interne Schiffe aus der placeShips Methode löschen

            for (IShip ship : newShips) {
                kiOwnPlayground.isShipPlacementValid(ship.getPosStart(), ship.getPosEnd());
            }

        }

        // ki is the own player
        if(ActiveGameState.getModes() == GameMode.kiVsKi || ActiveGameState.getModes() == GameMode.kiVsRemote) {

            ActiveGameState.setOwnPlayerIEnemyPlayground(new EnemyPlayground());
            ActiveGameState.getOwnPlayerIEnemyPlayground().buildPlayground();

            IOwnPlayground ourKiOwnPlayground = new OwnPlayground();
            ActiveGameState.setOwnPlayerIOwnPlayground(ourKiOwnPlayground);

            ourKiOwnPlayground.buildPlayground();

            ActiveGameState.setPlacementKi(new Ki());
            ArrayList<IShip> newShips = ActiveGameState.getPlacementKi().placeships(ourKiOwnPlayground);
            ourKiOwnPlayground.setShipListOfThisPlayground( new ArrayList<IShip>()); //Interne Schiffe aus der placeShips Methode löschen

            for (IShip ship : newShips) {
                ourKiOwnPlayground.isShipPlacementValid(ship.getPosStart(), ship.getPosEnd());
            }
        }

    }



    // when Button cancleGame is pressed - save or no saving?
    public void cancleGameMethod() {
        Gui_View.HelpMethods.closeProgrammSaveGame();
    }

    //TODO Button kann nur gedrückt werden, wenn der Spieler an der Reihe ist
    //TODO "Vorschlag Netzwerkprotokol: Zumindest ein Spieler soll einen Namen eingeben können" -> Datei unter dem Namen speichern -> bei loadID, muss dennoch der richige Spielstand mit der ID geladen werden können
    //TODO Bei Singleplayer kein Problem, muss keine ID gesendet werden, aber multiplayer?

//Wenn ein spielstand, von einem anderen geladen wird, bei denen wir als spieler spielen, müssen wir auch als spieler spielen, egal was vorher ausgewählt wurde, da dass unsesre ki nicht kann -> Hinweisnachricht wäre gut, falls wir ki ausgewählt haben
    public void startSaveGame(ActionEvent actionEvent) {
        long id = System.currentTimeMillis();
        if ( ActiveGameState.isMultiplayer()){

        }
        else{

        }
    }
}