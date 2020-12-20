package Controller;

import Gui_View.Main;
import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;
import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
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
import sun.plugin.javascript.navig4.LayerArray;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaceShips implements Initializable {

    // connect everything to FXML
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
    private Button readyButton;
    @FXML
    private Group groupID;


    // define colors
    String green = "-fx-background-color: #a5fda5";
    String red = "-fx-background-color: #ffa1a1";
    String blue = "-fx-background-color: lightblue";
    String indicateValidPlacement = red;


    // at beginning, ship placement is set to horizontal
    boolean horizontal = true;


    // counter: how much ships of each type are placed -> is displayed on screen, updating when ship is placed
    int amountShipSize2placed = 0;
    int amountShipSize3placed = 0;
    int amountShipSize4placed = 0;
    int amountShipSize5placed = 0;


    // the size of the playground is depending on the field size the player (in mp only the host) has configured
    int gamesize = ActiveGameState.getPlaygroundSize();


    // ownFieldArray and localX localY have to be declared here -> must exist when they are called in Drag&Drop functions oft labels
    Object[] ownFieldArray = new Object[gamesize * gamesize];
    double localX;
    double localY;
//todo solution
    Label twoShiplabel;
    Label threeShiplabel;
    Label fourShiplabel;
    Label fiveShiplabel;


    /** initialize-method:
     * -----------------------------------------------------------------------------------------------------------------
     *  -> sets all basic stuff: Start-Button disabled, start-Image of the Rotate-Button, save all necessary in ActiveGameState...
     *
     *  -> creates labels that fill the GridPane: this labels recognize Drag&Drop -> Ships can be dropped into them
     *                                                                            -> backend-stuff executed
     *
     *  -> make ShipLabels at the right side of the Playground draggable -> can be placed on Playground
     *
     *  -> when ship is Placed: a Label is placed on top of the Grid-Pane to indicate a Placed-Ship:
     *     this Label is just like the ShipLabel at the right side, it can be dragged -> so already placed ships can be moved
     *----------------------------------------------------------------------------------------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // basic initialization tasks ----------------------------------------------------------------------------------
        ActiveGameState.setSceneIsPlaceShips(true);

        // create new playgrounds, save them in ActiveGameState
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.setOwnPlayerIEnemyPlayground(new EnemyPlayground());
        if (!ActiveGameState.isMultiplayer()) {
            ActiveGameState.setEnemyPlayerOwnPlayground(new OwnPlayground());
            ActiveGameState.setEnemyPlayerEnemyPlayground(new EnemyPlayground());
        }

        // build these playgrounds - playgrounds can now be displayed on screen
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIEnemyPlayground().buildPlayground();
        if (!ActiveGameState.isMultiplayer()) {
            ActiveGameState.getEnemyPlayerOwnPlayground().buildPlayground();
            ActiveGameState.getEnemyPlayerEnemyPlayground().buildPlayground();
        }


        //Sets the Image for the rotate 90 Degree button - horizontal at beginning
        rotate90.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/rotateShipButtonHorizontal.png"))));
        //disables the readyButton -> game can only be started if all ships are placed
        readyButton.setDisable(true);
        //--------------------------------------------------------------------------------------------------------------



        // TODO BIG: make resizeable window looking nice
        System.out.println("window size: " + Main.primaryStage.getHeight()); //todo use or delete - debug-line
        //todo public scale - for fields -> pref size -> better way 2:3 for programm fix -> use SceneSizeChangeListener to scale properly all the time - no strange things happening
        //The scale of one Field,   Ship size 2 -> Image: | 30px | 30px |
        //                          Ship size 3 -> Image: | 30px | 30px | 30px |
        int scale = 30; //todo: scale should be dependent on window size, no fix value like now



        // for ShipLabels at the right side of the Playground + the (x/x placed)-Labels --------------------------------
        // Sets the prefSize depending on the size of the ship + loads the Image of each ship
        twoShip.setPrefSize(2 * scale, scale);
        twoShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png"))));

        threeShip.setPrefSize(3 * scale, scale);
        threeShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png"))));

        fourShip.setPrefSize(4 * scale, scale);
        fourShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png"))));

        fiveShip.setPrefSize(5 * scale, scale);
        fiveShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png"))));


        // Labels e.g. (0 out of 4 Placed) -> set Height Matching to scale -> ship labels depend on scale
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
        //--------------------------------------------------------------------------------------------------------------



        // playground field --------------------------------------------------------------------------------------------
        // Label right above the GridPane (= the field) will show the Name of the Player
        ownFieldLabel.setText(ActiveGameState.getOwnPlayerName() + "'s Spielfeld");


        // the gridPane has gaps - little Lines between all the fields
        ownField.setHgap(1);
        ownField.setVgap(1);


        // The field (= the GridPane) is filled with Labels. This labels recognize Drag&Drop -> Ships can be dropped into them
        // when ship is dragged over a label, it indicates if the placement would be valid, if so, it can be dropped -> backend stuff is handled when dropped successfully
        // all Labels of the GridPane are connected to the gamePlayground via the method setLabels()

        // nested for loop -> reaches all fields of the playground
        // -> that means: the following code is done for every label on the playground
        for (int x = 0; x < gamesize; x++) {
            for (int y = 0; y < gamesize; y++) {


                //Creating a new label
                Label label = new Label();
                label.setStyle(blue); // at the moment, water is displayed blue (color can be changed here)
                label.setMinSize(5, 5);
                label.setPrefSize(scale, scale);
                label.setMaxSize(scale, scale);


                // finalX is the x position of the current label
                // finalY is the y position of the current label
                int finalX = x;
                int finalY = y;


                //-start of setOnDragOver + giving visual feedback------------------------------------------------------
                // Every label gets an OnDragOver-Listener, listening if a ship is hovered over it
                // if the placement is valid, this will be displayed by green fields, if not by red
                // ships can only be dropped, if the placement is valid (green) - dropping will be handled by setOnDragDropped
                label.setOnDragOver(event -> {

                    // method is placed in an try-catch block with Exception ignored, because hovering over labels at the borders of the playground
                    // will produce many (nonfatal) Errors - but they should not be displayed in console all the time
                    try {

                        // event.getGestureSource() is getting the Element, which is hovered over the label (here the placeable ship label)

                        // for every placeable ship label (size2, size3, size4, size5)-> own reaction, code really similar
                        // here (twoShip) everything commented, for 3,4,5-ship: similar, look here
                        if (event.getGestureSource() == twoShip || event.getGestureSource() == twoShiplabel) { //todo
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


                        // for comments -> look at twoShip
                        if (event.getGestureSource() == threeShip || event.getGestureSource() == threeShiplabel) { //todo
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


                        // for comments -> look at twoShip
                        if (event.getGestureSource() == fourShip || event.getGestureSource() == fourShiplabel) { //todo
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


                        // for comments -> look at twoShip
                        if (event.getGestureSource() == fiveShip || event.getGestureSource() == fiveShiplabel) { //todo
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


                // remove visual feedback (green/red) when moving on with the dragged ship (label is blue again)
                label.setOnDragExited(event -> {
                    ObservableList<Node> children = ownField.getChildren();
                    for (Node labelx : children) {
                        labelx.setStyle("-fx-background-color: lightblue");
                    }
                });
                //-end of setOnDragOver + giving visual feedback--------------------------------------------------------



                /*TODO DIE LÖSUNG FÜR ALL UNSERE PROBLEME
                // Group a = new Group();
                // a.getChildren().add(Hintergrundelemente (Labels)); <- Gesammtes Gridpane
                // a.getChildren().add(Vordergrundelemente (Schiffe));  <- Schifflabels hinzufügen

                // Anzeige eigenes Spielfeld im Spiel
                // label -> ArrayList -> Game Szene -> Group by -> eigenes Spielfeld hinzufügen

                // Anzeige gegnerisches Spielfeld im Spiel
                // -> siehe Szene Game


                //Gridpane
                // ________________________
                // |      |       |       |
                // |-----------------------
                // |      |       |       |
                // ------------------------

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
                //On Drag Exited Event -> { Anstatt von isPlacementValid <-> moveShip Rest: Copy Paste von bisherigem }*/



                //-start of setOnDragDropped----------------------------------------------------------------------------
                // if ship is dropped (can only be dropped if label is a valid location) -> place ship in back end
                // increment the ship counter for the added type of ship

                // GUI: on desired the location, a ship label is added (very similar to the labels on the right hand side
                // of the playground) - this label can be dragged/dropped again to make it possible to change the location
                // of an already placed ship

                // how it works: GridPane(Playground) and the ship labels are organized in a Group
                // GridPane is the bottom layer, ship labels are placed above it

                label.setOnDragDropped(event -> {
                    System.out.println("Dropped successfully");


                    // the bounds are set: minX and minY position = where the upper left corner of the label should be on screen //todo for bigger ship size + how the fuck to resize???
                  //  Bounds bounds = label.getBoundsInParent();
                   // localX = bounds.getMinX(); //todo with simon - is this needed? no code occurrences found
                    //localY = bounds.getMinY();


                    // depending on the type of ship, different code is executed when ship is dropped successfully- very similar, only commented in twoShip
                    if (event.getGestureSource() == twoShip || event.getGestureSource() == twoShiplabel) {


                        if(event.getGestureSource() == twoShip) {

                            // when ship is dropped into the playground, a new label is created for this ship
                            twoShiplabel = new Label();

                            // making this label draggable too //todo
                            twoShiplabel.setOnDragDetected(e -> {
                                handlerSetOnDragDetected(twoShiplabel, 2, true);
                                e.consume();
                            });


                            if (horizontal) {

                                // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png")));
                                twoShiplabel.setGraphic(image);

                                twoShiplabel.setLayoutX(label.getLayoutX());
                                twoShiplabel.setLayoutY(label.getLayoutY());
                                groupID.getChildren().add(twoShiplabel);

                                // this will place the ship in back-end representation of playground
                                ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX + 1, finalY));
                            } else {

                                // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiffVertical.png")));
                                twoShiplabel.setGraphic(image);

                                twoShiplabel.setLayoutX(label.getLayoutX());
                                twoShiplabel.setLayoutY(label.getLayoutY());
                                groupID.getChildren().add(twoShiplabel);

                                // this will place the ship in back-end representation of playground
                                ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY + 1));
                            }
                        }

                        else { //todo yeah it is working like i thought :))))
                            if(horizontal) {

                                // take the existing label of the ship and change it's position
                                twoShiplabel.setLayoutX(label.getLayoutX());
                                twoShiplabel.setLayoutY(label.getLayoutY());

                                // this will place the ship in back-end representation of playground //todo move ship
                                ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX + 1, finalY));
                            }

                            else {

                                // take the existing label of the ship and change it's position
                                twoShiplabel.setLayoutX(label.getLayoutX());
                                twoShiplabel.setLayoutY(label.getLayoutY());

                                // this will place the ship in back-end representation of playground //todo move ship
                                ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX + 1, finalY));
                            }
                        }


                        // incrementing the counter for the number of ShipSize2s are placed //todo only when placing new ship
                        amountShipSize2placed++;
                        // displaying the new number of the counter on screen
                        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed);
                        twoOf.textProperty().bind(Bindings.convert(two));


                        // only make that much ships placeable, that are allowed by gameConfig
                        // when all ships of the type are placed, button for new ship of this size gets disabled
                        if (ActiveGameState.getAmountShipSize2() <= amountShipSize2placed)
                            twoShip.setDisable(true);







                    }



                    if (event.getGestureSource() == threeShip || event.getGestureSource() == threeShiplabel) {

                        // when ship is dropped into the playground, a new label is created for this ship
                        threeShiplabel = new Label();

                        // making this label draggable too //todo
                        threeShiplabel.setOnDragDetected(e -> {handlerSetOnDragDetected(threeShiplabel, 3, true); e.consume();});

                        if (horizontal) {

                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png")));
                            threeShiplabel.setGraphic(image);

                            threeShiplabel.setLayoutX(label.getLayoutX() - scale);
                            threeShiplabel.setLayoutY(label.getLayoutY());
                            groupID.getChildren().add(threeShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 1, finalY));
                        }

                        else {
                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiffVertical.png")));
                            threeShiplabel.setGraphic(image);

                            threeShiplabel.setLayoutX(label.getLayoutX());
                            threeShiplabel.setLayoutY(label.getLayoutY() - scale);
                            groupID.getChildren().add(threeShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 1));
                        }

                        amountShipSize3placed++;

                        SimpleIntegerProperty three = new SimpleIntegerProperty(amountShipSize3placed);
                        threeOf.textProperty().bind(Bindings.convert(three));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize3() <= amountShipSize3placed)
                            threeShip.setDisable(true);
                    }

                    if (event.getGestureSource() == fourShip || event.getGestureSource() == fourShiplabel) {

                        // when ship is dropped into the playground, a new label is created for this ship
                        fourShiplabel = new Label();

                        // making this label draggable too //todo
                        fourShiplabel.setOnDragDetected(e -> {handlerSetOnDragDetected(fourShiplabel, 4, true); e.consume();});

                        if (horizontal) {
                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png")));
                            fourShiplabel.setGraphic(image);

                            fourShiplabel.setLayoutX(label.getLayoutX() - scale);
                            fourShiplabel.setLayoutY(label.getLayoutY());
                            groupID.getChildren().add(fourShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 2, finalY));
                        }

                        else {
                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiffVertical.png")));
                            fourShiplabel.setGraphic(image);

                            fourShiplabel.setLayoutX(label.getLayoutX());
                            fourShiplabel.setLayoutY(label.getLayoutY() - scale);
                            groupID.getChildren().add(fourShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 2));
                        }

                        amountShipSize4placed++;

                        SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed);
                        fourOf.textProperty().bind(Bindings.convert(four));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize4() <= amountShipSize4placed)
                            fourShip.setDisable(true);
                    }

                    if (event.getGestureSource() == fiveShip || event.getGestureSource() == fiveShiplabel) {

                        // when ship is dropped into the playground, a new label is created for this ship
                        fiveShiplabel = new Label();

                        // making this label draggable too //todo
                        fiveShiplabel.setOnDragDetected(e -> {handlerSetOnDragDetected(fiveShiplabel, 5, true); e.consume();});

                        if (horizontal) {
                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png")));
                            fiveShiplabel.setGraphic(image);

                            fiveShiplabel.setLayoutX(label.getLayoutX() - 2*scale);
                            fiveShiplabel.setLayoutY(label.getLayoutY());
                            groupID.getChildren().add(fiveShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 2, finalY), new Point(finalX + 2, finalY));
                        }

                        else {
                            // adding the right image to the ship label //todo cinematic graphics lol, todo making resizeable
                            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiffVertical.png")));
                            fiveShiplabel.setGraphic(image);

                            fiveShiplabel.setLayoutX(label.getLayoutX());
                            fiveShiplabel.setLayoutY(label.getLayoutY() - 2*scale);
                            groupID.getChildren().add(fiveShiplabel);

                            // this will place the ship in back-end representation of playground
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 2), new Point(finalX, finalY + 2));
                        }

                        amountShipSize5placed++;

                        SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed);
                        fiveOf.textProperty().bind(Bindings.convert(five));

                        // only make that much ships placeable, that are allowed by gameConfig
                        if (ActiveGameState.getAmountShipSize5() <= amountShipSize5placed)
                            fiveShip.setDisable(true);
                    }


                    System.out.println("Ship placed!");
                    // Labels for ship were changed -> update
                    ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
                    ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


                    //ensure all ships are placed: ready only clickable when all ships are placed in a valid way
                    if (ActiveGameState.getAmountShipSize2() == amountShipSize2placed
                            && ActiveGameState.getAmountShipSize3() == amountShipSize3placed
                            && ActiveGameState.getAmountShipSize4() == amountShipSize4placed
                            && ActiveGameState.getAmountShipSize5() == amountShipSize5placed)
                        readyButton.setDisable(false);
                    //todo not really needed when all is working - but now since there can be more ships placed than allowed,
                    // this is important
                    else
                        readyButton.setDisable(true);


                    // is always true - valid placement already called in OnDragOver
                    event.setDropCompleted(true);
                    event.consume();
                });
                //-end of setOnDragDropped------------------------------------------------------------------------------


                // the fully created label, with all its methods is finally added to the grid pane
                GridPane.setConstraints(label, x, y);
                ownField.getChildren().addAll(label);
            }
        }
        // end of creating playground field ----------------------------------------------------------------------------



        // connecting Labels to backend representation of the playground and draw this - now labels are displayed on screen
        // as water and will be updated when a ship is placed/shot
        ownFieldArray = ownField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        // setting the text of the labels e.g (3 out of 7) ships of size 2 placed
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed);
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



        // making the ship labels at the right hand side of the playground draggable -----------------------------------
        twoShip.setOnDragDetected(event -> {
            handlerSetOnDragDetected(twoShip, 2, false);
            event.consume();
        });

        threeShip.setOnDragDetected(event -> {
            handlerSetOnDragDetected(threeShip, 3, false);
            event.consume();
        });

        fourShip.setOnDragDetected(event -> {
            handlerSetOnDragDetected(fourShip, 4, false);
            event.consume();
        });

        fiveShip.setOnDragDetected(event -> {
            handlerSetOnDragDetected(fiveShip, 5, false);
            event.consume();
        });
        // end of making the labels on the right hand side of the playground draggable ---------------------------------
    }
    // end of initialize-method ----------------------------------------------------------------------------------------



    /** rotate ship button:
     *------------------------------------------------------------------------------------------------------------------
     * -> changes graphic: the arrow point of the arrows change between being horizontal and vertical
     * -> boolean horizontal is inversed - used by ship placement methods
     *----------------------------------------------------------------------------------------------------------------*/
    public void rotateShip90() {
        horizontal = !horizontal;
        if (horizontal)
            rotate90.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/rotateShipButtonHorizontal.png"))));
        else
            rotate90.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/rotateShipButtonVertical.png"))));
    }



    /** random placement button:
     *------------------------------------------------------------------------------------------------------------------
     * -> creates a new playground - eventually placed ships are deleted
     * -> places all ships randomly - KI does that
     * -> enables the ready button due to all ships being placed
     *----------------------------------------------------------------------------------------------------------------*/
    public void newRandomPlacement() {

        // create new OwnPlayground - link the same Labels -> playground is empty again, old placement is deleted //todo delete ship labels above!!
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        //todo -> call random place function
        //todo -> update Gui after that -> thread? .runlater nutzen für refresh


        // set counters of placed ships to max because all are added
        amountShipSize2placed = ActiveGameState.getAmountShipSize2();
        amountShipSize3placed = ActiveGameState.getAmountShipSize3();
        amountShipSize4placed = ActiveGameState.getAmountShipSize4();
        amountShipSize5placed = ActiveGameState.getAmountShipSize5();


        // display on screen that all ships of each types are added to the playground now
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed);
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


        // enable ready button due to all ships being placed, game can be started now
        readyButton.setDisable(false);
    }



    /** reset placement button:
     *------------------------------------------------------------------------------------------------------------------
     * -> creates a new playground - eventually placed ships are deleted
     * -> places all ships randomly - KI does that
     * -> enables the ready button due to all ships being placed
     *----------------------------------------------------------------------------------------------------------------*/
    public void resetPlacement() { //todo delete ship labels group

        // create new OwnPlayground - link the same Labels -> playground is empty again, old placement is deleted //todo delete ship labels above!!
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        // set counters of placed ships to zero because all are removed
        amountShipSize2placed = 0;
        amountShipSize3placed = 0;
        amountShipSize4placed = 0;
        amountShipSize5placed = 0;


        // display on screen that no ships of each types are added to the playground now
        SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed);
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


        // disable ready button due to no ships being placed, game can not be started now
        readyButton.setDisable(true);
    }



    /** ready button:
     *------------------------------------------------------------------------------------------------------------------
     * -> starts the game ( = switches scene to gamePlayground), only clickable when all ships are placed in a valid way
     *----------------------------------------------------------------------------------------------------------------*/
    public void startGame() throws IOException {
        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }



    /** getNodeByRowColumIndex - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> gives back the node at a a specific column index of a GridPane
     * -> used in initialize method to indicate valid placement
     *----------------------------------------------------------------------------------------------------------------*/
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



    /** makeLabelDraggable - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> makes a label draggable - for ShipSize 2,3,4,5
     * -> difference between placing new ship and moving existing ship:
     *      -   new ship means the dragged label is a label at the right hand side of the playground
     *          -> this label will still be displayed
     *      -   existing ship means the dragged label is label in the playground
     *          -> this label has to disappear once it is moved
     *----------------------------------------------------------------------------------------------------------------*/
    public void handlerSetOnDragDetected(Label shipLabel, int shipSize, boolean alreadyPlaced) {

        // if ship is already existing, label must be deleted when moving - or not shown //todo: important: label should move to new direction would be easy but old has to disapper really



        // clip board is not really needed because nothing has to be transferred, but without a drag and drop event is not working: have to transfer String ""
        // -> Dragboard needs to have content
        Dragboard db = shipLabel.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent(); //todo make use of transfer mode copy and move
        content.putString("");
        db.setContent(content);


        // depending on the ship size a drag view is set, displayed when dragging the element
        // this view is horizontal or vertical, depending on the current setting made by the rotate90-Button
        String horizontalURL = "/Gui_View/images/" + shipSize + "erSchiff.png";
        String verticalURL = "/Gui_View/images/" + shipSize + "erSchiffVertical.png";


        // setting the drag view with little offset
        // -> ship should not be in middle of mouse pointer because that would make placing an awkward experience
        //    due to be always in the middle of two labels when placing the ship to fit right into the label
        if (horizontal)
            db.setDragView(new Image(horizontalURL), 10, 0);
        else
            db.setDragView(new Image(verticalURL), 0, -10);
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