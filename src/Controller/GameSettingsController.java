package Controller;


import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.Main;
import KI.Ki;
import Player.ActiveGameState;
import Player.GameMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.animation.*;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;

public class GameSettingsController implements Initializable{

    /** FXML Elements**/
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public Text selectPlaygroundsizeText;
    public Slider selectPlaygroundsizeSlider;
    public Text selectAmountOfShipsText;
    public Label labelShipSize2;
    public Label labelShipSize3;
    public Label labelShipSize4;
    public Label labelShipSize5;
    public VBox vBox_KISettings;
    public VBox vBox_EnemyKi;
    public Text selectDifficultyEnemyKIText;
    public RadioButton rB_difficultyEnemyNormal;
    public RadioButton rB_difficultyEnemyHard;
    public VBox vBox_OwnKi;
    public Text selectDifficultyOwnKIText;
    public RadioButton rB_difficultyOwnNormal;
    public RadioButton rB_difficultyOwnHard;
    public Button button_backToMainMenu;
    public Button button_Start;
    public AnchorPane anchorPane;

    public Spinner<Integer> selectAmount2Ships;
    public Spinner<Integer> selectAmount3Ships;
    public Spinner<Integer> selectAmount4Ships;
    public Spinner<Integer> selectAmount5Ships;
    public Text sliderValueText;
    public VBox host_selectRole;
    public RadioButton host_RbSelectKInormal;
    public RadioButton host_RbSelectKIhard;
    public StackPane sP_KIselections;


    /** External Handling**/
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

/*    public void startLoadMultiplayerGame(ActionEvent actionEvent) throws IOException{
        Parent loadGame = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/loadGame.fxml"));
        Main.primaryStage.setScene(new Scene(loadGame));
        Main.primaryStage.show();
    }*/


    public void setGameModeAndKi(){
        if (this.host_RbSelectKInormal.isSelected() || this.host_RbSelectKIhard.isSelected()) {
            ActiveGameState.setModes(GameMode.kiVsRemote);
            if (this.host_RbSelectKInormal.isSelected())
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.normal);
            else
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.hard);

