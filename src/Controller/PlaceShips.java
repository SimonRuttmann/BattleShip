package Controller;

import Gui_View.Main;
import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShipLabel;
import GameData.ActiveGameState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlaceShips implements Initializable {

    public static final Logger logGameOptions = Logger.getLogger("parent.GameOptions");

    /** FXML Elements **/
    public AnchorPane anchorPane;
    public Text ownFieldText;
    public GridPane ownField;
    public Label twoShip;
    public Label threeShip;
    public Label fourShip;
    public Label fiveShip;
    public Text bracket2;
    public Text bracket3;
    public Text bracket4;
    public Text bracket5;
    public Text twoOf;
    public Text threeOf;
    public Text fourOf;
    public Text fiveOf;
    public Text xOfx2Ships;
    public Text xOfx3Ships;
    public Text xOfx4Ships;
    public Text xOfx5Ships;
    public Button rotate90;
    public Button randomPlacement;
    public Button resetPlacement;
    public Button readyButton;
    public Group groupID;


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
    int gameSize = ActiveGameState.getPlaygroundSize();


    // ownFieldArray and localX localY have to be declared here -> must exist when they are called in Drag&Drop functions oft labels
    Object[] ownFieldArray = new Object[gameSize * gameSize];
    ShipLabel twoShipLabel;
    ShipLabel threeShipLabel;
    ShipLabel fourShipLabel;
    ShipLabel fiveShipLabel;



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
        ActiveGameState.setSceneIsGamePlayground(false);
        setLanguage();
        setBackground();


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


        //The scale of one Field,   Ship size 2 -> Image: | 30px | 30px |
        //                          Ship size 3 -> Image: | 30px | 30px | 30px |
        // scale is depended on playground size
        int scale = ActiveGameState.getPlaygroundScale();


        // for ShipLabels at the right side of the Playground + the (x/x placed)-Labels --------------------------------
        // Sets the prefSize depending on the size of the ship + loads the Image of each ship

        twoShip.setPrefSize(2 * scale, scale);
        ImageView image2 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png")));
        image2.fitWidthProperty().bind(new SimpleIntegerProperty(2 * scale).asObject());
        image2.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        twoShip.setGraphic(image2);

        threeShip.setPrefSize(3 * scale, scale);
        ImageView image3 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png")));
        image3.fitWidthProperty().bind(new SimpleIntegerProperty(3 * scale).asObject());
        image3.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        threeShip.setGraphic(image3);

        fourShip.setPrefSize(4 * scale, scale);
        ImageView image4 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png")));
        image4.fitWidthProperty().bind(new SimpleIntegerProperty(4 * scale).asObject());
        image4.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        fourShip.setGraphic(image4);

        fiveShip.setPrefSize(5 * scale, scale);
        ImageView image5 = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png")));
        image5.fitWidthProperty().bind(new SimpleIntegerProperty(5 * scale).asObject());
        image5.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
        fiveShip.setGraphic(image5);


        // View Ships nicely -> difference to dark Background
        twoShip.setEffect(new DropShadow(5, Color.WHITE));
        threeShip.setEffect(new DropShadow(5, Color.WHITE));
        fourShip.setEffect(new DropShadow(5, Color.WHITE));
        fiveShip.setEffect(new DropShadow(5, Color.WHITE));


        // Labels e.g. (0 out of 4 Placed) -> set Height Matching to scale -> ship labels depend on scale
        bracket2.prefWidth(scale);
        bracket3.prefWidth(scale);
        bracket4.prefWidth(scale);
        bracket5.prefWidth(scale);
        twoOf.prefWidth(scale);
        threeOf.prefWidth(scale);
        fourOf.prefWidth(scale);
        fiveOf.prefWidth(scale);
        xOfx2Ships.prefWidth(scale);
        xOfx3Ships.prefWidth(scale);
        xOfx4Ships.prefWidth(scale);
        xOfx5Ships.prefWidth(scale);


        bracket2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        bracket2.setFill(Color.WHITE);
        bracket2.setEffect(new DropShadow(3, Color.BLACK));

        bracket3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        bracket3.setFill(Color.WHITE);
        bracket3.setEffect(new DropShadow(3, Color.BLACK));

        bracket4.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        bracket4.setFill(Color.WHITE);
        bracket4.setEffect(new DropShadow(3, Color.BLACK));

        bracket5.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        bracket5.setFill(Color.WHITE);
        bracket5.setEffect(new DropShadow(3, Color.BLACK));

        twoOf.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        twoOf.setFill(Color.WHITE);
        twoOf.setEffect(new DropShadow(3, Color.BLACK));

        threeOf.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        threeOf.setFill(Color.WHITE);
        threeOf.setEffect(new DropShadow(3, Color.BLACK));

        fourOf.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        fourOf.setFill(Color.WHITE);
        fourOf.setEffect(new DropShadow(3, Color.BLACK));

        fiveOf.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        fiveOf.setFill(Color.WHITE);
        fiveOf.setEffect(new DropShadow(3, Color.BLACK));

        xOfx2Ships.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        xOfx2Ships.setFill(Color.WHITE);
        xOfx2Ships.setEffect(new DropShadow(3, Color.BLACK));

        xOfx3Ships.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        xOfx3Ships.setFill(Color.WHITE);
        xOfx3Ships.setEffect(new DropShadow(3, Color.BLACK));

        xOfx4Ships.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        xOfx4Ships.setFill(Color.WHITE);
        xOfx4Ships.setEffect(new DropShadow(3, Color.BLACK));

        xOfx5Ships.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        xOfx5Ships.setFill(Color.WHITE);
        xOfx5Ships.setEffect(new DropShadow(3, Color.BLACK));
        //--------------------------------------------------------------------------------------------------------------



        // playground field --------------------------------------------------------------------------------------------
        // set Labels for the Playground
        ownFieldText.setText((ActiveGameState.getLanguage() == ActiveGameState.Language.english) ? "Own Playground" : "Eigenes Spielfeld");
        ownFieldText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ownFieldText.setFill(Color.WHITE);
        ownFieldText.setEffect(new DropShadow(10, Color.BLACK));


        // the gridPane has gaps - little Lines between all the fields
        ownField.setHgap(1);
        ownField.setVgap(1);

        // the constraints have to be cleared, otherwise bugs with big playgrounds
        ownField.getColumnConstraints().clear();
        ownField.getRowConstraints().clear();


        // The field (= the GridPane) is filled with Labels. This labels recognize Drag&Drop -> Ships can be dropped into them
        // when ship is dragged over a label, it indicates if the placement would be valid, if so, it can be dropped -> backend stuff is handled when dropped successfully
        // all Labels of the GridPane are connected to the gamePlayground via the method setLabels()


        // nested for loop -> reaches all fields of the playground
        // -> that means: the following code is done for every label on the playground
        for (int x = 0; x < gameSize; x++) {
            for (int y = 0; y < gameSize; y++) {


                //Creating a new label
                Label label = new Label();
                label.setStyle(blue); // at the moment, water is displayed blue (color can be changed here)
                label.setMinSize(scale, scale);
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

                        // the Dragboard is used to indicate which ship type is hovered -> String indicates Ship Size, 2N: size 2, new ship, 2P: size 2, already placed ship
                        if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("2N") || event.getDragboard().getString().equals("2P"))) {
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
                        if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("3N") || event.getDragboard().getString().equals("3P"))) {
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
                        if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("4N") || event.getDragboard().getString().equals("4P"))) {
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
                        if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("5N") || event.getDragboard().getString().equals("5P"))) {
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



                //-start of setOnDragDropped----------------------------------------------------------------------------
                // if new ship is dropped (can only be dropped if label is a valid location) -> place ship in back end
                // increment the ship counter for the added type of ship

                // if a already placed ship is moved -> move ship in back end, do not increment the ship counter

                // GUI: on desired the location, a ship label is added (very similar to the labels on the right hand side
                // of the playground) - this label can be dragged/dropped again to make it possible to change the location
                // of an already placed ship

                // how it works: GridPane(Playground) and the ship labels are organized in a Group
                // GridPane is the bottom layer, ship labels are placed above it
                // Ship labels are added when a ship is placed for the first time, when it is moved the labels will change
                // their position. While changing the position, the labels are invisible - you can still see the ship
                // using the dragView (which contains the same image)

                label.setOnDragDropped(event -> {

                    // Beginning Ship Size two -------------------------------------------------------------------------
                    // code is similar for other ship sizes -> only commented here

                    // the Dragboard is used to indicate which ship type is hovered -> String indicates Ship Size, 2N: size 2, new ship, 2P: size 2, already placed ship
                    if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("2N") || event.getDragboard().getString().equals("2P"))) {

                        // first time placing ship: create a new ship label
                        if(event.getDragboard().getString().equals("2N")) {

                            // when ship is dropped into the playground, a new label is created for this ship
                            twoShipLabel = new ShipLabel(new Point(finalX,finalY),horizontal,2);

                            // help Method placeNewShip is called -> places Ship in Gui and backend
                            placeNewShip(twoShipLabel);


                            // incrementing the counter for the number of ShipSize2s are placed
                            amountShipSize2placed++;
                            // displaying the new number of the counter on screen
                            SimpleIntegerProperty two = new SimpleIntegerProperty(amountShipSize2placed);
                            twoOf.textProperty().bind(Bindings.convert(two));


                            // only make that much ships placeable, that are allowed by gameConfig
                            // when all ships of the type are placed, button for new ship of this size gets disabled
                            if (ActiveGameState.getAmountShipSize2() <= amountShipSize2placed)
                                twoShip.setDisable(true);
                        }

                        // moving already placed ship to a new location: move the ship label
                        else {
                            moveShip((ShipLabel) event.getGestureSource(), finalX, finalY);
                        }
                    }
                    // End Ship Size two -------------------------------------------------------------------------------


                    // Beginning Ship Size three -----------------------------------------------------------------------
                    // very similar for each ship size, only commented in twoShip
                    if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("3N") || event.getDragboard().getString().equals("3P"))) {

                        if(event.getDragboard().getString().equals("3N")) {
                            threeShipLabel = new ShipLabel((horizontal) ? new Point(finalX-1, finalY) : new Point(finalX, finalY-1), horizontal, 3);
                            placeNewShip(threeShipLabel);

                            amountShipSize3placed++;
                            SimpleIntegerProperty three = new SimpleIntegerProperty(amountShipSize3placed);
                            threeOf.textProperty().bind(Bindings.convert(three));

                            if (ActiveGameState.getAmountShipSize3() <= amountShipSize3placed)
                                threeShip.setDisable(true);
                        }
                        else {
                            moveShip((ShipLabel) event.getGestureSource(), finalX, finalY);
                        }
                    }
                    // End Ship Size three -----------------------------------------------------------------------------


                    // Beginning Ship Size four ------------------------------------------------------------------------
                    // very similar for each ship size, only commented in twoShip
                    if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("4N") || event.getDragboard().getString().equals("4P"))) {

                        if(event.getDragboard().getString().equals("4N")) {
                            fourShipLabel = new ShipLabel((horizontal) ? new Point(finalX-1, finalY) : new Point(finalX, finalY-1), horizontal, 4);
                            placeNewShip(fourShipLabel);

                            amountShipSize4placed++;
                            SimpleIntegerProperty four = new SimpleIntegerProperty(amountShipSize4placed);
                            fourOf.textProperty().bind(Bindings.convert(four));

                            if (ActiveGameState.getAmountShipSize4() <= amountShipSize4placed)
                                fourShip.setDisable(true);
                        }
                        else {
                            moveShip((ShipLabel) event.getGestureSource(), finalX, finalY);
                        }
                    }
                    // End Ship Size four ------------------------------------------------------------------------------


                    // Beginning Ship Size five ------------------------------------------------------------------------
                    // very similar for each ship size, only commented in twoShip
                    if (event.getDragboard().hasString() && (event.getDragboard().getString().equals("5N") || event.getDragboard().getString().equals("5P"))) {

                        if(event.getDragboard().getString().equals("5N")) {
                            fiveShipLabel = new ShipLabel((horizontal) ? new Point(finalX-2, finalY) : new Point(finalX, finalY-2), horizontal, 5);
                            placeNewShip(fiveShipLabel);

                            amountShipSize5placed++;
                            SimpleIntegerProperty five = new SimpleIntegerProperty(amountShipSize5placed);
                            fiveOf.textProperty().bind(Bindings.convert(five));

                            if (ActiveGameState.getAmountShipSize5() <= amountShipSize5placed)
                                fiveShip.setDisable(true);
                        }
                        else {
                            moveShip((ShipLabel) event.getGestureSource(), finalX, finalY);
                        }
                    }
                    // End Ship Size five ------------------------------------------------------------------------------


                    // Labels for ship were changed -> refresh Gui Playground
                    ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
                    ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


                    //ensure all ships are placed: ready only clickable when all ships are placed in a valid way
                    readyButton.setDisable(ActiveGameState.getAmountShipSize2() != amountShipSize2placed
                            || ActiveGameState.getAmountShipSize3() != amountShipSize3placed
                            || ActiveGameState.getAmountShipSize4() != amountShipSize4placed
                            || ActiveGameState.getAmountShipSize5() != amountShipSize5placed);


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


        // if we have 0 of a ship - label is disable from beginning
        if(ActiveGameState.getAmountShipSize2() == 0) twoShip.setDisable(true);
        if(ActiveGameState.getAmountShipSize3() == 0) threeShip.setDisable(true);
        if(ActiveGameState.getAmountShipSize4() == 0) fourShip.setDisable(true);
        if(ActiveGameState.getAmountShipSize5() == 0) fiveShip.setDisable(true);



        // end of making the labels on the right hand side of the playground draggable ---------------------------------
    }
    // end of initialize-method ----------------------------------------------------------------------------------------
    public void setLanguage(){
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.german){
            readyButton.setText("Bereit");
            xOfx2Ships.setText("(x/x platziert)");
            xOfx3Ships.setText("(x/x platziert)");
            xOfx4Ships.setText("(x/x platziert)");
            xOfx5Ships.setText("(x/x platziert)");
            randomPlacement.setText("Zufällig platzieren");
            resetPlacement.setText("Zurücksetzen");

        }
        if (ActiveGameState.getLanguage() == ActiveGameState.Language.english){
            readyButton.setText("Ready");
            xOfx2Ships.setText("(x/x placed)");
            xOfx3Ships.setText("(x/x placed)");
            xOfx4Ships.setText("(x/x placed)");
            xOfx5Ships.setText("(x/x placed)");
            randomPlacement.setText("Random placement");
            resetPlacement.setText("Reset");
        }
    }


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

        // create new OwnPlayground - link the same Labels -> playground is empty again, old placement is deleted
        IOwnPlayground ownPlayground = new OwnPlayground();
        ActiveGameState.setOwnPlayerIOwnPlayground(ownPlayground);

        ownPlayground.buildPlayground();


        // delete old ship labels -> GUI Playground is also empty again
        // groupID.lookupAll finds all Nodes of type Label and removes them - possible here, the only item which should
        // not be remvoed from the group is the gridPane -> no Label, no problem
        groupID.getChildren().removeAll(groupID.lookupAll(".label"));

        ArrayList<IShip> newShips = ActiveGameState.getPlacementKi().placeships(ownPlayground);
        ownPlayground.setShipListOfThisPlayground( new ArrayList<>()); //Interne Schiffe aus der placeShips Methode löschen

        for (IShip ship : newShips) {

            boolean horiz = ship.getPosStart().getX() != ship.getPosEnd().getX();

            ShipLabel shiplabel = new ShipLabel(new Point(ship.getPosStart().getX(), ship.getPosStart().getY()), horiz, ship.getSize());
            placeNewShip(shiplabel);
        }

        ownPlayground.setLabels(ownFieldArray);
        ownPlayground.drawPlayground();


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

        logGameOptions.log(Level.INFO, "Ships placed randomly");

        // enable ready button due to all ships being placed, game can be started now
        readyButton.setDisable(false);
    }



    /** reset placement button:
     *------------------------------------------------------------------------------------------------------------------
     * -> creates a new playground - eventually placed ships are deleted
     * -> places all ships randomly - KI does that
     * -> enables the ready button due to all ships being placed
     *----------------------------------------------------------------------------------------------------------------*/
    public void resetPlacement() {

        // create new OwnPlayground - link the same Labels -> playground is empty again, old placement is deleted
        ActiveGameState.setOwnPlayerIOwnPlayground(new OwnPlayground());
        ActiveGameState.getOwnPlayerIOwnPlayground().buildPlayground();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        // delete old ship labels -> GUI Playground is also empty again
        // groupID.lookupAll finds all Nodes of type Label and removes them - possible here, the only item which should
        // not be remvoed from the group is the gridPane -> no Label, no problem
        groupID.getChildren().removeAll(groupID.lookupAll(".label"));

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


    /** setBackground - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> sets the background of the scene
     *----------------------------------------------------------------------------------------------------------------*/
    public void setBackground(){

        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("/Gui_View/images/IngameBackground.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.anchorPane.setBackground(new Background(myBI));
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
     *
     * @param shipLabel: the Label (!! not a ShipLabel) that has to be made draggable
     * @param shipSize: the size of the ship, needed here because shipLabel is a Label
     * @param alreadyPlaced: boolean that indicates whether ship is a new one or an existing one that has to be moved
     *----------------------------------------------------------------------------------------------------------------*/
    public void handlerSetOnDragDetected(Label shipLabel, int shipSize, boolean alreadyPlaced) {

        int scale = ActiveGameState.getPlaygroundScale();

        // if ship is already existing, label will be invisible while moving - coming back visible when moving is complete (done by on drag dropped)
        if (alreadyPlaced) shipLabel.setVisible(false);


        // the Dragboard is used to indicate which ship type is hovered -> String indicates Sh4Pip Size, 2N: size 2, new ship, 2P: size 2, already placed ship
        Dragboard db = shipLabel.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        String shipInfo;
        if (alreadyPlaced)
            shipInfo = shipSize + "P";
        else
            shipInfo = shipSize + "N";
        content.putString(shipInfo);
        db.setContent(content);


        // depending on the ship size a drag view is set, displayed when dragging the element
        // this view is horizontal or vertical, depending on the current setting made by the rotate90-Button
        String horizontalURL = "/Gui_View/images/" + shipSize + "erSchiff.png";
        String verticalURL = "/Gui_View/images/" + shipSize + "erSchiffVertical.png";


        // setting the drag view with little offset
        // -> ship should not be in middle of mouse pointer because that would make placing an awkward experience
        //    due to be always in the middle of two labels when placing the ship to fit right into the label
        // -> for ships with length 2 and 4 we need a little offset
        int offset = (shipSize == 2 || shipSize == 4) ? ((shipSize == 2) ? 5 : 10) : 0;
        if (horizontal) {
            db.setDragView(new Image(horizontalURL, shipSize * scale, scale, false, false), offset, 0);
        } else {
            db.setDragView(new Image(verticalURL, scale, shipSize * scale, false, false), 0, -offset);
        }
    }



    /** setShipLabelPlace - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> when ship is placed or moved, shipLabel of Group indicating its position has to be placed at the right spot
     * -> this spot is computed and set here for the label
     *
     * @param shipLabel: ShipLabel of the to be placed ship
     *----------------------------------------------------------------------------------------------------------------*/
    public void setShipLabelPlace(ShipLabel shipLabel) {

        int sizeFactor = 0;
        switch (shipLabel.getSize()) {
            case 2: sizeFactor = 0;break;
            case 3:
            case 4: sizeFactor = 1;break;
            case 5: sizeFactor = 2;break;
        }

        double shipBaseLabelX;
        double shipBaseLabelY;
        if (shipLabel.isHorizontal()) {
            shipBaseLabelX = (shipLabel.getPosHead().getX() + sizeFactor) * (ActiveGameState.getPlaygroundScale() + 1);
            shipBaseLabelY = shipLabel.getPosHead().getY() * (ActiveGameState.getPlaygroundScale() + 1);
        } else {
            shipBaseLabelX = shipLabel.getPosHead().getX() * (ActiveGameState.getPlaygroundScale() + 1);
            shipBaseLabelY = (shipLabel.getPosHead().getY() + sizeFactor) * (ActiveGameState.getPlaygroundScale() + 1);
        }


        if (shipLabel.isHorizontal()) {
            switch (shipLabel.getSize()) {
                case 2:
                    shipLabel.setLayoutX(shipBaseLabelX);
                    shipLabel.setLayoutY(shipBaseLabelY);
                    break;
                case 3:
                case 4:
                    shipLabel.setLayoutX(shipBaseLabelX - ActiveGameState.getPlaygroundScale());
                    shipLabel.setLayoutY(shipBaseLabelY);
                    break;
                case 5:
                    shipLabel.setLayoutX(shipBaseLabelX - 2 * ActiveGameState.getPlaygroundScale());
                    shipLabel.setLayoutY(shipBaseLabelY);
                    break;
            }
        } else {
            switch (shipLabel.getSize()) {
                case 2:
                    shipLabel.setLayoutX(shipBaseLabelX);
                    shipLabel.setLayoutY(shipBaseLabelY);
                    break;
                case 3:
                case 4:
                    shipLabel.setLayoutX(shipBaseLabelX);
                    shipLabel.setLayoutY(shipBaseLabelY - ActiveGameState.getPlaygroundScale());
                    break;
                case 5:
                    shipLabel.setLayoutX(shipBaseLabelX);
                    shipLabel.setLayoutY(shipBaseLabelY - 2 * ActiveGameState.getPlaygroundScale());
                    break;
            }
        }
    }



    /** placeNewShip - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> called when ship label is successfully dropped for the first time
     * -> at first, the ship label is made draggable
     * -> then the ship label is placed at the right place in the group
     * -> at the end a new ship is created in the back end representation of the playground
     *
     * @param newShipLabel: ShipLabel of the new to be placed ship
     *----------------------------------------------------------------------------------------------------------------*/
    public void placeNewShip(ShipLabel newShipLabel){

        int finalX = newShipLabel.getPosHead().getX();
        int finalY = newShipLabel.getPosHead().getY();

        // making the newly created ShipLabel draggable too
        newShipLabel.setOnDragDetected(e -> {
            handlerSetOnDragDetected(newShipLabel, newShipLabel.getSize(), true);
            e.consume();
        });

        // making the newly create ShipLabel visible again when drag is exited
        newShipLabel.setOnDragDone(e -> newShipLabel.setVisible(true));


        // decide if ship has to be placed horizontal or vertical
        if (newShipLabel.isHorizontal()) {

            // this will place the ship in back-end representation of playground
            switch (newShipLabel.getSize()) {
                case 2: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX +1, finalY))); break;
                case 3: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX +2, finalY))); break;
                case 4: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX +3, finalY))); break;
                case 5: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX +4, finalY))); break;
            }

        } else {

            // this will place the ship in back-end representation of playground
            switch (newShipLabel.getSize()) {
                case 2: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY +1))); break;
                case 3: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY +2))); break;
                case 4: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY +3))); break;
                case 5: newShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY +4))); break;
            }
        }


        // place the label at the right position on the gridPane
        setShipLabelPlace(newShipLabel);


        // add the new placed ship's label to the shipLabelList
        groupID.getChildren().add(newShipLabel);
    }

    /** moveShip - help method:
     *------------------------------------------------------------------------------------------------------------------
     * -> called when existing ship label is successfully moved
     * -> Gui: moving already placed ship to a new location: change coordinates of the moved ship label
     * -> backend representation: moveShip is called
     *
     * @param existingShipLabel: the ShipLabel of the Ship that has to be moved
     * @param finalX: the X-postion where the Ship should be moved to
     * @param finalY: the Y-position where the Ship should be moved to
     *----------------------------------------------------------------------------------------------------------------*/
    public void moveShip(ShipLabel existingShipLabel, int finalX, int finalY) {

        // horizontal is the new alignment of the ship: Image will eventually be updated
        existingShipLabel.setHorizontal(horizontal);
        existingShipLabel.setImage();

        int shipSize = existingShipLabel.getSize();

        // Point has to be initialized -> if not warning: but this initialization should never occur: -> -1 would throw
        // error if so
        Point shipHead = new Point(-1 , -1);
        Point shipTail= new Point(-1, -1);

        // decide if ship has to be re-placed horizontal or vertical
        if(horizontal) {

            // this will move the ship in back-end representation of playground
            switch (shipSize) {
                case 2:
                    shipHead = new Point(finalX, finalY);
                    shipTail =new Point(finalX + 1, finalY);
                    break;
                case 3:
                    shipHead = new Point(finalX-1, finalY);
                    shipTail = new Point(finalX +1, finalY);
                    break;
                case 4:
                    shipHead = new Point(finalX-1, finalY);
                    shipTail = new Point(finalX +2, finalY);
                    break;
                case 5:
                    shipHead = new Point(finalX-2, finalY);
                    shipTail = new Point(finalX +2, finalY);
                    break;
            }
        }
        else {

            // this will move the ship in back-end representation of playground
            switch (shipSize) {
                case 2:
                    shipHead = new Point(finalX, finalY);
                    shipTail = new Point(finalX, finalY+1);
                    break;
                case 3:
                    shipHead = new Point(finalX, finalY-1);
                    shipTail = new Point(finalX, finalY+1);
                    break;
                case 4:
                    shipHead = new Point(finalX, finalY-1);
                    shipTail = new Point(finalX, finalY+2);
                    break;
                case 5:
                    shipHead = new Point(finalX, finalY-2);
                    shipTail = new Point(finalX, finalY+2);
                    break;
            }
        }
        existingShipLabel.setShip(ActiveGameState.getOwnPlayerIOwnPlayground().moveShip(existingShipLabel.getShip(), shipHead, shipTail));
        existingShipLabel.setPosHead(shipHead);

        // place the label at the right position on the gridPane
        setShipLabelPlace(existingShipLabel);
    }
}
