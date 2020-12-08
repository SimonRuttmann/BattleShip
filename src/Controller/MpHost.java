package Controller;

import Gui_View.Main;
import Network.*;
import Player.ActiveGameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        ActiveGameState.setAmIServer(true);
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
                if(server.startSeverConnection()){
                    Parent newOrLoad = null;
                    try {
                        newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ActiveGameState.setServer(server);

                    if ( newOrLoad == null) {
                        System.out.println("Error at loading Scene newOrLoad");
                    }
                    else {
                        Main.primaryStage.setScene(new Scene(newOrLoad));
                        Main.primaryStage.show(); //todo geht nicht in diesem thread -> muss im javafx application thread erfolgen
                    }
                }
                else
                    System.out.println("didn't work");//todo fehler
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

    public void establishConnection() throws IOException {
        Parent newOrLoad = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/newOrLoad.fxml"));
        Main.primaryStage.setScene(new Scene(newOrLoad));
        Main.primaryStage.show();
    }
}