            ActiveGameState.setOwnKi(new Ki(ActiveGameState.getOwnKiDifficulty()));
        } else {
            ActiveGameState.setModes(GameMode.playerVsRemote);
        }
    }



    public void startShipPlacement(ActionEvent actionEvent) throws IOException{


        //Set the selected Settings to ActiveGameState
        ActiveGameState.setPlaygroundSize(this.selectPlaygroundsizeSlider.valueProperty().intValue());
        ActiveGameState.setAmountShipSize2(selectAmount2Ships.getValue());
        ActiveGameState.setAmountShipSize3(selectAmount3Ships.getValue());
        ActiveGameState.setAmountShipSize4(selectAmount4Ships.getValue());
        ActiveGameState.setAmountShipSize5(selectAmount5Ships.getValue());
        ActiveGameState.setAmountOfShips( selectAmount2Ships.getValue() + selectAmount3Ships.getValue() +
                                          selectAmount4Ships.getValue() + selectAmount5Ships.getValue());

        //Start MultiplayerControlThread if multiplayer mode selected
        //GameMode and multiplayer related flags are set by the Menu Scene
        //Client and Server socket are set by the Client, Server Scenes

        //Hier sind wir der Host -> Thread zeigt neue Szene
        if (ActiveGameState.isMultiplayer()){

            setGameModeAndKi();

            MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
            multiplayerControlThreadConfigCommunication.start();
        }
        //Singleplayer
        else {

            if (this.rB_difficultyOwnNormal.isSelected()){
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.normal);
            }
            if (this.rB_difficultyOwnHard.isSelected()){
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.hard);
            }
            if (this.rB_difficultyEnemyNormal.isSelected()){
                ActiveGameState.setEnemyKiDifficulty(Ki.Difficulty.normal);
            }
            if (this.rB_difficultyEnemyHard.isSelected()){
                ActiveGameState.setEnemyKiDifficulty(Ki.Difficulty.hard);
            }

            //Set Scene if singleplayer mode is selected
            switch (ActiveGameState.getModes()) {
                case kiVsKi:

                    ActiveGameState.setOwnKi(new Ki(ActiveGameState.getOwnKiDifficulty()));
                    ActiveGameState.setEnemyKi(new Ki ( ActiveGameState.getEnemyKiDifficulty()));

                    ActiveGameState.setRunning(true);
                    Parent gamePlayground = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                    Main.primaryStage.setScene(new Scene(gamePlayground));
                    Main.primaryStage.show();
                    break;

                case playerVsKi:

                    ActiveGameState.setEnemyKi(new Ki(ActiveGameState.getEnemyKiDifficulty()));

                    ActiveGameState.setRunning(true);
                    Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
                    Main.primaryStage.setScene(new Scene(placeShips));
                    Main.primaryStage.show();
                    break;
            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Game Settings");
        setLanguage();
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        setShipImages();
        startAnimation();
        setValuesOfPlaygroundAndShip();
        setRadioButtonSettings();

        //if (ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.kiVsKi) setMultiplayerLoadGameInvisible();

        //System.out.println(ActiveGameState.getModes()); Modus liegt hier beim Multiplayer noch nicht fest
        //if ( (ActiveGameState.getModes() == GameMode.playerVsKi) || ( ActiveGameState.getModes() == GameMode.playerVsRemote)) setOwnKiSelectionInvisible();
        //Modes:    Hier ist nur der host bei multiplayer -> player vs Ki -> Own ki invisible, kivs ki nichts invisible
        //Multiplayer -> kein Modus gesetzt

        setRadioSettingsForMultiplayerSelection();
        //setMultiplayerSelectRoleInvisible();

        if (ActiveGameState.getModes() == GameMode.playerVsKi) setOwnKiSelectionInvisible();




        if (ActiveGameState.isMultiplayer()) {
            //setEnemyKiSelectionInvisible();
            //setOwnKiSelectionInvisible();
            //setMultiplayerSelectRoleVisible();

            disableSingleplayerKiSelection();
        }
        else{
            disableHostKiSelection();
        }
        //Immer alles an, außer bei player vs ki

    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("Spieleinstellungen");
            selectPlaygroundsizeText.setText("Wähle die Spielfeldgröße");
            selectAmountOfShipsText.setText("Wählen sie die Anzahl der Schiffe aus");
            selectDifficultyEnemyKIText.setText("Schwierigkeit der gegenerischen KI");
            selectDifficultyOwnKIText.setText("Schwierigkeit der eigenen Ki");
            button_backToMainMenu.setText("Zurück zum Hauptmenü");
            button_Start.setText("Start");
            rB_difficultyEnemyNormal.setText("normal");
            rB_difficultyEnemyHard.setText("schwer");
            rB_difficultyOwnNormal.setText("normal");
            rB_difficultyOwnHard.setText("schwer");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("Game Settings");
            selectPlaygroundsizeText.setText("Select Playgroundsize");
            selectAmountOfShipsText.setText("Select the amount of ships");
            selectDifficultyEnemyKIText.setText("Difficulty of Enemy Ki");
            selectDifficultyOwnKIText.setText("Difficulty of Own Ki");
            button_backToMainMenu.setText("Back to Main Menu");
            button_Start.setText("Start");
            rB_difficultyEnemyNormal.setText("normal");
            rB_difficultyEnemyHard.setText("hard");
            rB_difficultyOwnNormal.setText("normal");
            rB_difficultyOwnHard.setText("hard");
        }
    }

    public void disableHostKiSelection(){
        sP_KIselections.getChildren().remove(host_selectRole);
    }
    public void disableSingleplayerKiSelection(){
        sP_KIselections.getChildren().remove(vBox_KISettings);
    }
