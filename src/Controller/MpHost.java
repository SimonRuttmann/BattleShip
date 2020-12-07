package Controller;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Network.*;
import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MpHost implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label ipAddressLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IServer server = new Server();
        ipAddressLabel.setText("getIPAddress(): " + server.getIPAddress() + "\ngetAllIPAddress(): " + java.util.Arrays.asList(server.getAllIPAddress()).toString());
        System.out.println(server.getIPAddress());
        System.out.println(java.util.Arrays.asList(server.getAllIPAddress()).toString()); //todo in label schreiben
        // todo problem bei mehrfachem aufruf -> java.net.BindException: Address already in use (Bind failed)

        // new Thread for Connecting
        Thread offerConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Connection offered - waiting for paring");
                if (server.startSeverConnection()) {
                    ActiveGameState.setServer(server);
                    // platform run later -> sends task to GuiThread -> Gui does this as soon as this piece of code is reached
                    Platform.runLater(() -> {
                        Parent a = null;
                        try {
                            a = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (a != null)
                            Main.primaryStage.setScene(new Scene(a));
                    });
                } else {
                    System.out.println("Connection could not be established");
                    // Method reference -> "Lambda could be replaced with method reference" -> done that
                    Platform.runLater(HelpMethods::connectionFailed);
                }

            }
        });
        offerConnection.start();
    }

    public void backToLastScene() throws IOException {
        // todo shutdown server + close everything
        Parent mpSelect = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }
}
