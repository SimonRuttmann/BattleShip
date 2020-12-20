package Controller;


import Gui_View.Main;
import Player.ActiveGameState;
import Player.GameMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
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

    /** External Handling**/
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    public void startShipPlacement(ActionEvent actionEvent) throws IOException{
        Scene nextScene;
        Parent parent = null;
        switch (ActiveGameState.getModes()){
            case kiVsKi:        parent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml")); break;
            case kiVsRemote:    parent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml")); break;
            case playerVsKi:    parent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml")); break;
            case playerVsRemote:parent = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml")); break;
            default:
                System.out.println( "Mode is unknown");

        }
        nextScene = new Scene(parent);

        Main.primaryStage.setScene(nextScene);
        Main.primaryStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Game Settings");
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        setShipImages();
        startAnimation();

        if (ActiveGameState.getModes() == GameMode.kiVsKi) setOwnKiSelectionInvisible();
    }

    public void setOwnKiSelectionInvisible(){
        this.rB_difficultyOwnNormal.setVisible(false);
        this.rB_difficultyOwnHard.setVisible(false);
        this.selectDifficultyOwnKIText.setVisible(false);
    }

    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/BattleshipSuited.jpg")),
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

        int scale = 20;

        // Sets the prefSize depending on the size of the ship
        // Loads the Image for the Ship's
        this.labelShipSize2.setPrefSize(2 * scale, scale);
        this.labelShipSize2.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png"))));

        this.labelShipSize3.setPrefSize(3 * scale, scale);
        this.labelShipSize3.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png"))));

        this.labelShipSize4.setPrefSize(4 * scale, scale);
        this.labelShipSize4.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png"))));

        this.labelShipSize5.setPrefSize(5 * scale, scale);
        this.labelShipSize5.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png"))));

    }
}

