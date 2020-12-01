package Controller;

import Gui_View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.IOException;

public class PlaceShips {

    @FXML
    private Button readyButton;

    public void startGame() throws IOException {
        // Versehentliches Schließen des Spiels verhindern + Speicheraufforderung
        Main.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Gui_View.HelpMethods.closeProgrammSaveGame();
        });
        Parent game = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
        Main.primaryStage.setScene(new Scene(game));
        Main.primaryStage.show();
    }

    //TODO Prio
    //TODO Gridpane von Game für eigenes Spielfeld kopieren
    // zurest buildPlayground
    // Dann Connect Labels mit Playground ( Playground ist bereits konstruiert über cache zugreifbar)
    // DRAG AN DROP Event ->
    // FÜR JEDES SCHIFF: danach isPlacementValid() und wenn valid, dann wird das schiff direkt im Feld plaziert mit dem Labelwechseln @RobinRöcker
    // bei false:
    // Fehlermeldung: Das Schiff kann dort nicht plaziert werden
    // Button ready -> Spielfeld



}
