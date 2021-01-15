package Controller;

import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.HelpMethods;
import Gui_View.Main;
import KI.Ki;
import Network.Client;
import Player.ActiveGameState;
import Player.GameMode;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ResourceBundle;

public class MpClientController implements Initializable {
    public AnchorPane anchorPane;
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public Text textInsertIP;
    public TextField textfieldIP;
    public Button button_backToMainMenu;
    public Button button_Start;
    public ProgressIndicator loadingIndicator;
    public Label infoLabel;

    public static Label infoLabelS;
    public RadioButton client_RbSelectKInormal;
    public VBox client_selectRole;
    public RadioButton client_RbSelectKIhard;


    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }


    public void setGameModeAndKi(){
        if (this.client_RbSelectKInormal.isSelected() || this.client_RbSelectKIhard.isSelected()) {
            ActiveGameState.setModes(GameMode.kiVsRemote);
            if (this.client_RbSelectKInormal.isSelected()){
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.normal);
                ActiveGameState.setOwnKi(new Ki(ActiveGameState.getOwnKiDifficulty()));
            }
            else {
                ActiveGameState.setOwnKiDifficulty(Ki.Difficulty.hard);
                ActiveGameState.setOwnKi(new Ki(ActiveGameState.getOwnKiDifficulty()));
            }
        } else {
            ActiveGameState.setModes(GameMode.playerVsRemote);
        }
    }


    public void connect(ActionEvent actionEvent) {

        setGameModeAndKi();

        ActiveGameState.setAmIServer(false);
        loadingIndicator.setVisible(true);
        button_Start.setVisible(false);
        System.out.println("connectButton pressed");


        Thread searchHost = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Searching for Host");

                ActiveGameState.setClient(new Client(textfieldIP.getText()));
               // ActiveGameState.setRunning(true);

                if (ActiveGameState.isRunning()) {

                        MpClientController.infoLabelS.setVisible(true);

                        MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
                        multiplayerControlThreadConfigCommunication.start();


                } else {
                    System.out.println("Connection could not be established");

                    HelpMethods.connectionFailed();
                }

            }
        });
        searchHost.start();

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoLabelS = infoLabel; //Necessary to access infoLabel in Thread
        setLanguage();
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        setInfoLabelInvisible(true);
        setRadioButtonSettings();
        startAnimation();

    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("Spiel beitreten");
            textInsertIP.setText("Geben sie die IP des Hosts ein:");
            infoLabel.setText("Die Verbindung wurde hergestellt. Warten sie bis der Host die Einstellungen eingestellt hat.");
            button_backToMainMenu.setText("Zurück zum Hauptmenü");
            button_Start.setText("Start");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("Join Game");
            textInsertIP.setText("Insert IP of Host:");
            infoLabel.setText("Connection established. Wait until the host has set the settings");
            button_backToMainMenu.setText("Back to Main Menu");
            button_Start.setText("Start");
        }
    }



    public void setRadioButtonSettings(){
        Color rB_color = new Color(0.8, 0.8, 0.8, 1);

        //Radio Buttons
        //Shadows are too big -> Events will trigger to far

        this.client_RbSelectKInormal.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.client_RbSelectKInormal.setTextFill(rB_color);
        //  this.rightBarMultiplayer_RbSelectKInormal.setEffect(new DropShadow(30, Color.BLACK));

        this.client_RbSelectKIhard.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        this.client_RbSelectKIhard.setTextFill(rB_color);
        //   this.rightBarMultiplayer_RbSelectKIhard.setEffect(new DropShadow(30, Color.BLACK));

        client_RbSelectKInormal.setOnAction( event -> {
            client_RbSelectKIhard.setSelected(false);
        });

        client_RbSelectKIhard.setOnAction( event -> {
            client_RbSelectKInormal.setSelected(false);
        });

    }




    public void setInfoLabelInvisible(boolean invisible){
        this.infoLabel.setVisible(!invisible);
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

    public void setLineSettings(){
        Color lineColor = new Color(1,1,1, 0.75);
        this.lineLeftSide.setStroke(lineColor);
        this.lineLeftSide.setStrokeWidth(3);

        this.lineLeftSide.setEffect(new DropShadow(5, Color.BLACK));

        // Sets the Scaling of the Bars, as we want to show them from nothing, we set the to 0
        // If we would set this one to 1.5 the Line (which is 300 Long) would decrease from 450 to 300
        this.lineLeftSide.setScaleY(0);

    }

    public void setTitleSettings(){
        this.title.setText("C O N N E C T I O N");
        //this.title.setStyle("-fx-font: 70 sans-serif;");
        //   this.title.setTranslateX((double)Menu4.WIDTH/2);
        //    this.title.setTranslateY((double)Menu4.WIDTH/2);

        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }

    public void setTextSettings(){
        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);
        textInsertIP.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textInsertIP.setFill(textColorHeadlines);
        textInsertIP.setEffect(new DropShadow(30, Color.BLACK));

        infoLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        infoLabel.setTextFill(textColorHeadlines);
        infoLabel.setEffect(new DropShadow(30, Color.BLACK));
    }

    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }


}



