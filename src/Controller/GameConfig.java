package Controller;

import Controller.Handler.MultiplayerControlThreadConfigCommunication;
import Gui_View.Main;
import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GameConfig implements Initializable {

    @FXML private Button backButton;
    @FXML private TextField playerName;
    @FXML private Spinner<Integer> fieldSize;
    @FXML private Spinner<Integer> numberOfShips;
    @FXML private Spinner<Integer> number2Ships;
    @FXML private Spinner<Integer> number3Ships;
    @FXML private Spinner<Integer> number4Ships;
    @FXML private Spinner<Integer> number5Ships;
    @FXML private Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set Spinner on initial values, add spinnerValueFactory for each to declare ranges
        // ensurers that parameters of ActiveGameState are initialised correctly
        // playerName can only have playernameMaxLen characters, no Space " " characters allowed
        int playernameMaxLen = 20;

        // TextFormatter defines rules for String in Textfield
        // source: https://stackoverflow.com/questions/16538849/how-to-use-javafx-textfield-maxlength/33191834
        // here we reject any change which exceeds the length - can't add more character in UI TextField then
        UnaryOperator<TextFormatter.Change> rejectChange = c -> {
            // check if the change might effect the validating predicate
            if (c.isContentChange()) {
                // check if change is valid
                if (c.getControlNewText().length() > playernameMaxLen || c.getControlNewText().contains(" ")) {
                    // invalid change
                    /* we do not need error message -> more than 10 characters just won't work...
                    // sugar: show a context menu with error message
                    final ContextMenu menu = new ContextMenu();
                    menu.getItems().add(new MenuItem("Name darf höchstens\n"+playernameMaxLen+" Zeichen lang sein."));
                    menu.show(c.getControl(), Side.BOTTOM, 0, 0);*/
                    // return null to reject the change
                    return null;
                }
            }
            // valid change: accept the change by returning it
            return c;
        };
        // call TextFormatter - called every time when change is made -> every Keyboard click
        playerName.setTextFormatter(new TextFormatter<>(rejectChange));

        playerName.setPromptText("Spielername eingeben");
        fieldSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5,30, 10));
        // todo: get Number of ships + make 2,3,4,5 dependend on number + make number of ships depened on field size
        numberOfShips.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4,8,5));
        number2Ships.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2,1));
        number3Ships.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2,2,2));
        number4Ships.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2,1));
        number5Ships.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2,1));
    }

    //TODO BIG use nice GREEN COMMENTS for STRUCTURE -> like in Oracle's Java classes

    public void startGame() throws IOException {
        // read in all parameters
        if(playerName.getText().equals(""))
            ActiveGameState.setOwnPlayerName("Secret Agent");
        else
            ActiveGameState.setOwnPlayerName(playerName.getText());

        // save configured settings
        ActiveGameState.setPlaygroundSize(fieldSize.getValue());
        ActiveGameState.setAmountShipSize2(number2Ships.getValue());
        ActiveGameState.setAmountShipSize3(number3Ships.getValue());
        ActiveGameState.setAmountShipSize4(number4Ships.getValue());
        ActiveGameState.setAmountShipSize5(number5Ships.getValue());

        // todo Simon -> neuer Thread : wenn Server -> send cmd ships 222 333 44 ... to client

        //MultiplayerControlThreadConfigCommunication multiplayerControlThreadConfigCommunication = new MultiplayerControlThreadConfigCommunication();
        //multiplayerControlThreadConfigCommunication.start();

        Parent start = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/chooseSelfOrKi.fxml"));
        Main.primaryStage.setScene(new Scene(start));
        Main.primaryStage.show();
    }


    public void backToLastScene() throws IOException{
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }
}
