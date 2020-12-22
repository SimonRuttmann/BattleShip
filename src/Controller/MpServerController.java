package Controller;

import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.HelpMethods;
import Gui_View.Main;
import Network.IServer;
import Network.Server;
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
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
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

public class MpServerController implements Initializable {
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public Text textShowIP;
    public ProgressIndicator waitForClient;
    public Button button_backToMainMenu;
    public ProgressIndicator loadingIndicator;
    public Text textToShowIP;
    public Text textWaiting;
    public AnchorPane anchorPane;

    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        if ( ActiveGameState.isRunning()) ActiveGameState.getServer().closeConnection();
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        startAnimation();
        offerConnection();

    }


    public void offerConnection(){
        IServer server = new Server();
        textToShowIP.setText(" " + server.getIPAddress());

        // new Thread for Connecting
        Thread offerConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Connection offered - waiting for paring");
                if (server.startSeverConnection()) {
                    ActiveGameState.setServer(server);
                    ActiveGameState.setRunning(true);

                    MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
                    multiplayerControlThreadConfigCommunication.start();

                    //TODO Automatischer Szenewechsel im MultiplayerControlThread, wenn Übertragung erledigt
                } else {
                    System.out.println("Connection could not be established");
                    HelpMethods.connectionFailed();
                }

            }
        });
        offerConnection.start();
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
        this.title.setText("H O S T  G A M E");
        //this.title.setStyle("-fx-font: 70 sans-serif;");
        //   this.title.setTranslateX((double)Menu4.WIDTH/2);
        //    this.title.setTranslateY((double)Menu4.WIDTH/2);

        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }

    public void setTextSettings(){
        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);
        textShowIP.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textShowIP.setFill(textColorHeadlines);
        textShowIP.setEffect(new DropShadow(30, Color.BLACK));

        textToShowIP.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textToShowIP.setFill(textColorHeadlines);
        textToShowIP.setEffect(new DropShadow(30, Color.BLACK));

        textWaiting.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textWaiting.setFill(textColorHeadlines);
        textWaiting.setEffect(new DropShadow(30, Color.BLACK));


    }

    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/BattleshipSuited.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }


    public void dummySwitchScene(ActionEvent actionEvent) throws IOException {
        Parent scene;
        scene = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));


        Main.primaryStage.setScene(new Scene(scene));
        Main.primaryStage.show();
    }

}
