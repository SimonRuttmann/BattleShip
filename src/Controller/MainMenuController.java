package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;
import KI.Ki;
import Player.ActiveGameState;
import Player.GameMode;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.text.Text;




public class MainMenuController implements Initializable {


    public StackPane rightBar_Mult_SpSelectRole;
    public Polygon rightBarMultiplayer_PolySelectRole;
    public VBox rightBarMultiplayer_VBoxSelectRole;
    public RadioButton rightBarMultiplayer_RbSelectKI;

    /**External Handling's**/
    //Singleplayer Player vs KI
    public void startPlayerVsKI(MouseEvent mouseEvent) throws IOException {
        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.playerVsKi);

        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    //Singleplayer KI vs KI
    public void startKIvsKI(MouseEvent mouseEvent) throws IOException {

        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.kiVsKi);

        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    //Singleplayer Load Game
    public void startLoadGame(MouseEvent mouseEvent) throws IOException{

        ActiveGameState.setMultiplayer(false);
        ActiveGameState.setModes(GameMode.playerVsKi);

        Parent loadGame = FXMLLoader.load(getClass().getResource("/OldView/loadGame2.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }

    //Multiplayer Client
    public void startClient(MouseEvent mouseEvent) throws IOException{

        ActiveGameState.setMultiplayer(true);
        ActiveGameState.setAmIServer(false);
        ActiveGameState.setYourTurn(false);

        if (this.rightBarMultiplayer_RbSelectKI.isSelected()) {
            ActiveGameState.setModes(GameMode.kiVsRemote);
        }
        else{
            ActiveGameState.setModes(GameMode.playerVsRemote);
        }
        Parent mpJoin = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpClient.fxml")); //mpJoin alt
        Main.primaryStage.setScene(new Scene(mpJoin));
        Main.primaryStage.show();
    }

    //Multiplayer Host
    public void startHost(MouseEvent mouseEvent) throws IOException {

        ActiveGameState.setMultiplayer(true);
        ActiveGameState.setAmIServer(true);
        ActiveGameState.setYourTurn(true);
        if (this.rightBarMultiplayer_RbSelectKI.isSelected()) {
            ActiveGameState.setModes(GameMode.kiVsRemote);
        }
        else{
            ActiveGameState.setModes(GameMode.playerVsRemote);
        }

        Parent mpHost = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpServer.fxml"));
        Main.primaryStage.setScene(new Scene(mpHost));
        Main.primaryStage.show();
    }

    public void quitGameSelected(MouseEvent mouseEvent) {
        Main.primaryStage.close();
    }



    /** ID's from FXML **/
    public Text title;
    public AnchorPane anchorPane;

    /** Left Bar **/
    public HBox hBox_leftBar;   //In the HBox is the Line and the VBox with the Elements
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

    /** Right Bar Settings **/


    /**Intern Variables**/
    private boolean singleRightNotShown = true;
    private boolean multRightNotShown = true;
    private boolean setRightNotShown = true;

    enum Selection {Singleplayer, Multiplayer, Settings}
    public Selection selection; //<- Shows, which right bar is now visible

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //KI needs to be added, to get functionality of KI Methods (place ships random, shoot enemy)
        ActiveGameState.setKi(new Ki());

        System.out.println("Main Menu");
        setBackground();
        //setRightBarInvisible(true);
        setRightBarSingleplayerInvisible(true);
        setRightBarMultiplayerInvisible(true);
        setTextSettings();
        setTitleSettings();
        setRadioButtonSettings();
        setLineSettings();
        setPolygonSettings(); //BOTH SIDES!
        startAnimationLeftSide();

        ActiveGameState.setSceneIsPlaceShips(false);
        // Close Request
        Main.primaryStage.setOnCloseRequest(e -> {
            // e.consume catches closeWindowEvent, which would close the scene
            e.consume();
            HelpMethods.closeProgramm();
        });
    }

    public void setRightBarInvisible(boolean invisible){
        this.sP_RightBar.setVisible(!invisible);
    }

    public void setRightBarSingleplayerInvisible(boolean invisible){
        this.rightBarSingleplayer.setVisible(!invisible);
    }

    public void setRightBarMultiplayerInvisible(boolean invisible){
        this.rightBarMultiplayer.setVisible(!invisible);
    }

    public void setBackground(){
    //    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/Menu1/res/BlueBackground.png")));
    //    imageView.setFitWidth(Main.WIDTH);
    //    imageView.setFitHeight(Main.HEIGHT);


        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        //then you set to your node
        this.anchorPane.setBackground(new Background(myBI));
    }

    public void setTitleSettings(){
        this.title.setText("B a t t l e s h i p");
        //this.title.setStyle("-fx-font: 70 sans-serif;");
        //   this.title.setTranslateX((double)Menu4.WIDTH/2);
        //    this.title.setTranslateY((double)Menu4.WIDTH/2);

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

        //Dadurch wird die Startposition auf -200 gesetzt
        int from = -300 ;
        this.leftBar_SingPolyText.setTranslateX(from);
        this.leftBar_MultPolyText.setTranslateX(from);
        this.leftBar_SettPolyText.setTranslateX(from);
        this.leftBar_QuitPolyText.setTranslateX(from);

        //Notwendiges Rechteck, damit die Items erst angezeigt werden, wenn sie durch die Linie hindurchgehen
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

        //Dadurch wird die Startposition auf -500 gesetzt
        int from = -500 ;

        //Die StackPanes
        this.rightBar_Sing_PlayerVsKIPolyText.setTranslateX(from);
        this.rightBar_Sing_KIVsKIPolyText.setTranslateX(from);

        this.rightBar_Sing_LoadPolyText.setTranslateX(from);

        //Die Labels + Radio Buttons
     /*   this.rightBarSinglplayer_LabelChooseDifficultyEnemyKI.setTranslateX(from);
        this.rightBarSinglplayer_RB_EnemyKINormal.setTranslateX(from);
        this.rightBarSinglplayer_RB_EnemyKIHard.setTranslateX(from);
        this.rightBarSinglplayer_LabelChooseDifficultyOwnKI.setTranslateX(from);
        this.rightBarSinglplayer_RB_OwnKINormal.setTranslateX(from);
        this.rightBarSinglplayer_RB_OwnKIHard.setTranslateX(from);
*/
        //Notwendiges Rechteck, damit die Items erst angezeigt werden, wenn sie durch die Linie hindurchgehen
        Rectangle clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Sing_PlayerVsKIPolyText.translateXProperty().negate());
        this.rightBar_Sing_PlayerVsKIPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Sing_KIVsKIPolyText.translateXProperty().negate());
        this.rightBar_Sing_KIVsKIPolyText.setClip(clip);
/*
        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_LabelChooseDifficultyEnemyKI.translateXProperty().negate());
        this.rightBarSinglplayer_LabelChooseDifficultyEnemyKI.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_RB_EnemyKINormal.translateXProperty().negate());
        this.rightBarSinglplayer_RB_EnemyKINormal.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_RB_EnemyKIHard.translateXProperty().negate());
        this.rightBarSinglplayer_RB_EnemyKIHard.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_LabelChooseDifficultyOwnKI.translateXProperty().negate());
        this.rightBarSinglplayer_LabelChooseDifficultyOwnKI.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_RB_OwnKINormal.translateXProperty().negate());
        this.rightBarSinglplayer_RB_OwnKINormal.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBarSinglplayer_RB_OwnKIHard.translateXProperty().negate());
        this.rightBarSinglplayer_RB_OwnKIHard.setClip(clip);
*/


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

        /*    slideElements = new TranslateTransition(Duration.seconds(1+1.3*slideSpeed), this.rightBarSinglplayer_LabelChooseDifficultyEnemyKI);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.6*slideSpeed), this.rightBarSinglplayer_RB_EnemyKINormal);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.7*slideSpeed), this.rightBarSinglplayer_RB_EnemyKIHard);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.8*slideSpeed), this.rightBarSinglplayer_LabelChooseDifficultyOwnKI);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.9*slideSpeed), this.rightBarSinglplayer_RB_OwnKINormal);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+2*slideSpeed), this.rightBarSinglplayer_RB_OwnKIHard);
            slideElements.setToX(0);
            slideElements.play();
*/

        });

        scaleLineRight.play();

    }

    public void startAnimationRightSideMultiplayer(){


        double lineScaleDuration = 0.75;
        double slideSpeed = 1.0;
        ScaleTransition scaleLineRight = new ScaleTransition(Duration.seconds(lineScaleDuration),this.rightBarLineMult);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineRight.setToY(1);

        //Dadurch wird die Startposition auf -500 gesetzt
        int from = -500 ;

        //Die StackPanes
        this.rightBar_Mult_HostPolyText.setTranslateX(from);
        this.rightBar_Mult_ClientPolyText.setTranslateX(from);
        this.rightBar_Mult_SpSelectRole.setTranslateX(from);


        //Notwendiges Rechteck, damit die Items erst angezeigt werden, wenn sie durch die Linie hindurchgehen
        Rectangle clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_HostPolyText.translateXProperty().negate());
        this.rightBar_Mult_HostPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_ClientPolyText.translateXProperty().negate());
        this.rightBar_Mult_ClientPolyText.setClip(clip);

        clip = new Rectangle(300,100);
        clip.translateXProperty().bind(rightBar_Mult_SpSelectRole.translateXProperty().negate());
        this.rightBar_Mult_SpSelectRole.setClip(clip);


        //-> After the right line is drawn
        scaleLineRight.setOnFinished( e -> {


            //Slide in the StackPane, containing the Polygon and the Text
            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(1+0.6*slideSpeed), this.rightBar_Mult_HostPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1*slideSpeed), this.rightBar_Mult_ClientPolyText);
            slideElements.setToX(0);
            slideElements.play();

            slideElements = new TranslateTransition(Duration.seconds(1+1.3*slideSpeed), this.rightBar_Mult_SpSelectRole);
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
        polygons.add(rightBarMultiplayer_PolySelectRole);

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
        stackPanes.add(rightBar_Mult_SpSelectRole);

        for(Polygon polygon : polygons) {
            //Draw the Polygons right, for any reason it is not allowed to do in the scene builder...
            polygon.getPoints().removeAll();
            polygon.getPoints().setAll(
                    -75.0, 0.0,         //Oben links
                    180.0, 0.0,         //Oben rechts
                    215.0, 30.0,        //Spitze
                    180.0, 60.0,        //Unten rechts
                    -75.0, 60.0         //Unten links
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

            if (i == 9) break;  // Role selection has no text
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


        Color textColorRightBar = new Color(0.2, 0.2, 0.2, 1);

        for ( Text text : rightBarText ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorRightBar);
            text.setEffect(new DropShadow(30, Color.BLACK));

        }



    }

    public void setRadioButtonSettings(){
        Color textColorRightBar = new Color(0.2, 0.2, 0.2, 1);

        //Radio Buttons

        this.rightBarMultiplayer_RbSelectKI.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.rightBarMultiplayer_RbSelectKI.setTextFill(textColorRightBar);
        this.rightBarMultiplayer_RbSelectKI.setEffect(new DropShadow(30, Color.BLACK));

    }


    public void singleplayerSelected(MouseEvent mouseEvent) {
        selection = Selection.Singleplayer;
        //singleRightNotShown ist ein boolean der anfangs auf true gesetzt wrid, -> True heist es wird noch nicht angezeigt
        // -> Wenn Singleplayer nicht angezeigt wird -> Schließe die anderen Bars und öffne Singleplayer
        // -> Wenn Singleplayer schon angezeigt wird -> Schließe rechte bar (else)
        if (this.singleRightNotShown) {

            //Wenn Multiplayer oder Settings ausgewählt sind, diese zuerst invisible setzen

            //Multiplayer wird gerade angezeigt
            if(!this.multRightNotShown){
                this.showMultBar(false);
                this.multRightNotShown = true;
            }
            //Settings wird grade angezeigt
            if(!this.setRightNotShown){
                this.showSetBar(false);
                this.setRightNotShown = true;
            }
            //Zeige Singleplayer an
            this.showSingBar(true);


        }
        else{
            this.showSingBar(false);

        }

        singleRightNotShown = !singleRightNotShown;
    }

    public void multiplayerSelected(MouseEvent mouseEvent) {
        selection = Selection.Multiplayer;
        if(this.multRightNotShown){

            //Singleplayer wird gerade angezeigt
            if(!this.singleRightNotShown){
                this.showSingBar(false);
                this.singleRightNotShown = true;
            }
            //Settings wird grade angezeigt
            if(!this.setRightNotShown){
                this.showSetBar(false);
                this.setRightNotShown = true;
            }

            //Zeige Multiplayer an
            this.showMultBar(true);
        }
        else{
            this.showMultBar(false);
        }

        multRightNotShown = !multRightNotShown;
    }

    public void settingsSelected(MouseEvent mouseEvent) {
        selection = Selection.Settings;
        if(this.multRightNotShown){

            //Singleplayer wird gerade angezeigt
            if(!this.singleRightNotShown){
                this.showSingBar(false);
                this.singleRightNotShown = true;
            }
            //Multiplayer wird grade angezeigt
            if(!this.multRightNotShown){
                this.showSetBar(false);
                this.multRightNotShown = true;
            }

            //Zeige Settings an
            this.showSetBar(true);
        }
        else{
            this.showSetBar(false);
        }

        setRightNotShown = !setRightNotShown;
    }


    public void showSingBar(boolean show){
        if ( show ){

            setRightBarSingleplayerInvisible(false);
            startAnimationRightSideSingleplayer();
        }
        else{

            setRightBarSingleplayerInvisible(true);
            //Set the rightBar back to Scale 0
            this.rightBarLine.setScaleY(0);
        }
    }
    public void showMultBar(boolean show){
        if ( show ){

            setRightBarMultiplayerInvisible(false);
            startAnimationRightSideMultiplayer();
        }
        else{

            setRightBarMultiplayerInvisible(true);
            this.rightBarLineMult.setScaleY(0);
        }
    }

    public void showSetBar(boolean show){

    }
}
