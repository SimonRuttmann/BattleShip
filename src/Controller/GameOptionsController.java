package Controller;

import Gui_View.Main;
import Player.ActiveGameState;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameOptionsController implements Initializable {

    /** FXML Elements**/
    public AnchorPane anchorPane;
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public Text selectMusicVolumeText;
    public Text musicSliderValueText;
    public Slider selectMusicVolumeSlider;
    public Text selectAIVelocityText;
    public Text velocitySliderValueText;
    public Slider selectAIVelocitySlider;
    public Text languageText;
    public RadioButton rB_selectEnglish;
    public RadioButton rB_selectGerman;
    public Button button_backToMainMenu;
    public Button button_saveSettings;

    /** External Handling**/
    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }

    public void saveSettings(ActionEvent actionEvent) {
        ActiveGameState.setMusicVolume(selectMusicVolumeSlider.valueProperty().intValue());
        ActiveGameState.getMusicController().setVolume(selectMusicVolumeSlider.valueProperty().intValue());
        ActiveGameState.setAiVelocity(selectAIVelocitySlider.valueProperty().intValue());

        if (rB_selectEnglish.isSelected()) ActiveGameState.setLanguage(ActiveGameState.Language.english);
        else ActiveGameState.setLanguage(ActiveGameState.Language.german);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Game Options");
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        startAnimation();
        setRadioButtonSettings();
        setValuesOfMusicSlider();
        setValuesOfVelocitySlider();

    }

    public void setValuesOfMusicSlider(){

        musicSliderValueText.setText("50%");
        selectMusicVolumeSlider.setMin(0);
        selectMusicVolumeSlider.setMax(100);
        selectMusicVolumeSlider.setValue(50);
        this.selectMusicVolumeSlider.setBlockIncrement(5);

        this.selectMusicVolumeSlider.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                musicSliderValueText.setText("" + newValue.intValue() + "%");
            }
        });
    }

    public void setValuesOfVelocitySlider(){
        velocitySliderValueText.setText("1s");
        selectAIVelocitySlider.setMin(0);
        selectAIVelocitySlider.setMax(10);
        selectAIVelocitySlider.setValue(1);
        this.selectAIVelocitySlider.setBlockIncrement(2);

        this.selectAIVelocitySlider.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                velocitySliderValueText.setText("" + newValue.intValue() + "s");
            }
        });
    }

    public void setRadioButtonSettings(){
        ToggleGroup toggleOwn = new ToggleGroup();
        this.rB_selectEnglish.setToggleGroup(toggleOwn);
        this.rB_selectGerman.setToggleGroup(toggleOwn);

        this.rB_selectEnglish.setSelected(true);

    }

    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }

    public void setTitleSettings(){
        this.title.setText("G a m e  O p t i o n s");
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
        headlines.add(this.musicSliderValueText);
        headlines.add(this.velocitySliderValueText);
        headlines.add(this.selectMusicVolumeText);
        headlines.add(this.selectAIVelocityText);
        headlines.add(this.languageText);

        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);

        for ( Text text : headlines ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorHeadlines);
            text.setEffect(new DropShadow(30, Color.BLACK));
            //text.setStyle("-fx-font: 24 arial;");

        }

        this.rB_selectEnglish.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        rB_selectEnglish.setTextFill(textColorHeadlines);
        this.rB_selectGerman.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        rB_selectGerman.setTextFill(textColorHeadlines);
    }
}
