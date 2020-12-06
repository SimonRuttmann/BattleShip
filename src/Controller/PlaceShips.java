package Controller;

import Gui_View.Main;
import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;
import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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


        // Labels e.g. (0 out of 4 Placed)
        xOfx2Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx3Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx4Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);
        xOfx5Ships.setPrefSize(Region.USE_COMPUTED_SIZE, scale);


        // set Labels to Player Names
        ownFieldLabel.setText(ActiveGameState.getOwnPlayerName() + "'s Spielfeld");
        // 2D field for Labels:
        int gamesize = ActiveGameState.getPlaygroundSize();
        ownField.setHgap(1);
        ownField.setVgap(1);

        // Creates the ownPlayground as a GridPane. Creating a table of Labels, these are later connected to the game playground via the method setLabels()
        for (int x = 0; x < gamesize; x++) {
            for (int y = 0; y < gamesize; y++) {

                //Creating the Labels
                Label label = new Label();
                label.setStyle(blue);
                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);


                // finalX is the y position of the label
                // finalY is the x position of the label
                int finalX = x;
                int finalY = y;

                //Every label is getting a OnDragOver-Listener, when a ship is hovered over a label in the playground
                //The created events set the labels on green when ship placement is valid and red when the ship placement is not
                label.setOnDragOver(event -> {


                    //The following is done for every ship label 2, 3, 4, 5

                    // event.getGestureSource() is getting the Element, which is hovered over the item
                    // In this case the ship Label is returned when hover over an label
                    if (event.getGestureSource() == twoShip) {


                        System.out.println("two - x: " + finalX + " y: " + finalY);
                        //When the ship placement is allowed, the String indicateValidPlacement is set to the string green, which contains a -fx css color-scheme
                        //if the ship placement is not allowed the String is set to the string red, containing another -fx css color-scheme
                        if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY), new Point(finalX + 1, finalY))) ||
                                (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY), new Point(finalX, finalY + 1)))) {
                            /* label does only accept ship if placement is valid -> when not valid, ship is not placeable, shiplabel gets dropped back to start position */
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
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
                        System.out.println("three - x: " + finalX + " y: " + finalY);
                        if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 1, finalY), new Point(finalX + 1, finalY))) ||
                                (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 1), new Point(finalX, finalY + 1)))) {
                            /* label does only accept ship if placement is valid -> when not valid, ship is not placeable, shiplabel gets dropped back to start position */
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
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
                        System.out.println("four - x: " + finalX + " y: " + finalY);
                        if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 1, finalY), new Point(finalX + 2, finalY))) ||
                                (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 1), new Point(finalX, finalY + 2)))) {
                            /* label does only accept ship if placement is valid -> when not valid, ship is not placeable, shiplabel gets dropped back to start position */
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
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
                        System.out.println("five - x: " + finalX + " y: " + finalY);
                        if ((horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX - 2, finalY), new Point(finalX + 2, finalY))) ||
                                (!horizontal && ActiveGameState.getOwnPlayerIOwnPlayground().isValidPlacement(new Point(finalX, finalY - 2), new Point(finalX, finalY + 2)))) {
                            /* label does only accept ship if placement is valid -> when not valid, ship is not placeable, shiplabel gets dropped back to start position */
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
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

                    // event should not be dispatched to any further event listeners
                    event.consume();
                });


                //TODO DIE LÖSUNG FÜR ALL UNSERE PROBLEME
                // Group a = new Group();
                // a.getChildren().add(Hintergrundelemente (Labels));
                // a.getChildren().add(Vordergrundelemente (Schiffe));

                // remove visual feedback when moving on //todo make red correct -> manchmal zu wenig felder rot markiert im verlgeich zur schiffsgroesse
                label.setOnDragExited(event -> {
                    ObservableList<Node> children = ownField.getChildren();
                    for (Node labelx : children) {
                        labelx.setStyle("-fx-background-color: lightblue");
                    }
                });


                label.setOnDragDropped(event -> {
                    System.out.println("Dropped successfully");
                    if (event.getGestureSource() == twoShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX + 1, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY), new Point(finalX, finalY + 1));
                        //todo counter erhoehen
                    }

                    if (event.getGestureSource() == threeShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 1, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 1));
                        //todo counter erhoehen
                    }

                    if (event.getGestureSource() == fourShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 1, finalY), new Point(finalX + 2, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 1), new Point(finalX, finalY + 2));
                        //todo counter erhoehen
                    }

                    if (event.getGestureSource() == fiveShip) {
                        if (horizontal)
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX - 2, finalY), new Point(finalX + 2, finalY));
                        else
                            ActiveGameState.getOwnPlayerIOwnPlayground().isShipPlacementValid(new Point(finalX, finalY - 2), new Point(finalX, finalY + 2));
                        //todo counter erhoehen
                    }


                    System.out.println("Ship placed!");
                    // is always true - valid placement already called in OnDragOver
                    event.setDropCompleted(true);
                    event.consume();
                });

                GridPane.setConstraints(label, x, y);
                ownField.getChildren().addAll(label);
            }
        }

        // connect Labels to Playground
        Object[] ownFieldArray = new Object[gamesize * gamesize];
        ownFieldArray = ownField.getChildren().toArray();
        ActiveGameState.getOwnPlayerIOwnPlayground().setLabels(ownFieldArray);
        ActiveGameState.getOwnPlayerIOwnPlayground().drawPlayground();


        // Sets the text of the labels e.g (3 out of 7) ships of size 2 placed
        //todo - 0/ -> variable
        xOfx2Ships.setText("(" + ActiveGameState.getAmountShipSize2placed() + "/" + ActiveGameState.getAmountShipSize2() + " platziert)");
        xOfx3Ships.setText("(" + ActiveGameState.getAmountShipSize3placed() + "/" + ActiveGameState.getAmountShipSize3() + " platziert)");
        xOfx4Ships.setText("(" + ActiveGameState.getAmountShipSize4placed() + "/" + ActiveGameState.getAmountShipSize4() + " platziert)");
        xOfx5Ships.setText("(" + ActiveGameState.getAmountShipSize5placed() + "/" + ActiveGameState.getAmountShipSize5() + " platziert)");


        // Drag&Drop Ships -> Placement
        // For all ship labels with the size 2, 3, 4, 5
        //TODO To Yannick, für was braucht ein ClipboardContent? Die Funktion die hier ausgeführt werden muss ist doch nur:
        // Wenn auf ein Schiff geklickt wird und es ist der horizontal wert gesetzt -> Lade Bild 2erSchiff horizontal, sonst Lade Bilde 2er Schiff vertikal
        twoShip.setOnDragDetected(event -> {
            Dragboard db = twoShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(""); //todo
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
            content.putString("");//todo put right content beiing dropped - ship
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
            content.putString(""); //todo
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
            content.putString(""); //todo
            db.setContent(content);
            if (horizontal)
                db.setDragView(new Image("/Gui_View/images/5erSchiff.png"));
            else
                db.setDragView(new Image("/Gui_View/images/5erSchiffVertical.png"));
            event.consume();
            ActiveGameState.setAmountShipSize5placed(ActiveGameState.getAmountShipSize5placed() + 1);
        });


        // disable Dragging when all Ships of each kind are placed //TODO To Yannick Falscher Kommentar? <- nicht das Loslassen wird unterbunden, sondern das Klicken auf die Schiffslabels, welche das DragDetected Event auslösen würden
        if (ActiveGameState.getAmountShipSize2() <= ActiveGameState.getAmountShipSize2placed())
            twoShip.setDisable(true);
        if (ActiveGameState.getAmountShipSize3() <= ActiveGameState.getAmountShipSize3placed())
            threeShip.setDisable(true);
        if (ActiveGameState.getAmountShipSize4() <= ActiveGameState.getAmountShipSize4placed())
            fourShip.setDisable(true);
        if (ActiveGameState.getAmountShipSize5() <= ActiveGameState.getAmountShipSize5placed())
            fiveShip.setDisable(true);
    }

    public void rotateShip90() {
        horizontal = !horizontal;
    }

    public void newRandomPlacement() {
        //todo -> call random place function
        //todo -> update Gui after that

        // todo set counter of placed ships to max - in place ships or here?

        // disable Dropping for all Labels cause all ships are placed
        twoShip.setDisable(true);
        threeShip.setDisable(true);
        fourShip.setDisable(true);
        fiveShip.setDisable(true);
        System.out.println("Ships placed randomly");
    }

    public void resetPlacement() {
        // todo remove ships
        // todo reset counter of placed ships - in remove ships or here??

        // enable Dropping for all Labels cause all ships are unplaced
        twoShip.setDisable(false);
        threeShip.setDisable(false);
        fourShip.setDisable(false);
        fiveShip.setDisable(false);
    }

    public void startGame() throws IOException {
        //todo: only clickable when all ships are placed in a valid way

        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Gui_View.HelpMethods.closeProgrammSaveGame();
        });
        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }

    //todo
    // zurest buildPlayground
    // Dann Connect Labels mit Playground ( Playground ist bereits konstruiert über cache zugreifbar)
    // DRAG AN DROP Event ->
    // FÜR JEDES SCHIFF: danach isPlacementValid() und wenn valid, dann wird das schiff direkt im Feld plaziert mit dem Labelwechseln @RobinRöcker
    // bei false:
    // Fehlermeldung: Das Schiff kann dort nicht plaziert werden
    // Button ready -> Spielfeld


    //todo reset label

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
Layer 0     Hintergrundbild
Layer 1     Labels der GridPane                           ->    In Modell: bei IsValidPlacementAbgeändert(kein Schiff erstellen): Blau (Transparent) GRÜN und ROT wird von Yannick angezeigt: Rot wenn nicht plazierbar, Grün wenn plazierbar -> Nur die unterm Schiff
Layer 2                                                   ->    Bilder der Labels er GridPane bei EnemyPlayground wenn Schiff angreifen -> Schiffsteile
Layer 3     Labels der Schiffe (Bilder)


Bei Ingame: Modell:     Rot wenn Schiff getroffen bzw Bild setzen / Bild kommt von Yannick

                        ShotWater - Kein Bild sondern transparenter Hintergrund
                        Wasser ->   Blau und dann schauen wies aussieht wenn disabled wird


 */