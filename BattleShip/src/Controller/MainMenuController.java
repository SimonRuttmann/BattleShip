package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;
import KI.Ki;
import GameData.ActiveGameState;
import GameData.GameMode;
import LoggingNetwork.NetworkLogger;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.text.Text;


public class MainMenuController implements Initializable {
    public static final Logger logMainMenuController = Logger.getLogger("parent.MainMenuController");


    /** FXML Elements **/
    public Text title;
    public AnchorPane anchorPane;

    /** Left Bar **/
    public HBox hBox_leftBar;
    public Line leftBarLine;
    public VBox vBox_LeftBar;

    public Text textSingleplayer;
    public Polygon polySingleplayer;

    public Polygon polyMultiplayer;
    public Text textMultiplayer;

    public Polygon polySettings;
    public Text textSettings;

    public Polygon polyQuitGame;
    public Text textQuitGame;

    public StackPane leftBar_SingPolyText;
    public StackPane leftBar_MultPolyText;
    public StackPane leftBar_SettPolyText;
    public StackPane leftBar_QuitPolyText;

    /** Right Bar **/
    public StackPane sP_RightBar;

    /** Right Bar Singleplayer **/
    public HBox         rightBarSingleplayer;
    public Line         rightBarLine;
    public Polygon      rightBarSinglplayer_PolyPlayerVsKI;
    public Text         rightBarSinglplayer_TextPlayerVsKI;
    public Polygon      rightBarSinglplayer_PolyKIVsKI;
    public Text         rightBarSinglplayer_TextKIVsKI;
    public StackPane    rightBar_Sing_PlayerVsKIPolyText;
    public StackPane    rightBar_Sing_KIVsKIPolyText;

    public StackPane    rightBar_Sing_LoadPolyText;
    public Polygon      rightBarSinglplayer_PolyLoad;
    public Text         rightBarSinglplayer_TextLoad;

    /** Right Bar Multiplayer **/
    public HBox rightBarMultiplayer;
    public Line rightBarLineMult;
    public StackPane rightBar_Mult_HostPolyText;
    public Polygon rightBarMultplayer_PolyHost;
    public Text rightBarMultiplayer_TextHost;
    public StackPane rightBar_Mult_ClientPolyText;
    public Polygon rightBarMultiplayer_PolyClient;
    public Text rightBarMultiplayer_TextClient;

    public StackPane rightBar_Mult_LoadPolyText;
    public Polygon rightBarMultiplayer_PolyLoad;
    public Text rightBarMultiplayer_TextLoad;


    /**Intern Variables**/
    private boolean singleRightNotShown = true;
    private boolean multRightNotShown = true;
    private boolean setRightNotShown = true;

    //Shows, which right bar is now visible
    enum Selection {Singleplayer, Multiplayer, Settings}
    public Selection selection;

    //Static instance needed, otherwise the garbage collector will stop music
    private static MusicController music;
    private static boolean playingMusic;


    /**External Handling's**/
    //Singleplayer Player vs KI, called by mouseEvent
    public void startPlayerVsKI() throws IOException {

        logMainMenuController.log(Level.INFO, "Switching Scene to Game Settings, Mode Player vs AI ");

        ActiveGameState.setYourTurn(true);
        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.playerVsKi);
        ActiveGameState.setEnemyKi(new Ki(ActiveGameState.getEnemyKiDifficulty()));

        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    //Singleplayer KI vs KI, called by mouseEvent
    public void startKIvsKI() throws IOException {

        logMainMenuController.log(Level.INFO, "Switching Scene to Game Settings, Mode AI vs AI");

        ActiveGameState.setYourTurn(true);
        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.kiVsKi);

        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    //Singleplayer Load Game, called by mouseEvent
    public void loadSinglGame() throws IOException{

        logMainMenuController.log(Level.INFO, "Switching Scene to LoadGameController, Mode of loading is Singleplayer");

        ActiveGameState.setLoading(ActiveGameState.Loading.singleplayer);
        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.playerVsKi);

