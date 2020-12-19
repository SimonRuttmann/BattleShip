package Controller;

import Gui_View.Main;
import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;
import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import javax.naming.Binding;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaceShips implements Initializable {


    @FXML
    private Label ownFieldLabel;
    @FXML
    private GridPane ownField;
    @FXML
    private Label twoShip;
    @FXML
    private Label threeShip;
    @FXML
    private Label fourShip;
    @FXML
    private Label fiveShip;
    @FXML
    private Label bracket2;
    @FXML
    private Label bracket3;
    @FXML
    private Label bracket4;
    @FXML
    private Label bracket5;
    @FXML
    private Label twoOf;
    @FXML
    private Label threeOf;
    @FXML
    private Label fourOf;
    @FXML
    private Label fiveOf;
    @FXML
    private Label xOfx2Ships;
    @FXML
    private Label xOfx3Ships;
    @FXML
    private Label xOfx4Ships;
    @FXML
    private Label xOfx5Ships;
    @FXML
    private Button rotate90;
    @FXML
    private Button randomPlacement;
    @FXML
    private Button resetPlacement;
    @FXML
    private Button readyButton;

    String green = "-fx-background-color: #a5fda5";
    String red = "-fx-background-color: #ffa1a1";
    String blue = "-fx-background-color: lightblue";
    String indicateValidPlacement = red;
    boolean horizontal = true;

    int amountShipSize2placed = 0;
    int amountShipSize3placed = 0;//todo weg or needed???
    int amountShipSize4placed = 0;
    int amountShipSize5placed = 0;

    int gamesize = ActiveGameState.getPlaygroundSize();
    Object[] ownFieldArray = new Object[gamesize * gamesize];


    double localX;
    double localY;
    public Group groupID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActiveGameState.setSceneIsPlaceShips(true);

        // create playgrounds
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.setOwnPlayerIEnemyPlayground(new EnemyPlayground());
        if (!ActiveGameState.isMultiplayer()) {
            ActiveGameState.setEnemyPlayerOwnPlayground(new OwnPlayground());
            ActiveGameState.setEnemyPlayerEnemyPlayground(new EnemyPlayground());
        }

        // build playgrounds
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIEnemyPlayground().buildPlayground();
        if (!ActiveGameState.isMultiplayer()) {
            ActiveGameState.getEnemyPlayerOwnPlayground().buildPlayground();
            ActiveGameState.getEnemyPlayerEnemyPlayground().buildPlayground();
        }

        //Sets the Image for the rotate 90 Degree button
        rotate90.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/rotateShipButton.png"))));
        //disables the readyButton -> game can only be started if all ships are placed
        readyButton.setDisable(true);

        System.out.println("window size: " + Main.primaryStage.getHeight());
        //todo public scale - for fields -> pref size -> better way 2:3 for programm fix -> use SceneSizeChangeListener to scale properly all the time - no strange things happening

        //The scale of one Field,   Ship size 2 -> Image: | 30px | 30px |
        //                          Ship size 3 -> Image: | 30px | 30px | 30px |
        int scale = 30;


        // Sets the prefSize depending on the size of the ship
        // Loads the Image for the Ship's
        twoShip.setPrefSize(2 * scale, scale);
        twoShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png"))));

        threeShip.setPrefSize(3 * scale, scale);
        threeShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png"))));

        fourShip.setPrefSize(4 * scale, scale);
        fourShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png"))));

        fiveShip.setPrefSize(5 * scale, scale);
        fiveShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png"))));


        // Labels e.g. (0 out of 4 Placed) -> set Hight Matching to scale -> ship labels depend on scale //todo evlt. einfach generell auf 30 statt so umstaendlich
        bracket2.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        bracket3.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        bracket4.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        bracket5.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        twoOf.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        threeOf.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        fourOf.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        fiveOf.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx2Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx3Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx4Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx5Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);


        // set field title Label to Player Name
        ownFieldLabel.setText(ActiveGameState.getOwnPlayerName() + "'s Spielfeld");
        // 2D field for Labels:
        ownField.setHgap(1);
        ownField.setVgap(1);

        // Creates the ownPlayground as a GridPane. Creating a table of Labels, these are later connected to the game playground via the method setLabels()
        for (int x = 0; x < gamesize; x++) {
            for (int y = 0; y < gamesize; y++) {

                //Creating the Labels
                Label label = new Label();
                label.setStyle(blue);
                label.setMinSize(5, 5);
                label.setPrefSize(scale, scale);
                label.setMaxSize(scale, scale);


                // finalX is the x position of the label
                // finalY is the y position of the label
                int finalX = x;
                int finalY = y;

                //Every label is getting an OnDragOver-Listener, listening if a ship is hovered over it
                //if the placement is valid, this will be displayed by green fields, if not by red
                //ships can only be dropped, if the placement is valid (green)
                label.setOnDragOver(event -> {

                    //The following is done for every ship size (all different placeable ship labels)

                    try {
                        // event.getGestureSource() is getting the Element, which is hovered over the label (here the placable ship label)
                        if (event.getGestureSource() == twoShip) {

                            //When the ship placement is allowed, the String indicateValidPlacement is set to the string green, which contains a -fx css color-scheme
                            //if the ship placement is not allowed the String is set to the string red, containing another -fx css color-scheme
                            if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY), new Point(finalX + 1, finalY))) ||
                                    (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY), new Point(finalX, finalY + 1)))) {
                                // label does only accept ship if placement is valid -> when not valid, ship is not placeable, shipLabel gets dropped back to start position
                                event.acceptTransferModes(TransferMode.ANY);
                                indicateValidPlacement = green;
                            } else
                                indicateValidPlacement = red;
                            label.setStyle(indicateValidPlacement);

                            // Set the style of the labels for all labels, where the ship is hovered over
                            if (horizontal)
                                (getNodeByRowColumnIndex(finalY, finalX + 1, ownField)).setStyle(indicateValidPlacement);
                            else
                                (getNodeByRowColumnIndex(finalY + 1, finalX, ownField)).setStyle(indicateValidPlacement);
                        }


                        if (event.getGestureSource() == threeShip) {
                            if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 1, finalY), new Point(finalX + 1, finalY))) ||
                                    (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 1), new Point(finalX, finalY + 1)))) {
                                // label does only accept ship if placement is valid -> when not valid, ship is not placeable, shipLabel gets dropped back to start position
                                event.acceptTransferModes(TransferMode.ANY);
                                indicateValidPlacement = green;
                            } else
                                indicateValidPlacement = red;
                            label.setStyle(indicateValidPlacement);
                            if (horizontal) {
                                (getNodeByRowColumnIndex(finalY, finalX + 1, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX - 1, ownField)).setStyle(indicateValidPlacement);
                            } else {
                                (getNodeByRowColumnIndex(finalY + 1, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY - 1, finalX, ownField)).setStyle(indicateValidPlacement);
                            }
                        }


                        if (event.getGestureSource() == fourShip) {
                            if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 1, finalY), new Point(finalX + 2, finalY))) ||
                                    (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 1), new Point(finalX, finalY + 2)))) {
                                // label does only accept ship if placement is valid -> when not valid, ship is not placeable, shipLabel gets dropped back to start position
                                event.acceptTransferModes(TransferMode.ANY);
                                indicateValidPlacement = green;
                            } else
                                indicateValidPlacement = red;
                            label.setStyle(indicateValidPlacement);
                            if (horizontal) {
                                (getNodeByRowColumnIndex(finalY, finalX + 1, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX - 1, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX + 2, ownField)).setStyle(indicateValidPlacement);
                            } else {
                                (getNodeByRowColumnIndex(finalY + 1, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY - 1, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY + 2, finalX, ownField)).setStyle(indicateValidPlacement);
                            }
                        }


                        if (event.getGestureSource() == fiveShip) {
                            if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 2, finalY), new Point(finalX + 2, finalY))) ||
                                    (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 2), new Point(finalX, finalY + 2)))) {
                                // label does only accept ship if placement is valid -> when not valid, ship is not placeable, shiplabel gets dropped back to start position
                                event.acceptTransferModes(TransferMode.ANY);
                                indicateValidPlacement = green;
                            } else
                                indicateValidPlacement = red;
                            label.setStyle(indicateValidPlacement);
                            if (horizontal) {
                                (getNodeByRowColumnIndex(finalY, finalX + 1, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX - 1, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX + 2, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY, finalX - 2, ownField)).setStyle(indicateValidPlacement);
                            } else {
                                (getNodeByRowColumnIndex(finalY + 1, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY - 1, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY + 2, finalX, ownField)).setStyle(indicateValidPlacement);
                                (getNodeByRowColumnIndex(finalY - 2, finalX, ownField)).setStyle(indicateValidPlacement);
                            }
                        }
                    } catch (Exception ignored) {
                        // ignore Exceptions when placing wrong -> console should not be full of them
                    }

                    // consume -> event should not be dispatched to any further event listeners
                    event.consume();
                });


                //TODO DIE LÖSUNG FÜR ALL UNSERE PROBLEME
                // Group a = new Group();
                // a.getChildren().add(Hintergrundelemente (Labels)); <- Gesammtes Gridpane
                // a.getChildren().add(Vordergrundelemente (Schiffe));  <- Schifflabels hinzufügen

                // Anzeige eigenes Spielfeld im Spiel
                // label -> ArrayList -> Game Szene -> Group by -> eigenes Spielfeld hinzufügen

                // Anzeige gegnerisches Spielfeld im Spiel
                // -> siehe Szene Game




                //Gridpane
                //________________________
                //|      |       |       |
                //|-----------------------
                //|      |       |       |
                //-------------------------

                //1
                //Group Anzeige = new Group();
                //Anzeige.add(Gridpane)

                //2
                // -> On Drag Exited Event
                //Label label = new Label()
                //label.setGraphics(getClass.getRessource("...."));

                //3
                //Anzeige.add(localX:    ,localY:      ,Schiffslabel)

                // X und Y Position -> Postion von Label der Position oben links
                // localX = Label(x,y) .getLocalBoundX
                // localY = Label(x,y) .getLocalBoundY

                //Event für Label adden: (Hilfsmethode)
                //On Drag Exited Event -> { Anstatt von isPlacementValid <-> moveShip Rest: Copy Paste von bisherigem }



                // remove visual feedback when moving on
                label.setOnDragExited(event -> {
                    ObservableList<Node> children = ownField.getChildren();
                    for (Node labelx : children) {
                        labelx.setStyle("-fx-background-color: lightblue");
                    }
                });


                // if ship is dropped (can only be dropped if label is a valid location) -> place ship in back end
                // increment the ship counter for the added type of ship
                label.setOnDragDropped(event -> {
                    //TODO
                    //2
                    Label shiplabel = new Label();

                    Bounds bounds = label.getBoundsInParent();
                    localX = bounds.getMinX();
                    localY = bounds.getMinY();


                    System.out.println("Dropped successfully");
                    if (event.getGestureSource() == twoShip) {
                        if (horizontal) {

                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png")));
                            shiplabel.setGraphic(image);
                            shiplabel.setLayoutX(label.getLayoutX());
                            shiplabel.setLayoutY(label.getLayoutY());

                            groupID.getChildren().add( shiplabel);


                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX + 1, finalY));
                        }
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY + 1));
                        ActiveGameState.setAmountShipSize2placed(ActiveGameState.getAmountShipSize2placed() + 1);
                        amountShipSize2placed++; //todo decide which amountshipsize5placed... same for 2,3,4

                        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed); //todo wtf still does not work
                        twoOf.textProperty().bind(Bindings.convert(two));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize2() <= ActiveGameState.getAmountShipSize2placed())
                            twoShip.setDisable(true);
                    }

                    if (event.getGestureSource() == threeShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 1, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 1));
                        ActiveGameState.setAmountShipSize3placed(ActiveGameState.getAmountShipSize3placed() + 1);
                        amountShipSize3placed++; //todo decide which amountshipsize5placed... same for 2,3,4

                        SimpleIntegerProperty three= new SimpleIntegerProperty(amountShipSize3placed); //todo wtf still does not work
                        threeOf.textProperty().bind(Bindings.convert(three));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize3() <= ActiveGameState.getAmountShipSize3placed())
                            threeShip.setDisable(true);
                    }

                    if (event.getGestureSource() == fourShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 2, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 2));
                        ActiveGameState.setAmountShipSize4placed(ActiveGameState.getAmountShipSize4placed() + 1);
                        amountShipSize4placed++; //todo decide which amountshipsize5placed... same for 2,3,4

                        SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed); //todo wtf still does not work
                        fourOf.textProperty().bind(Bindings.convert(four));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize4() <= ActiveGameState.getAmountShipSize4placed())
                            fourShip.setDisable(true);
                    }

                    if (event.getGestureSource() == fiveShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 2, finalY), new Point(finalX + 2, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 2), new Point(finalX, finalY + 2));
                        ActiveGameState.setAmountShipSize5placed(ActiveGameState.getAmountShipSize5placed() + 1);
                        amountShipSize5placed++; //todo decide which amountshipsize5placed... same for 2,3,4

                        SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed); //todo wtf still does not work
                        fiveOf.textProperty().bind(Bindings.convert(five));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize5() <= ActiveGameState.getAmountShipSize5placed())
                            fiveShip.setDisable(true);
                    }

                    System.out.println("Ship placed!");
                    // Labels for ship were changed -> update
                    ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
                    ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();

                    //ensure all ships are placed: ready only clickable when all ships are placed in a valid way
                    if (ActiveGameState.getAmountShipSize2() == ActiveGameState.getAmountShipSize2placed()
                            && ActiveGameState.getAmountShipSize3() == ActiveGameState.getAmountShipSize3placed()
                            && ActiveGameState.getAmountShipSize4() == ActiveGameState.getAmountShipSize4placed()
                            && ActiveGameState.getAmountShipSize5() == ActiveGameState.getAmountShipSize5placed())
                        readyButton.setDisable(false);

                    // is always true - valid placement already called in OnDragOver
                    event.setDropCompleted(true);
                    event.consume();
                });

                // the fully created label, with all its methods is finally added to the grid pane
                GridPane.setConstraints(label, x, y);
                ownField.getChildren().addAll(label);
            }
        }


        //TODO
        //1 IN FXML
        //groupID.getChildren().add(ownField);


        // connect Labels to Playground (backend)
        //Object[] ownFieldArray = new Object[gamesize * gamesize];
        ownFieldArray = ownField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        // Sets the text of the labels e.g (3 out of 7) ships of size 2 placed // todo -> aktualisierend machen
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed); //todo wtf still does not work
        twoOf.textProperty().bind(Bindings.convert(two));
        SimpleIntegerProperty three = new SimpleIntegerProperty(amountShipSize3placed);
        threeOf.textProperty().bind(Bindings.convert(three));
        SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed);
        fourOf.textProperty().bind(Bindings.convert(four));
        SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed);
        fiveOf.textProperty().bind(Bindings.convert(five));

        xOfx2Ships.setText("/" + ActiveGameState.getAmountShipSize2() + " platziert)");
        xOfx3Ships.setText("/" + ActiveGameState.getAmountShipSize3() + " platziert)");
        xOfx4Ships.setText("/" + ActiveGameState.getAmountShipSize4() + " platziert)");
        xOfx5Ships.setText("/" + ActiveGameState.getAmountShipSize5() + " platziert)");

        // Drag&Drop Ships -> Placement
        // For all ship labels with the size 2, 3, 4, 5"
        // clip board is not really needed because nothing has to be transferred, but without a drag and drop event is not working: have to transfer String ""
        // -> dragboard needs to have content
        twoShip.setOnDragDetected(event -> {
            Dragboard db = twoShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);
            if (horizontal)
                db.setDragView(new Image("/Gui_View/images/2erSchiff.png"), 10, 0);
            else
                db.setDragView(new Image("/Gui_View/images/2erSchiffVertical.png"), 0, -10);
            event.consume();
        });

        threeShip.setOnDragDetected(event -> {
            Dragboard db = threeShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);
            if (horizontal)
                db.setDragView(new Image("/Gui_View/images/3erSchiff.png"));
            else
                db.setDragView(new Image("/Gui_View/images/3erSchiffVertical.png"));
            event.consume();
        });

        fourShip.setOnDragDetected(event -> {
            Dragboard db = fourShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);
            if (horizontal)
                db.setDragView(new Image("/Gui_View/images/4erSchiff.png"), 10, 0);
            else
                db.setDragView(new Image("/Gui_View/images/4erSchiffVertical.png"), 0, -10);
            event.consume();
        });

        fiveShip.setOnDragDetected(event -> {
            Dragboard db = fiveShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            db.setContent(content);
            if (horizontal)
                db.setDragView(new Image("/Gui_View/images/5erSchiff.png"));
            else
                db.setDragView(new Image("/Gui_View/images/5erSchiffVertical.png"));
            event.consume();
        });

    }//end of initialize


    public void rotateShip90() {
        horizontal = !horizontal;
        if(horizontal)
            rotate90.setStyle(red);
        else
            rotate90.setStyle(green);
    }

    public void newRandomPlacement() {
        // todo really needed - resets all... -> probably yes, old ships should not be there anymore
        // create new OwnPlayground - link same Labels
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();
        //todo -> call random place function
        //todo -> update Gui after that -> thread? .runlater nutzen für refresh
        // set counters of placed ships to max because all are added
        ActiveGameState.setAmountShipSize2placed(ActiveGameState.getAmountShipSize2());
        ActiveGameState.setAmountShipSize3placed(ActiveGameState.getAmountShipSize3());
        ActiveGameState.setAmountShipSize4placed(ActiveGameState.getAmountShipSize4());
        ActiveGameState.setAmountShipSize5placed(ActiveGameState.getAmountShipSize5());

        amountShipSize2placed = ActiveGameState.getAmountShipSize2();
        amountShipSize3placed = ActiveGameState.getAmountShipSize3();;//todo weg or needed???
        amountShipSize4placed = ActiveGameState.getAmountShipSize4();
        amountShipSize5placed = ActiveGameState.getAmountShipSize5();
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed); //todo works but wtf clean this fucking code
        twoOf.textProperty().bind(Bindings.convert(two));
        SimpleIntegerProperty three = new SimpleIntegerProperty(amountShipSize3placed);
        threeOf.textProperty().bind(Bindings.convert(three));
        SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed);
        fourOf.textProperty().bind(Bindings.convert(four));
        SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed);
        fiveOf.textProperty().bind(Bindings.convert(five));

        // disable Dropping for all Labels cause all ships are placed
        twoShip.setDisable(true);
        threeShip.setDisable(true);
        fourShip.setDisable(true);
        fiveShip.setDisable(true);
        System.out.println("Ships placed randomly");

        // enable ready button
        //readyButton.setDisable(false); //todo enable again when random placement is implemented
    }

    public void resetPlacement() {
        // create new OwnPlayground - link same Labels
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();

        // set counters of placed ships to zero because all are removed
        ActiveGameState.setAmountShipSize2placed(0);
        ActiveGameState.setAmountShipSize3placed(0);
        ActiveGameState.setAmountShipSize4placed(0);
        ActiveGameState.setAmountShipSize5placed(0);
        amountShipSize2placed = 0;
        amountShipSize3placed = 0;//todo weg or needed???
        amountShipSize4placed = 0;
        amountShipSize5placed = 0;
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed); //todo wtf still does not work
        twoOf.textProperty().bind(Bindings.convert(two));
        SimpleIntegerProperty three = new SimpleIntegerProperty(amountShipSize3placed);
        threeOf.textProperty().bind(Bindings.convert(three));
        SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed);
        fourOf.textProperty().bind(Bindings.convert(four));
        SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed);
        fiveOf.textProperty().bind(Bindings.convert(five));


        // enable Dropping for all Labels cause all ships are unplaced
        twoShip.setDisable(false);
        threeShip.setDisable(false);
        fourShip.setDisable(false);
        fiveShip.setDisable(false);

        // disable ready button
        readyButton.setDisable(true);
    }


    // readyButton -> starts the game, only clickable when ships are placed in a valid way
    public void startGame() throws IOException {
        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }

    //todo reset label ??? was soll das heißen

    // gives back the node at a a specific column index of a GridPane
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}


/*
todo
Layer 0     Hintergrundbild
Layer 1     Labels der GridPane  ->    In Modell: bei IsValidPlacementAbgeändert(kein Schiff erstellen): Blau (Transparent) GRÜN und ROT wird von Yannick angezeigt: Rot wenn nicht plazierbar, Grün wenn plazierbar -> Nur die unterm Schiff
Layer 2                          ->    Bilder der Labels er GridPane bei EnemyPlayground wenn Schiff angreifen -> Schiffsteile
Layer 3     Labels der Schiffe (Bilder)


Bei Ingame: Modell:     ShipHit -> Bild shipHit.png für Label setzten
                        ShotWater -> Bild waterHit.png für Label setzten
                        Wasser ->  kein Bild - nur der Hintergrund (blau oder transparent) wird angezeigt
 */