/*
public VBox host_selectRole;
    public RadioButton host_RbSelectKInormal;
    public RadioButton host_RbSelectKIhard;
 */

    public void setRadioSettingsForMultiplayerSelection(){
        Color rB_color = new Color(0.8, 0.8, 0.8, 1);

        //Radio Buttons
        //Shadows are too big -> Events will trigger to far

        this.host_RbSelectKInormal.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.host_RbSelectKInormal.setTextFill(rB_color);
        //  this.rightBarMultiplayer_RbSelectKInormal.setEffect(new DropShadow(30, Color.BLACK));

        this.host_RbSelectKIhard.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.host_RbSelectKIhard.setTextFill(rB_color);
        //   this.rightBarMultiplayer_RbSelectKIhard.setEffect(new DropShadow(30, Color.BLACK));

        host_RbSelectKInormal.setOnAction( event -> {
            host_RbSelectKIhard.setSelected(false);
        });

        host_RbSelectKIhard.setOnAction( event -> {
            host_RbSelectKInormal.setSelected(false);
        });

    }



    public void setMultiplayerSelectRoleVisible(){
        host_RbSelectKInormal.setVisible(true);
        host_RbSelectKIhard.setVisible(true);
    }

    public void setMultiplayerSelectRoleInvisible(){
        host_RbSelectKInormal.setVisible(false);
        host_RbSelectKIhard.setVisible(false);
    }

    public void setRadioButtonSettings(){
        ToggleGroup toggleOwn = new ToggleGroup();
        this.rB_difficultyOwnNormal.setToggleGroup(toggleOwn);
        this.rB_difficultyOwnHard.setToggleGroup(toggleOwn);

        this.rB_difficultyOwnNormal.setSelected(true);

        ToggleGroup toggleEnemy = new ToggleGroup();
        this.rB_difficultyEnemyNormal.setToggleGroup(toggleEnemy);
        this.rB_difficultyEnemyHard.setToggleGroup(toggleEnemy);

        this.rB_difficultyEnemyNormal.setSelected(true);
    }



    public void setValuesOfPlaygroundAndShip(){

            //Slider
            this.sliderValueText.setText("10");
            this.selectPlaygroundsizeSlider.setMin(5);
            this.selectPlaygroundsizeSlider.setMax(30);
            this.selectPlaygroundsizeSlider.setValue(10);
            this.selectPlaygroundsizeSlider.setBlockIncrement(1);

            this.selectPlaygroundsizeSlider.valueProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    sliderValueText.setText("" + newValue.intValue());
                }
            });

        /*
        SimpleIntegerProperty initialValue2 = new SimpleIntegerProperty();
        SimpleIntegerProperty initialValue3 = new SimpleIntegerProperty();
        SimpleIntegerProperty initialValue4 = new SimpleIntegerProperty();
        SimpleIntegerProperty initialValue5 = new SimpleIntegerProperty();

        initialValue2.bind(selectPlaygroundsizeSlider.valueProperty().divide(2));
        initialValue3.bind(selectPlaygroundsizeSlider.valueProperty().divide(3));
        initialValue4.bind(selectPlaygroundsizeSlider.valueProperty().divide(5));
        initialValue5.bind(selectPlaygroundsizeSlider.valueProperty().divide(10));
*/

            //Spinner
            SimpleIntegerProperty maxValue2 = new SimpleIntegerProperty();
            SimpleIntegerProperty maxValue3 = new SimpleIntegerProperty();
            SimpleIntegerProperty maxValue4 = new SimpleIntegerProperty();
            SimpleIntegerProperty maxValue5 = new SimpleIntegerProperty();

            maxValue2.bind(selectPlaygroundsizeSlider.valueProperty().divide(1.7));
            maxValue3.bind(selectPlaygroundsizeSlider.valueProperty().divide(2.3));
            maxValue4.bind(selectPlaygroundsizeSlider.valueProperty().divide(3));
            maxValue5.bind(selectPlaygroundsizeSlider.valueProperty().divide(7));


            SpinnerValueFactory.IntegerSpinnerValueFactory spinner2Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner3Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner4Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 2);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner5Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);

            IntegerProperty max2Property = spinner2Factory.maxProperty();
            IntegerProperty max3Property = spinner3Factory.maxProperty();
            IntegerProperty max4Property = spinner4Factory.maxProperty();
            IntegerProperty max5Property = spinner5Factory.maxProperty();


            max2Property.bindBidirectional(maxValue2);
            max3Property.bindBidirectional(maxValue3);
            max4Property.bindBidirectional(maxValue4);
            max5Property.bindBidirectional(maxValue5);


            this.selectAmount2Ships.setValueFactory(spinner2Factory);
            this.selectAmount3Ships.setValueFactory(spinner3Factory);
            this.selectAmount4Ships.setValueFactory(spinner4Factory);
            this.selectAmount5Ships.setValueFactory(spinner5Factory);



    }

    public void setEnemyKiSelectionInvisible(){
        this.rB_difficultyEnemyNormal.setVisible(false);
        this.rB_difficultyEnemyHard.setVisible(false);
        this.selectDifficultyEnemyKIText.setVisible(false);
    }

    public void setOwnKiSelectionInvisible(){
        this.rB_difficultyOwnNormal.setVisible(false);
        this.rB_difficultyOwnHard.setVisible(false);
        this.selectDifficultyOwnKIText.setVisible(false);
    }

    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }

    public void setTitleSettings(){
        this.title.setText("G a m e  S e t t i n g s");
        //this.title.setStyle("-fx-font: 70 sans-serif;");
        //   this.title.setTranslateX((double)Menu4.WIDTH/2);
        //    this.title.setTranslateY((double)Menu4.WIDTH/2);

        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

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

        int lineScaleDuration = 1;
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
        scaleLineLeft.setOnFinished( e -> {

            //Slide in the StackPane, containing the Polygon and the Text
            TranslateTransition slideElements = new TranslateTransition(Duration.seconds(1+0.6*slideSpeed), this.sP_RectangleAndElements);
            slideElements.setToX(1);
            slideElements.play();


        });

        scaleLineLeft.play();

    }

    public void setRectangleSettings(){
        Effect shadow = new DropShadow(5, Color.BLACK);
        Effect blur = new BoxBlur(1, 1, 3);

        this.rectangle.setStroke(Color.color(0,0,0,0.5));
        this.rectangle.setEffect(new GaussianBlur());
        this.rectangle.setFill(Color.color(0,0,0,0.5));
    }

    public void setTextSettings(){

        //TextSettings of Left Bar
        ArrayList<Text> headlines = new ArrayList<>();
        headlines.add(this.selectAmountOfShipsText);
        headlines.add(this.selectPlaygroundsizeText);
        headlines.add(this.selectDifficultyEnemyKIText);
        headlines.add(this.selectDifficultyOwnKIText);
        headlines.add(this.sliderValueText);


        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);

        for ( Text text : headlines ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorHeadlines);
            text.setEffect(new DropShadow(30, Color.BLACK));
            //text.setStyle("-fx-font: 24 arial;");

        }

        this.rB_difficultyEnemyNormal.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.rB_difficultyEnemyHard.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.rB_difficultyOwnNormal.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.rB_difficultyOwnHard.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        //this.multiplayerLoadGame.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        //this.multiplayerLoadGame.setTextFill(textColorHeadlines);
    }

    public void setShipImages(){

        int scale = 30;

        // Sets the prefSize depending on the size of the ship
        // Loads the Image for the Ship's
        this.labelShipSize2.setPrefSize(2 * scale, scale);
        ImageView image2 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png")));
        image2.fitWidthProperty().bind(new SimpleIntegerProperty(2 * scale).asObject());
        image2.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        this.labelShipSize2.setGraphic(image2);


        this.labelShipSize3.setPrefSize(3 * scale, scale);
        ImageView image3 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png")));
        image3.fitWidthProperty().bind(new SimpleIntegerProperty(3 * scale).asObject());
        image3.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        this.labelShipSize3.setGraphic(image3);

        this.labelShipSize4.setPrefSize(4 * scale, scale);
        ImageView image4 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png")));
        image4.fitWidthProperty().bind(new SimpleIntegerProperty(4 * scale).asObject());
        image4.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        this.labelShipSize4.setGraphic(image4);

        this.labelShipSize5.setPrefSize(5 * scale, scale);
        ImageView image5 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png")));
        image5.fitWidthProperty().bind(new SimpleIntegerProperty(5 * scale).asObject());
        image5.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        this.labelShipSize5.setGraphic(image5);

    }


}

