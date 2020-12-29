package Controller;

import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.Main;
import Player.ActiveGameState;
import Player.GameMode;
import Player.SaveAndLoad;
import Player.Savegame;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoadGameController implements Initializable {

    public AnchorPane anchorPane;
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public VBox vBoxElementsOverRectangle;
    public Button loadGameButton;
    public Button backButton;

    @FXML
    private ListView<File> gameList;



    public void backToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(mainMenu));
        Main.primaryStage.show();
    }

//TODO Load Singleplayer, MUltiplayer, none setzen
    public void loadSelectedGame(ActionEvent actionEvent) throws IOException{
        ObservableList<File> fileObservableList = this.gameList.getSelectionModel().getSelectedItems();
        File fileToLoad  = fileObservableList.get(0); //As only one items is selected
        String pathOfFile = fileToLoad.getPath();

        Savegame loadedSavegame = SaveAndLoad.load(pathOfFile);
        if ( loadedSavegame == null) {
            System.out.println( "loading failed" );
        }
        else{
            System.out.println( "loading successfull");
        }

        //Singleplayergame -> Playground
        if (ActiveGameState.getLoading() == ActiveGameState.Loading.singleplayer ){
        Platform.runLater( () -> {
                    Parent game;
                    try {
                        game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                        Main.primaryStage.setScene(new Scene(game));
                        Main.primaryStage.show();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }


                });
        }
        //Starting Multiplayer games by hosting a game
        else if ( ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer){
            Platform.runLater( () -> {
                Parent game;
                try {
                    game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpServer.fxml"));
                    Main.primaryStage.setScene(new Scene(game));
                    Main.primaryStage.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            });
        }
        else{
            System.out.println( "Loading: Multiplayer or Singleplayer not selected at loading a game");
        }
        //TODO
        //ActiveGameState.setLoadId(0);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUpListView();

        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        startAnimation();

        if ( ActiveGameState.getModes() == GameMode.kiVsRemote || ActiveGameState.getModes() == GameMode.playerVsRemote)
            this.backButton.setText("Back to Settings");

    }

    public void setUpListView(){
        // create observable list of the Game Files
        ObservableList<File> observableList = FXCollections.observableArrayList();
        // dir is the folder that contains our saved game files - different for singleplayer and multiplayer
        File dir;
        if(ActiveGameState.isMultiplayer())
            dir = new File(".multiplayerGames");
        else
            dir = new File(".singleplayerGames");

        File[] savedGames = dir.listFiles((directory, filename) -> filename.endsWith(".json"));
        // add files to observable List and furthermore to gameList
        observableList.clear();
        observableList.addAll(savedGames);
        gameList.setItems(observableList);


        // use Cell Factory to display only the Name of the Files + make ContextMenu for loading/deleting gameFile
        gameList.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> gameList) {

                // change updateItem method - display name of file
                ListCell<File> cell = new ListCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null)
                            setText("");
                        else
                            setText(item.getName());
                    }
                };


                // create context menu + function for its items
                ContextMenu contextMenu = new ContextMenu();

                MenuItem load = new MenuItem("Spielstand laden");
                load.setOnAction(e -> {

                    System.out.println(cell.getItem());

                    String temp = cell.getItem().toString();
                    Savegame gameObject = SaveAndLoad.load(temp);

                    if (gameObject != null) {
                        System.out.println("loading successful");
                    }
                    else{
                        System.out.println("loading failed");
                    }

                    // Change scene to game Playground
               /*     Platform.runLater( () -> {
                        Parent game = null;
                        try {
                            game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        Main.primaryStage.setScene(new Scene(game));
                        Main.primaryStage.show();
*/
                   // });


                });

                // delete -> deltes game file from list and also from system
                MenuItem delete = new MenuItem("Spielstand löschen");
                delete.setOnAction(e -> {
                    System.out.println("Delete Item" + cell.getItem().toString());

                    File game = cell.getItem();
                    gameList.getItems().remove(game);
                  //  gameList.getItems().remove(cell.getItem());
                    if(!(game.delete())) System.out.println( "Couldn`t delete file");
                });
                contextMenu.getItems().addAll(load, delete);

                // display context menu only for cells that contain file - not for empty cells
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });

                return cell;
            }
        });
    }







    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/WindowBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
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


        ArrayList<Text> headlines = new ArrayList<>();
        //Add any text element here
        //headlines.add(this.selectAmountOfShipsText);

        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);

        for ( Text text : headlines ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorHeadlines);
            text.setEffect(new DropShadow(30, Color.BLACK));

        }

    }

    public void setTitleSettings(){
        this.title.setText("L O A D  G A M E");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }



}
