package Controller;

import Gui_View.Main;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaceShips implements Initializable {

    @FXML
    private Label ownFieldLabel;
    @FXML
    private GridPane ownField;
    @FXML
    private Button randomPlacement;
    @FXML
    private Label xOfx2Ships;
    @FXML
    private Label xOfx3Ships;
    @FXML
    private Label xOfx4Ships;
    @FXML
    private Label xOfx5Ships;
    @FXML
    private Button readyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                label.setStyle("-fx-background-color: lightblue");
                label.setMinSize(5, 5);
                label.setPrefSize(30, 30);
                label.setMaxSize(30, 30);
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

        // method for placing ships - thread? drag&drop
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

}
