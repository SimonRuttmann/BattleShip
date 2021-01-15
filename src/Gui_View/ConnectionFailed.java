package Gui_View;

import Player.NetworkLogger;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionFailed {

    static Scene conFailed;
    static int width = 300;
    static int height = 100;

    public static void display() {
        Stage failed = new Stage();
        failed.initModality(Modality.APPLICATION_MODAL);

        Label showError = new Label("Verbindungsaufbau fehlgeschlagen");
        Button backToStart = new Button("Hauptmenü");
        Button endGame = new Button("Spiel beenden");
        backToStart.setOnAction(event -> {
            Parent mainMenu = null;
            try {
                mainMenu = FXMLLoader.load(unexceptedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                failed.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endGame.setOnAction(event -> {
            failed.close();
            NetworkLogger.terminateLogging();
            Main.primaryStage.close();
        });

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToStart, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);


        conFailed = new Scene(layout1, width, height);
        conFailed.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");

        failed.setScene(conFailed);
        HelpMethods.alignStageCenter(failed, width, height);
        failed.setResizable(false);
        failed.showAndWait();
    }
}
