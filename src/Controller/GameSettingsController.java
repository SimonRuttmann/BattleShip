package Controller;

import Controller.WorkingThreads.MultiplayerControlThreadConfigCommunication;
import Gui_View.Main;
import KI.Ki;
import GameData.ActiveGameState;
import GameData.GameMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.logging.Level;
import java.util.logging.Logger;



public class GameSettingsController implements Initializable{

    public static final Logger logGameSettings = Logger.getLogger("parent.GameSettings");

    /** FXML Elements **/
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
    //Action Event call
    public void backToMainMenu() throws IOException {
        //Close server connection in mode player vs remote or ki vs remote, if the player decided to go move back to the menu
        if (ActiveGameState.getServer() != null) ActiveGameState.getServer().closeConnection();

        logGameSettings.log(Level.INFO, "Switching Scene to Main Menu");
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }




    /**
     * Action Event call
     * Sets the AmountOfShips, KI difficulty and GameMode depending on the user input
     * @throws IOException The exception only occurs, when the
     * scene couldn`t be loaded. In this case there is no meaningful handle possible
     */
    public void startShipPlacement() throws IOException{


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

        //In multiplayer modes only the host occurs here
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

                    logGameSettings.log(Level.INFO, "\nSettings: " + "\n" +
                            "\t Singleplayer AI vs AI" + "\n" +
                            "\t Playgroundsize: " + ActiveGameState.getPlaygroundSize() + "\n" +
                            "\t Amount of Ships 2: " + ActiveGameState.getAmountShipSize2() + "\n" +
                            "\t Amount of Ships 3: " + ActiveGameState.getAmountShipSize3() + "\n" +
                            "\t Amount of Ships 4: " + ActiveGameState.getAmountShipSize4() + "\n" +
                            "\t Amount of Ships 5: " + ActiveGameState.getAmountShipSize5() + "\n" +
                            "\t Own AI Difficulty: " + ActiveGameState.getOwnKiDifficulty() + "\n" +
                            "\t Enemy AI Difficulty: " + ActiveGameState.getEnemyKiDifficulty()
                    );

                    ActiveGameState.setRunning(true);
                    Parent gamePlayground = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                    Main.primaryStage.setScene(new Scene(gamePlayground));
                    Main.primaryStage.show();
                    break;

                case playerVsKi:

                    ActiveGameState.setEnemyKi(new Ki(ActiveGameState.getEnemyKiDifficulty()));

                    logGameSettings.log(Level.INFO, "\nSettings: " + "\n" +
                            "\t Singleplayer Player vs AI" + "\n" +
                            "\t Playgroundsize: " + ActiveGameState.getPlaygroundSize() + "\n" +
                            "\t Amount of Ships 2: " + ActiveGameState.getAmountShipSize2() + "\n" +
                            "\t Amount of Ships 3: " + ActiveGameState.getAmountShipSize3() + "\n" +
                            "\t Amount of Ships 4: " + ActiveGameState.getAmountShipSize4() + "\n" +
                            "\t Amount of Ships 5: " + ActiveGameState.getAmountShipSize5() + "\n" +
                            "\t Enemy AI Difficulty: " + ActiveGameState.getEnemyKiDifficulty()
                    );

                    ActiveGameState.setRunning(true);
                    Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
                    Main.primaryStage.setScene(new Scene(placeShips));
                    Main.primaryStage.show();
                    break;
            }
        }

    }

    /**
     * Help Method, called by startShipPlacement();
     * Sets the GameMode and the Ki with the selected Difficulty, when multiplayer is selected
     */
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

        setRadioSettingsForMultiplayerSelection();

        //Switching the shown KI selections, depending on multiplayer or singleplayer mode
        if (ActiveGameState.getModes() == GameMode.playerVsKi) setOwnKiSelectionInvisible();

        if (ActiveGameState.isMultiplayer()) {
            disableSingleplayerKiSelection();
        }
        else{
            disableHostKiSelection();
        }

    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("Spieleinstellungen");
            selectPlaygroundsizeText.setText("Wähle die Spielfeldgröße");
            selectAmountOfShipsText.setText("Anzahl der Schiffe auswählen");
            selectDifficultyEnemyKIText.setText("Gegenerische KI");
            selectDifficultyOwnKIText.setText("Eigene KI");
            button_backToMainMenu.setText("Hauptmenü");
            button_Start.setText("Start");
            rB_difficultyEnemyNormal.setText("normal");
            rB_difficultyEnemyHard.setText("schwer");
            rB_difficultyOwnNormal.setText("normal");
            rB_difficultyOwnHard.setText("schwer");
            host_RbSelectKInormal.setText("Normale KI spielt");
            host_RbSelectKIhard.setText("Schwere KI spielt");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("Game Settings");
            selectPlaygroundsizeText.setText("Select Playgroundsize");
            selectAmountOfShipsText.setText("Select the amount of ships");
            selectDifficultyEnemyKIText.setText("Difficulty of Enemy AI");
            selectDifficultyOwnKIText.setText("Difficulty of Own AI");
            button_backToMainMenu.setText("Main Menu");
            button_Start.setText("Start");
            rB_difficultyEnemyNormal.setText("normal");
            rB_difficultyEnemyHard.setText("hard");
            rB_difficultyOwnNormal.setText("normal");
            rB_difficultyOwnHard.setText("hard");
            host_RbSelectKInormal.setText("Normal AI plays");
            host_RbSelectKIhard.setText("Hard AI plays");
        }
    }

    /**
     * Disabling the singleplayer or host ki selection by removing all fxml children from the parent
     */
    public void disableHostKiSelection(){
        sP_KIselections.getChildren().remove(host_selectRole);
    }
    public void disableSingleplayerKiSelection(){
        sP_KIselections.getChildren().remove(vBox_KISettings);
    }


    public void setRadioSettingsForMultiplayerSelection(){
        Color rB_color = new Color(0.8, 0.8, 0.8, 1);

        //Radio Buttons
        //Shadows are commented out because the effects are too big -> Events will trigger to far

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


    /**
     * Currently commented out, as the RB still block each other when set invisible
     */
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

    //Garbage Collector workaround
    private static SimpleIntegerProperty maxValue2;
    private static SimpleIntegerProperty maxValue3;
    private static SimpleIntegerProperty maxValue4;
    private static SimpleIntegerProperty maxValue5;

    private static IntegerProperty max2Property;
    private static IntegerProperty max3Property;
    private static IntegerProperty max4Property;
    private static IntegerProperty max5Property;

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


            //Spinner
            maxValue2 = new SimpleIntegerProperty();
            maxValue3 = new SimpleIntegerProperty();
            maxValue4 = new SimpleIntegerProperty();
            maxValue5 = new SimpleIntegerProperty();

            maxValue2.bind(selectPlaygroundsizeSlider.valueProperty().divide(2.5));
            maxValue3.bind(selectPlaygroundsizeSlider.valueProperty().divide(3.3));
            maxValue4.bind(selectPlaygroundsizeSlider.valueProperty().divide(5));
            maxValue5.bind(selectPlaygroundsizeSlider.valueProperty().divide(10));


            SpinnerValueFactory.IntegerSpinnerValueFactory spinner2Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner3Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 3);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner4Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 2);
            SpinnerValueFactory.IntegerSpinnerValueFactory spinner5Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);

            max2Property = spinner2Factory.maxProperty();
            max3Property = spinner3Factory.maxProperty();
            max4Property = spinner4Factory.maxProperty();
            max5Property = spinner5Factory.maxProperty();


            max2Property.bindBidirectional(maxValue2);
            max3Property.bindBidirectional(maxValue3);
            max4Property.bindBidirectional(maxValue4);
            max5Property.bindBidirectional(maxValue5);


            this.selectAmount2Ships.setValueFactory(spinner2Factory);
            this.selectAmount3Ships.setValueFactory(spinner3Factory);
            this.selectAmount4Ships.setValueFactory(spinner4Factory);
            this.selectAmount5Ships.setValueFactory(spinner5Factory);



    }

    /**
     * Sets the Ki selections invisible, depending on the set mode
     * setEnemyKiSelectionInvisible is currently not in use
     */
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
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }

    public void setLineSettings(){
        Color lineColor = new Color(1,1,1, 0.75);
        this.lineLeftSide.setStroke(lineColor);
        this.lineLeftSide.setStrokeWidth(3);

        this.lineLeftSide.setEffect(new DropShadow(5, Color.BLACK));

        // Sets the Scaling of the Bars, as we want to show them from nothing, we set the Line to 0
        // If we would set this one to 1.5 the Line (which is 300 Long) would decrease from 450 to 300
        this.lineLeftSide.setScaleY(0);

    }

    public void startAnimation() {

        int lineScaleDuration = 1;
        double slideSpeed = 1.5;
        ScaleTransition scaleLineLeft = new ScaleTransition(Duration.seconds(lineScaleDuration),this.lineLeftSide);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineLeft.setToY(1);

        //Sets the starting position of the stackPane (containing Rectangle and all selection elements) to -1000
        int from = -1000;
        this.sP_RectangleAndElements.setTranslateX(from);


        //Simple clip to hide the slided rectangle till the line is reached
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

