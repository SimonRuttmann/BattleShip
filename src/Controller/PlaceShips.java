package Controller;

import Gui_View.Main;
import Player.ActiveGameState;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.concurrent.atomic.AtomicReference;

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
    private Button randomPlacement;
    @FXML
    private Button resetPlacement;
    @FXML
    private Button readyButton;

    String green = "-fx-background-color: #a5fda5";
    String red = "-fx-background-color: #ffa1a1";
    String blue = "-fx-background-color: lightblue";
    String indicateValidPlacement = red;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("window size: " + Main.primaryStage.getHeight());
        //todo public scale - for fields -> pref size -> better way 2:3 for programm fix -> use SceneSizeChangeListener to scale properly all the time - no strange things happening
        int scale = 30;
        // Ships
        twoShip.setPrefSize(2*scale, scale);
        twoShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/2erSchiff.png"))));
        threeShip.setPrefSize(3*scale, scale);
        threeShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/3erSchiff.png"))));
        fourShip.setPrefSize(4*scale, scale);
        fourShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/4erSchiff.png"))));
        fiveShip.setPrefSize(5*scale, scale);
        fiveShip.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/5erSchiff.png"))));


        // Labels
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

        // own Playground
        for (int h = 0; h < gamesize; h++) {
            for (int v = 0; v < gamesize; v++) {
                Label label = new Label();
                label.setStyle(blue);
                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);
                // label can recognize Drag
                int finalH = h;
                int finalV = v;
                label.setOnDragOver(event -> {
                    if (event.getGestureSource() == twoShip) {
                        System.out.println("two - x: " + finalH + " v: " + finalV);
                        if(true/*todo valid placement*/) {
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
                            indicateValidPlacement = green;
                        }
                        label.setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 1, ownField)).setStyle(indicateValidPlacement);
                    }
                    if (event.getGestureSource() == threeShip) {
                        System.out.println("three - x: " + finalH + " v: " + finalV);
                        if(true/*todo valid placement*/) {
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
                            indicateValidPlacement = green;
                        }
                        label.setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 1, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH - 1, ownField)).setStyle(indicateValidPlacement);
                    }
                    if (event.getGestureSource() == fourShip) {
                        System.out.println("four - x: " + finalH + " v: " + finalV);
                        if(true/*todo valid placement*/) {
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
                            indicateValidPlacement = green;
                        }
                        label.setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 1, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH - 1, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 2, ownField)).setStyle(indicateValidPlacement);
                    }
                    if (event.getGestureSource() == fiveShip) {
                        System.out.println("five - x: " + finalH + " v: " + finalV);
                        if(true/*todo valid placement*/) {
                            event.acceptTransferModes(TransferMode.ANY); //todo - is this needed????
                            indicateValidPlacement = green;
                        }
                        label.setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 1, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH - 1, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH + 2, ownField)).setStyle(indicateValidPlacement);
                        (getNodeByRowColumnIndex(finalV, finalH - 2, ownField)).setStyle(indicateValidPlacement);
                    }
                    event.consume();
                });

                // remove visual feedback when moving on
                label.setOnDragExited(event -> {
                    ObservableList<Node> children = ownField.getChildren();
                    for (Node labelx: children) {
                        labelx.setStyle("-fx-background-color: lightblue");
                    }
                });

                label.setOnDragDropped(event -> {
                    // todo make ship placed
                    System.out.println("Ship placed!");
                    // is always true - valid placement already called in OnDragOver
                    event.setDropCompleted(true);
                    event.consume();
                });

                GridPane.setConstraints(label, h, v);
                //todo label in array oder so speichern, um dann darauf zugreifen zu können???
                ownField.getChildren().addAll(label);
            }
        }

        // set initial Labels - x/x ships placed //todo - 0/ -> variable
        xOfx2Ships.setText("(0/" +ActiveGameState.getAmountShipSize2()+ " platziert)");
        xOfx3Ships.setText("(0/" +ActiveGameState.getAmountShipSize3()+ " platziert)");
        xOfx4Ships.setText("(0/" +ActiveGameState.getAmountShipSize4()+ " platziert)");
        xOfx5Ships.setText("(0/" +ActiveGameState.getAmountShipSize5()+ " platziert)");


        // Drag&Drop Ships -> Placement
        twoShip.setOnDragDetected(event -> {
            Dragboard db = twoShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(""); //todo
            db.setContent(content);
            db.setDragView(new Image("/Gui_View/images/2erSchiff.png"), 10, 0);
            event.consume();
        });
        threeShip.setOnDragDetected(event -> {
            Dragboard db = threeShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("");//todo put right content beiing dropped - ship
            db.setContent(content);
            db.setDragView(new Image("/Gui_View/images/3erSchiff.png"));
            event.consume();
        });
        fourShip.setOnDragDetected(event -> {
            Dragboard db = fourShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(""); //todo
            db.setContent(content);
            db.setDragView(new Image("/Gui_View/images/4erSchiff.png"), 10 ,0);
            event.consume();
        });
        fiveShip.setOnDragDetected(event -> {
            Dragboard db = fiveShip.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(""); //todo
            db.setContent(content);
            db.setDragView(new Image("/Gui_View/images/5erSchiff.png"));
            event.consume();
        });

        twoShip.setOnDragDone(event -> {
            System.out.println("Drag done!");
            // todo disable dropping when all ships placed
            event.consume();
        });
        threeShip.setOnDragDone(event -> {
            System.out.println("Drag done!");
            // todo disable dropping when all ships placed
            event.consume();
        });
        fourShip.setOnDragDone(event -> {
            System.out.println("Drag done!");
            // todo disable dropping when all ships placed
            event.consume();
        });
        fiveShip.setOnDragDone(event -> {
            System.out.println("Drag done!");
            // todo disable dropping when all ships placed
            event.consume();
        });
    }

    public void newRandomPlacement () {
        //todo -> call random place function
        //todo -> update Gui after that
        System.out.println("Ships placed randomly");
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

    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
}
