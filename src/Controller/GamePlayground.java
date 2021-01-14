package Controller;

import Controller.Handler.MultiplayerControlThreadKiShootsEnemy;
import Controller.Handler.MultiplayerControlThreadPerformEnemyAction;
import Controller.Handler.SingleplayerControlThreadKiVsKi;
import Gui_View.HelpMethods;
import Gui_View.Main;
import KI.Ki;
import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Model.Ship.Ship;
import Player.ActiveGameState;
import Player.GameMode;
import Controller.Handler.GameShootEnemy;
import Player.SaveAndLoad;
import Player.Savegame;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GamePlayground implements Initializable {




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

        initializeSaveGameBar();
  
    //todo make ship hits displayed on grid pane, not on labels > do it also in SinglePlayer
    //todo set labels of own playground to ships -> MultiplayercontrolThreadGetShot?

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
        int scale = ActiveGameState.getPlaygroundScale();


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

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/IngameBackground.jpg")),
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
            ActiveGameState.setPlacementKi(new Ki(Ki.Difficulty.undefined));


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

            ActiveGameState.setPlacementKi(new Ki(Ki.Difficulty.undefined));
            ArrayList<IShip> newShips = ActiveGameState.getPlacementKi().placeships(ourKiOwnPlayground);
            ourKiOwnPlayground.setShipListOfThisPlayground( new ArrayList<IShip>()); //Interne Schiffe aus der placeShips Methode löschen

            for (IShip ship : newShips) {
                ourKiOwnPlayground.isShipPlacementValid(ship.getPosStart(), ship.getPosEnd());
            }
        }

    }

//***************************************************************** SAVE GAME BAR *******************************************************************************************************************************************************************************************************************

    //Save Handling

    public void saveAndCloseGame(ActionEvent actionEvent){

        //Create an ID and save it to the ActiveGameState, necessary for loading (Savegame will contain the load id by loading)
        long id = System.currentTimeMillis();
        ActiveGameState.setLoadId(id);

        //The name the user put in -> Create a file with this text as name
        String savegamename = this.saveGameText.getText();

        //Check for not allowed characters -> no File creation possible
        char[] notAllowedCharacters = {'\\','/',':','!','?','*','"','|','<','>'};
        for ( char character : notAllowedCharacters){
            if (savegamename.contains( String.valueOf(character) )  || savegamename.isEmpty()  ) return; //TODO YANNICK Display not allowed (kein Popup, Popups sind für unerwartete nachrichten gedacht.)
        }                                                                                                //TODO Am besten den Button nur dann enablen, wenn keines dieser sonderzeichen enthalten ist

        boolean saveSuccess;
        //Save the game with ID, when multiplayer is selected
        if (ActiveGameState.isMultiplayer()) {
            saveSuccess = SaveAndLoad.save(savegamename, id);
        }
        else{
            saveSuccess = SaveAndLoad.save(savegamename);
        }

        if ( saveSuccess ) {
            System.out.println( "Game saved ");
            Platform.runLater( () -> {
                try {
                    Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                    Main.primaryStage.setScene(new Scene(gameSettings));
                    Main.primaryStage.show();
                }
                catch ( Exception e){
                    e.printStackTrace();
                }
            });
            //TODO YANNICK Display -> Spiel wurde gespeichert -> Zum Hauptmenü zurückkehren (Am besten anch 3-5 sekunden, kein button benötigt) Dabei alle Threads beenden (interrupt)
        }
        else{
            //TODO LoggingNetwork Level SEVERE -> Error at saving
        }

    }




    public Button saveAndCloseButton;
    public Button buttonShowSaveBar;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public HBox hBox_SaveTextfieldButton;
    public TextField saveGameText;
    private boolean saveBarShown = false;

    public void showSaveBar(ActionEvent actionEvent) {

        if (!saveBarShown) {
            hideSaveGameBar(false);
            startAnimation();
        }
        else{
            hideSaveGameBar(true);
            this.lineLeftSide.setScaleY(0);
        }
        saveBarShown = !saveBarShown;
    }

    public void initializeSaveGameBar(){
        setRectangleSettings();
        setLineSettings();
        hideSaveGameBar(true);
        this.lineLeftSide.setScaleY(0);

    }


    public void hideSaveGameBar(boolean hide){
        this.sP_RectangleAndElements.setVisible(!hide);
    }

    public void setLineSettings(){
        Color lineColor = new Color(1,1,1, 0.75);
        this.lineLeftSide.setStroke(lineColor);
        this.lineLeftSide.setStrokeWidth(3);

        this.lineLeftSide.setEffect(new DropShadow(5, Color.BLACK));

        // Sets the Scaling of the Bars, as we want to show them from nothing, we set the to 0
        // If we would set this one to 1.5 the Line (which is 300 Long) would decrease from 450 to 300
        this.lineLeftSide.setScaleY(0);

    }


    public void startAnimation() {

        double lineScaleDuration = 0.2;
        double slideSpeed = 1.5;
        ScaleTransition scaleLineLeft = new ScaleTransition(Duration.seconds(lineScaleDuration),this.lineLeftSide);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineLeft.setToY(1);

        //Dadurch wird die Startposition auf -200 gesetzt
        int from = -1000;
        this.sP_RectangleAndElements.setTranslateX(from);


        //Notwendiges Rechteck, damit die Items erst angezeigt werden, wenn sie durch die Linie hindurchgehen
        Rectangle clip = new Rectangle(1200,600);
        clip.translateXProperty().bind(sP_RectangleAndElements.translateXProperty().negate());
        this.sP_RectangleAndElements.setClip(clip);

        //-> After the left line is drawn
      //  scaleLineLeft.setOnFinished( e -> {

            //Slide in the StackPane, containing the Polygon and the Text

            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(0.6+0.6*slideSpeed), this.sP_RectangleAndElements);
            slideElements.setDelay(Duration.millis(0));
            slideElements.setToX(1);
            slideElements.play();


        //});

        scaleLineLeft.play();

    }

    public void setRectangleSettings(){

        this.rectangle.setStroke(Color.color(0,0,0,0.5));
        this.rectangle.setEffect(new GaussianBlur());
        this.rectangle.setFill(Color.color(0,0,0,0.5));
    }

}