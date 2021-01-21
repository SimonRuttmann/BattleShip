package Gui_View;

import GameData.ActiveGameState;
import LoggingNetwork.NetworkLogger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class Main extends Application {

    public static Stage primaryStage;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;


    public static void main(String[] args) {
        //Necessary to start Logging, constructor call defines configurations
        try {
            NetworkLogger networklogger = new NetworkLogger();

        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/MainMenu.fxml"));
        ActiveGameState.newView = true;

        primaryStage.setTitle("Battleship");

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        HelpMethods.alignStageCenter(primaryStage, WIDTH, HEIGHT);

        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
