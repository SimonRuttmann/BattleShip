package Controller;

import Controller.WorkingThreads.MultiplayerControlThreadKiShootsEnemy;
import Controller.WorkingThreads.MultiplayerControlThreadPerformEnemyAction;
import Controller.WorkingThreads.SingleplayerControlThreadKiVsKi;
import Gui_View.HelpMethods;
import KI.Ki;
import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Network.CMD;
import GameData.ActiveGameState;
import GameData.GameMode;
import Serialize.SaveAndLoad;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;




public class GamePlayground implements Initializable {

    // import from FXML
    public GridPane ownField;
    public GridPane enemyField;
    public Text ownFieldText;
    public Text enemyFieldText;
    public AnchorPane anchorPane;


    /** initialize-method:
     * -----------------------------------------------------------------------------------------------------------------
     *  -> initialize of default configuration:
     *      initializes Savegamebar
     *      initialize both playgrounds as GridPanes
     *      initialize backend playgrounds
     *      starts threads, depending on GameMode and Loading
     *----------------------------------------------------------------------------------------------------------------*/
    @Override

    public void initialize(URL location, ResourceBundle resources) {

        setLanguage();
        initializeSaveGameBar();


        ActiveGameState.setSceneIsGamePlayground(true);
        ActiveGameState.setSceneIsPlaceShips(false);

        if (ActiveGameState.getLoading() == ActiveGameState.Loading.noLoad) {

            // if ki is part of game, playgrounds are not created in placeShips -> done here
            initializeKiPlayground();

        }
        //initialize Playgrounds after loading with build, necessary because GSON can`t control references, so every ShipPart got a own Ship
        else{

            ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
            ActiveGameState.getOwnPlayerIEnemyPlayground().buildPlayground();


            if ( ActiveGameState.getEnemyPlayerOwnPlayground() != null)
            ActiveGameState.getEnemyPlayerOwnPlayground().buildPlayground();
            if ( ActiveGameState.getEnemyPlayerOwnPlayground() != null)
            ActiveGameState.getEnemyPlayerOwnPlayground().buildPlayground();

        }

             // After playground is initialized the thread SingleplayerControlThreadKivsKi has to be started, if the Gamemode KivsKi is selected
            if (ActiveGameState.getModes() == GameMode.kiVsKi) {
                SingleplayerControlThreadKiVsKi singleplayerControlThreadKiVsKi = new SingleplayerControlThreadKiVsKi();
                singleplayerControlThreadKiVsKi.start();
            }


             // Gamemode kiVsRemote -> Start Perform enemy action, if we are Client ( enemy´s Turn )
             //                     -> Start KI Shoots Enemy, if we are Server ( our Turn)
             //                     ->  If the game is loaded, start threads depending on turn

            if (ActiveGameState.getModes() == GameMode.kiVsRemote) {
                if (!ActiveGameState.isAmIServer()) {
                    MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
                    multiplayerControlThreadPerformEnemyAction.start();
                } else {

                    if(ActiveGameState.isLoadWithNext()){
                        MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
                        multiplayerControlThreadPerformEnemyAction.start();
                    }
                    else {
                        MultiplayerControlThreadKiShootsEnemy multiplayerControlThreadKiShootsEnemy = new MultiplayerControlThreadKiShootsEnemy();
                        multiplayerControlThreadKiShootsEnemy.start();
                    }
                }
            }


        //The scale of one Field,   Ship size 2 -> Image: | 30px | 30px |
        //                          Ship size 3 -> Image: | 30px | 30px | 30px |
        // scale is depended on playground size
        int scale = ActiveGameState.getPlaygroundScale();


        // playground field --------------------------------------------------------------------------------------------
        // set Labels for the Playgrounds
        ownFieldText.setText((ActiveGameState.getLanguage() == ActiveGameState.Language.english) ? "Own Playground" : "Eigenes Spielfeld");
        ownFieldText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ownFieldText.setFill(Color.WHITE);
        ownFieldText.setEffect(new DropShadow(10, Color.BLACK));

        enemyFieldText.setText((ActiveGameState.getLanguage() == ActiveGameState.Language.english) ? "Enemy Playground" : "Gegnerisches Spielfeld");
        enemyFieldText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        enemyFieldText.setFill(Color.WHITE);
        enemyFieldText.setEffect(new DropShadow(10, Color.BLACK));

        // 2D fields for Labels:
        int gamesize = ActiveGameState.getPlaygroundSize();
        ownField.setHgap(1);
        ownField.setVgap(1);
        enemyField.setHgap(1);
        enemyField.setVgap(1);

        // column and row constraints must be cleared before initializing ownField or EnemyField new
        ownField.getColumnConstraints().clear();
        ownField.getRowConstraints().clear();
        enemyField.getColumnConstraints().clear();
        enemyField.getRowConstraints().clear();


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

        //  the elements of a grid-pane can be returned as an array of Objects - cast Object to Label, than it is possible
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


        //Necessary for instant update of the playground
        if (ActiveGameState.getModes() == GameMode.playerVsRemote || ActiveGameState.getModes() == GameMode.playerVsKi) {
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
        }


        //Client starts Control Enemy Action
        if (! ActiveGameState.isAmIServer() && ActiveGameState.getModes() == GameMode.playerVsRemote){
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
            setSaveAndCloseButtonNonClickable();
            MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
            multiplayerControlThreadPerformEnemyAction.start();
        }


        //Server starts Control Enemy Action, when we Load with a game, where the enemy´s turn is present
        //In all other cases, no Threads have to be started, as the user is starting the shootThread by clicking the Labels
        if (ActiveGameState.isAmIServer() && ActiveGameState.isLoadWithNext() && ActiveGameState.getModes() == GameMode.playerVsRemote){
            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
            setSaveAndCloseButtonNonClickable();
            MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
            multiplayerControlThreadPerformEnemyAction.start();
        }


        // saveAndCloseButton is only clickable, when save name is valid AND not our Turn
        this.saveAndCloseButton.setDisable(true);
        this.saveGameText.textProperty().addListener((observable, oldValue, newValue) -> {

            //The name the user put in -> Create a file with this text as name
            String savegamename = this.saveGameText.getText();

            //Check for not allowed characters -> no File creation possible
            char[] notAllowedCharacters = {'\\', '/', ':', '!', '?', '*', '"', '|', '<', '>'};
            for (char character : notAllowedCharacters) {
                if (savegamename.contains(String.valueOf(character)) || savegamename.isEmpty() || !ActiveGameState.isYourTurn()) {
                    saveAndCloseButton.setDisable(true);
                    return;
                }
            }
            saveAndCloseButton.setDisable(false);
        });


        // setting the background image
        setBackground();
    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            buttonShowSaveBar.setText("Spiel speichern");
            saveGameText.setPromptText("Geben sie den Namen des Spielstandes ein");
            saveAndCloseButton.setText("Speichern und Schließen");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            buttonShowSaveBar.setText("Save Game");
            saveGameText.setPromptText("Enter the save name here");
            saveAndCloseButton.setText("Save and Close");
        }
    }


    // sets scene background
    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/IngameBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }


    /**
     * Is called by initialize method
     * Ki is enemy player -> Instantiate and build both playgrounds for the Ki
     * Ki is own player   -> Instantiate and build both playgrounds for the Ki
     */
    public void initializeKiPlayground() {

        // ki is the enemy player
        if(ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.kiVsKi) {

            ActiveGameState.setEnemyPlayerEnemyPlayground(new EnemyPlayground());
            ActiveGameState.getEnemyPlayerEnemyPlayground().buildPlayground();

            IOwnPlayground kiOwnPlayground = new OwnPlayground();
            ActiveGameState.setEnemyPlayerOwnPlayground(kiOwnPlayground);

            kiOwnPlayground.buildPlayground();


            ActiveGameState.setPlacementKi(new Ki(Ki.Difficulty.undefined));


            ArrayList<IShip> newShips = ActiveGameState.getPlacementKi().placeships(kiOwnPlayground);
            kiOwnPlayground.setShipListOfThisPlayground( new ArrayList<>());

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
            ourKiOwnPlayground.setShipListOfThisPlayground( new ArrayList<>());

            for (IShip ship : newShips) {
                ourKiOwnPlayground.isShipPlacementValid(ship.getPosStart(), ship.getPosEnd());
            }
        }

    }

