package Controller;

import Controller.WorkingThreads.MultiplayerControlThreadConfigCommunication;
import Gui_View.HelpMethods;
import Gui_View.Main;
import Network.IServer;
import Network.Server;
import GameData.ActiveGameState;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MpServerController implements Initializable {
    public static final Logger logMpServerController = Logger.getLogger("parent.MpServerController");

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

    //User actionEvent call, closes the offered connection
    public void backToMainMenu() throws IOException {
        ActiveGameState.getServer().closeConnection();
        Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(gameSettings));
        Main.primaryStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLanguage();
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        startAnimation();
        offerConnection();
    }


    /**
     * This method is started at the initialize method
     * Starts the offerConnection Thread, which calls server.startServerConnection();
     * When a connection could be established, running is set on true and the MultiplayerControlThreadConfigCommunication Thread is started
     * This thread will switch the scene to GamePlayground, when a game gets loaded
     * Otherwise the scene switches directly to GameSettings
     */
    public void offerConnection(){
        IServer server = new Server();
        textToShowIP.setText(" " + server.getIPAddress());
        ActiveGameState.setServer(server);

        // new Thread for Connecting
        Thread offerConnection = new Thread(new Runnable() {
            @Override
            public void run() {

                logMpServerController.log(Level.INFO, "Connection offered, waiting for paring");

                Server.ConnectionStatus connectionStatus = server.startSeverConnection();
                if (connectionStatus== Server.ConnectionStatus.Connected) {
                    ActiveGameState.setRunning(true);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if (ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer){
                                    MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
                                    multiplayerControlThreadConfigCommunication.start();
                                }
                                else{

                                    logMpServerController.log(Level.INFO, "Switching Scene to Game Settings");

                                    Parent gameSettings = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/GameSettings.fxml"));
                                    Main.primaryStage.setScene(new Scene(gameSettings));
                                    Main.primaryStage.show();
                                }
                            }catch (IOException e){
                                logMpServerController.log( Level.SEVERE,"Game Settings Scene couldn't be loaded");
                            }
                        }
                    });

                } else {
                    switch (connectionStatus){
                        case ManualClose:   break;
                        case Timeout:
                        case ioException:   HelpMethods.connectionFailed();

                    }
                }

            }
        });
        offerConnection.start();
    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("Spiel Hosten");
            textShowIP.setText("Deine IP Adresse:");
            textWaiting.setText("Warten auf den Client");
            button_backToMainMenu.setText("Hauptmenü");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("Host Game");
            textShowIP.setText("Your IP Adress:");
            textWaiting.setText("Waiting for Client");
            button_backToMainMenu.setText("Main Menu");
        }
    }


    public void startAnimation() {

        int lineScaleDuration = 1;
        double slideSpeed = 1.5;
        ScaleTransition scaleLineLeft = new ScaleTransition(Duration.seconds(lineScaleDuration),this.lineLeftSide);

        //This one says, how far we want to scale, with one the object will have the same size as initial, with 2 the object will have the double size
        scaleLineLeft.setToY(1);

        //Sets the starting positions of the StackPanes to -500
        int from = -1000;
        this.sP_RectangleAndElements.setTranslateX(from);


        //Simple clip, to show the slided elements only when they reached the line
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

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
    }



}