        Parent loadGame = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }

    //Multiplayer Load Game, called by mouseEvent
    public void loadMultGame() throws IOException{

        logMainMenuController.log(Level.INFO, "Switching Scene to LoadGameController, Mode of loading is Multiplayer");

        ActiveGameState.setLoading(ActiveGameState.Loading.multiplayer);
        ActiveGameState.setMultiplayer(true);
        ActiveGameState.setAmIServer(true);


        Parent loadGame = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }

    //Multiplayer Client, called by mouseEvent
    public void startClient() throws IOException{

        logMainMenuController.log(Level.INFO, "Switching Scene to MpClientController");

        ActiveGameState.setMultiplayer(true);
        ActiveGameState.setAmIServer(false);
        ActiveGameState.setYourTurn(false);

        Parent mpJoin = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpClient.fxml")); //mpJoin alt
        Main.primaryStage.setScene(new Scene(mpJoin));
        Main.primaryStage.show();
    }

    //Multiplayer Host, called by mouseEvent
    public void startHost() throws IOException {

        logMainMenuController.log(Level.INFO, "Switching Scene to MpServerController");

        ActiveGameState.setMultiplayer(true);
        ActiveGameState.setAmIServer(true);
        ActiveGameState.setYourTurn(true);

        Parent mpHost = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpServer.fxml"));
        Main.primaryStage.setScene(new Scene(mpHost));
        Main.primaryStage.show();
    }

    //Quit Game, called by mouseEvent
    public void quitGameSelected() {

        logMainMenuController.log(Level.INFO, "Closed game over quit");

        ActiveGameState.setLogging(false);
        NetworkLogger.terminateLogging();
        Main.primaryStage.close();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLanguage();
        ActiveGameState.setPlacementKi(new Ki(Ki.Difficulty.undefined));

        setBackground();
        setRightBarSingleplayerInvisible(true);
        setRightBarMultiplayerInvisible(true);
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setPolygonSettings();
        startAnimationLeftSide();

        //Music
        if (!playingMusic) {
            music = new MusicController();
            music.playMusic();
            playingMusic = true;
            ActiveGameState.setMusicController(music);
        }

        ActiveGameState.setLoading(ActiveGameState.Loading.noLoad);
        ActiveGameState.setSceneIsPlaceShips(false);

        //Close Request
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            HelpMethods.closeProgramm();
        });

    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("B a t t l e s h i p");
            rightBarMultiplayer_TextLoad.setText("Spiel laden");
            textSingleplayer.setText("Einzelspieler");
            textMultiplayer.setText("Mehrspieler");
            textSettings.setText("Optionen");
            textQuitGame.setText("Spiel verlassen");
            rightBarMultiplayer_TextClient.setText("Client");
            rightBarMultiplayer_TextHost.setText("Host");
            rightBarSinglplayer_TextKIVsKI.setText("KI vs. KI");
            rightBarSinglplayer_TextPlayerVsKI.setText("Spieler vs. KI");
            rightBarMultiplayer_TextHost.setText("Host");
            rightBarMultiplayer_TextClient.setText("Client");
            rightBarSinglplayer_TextLoad.setText("Spiel laden");

        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("B a t t l e s h i p");
            rightBarMultiplayer_TextLoad.setText("Load Game");
            textSingleplayer.setText("Singleplayer");
            textMultiplayer.setText("Multiplayer");
            textSettings.setText("Options");
            textQuitGame.setText("Quit Game");
            rightBarMultiplayer_TextClient.setText("Client");
            rightBarMultiplayer_TextHost.setText("Host");
            rightBarSinglplayer_TextKIVsKI.setText("AI vs. AI");
            rightBarSinglplayer_TextPlayerVsKI.setText("Player vs. AI");
            rightBarMultiplayer_TextHost.setText("Host");
            rightBarMultiplayer_TextClient.setText("Client");
            rightBarSinglplayer_TextLoad.setText("Load Game");
        }
    }



    public void setRightBarSingleplayerInvisible(boolean invisible){
        this.rightBarSingleplayer.setVisible(!invisible);
    }

    public void setRightBarMultiplayerInvisible(boolean invisible){
        this.rightBarMultiplayer.setVisible(!invisible);
    }

    public void setBackground(){
        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }

    public void setTitleSettings(){

        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }

    public void setLineSettings(){
        Color lineColor = new Color(1,1,1, 0.75);
        this.leftBarLine.setStroke(lineColor);
        this.leftBarLine.setStrokeWidth(3);

        //Singleplayer
        this.rightBarLine.setStroke(lineColor);
        this.rightBarLine.setStrokeWidth(3);
        //Multiplayer
        this.rightBarLineMult.setStroke(lineColor);
        this.rightBarLineMult.setStrokeWidth(3);


        this.leftBarLine.setEffect(new DropShadow(5, Color.BLACK));
        this.rightBarLine.setEffect(new DropShadow(5, Color.BLACK));
        this.rightBarLineMult.setEffect(new DropShadow(5, Color.BLACK));

        // Sets the Scaling of the Bars, as we want to show them from nothing, we set the to 0
        // If we would set this one to 1.5 the Line (which is 300 Long) would decrease from 450 to 300
        this.leftBarLine.setScaleY(0);
        this.rightBarLine.setScaleY(0);
        this.rightBarLineMult.setScaleY(0);
    }

    public void startAnimationLeftSide() {

        double lineScaleDuration = 0.75;
        double slideSpeed = 0.75;
        ScaleTransition scaleLineLeft = new ScaleTransition(Duration.seconds(lineScaleDuration),this.leftBarLine);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineLeft.setToY(1);

        //Sets the starting position of the StackPanes to -300
        int from = -300 ;
        this.leftBar_SingPolyText.setTranslateX(from);
        this.leftBar_MultPolyText.setTranslateX(from);
        this.leftBar_SettPolyText.setTranslateX(from);
        this.leftBar_QuitPolyText.setTranslateX(from);

        //Simple clip, to show the slided elements only when they reached the line
        Rectangle clip = new Rectangle(300,100);
        clip.translateXProperty().bind(leftBar_SingPolyText.translateXProperty().negate());
        this.leftBar_SingPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(leftBar_MultPolyText.translateXProperty().negate());
        this.leftBar_MultPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(leftBar_SettPolyText.translateXProperty().negate());
        this.leftBar_SettPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(leftBar_QuitPolyText.translateXProperty().negate());
        this.leftBar_QuitPolyText.setClip(clip);


        //-> After the left line is drawn
        scaleLineLeft.setOnFinished( e -> {


            //Slide in the StackPane, containing the Polygon and the Text
            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(1+0.6*slideSpeed), this.leftBar_SingPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1*slideSpeed), this.leftBar_MultPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.3*slideSpeed), this.leftBar_SettPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.6*slideSpeed), this.leftBar_QuitPolyText);
            slideElements.setToX(0);
            slideElements.play();


        });

        scaleLineLeft.play();

    }

    public void startAnimationRightSideSingleplayer(){


        double lineScaleDuration = 0.75;
        double slideSpeed = 1.0;
        ScaleTransition scaleLineRight = new ScaleTransition(Duration.seconds(lineScaleDuration),this.rightBarLine);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineRight.setToY(1);

        //Sets the starting positions of the StackPanes to -500
        int from = -500 ;
        this.rightBar_Sing_PlayerVsKIPolyText.setTranslateX(from);
        this.rightBar_Sing_KIVsKIPolyText.setTranslateX(from);
        this.rightBar_Sing_LoadPolyText.setTranslateX(from);


        //Simple clip, to show the slided elements only when they reached the line
        Rectangle clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Sing_PlayerVsKIPolyText.translateXProperty().negate());
        this.rightBar_Sing_PlayerVsKIPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Sing_KIVsKIPolyText.translateXProperty().negate());
        this.rightBar_Sing_KIVsKIPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Sing_LoadPolyText.translateXProperty().negate());
        this.rightBar_Sing_LoadPolyText.setClip(clip);

        //-> After the right line is drawn
        scaleLineRight.setOnFinished( e -> {


            //Slide in the StackPane, containing the Polygon and the Text
            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(1+0.6*slideSpeed), this.rightBar_Sing_PlayerVsKIPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1*slideSpeed), this.rightBar_Sing_KIVsKIPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.3*slideSpeed), this.rightBar_Sing_LoadPolyText);
            slideElements.setToX(0);
            slideElements.play();


        });

        scaleLineRight.play();

    }

    public void startAnimationRightSideMultiplayer(){


        double lineScaleDuration = 0.75;
        double slideSpeed = 1.0;
        ScaleTransition scaleLineRight = new ScaleTransition(Duration.seconds(lineScaleDuration),this.rightBarLineMult);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineRight.setToY(1);

        //Sets the starting positions of the StackPanes to -500
        int from = -500 ;
        this.rightBar_Mult_HostPolyText.setTranslateX(from);
        this.rightBar_Mult_ClientPolyText.setTranslateX(from);
        this.rightBar_Mult_LoadPolyText.setTranslateX(from);


        //Simple clip, to show the slided elements only when they reached the line
        Rectangle clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_HostPolyText.translateXProperty().negate());
        this.rightBar_Mult_HostPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_ClientPolyText.translateXProperty().negate());
        this.rightBar_Mult_ClientPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_LoadPolyText.translateXProperty().negate());
        this.rightBar_Mult_LoadPolyText.setClip(clip);


        //-> After the right line is drawn
        scaleLineRight.setOnFinished( e -> {

            //Slide in the StackPane, containing the Polygon and the Text
            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(1+0.6*slideSpeed), this.rightBar_Mult_HostPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1*slideSpeed), this.rightBar_Mult_ClientPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.3*slideSpeed), this.rightBar_Mult_LoadPolyText);
            slideElements.setToX(0);
            slideElements.play();

        });

        scaleLineRight.play();

    }


    public void setPolygonSettings(){
        Effect shadow = new DropShadow(5, Color.BLACK);
        Effect blur = new BoxBlur(1, 1, 3);

        //ArrayLists to avoid massive code duplication

        //Polygon
        //Left Side
        ArrayList<Polygon> polygons = new ArrayList<>();
        polygons.add (polySingleplayer);
        polygons.add(polyMultiplayer);
        polygons.add(polySettings);
        polygons.add(polyQuitGame);

        //Right Side
        //Singleplayer
        polygons.add(rightBarSinglplayer_PolyPlayerVsKI);
        polygons.add(rightBarSinglplayer_PolyKIVsKI);
        polygons.add(rightBarSinglplayer_PolyLoad);

        //Multiplayer
        polygons.add(rightBarMultplayer_PolyHost);
        polygons.add(rightBarMultiplayer_PolyClient);
        polygons.add(rightBarMultiplayer_PolyLoad);

        //Text
        //Left Side
        ArrayList<Text> texts = new ArrayList<>();
        texts.add (textSingleplayer);
        texts.add(textMultiplayer);
        texts.add(textSettings);
        texts.add(textQuitGame);

        //Right Side
        //Singleplayer
        texts.add(rightBarSinglplayer_TextPlayerVsKI);
        texts.add(rightBarSinglplayer_TextKIVsKI);
        texts.add(rightBarSinglplayer_TextLoad);

        //Multiplayer
        texts.add(rightBarMultiplayer_TextHost);
        texts.add(rightBarMultiplayer_TextClient);
        texts.add(rightBarMultiplayer_TextLoad);


        //The StackPane contains the Text and the Polygon
        //Left Side
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        stackPanes.add (leftBar_SingPolyText);
        stackPanes.add(leftBar_MultPolyText);
        stackPanes.add(leftBar_SettPolyText);
        stackPanes.add(leftBar_QuitPolyText);

        //Right Side
        //Singleplayer
        stackPanes.add(rightBar_Sing_PlayerVsKIPolyText);
        stackPanes.add(rightBar_Sing_KIVsKIPolyText);
        stackPanes.add(rightBar_Sing_LoadPolyText);

        //Multiplayer
        stackPanes.add(rightBar_Mult_HostPolyText);
        stackPanes.add(rightBar_Mult_ClientPolyText);
        stackPanes.add(rightBar_Mult_LoadPolyText);

        for(Polygon polygon : polygons) {

            //Draw the Polygons right as it is not possible to draw them in scene builder
            polygon.getPoints().removeAll();
            polygon.getPoints().setAll(
                    -75.0, 0.0,         //top left
                    180.0, 0.0,         //top right
                    215.0, 30.0,        //peek
                    180.0, 60.0,        //bottom right
                    -75.0, 60.0         //bottom left
            );
            polygon.setStroke(Color.color(1, 1, 1, 1));
            polygon.setEffect(new GaussianBlur());

        }

        //Adding effects to the Text and Polygons based on the properties of the StackPanes
        for (int i = 0; i < polygons.size(); i++){

            //Polygon effects
            polygons.get(i).fillProperty().bind(
                    Bindings.when(stackPanes.get(i).pressedProperty())
                            .then(Color.color(1, 1, 1, 0.9))
                            .otherwise(Color.color(1, 1, 1, 0.7))
            );

            //Text effects
            texts.get(i).effectProperty().bind(
                    Bindings.when(stackPanes.get(i).hoverProperty())
                            .then(shadow)
                            .otherwise(blur)
            );
        }



    }



    public void setTextSettings(){

        //TextSettings of Left Bar
        ArrayList<Text> leftBarText = new ArrayList<>();
        leftBarText.add(this.textSingleplayer);
        leftBarText.add(this.textMultiplayer);
        leftBarText.add(this.textSettings);
        leftBarText.add(this.textQuitGame);
        Color textColorLeftBar = new Color(0.2,0.2,0.2,1);

        for ( Text text : leftBarText ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorLeftBar);
            text.setEffect(new DropShadow(30, Color.BLACK));
            //text.setStyle("-fx-font: 24 arial;");
        }

        //TextSettings of Right Bar
        ArrayList<Text> rightBarText = new ArrayList<>();
        //Singleplayer
        rightBarText.add(this.rightBarSinglplayer_TextPlayerVsKI);
        rightBarText.add(this.rightBarSinglplayer_TextKIVsKI);

        rightBarText.add(this.rightBarSinglplayer_TextLoad);

        //Multiplayer
        rightBarText.add(this.rightBarMultiplayer_TextHost);
        rightBarText.add(this.rightBarMultiplayer_TextClient);
        rightBarText.add(this.rightBarMultiplayer_TextLoad);


        Color textColorRightBar = new Color(0.2, 0.2, 0.2, 1);

        for ( Text text : rightBarText ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorRightBar);
            text.setEffect(new DropShadow(30, Color.BLACK));

        }



    }

    /**
     * Displaying the menu´s singleplayer, multiplayer and options (not in use, extra Scene)
     * Closes the other right bar´s, if any selected
     * Clicking the menu twice, the bar will be closed again
     */

    //User mouseEvent call
    public void singleplayerSelected() {
        selection = Selection.Singleplayer;

        if (this.singleRightNotShown) {


            //Multiplayer is shown
            if(!this.multRightNotShown){
                this.showMultBar(false);
                this.multRightNotShown = true;
            }
            //Settings is shown
            if(!this.setRightNotShown){
                this.showSetBar(false);
                this.setRightNotShown = true;
            }
            //Show singleplayer
            this.showSingBar(true);


        }
        else{
            this.showSingBar(false);

        }

        singleRightNotShown = !singleRightNotShown;
    }

    //User mouseEvent call
    public void multiplayerSelected() {
        selection = Selection.Multiplayer;
        if(this.multRightNotShown){

            //Singleplayer is shown
            if(!this.singleRightNotShown){
                this.showSingBar(false);
                this.singleRightNotShown = true;
            }
            //Settings is shown
            if(!this.setRightNotShown){
                this.showSetBar(false);
                this.setRightNotShown = true;
            }

            //Show Multiplayer
            this.showMultBar(true);
        }
        else{
            this.showMultBar(false);
        }

        multRightNotShown = !multRightNotShown;
    }

    //User mouseEvent call
    public void settingsSelected() {
        selection = Selection.Settings;
        if(this.multRightNotShown){

            //Singleplayer is shown
            if(!this.singleRightNotShown){
                this.showSingBar(false);
                this.singleRightNotShown = true;
            }
            //Multiplayer is shown
            if(!this.multRightNotShown){
                this.showSetBar(false);
                this.multRightNotShown = true;
            }

            //Show Settings
            this.showSetBar(true);
        }
        else{
            this.showSetBar(false);
        }

        setRightNotShown = !setRightNotShown;


    }

    /**
     * Help Methods to show the singleplayer, multiplayer and settings bar
     */

    public void showSingBar(boolean show){
        if ( show ){

            setRightBarSingleplayerInvisible(false);
            startAnimationRightSideSingleplayer();
        }
        else{

            setRightBarSingleplayerInvisible(true);
            this.rightBarLine.setScaleY(0);
        }
    }
    public void showMultBar(boolean show){
        if ( show ){

            setRightBarMultiplayerInvisible(false);
            startAnimationRightSideMultiplayer();
        }
        else{
            logMainMenuController.log(Level.INFO, "Switching Scene to GameOptionsController");
            setRightBarMultiplayerInvisible(true);
            this.rightBarLineMult.setScaleY(0);
        }
    }

    //Boolean show currently not in use, the settings lead to GameOptions, as "GameSettings" is the game configuration scene
    public void showSetBar(boolean show) {
        try{Parent gameOptions = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameOptions.fxml"));
            Main.primaryStage.setScene(new Scene(gameOptions));
            Main.primaryStage.show();}
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