//***************************************************************** SAVE GAME BAR *******************************************************************************************************************************************************************************************************************


    //called with all setLabelsClickable/NonClickable
    public static void setSaveAndCloseButtonNonClickable() {
        GamePlayground.staticSaveAndCloseButton.setDisable(true);
    }

    public static void setSaveAndCloseButtonClickable() {
        GamePlayground.staticSaveAndCloseButton.setDisable(false);
    }
    //Save Handling

    //User actionEvent call, button is only enabled, when name in text field is valid save name
    public void saveAndCloseGame(){

        //Create an ID and save it to the ActiveGameState, necessary for loading (Savegame will contain the load id by loading)
        long id = System.currentTimeMillis();
        ActiveGameState.setLoadId(id);

        //The name the user put in -> Create a file with this text as name
        String savegamename = this.saveGameText.getText();


        boolean saveSuccess;
        //Save the game with ID, when multiplayer is selected
        if (ActiveGameState.isMultiplayer()) {
            saveSuccess = SaveAndLoad.save(savegamename, id);
            if (ActiveGameState.isAmIServer()){
                ActiveGameState.getServer().sendCMD(CMD.save, String.valueOf(id));
            }
            else{
                ActiveGameState.getClient().sendCMD(CMD.save, String.valueOf(id));
            }
        }
        else{
            saveSuccess = SaveAndLoad.save(savegamename);
        }

        if ( saveSuccess ) {
            HelpMethods.saveRequest(0,true);
        }
        else{
            final Logger logClient = Logger.getLogger("parent.client");
            logClient.log(Level.SEVERE,"Error at Saving.");
        }

    }

    public void setStaticSaveAndCloseButton(){
        staticSaveAndCloseButton = this.saveAndCloseButton;
    }
    public static Button staticSaveAndCloseButton;

    public Button saveAndCloseButton;
    public Button buttonShowSaveBar;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public HBox hBox_SaveTextfieldButton;
    public TextField saveGameText;
    private boolean saveBarShown = false;

    public void showSaveBar() {

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
        this.setStaticSaveAndCloseButton();
    }


    public void hideSaveGameBar(boolean hide){
        this.sP_RectangleAndElements.setVisible(!hide);
        //KI games shouldn't be saved
        if(ActiveGameState.getModes() == GameMode.kiVsKi || ActiveGameState.getModes() == GameMode.kiVsRemote) {
            this.lineLeftSide.setVisible(false);
            this.sP_RectangleAndElements.setVisible(false);
            this.saveAndCloseButton.setVisible(false);
            this.buttonShowSaveBar.setVisible(false);
        }
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

        //Set the starting position of the SP to -1000
        int from = -1000;
        this.sP_RectangleAndElements.setTranslateX(from);


        //Simple clip, used to display the rectangle only, when the line is reached
        Rectangle clip = new Rectangle(1200,600);
        clip.translateXProperty().bind(sP_RectangleAndElements.translateXProperty().negate());
        this.sP_RectangleAndElements.setClip(clip);

        //Slide in the StackPane, containing the Polygon and the Text
        TranslateTransition slideElements = new TranslateTransition(Duration.seconds(0.6+0.6*slideSpeed), this.sP_RectangleAndElements);
        slideElements.setDelay(Duration.millis(0));
        slideElements.setToX(1);
        slideElements.play();


        scaleLineLeft.play();

    }

    public void setRectangleSettings(){

        this.rectangle.setStroke(Color.color(0,0,0,0.5));
        this.rectangle.setEffect(new GaussianBlur());
        this.rectangle.setFill(Color.color(0,0,0,0.5));
    }

}