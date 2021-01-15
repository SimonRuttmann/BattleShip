package Gui_View;

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

public class unexpectedMessageFromRemote {

    static Scene wrongMessage;
    static int width = 300;
    static int height = 100;

    public static void display() {

        Stage unexceptedMessage = new Stage();
        unexceptedMessage.initModality(Modality.APPLICATION_MODAL);


        // Label + Button
        Label showError = new Label("Unerwartete Nachricht vom Spielpartner");
        Button backToStart = new Button("Hauptmenü");

        backToStart.setOnAction(event -> {
            Parent mainMenu = null;
            try {
                mainMenu = FXMLLoader.load(unexpectedMessageFromRemote.class.getResource("/Gui_View/fxmlFiles/MainMenu.fxml"));
                Main.primaryStage.setScene(new Scene(mainMenu));
                unexceptedMessage.close();
                Main.primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button endGame = new Button("Spiel beenden");
        endGame.setOnAction(event -> {
            unexceptedMessage.close();
            Main.primaryStage.close();
        });


        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(backToStart, endGame);
        buttons.setAlignment(Pos.CENTER);

        VBox layout1 = new VBox(15);
        layout1.getChildren().addAll(showError, buttons);
        layout1.setAlignment(Pos.CENTER);

        wrongMessage = new Scene(layout1, width, height);
        wrongMessage.getStylesheets().add("/Gui_View/Stylesheets/DefaultTheme.css");

        unexceptedMessage.setScene(wrongMessage);
        HelpMethods.alignStageCenter(unexceptedMessage, width, height);
        unexceptedMessage.setResizable(false);
        unexceptedMessage.showAndWait();
    }
}
