package Controller;

import Gui_View.Main;
import Player.*;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.util.Callback;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoadGameController implements Initializable {

    public static final Logger logLoadGame = Logger.getLogger("parent.LoadGame");

    /** FXML Elements **/
    public AnchorPane anchorPane;
    public Text title;
    public Line lineLeftSide;
    public StackPane sP_RectangleAndElements;
    public Rectangle rectangle;
    public VBox vBoxElementsOverRectangle;
    public Button loadGameButton;
    public Button backButton;
    public ListView<File> gameList;


    /** External Handling **/
    //Action Event call
    public void backToMainMenu() throws IOException {
        logLoadGame.log(Level.INFO, "Switching Scene to Main Menu");
        Parent mainMenu = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
        Main.primaryStage.setScene(new Scene(mainMenu));
        Main.primaryStage.show();
    }

    /**
     * Action event call
     * This method loads the selected game
     * The selected game is represented by an ListView of Files (packed in cells)
     */
    public void loadSelectedGame() {
        ObservableList<File> fileObservableList = this.gameList.getSelectionModel().getSelectedItems();
        File fileToLoad  = fileObservableList.get(0); //As only one items is selected

        if(fileToLoad == null){
            logLoadGame.log(Level.WARNING, "No Savegame selected");
            return;
        }

        String pathOfFile = fileToLoad.getPath();

        Savegame loadedSavegame = SaveAndLoad.load(pathOfFile);
        if ( loadedSavegame == null) {
            logLoadGame.log(Level.SEVERE, "Savegame can´t be loaded, savegame may be corrupted");
        }
        else{
            logLoadGame.log(Level.INFO, "Loading of the Game + " + fileToLoad.getName() + "successful");
        }

        //Singleplayergame -> Playground
        if (ActiveGameState.getLoading() == ActiveGameState.Loading.singleplayer ){

            logLoadGame.log(Level.INFO, "\nSettings: " + "\n" +
                    "\t Singleplayer Player vs AI" + "\n" +
                    "\t Playgroundsize: " + ActiveGameState.getPlaygroundSize() + "\n" +
                    "\t Amount of Ships 2: " + ActiveGameState.getAmountShipSize2() + "\n" +
                    "\t Amount of Ships 3: " + ActiveGameState.getAmountShipSize3() + "\n" +
                    "\t Amount of Ships 4: " + ActiveGameState.getAmountShipSize4() + "\n" +
                    "\t Amount of Ships 5: " + ActiveGameState.getAmountShipSize5() + "\n" +
                    "\t Enemy AI Difficulty: " + ActiveGameState.getEnemyKiDifficulty()
            );

        Platform.runLater( () -> {
                    Parent game;
                    try {
                        logLoadGame.log(Level.INFO, "Switching Scene to Game Playground");
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
                    logLoadGame.log(Level.INFO, "Switching Scene to Mp Server");
                    game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/MpServer.fxml"));
                    Main.primaryStage.setScene(new Scene(game));
                    Main.primaryStage.show();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });
        }
        else{
            logLoadGame.log(Level.SEVERE, "Multiplayer or Singleplayer not selected at loading a game");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLanguage();
        setUpListView();
        setBackground();
        setTextSettings();
        setTitleSettings();
        setLineSettings();
        setRectangleSettings();
        startAnimation();

    }

    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            title.setText("Spiel laden");
            loadGameButton.setText("Spiel laden");
            backButton.setText("Zurück zum Hauptmenü");
        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            title.setText("Load Game");
            loadGameButton.setText("Load Game");
            backButton.setText("Back to Main Menu");
        }
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

                //1// Currently Disabled Feature Load
                //By adding make sure to add the item to the context menu

                //Delete -> delete game file from list and system
                MenuItem delete = new MenuItem();
                delete.setText((ActiveGameState.getLanguage() == ActiveGameState.Language.german) ? "Spielstand löschen" : "Delete Game");
                delete.setOnAction(e -> {
                    logLoadGame.log(Level.INFO, "Delete Item" + cell.getItem().toString());

                    //Remove linker in multiplayer savegames
                    String fileNameToDelete = cell.getItem().toString();
                    String onlyFileNameJson = cell.getItem().getName();
                    String[] onlyFileName = onlyFileNameJson.split("\\.");
                    boolean linkerDeleted;
                    if (ActiveGameState.isMultiplayer()){
                        linkerDeleted = SavegameLinker.removeLinker(onlyFileName[0]);
                        if (!linkerDeleted) logLoadGame.log(Level.WARNING, "Savegame "+ fileNameToDelete +" link couldn`t removed from the savegame linker");
                    }


                    File game = cell.getItem();
                    gameList.getItems().remove(game);

                    if(!(game.delete())) logLoadGame.log(Level.WARNING, "Savegame "+ fileNameToDelete +" couldn't be deleted from system");
                });
                contextMenu.getItems().addAll(delete);

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

        //Sets the starting position of the Stack Pane (containing rectangle and ListView) to -1000
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

    //Placeholder for future text
    public void setTextSettings(){

        ArrayList<Text> headlines = new ArrayList<>();
        //Add any text element here
        //headlines.add(this.newText);

        Color textColorHeadlines = new Color(0.8,0.8,0.8,1);

        for ( Text text : headlines ){
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(textColorHeadlines);
            text.setEffect(new DropShadow(30, Color.BLACK));

        }

    }

    public void setTitleSettings(){
       // this.title.setText("Load Game");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(30, Color.BLACK));

    }



}


/*  //1//    Currently disabled Feature -> right click on the selected file to load
  // load -> changes scene to game, loads game using SaveAndLoad.load()
                MenuItem load = new MenuItem();
                load.setText((ActiveGameState.getLanguage() == ActiveGameState.Language.german) ? "Spielstand laden" : "Load Game");
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
               //     Platform.runLater( () -> {
               //         Parent game = null;
               //         try {
               //             game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
               //         } catch (IOException ioException) {
               //             ioException.printStackTrace();
               //         }
               //         Main.primaryStage.setScene(new Scene(game));
               //         Main.primaryStage.show();

// });


                });

 */
