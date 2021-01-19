package Controller.WorkingThreads;

import Controller.GamePlayground;
import Gui_View.HelpMethods;
import Model.Playground.IEnemyPlayground;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import GameData.ActiveGameState;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  This thread will:
 *          1. disable the saveButton and all Labels on enemy playground
 *          2. send the client/server a message containing all necessary information
 *          3. wait for an answer from the client/server
 *          4. perform all necessary actions depending on the report from client/server
 *          5. start the MultiplayerControlThreadPerformEnemyAction, when our turn is expired
 */

public class MultiplayerControlThreadShootEnemy extends Thread{

    public Event event;
    public MultiplayerControlThreadShootEnemy(Event event){
        this.event = event;
    }

    public static final Logger logMultiplayerControlThreadShootEnemy = Logger.getLogger("parent.MultiplayerControlThreadShootEnemy");
    @Override
    public void run() {

        logMultiplayerControlThreadShootEnemy.log(Level.FINE, "Starting Multiplayer Shoot Enemy Thread");

        //1.
        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
        GamePlayground.setSaveAndCloseButtonNonClickable();
        //2. and 3.
        int xPos = GridPane.getColumnIndex((Label) event.getSource());
        int yPos = GridPane.getRowIndex((Label) event.getSource());

        Point shootPosition = new Point(xPos, yPos);

        //Preparing command which has to be send
        //Communication Protocol: shot 1 1 = Pos 0, 0 in the Playground
        String cmdParameter = (xPos+1) + " " + (yPos+1);
        System.out.println( cmdParameter);
        //Report from the remoteSocket
        String[] cmdReceived;
        System.out.println("Sende: " + CMD.shot + " " + cmdParameter);

        //We are the server
        if ( ActiveGameState.isAmIServer()){
            ActiveGameState.getServer().sendCMD(CMD.shot, cmdParameter);
            cmdReceived = ActiveGameState.getServer().getCMD();
        }
        //We are the client
        else{
            ActiveGameState.getClient().sendCMD(CMD.shot, cmdParameter);
            cmdReceived = ActiveGameState.getClient().getCMD();
        }

        //4 determine the response and act depending on it
        IEnemyPlayground enemyPlayground = ActiveGameState.getOwnPlayerIEnemyPlayground();


        switch (cmdReceived[0]){
            case "answer":
                ShotResponse shotResponse = enemyPlayground.shoot(shootPosition, Integer.parseInt(cmdReceived[1]));


                //VIEW SHOW
                if ( Integer.parseInt(cmdReceived[1]) == 2) {
                  
                    Label headShipSunken = shotResponse.getLabel(); // <- == Label, des zerstörten Schiffs oben links
                    boolean horizontal = shotResponse.getAlignment();
                    int size = shotResponse.getSizeOfSunkenShip();

                    //für 2er, 3er, 4er, 5er
                    Label shipLabel = new Label();
                    shipLabel.setLayoutX(headShipSunken.getLayoutX());
                    shipLabel.setLayoutY(headShipSunken.getLayoutY());


                    int scale = ActiveGameState.getPlaygroundScale();
                    ImageView image;
                    String imageURL = "/Gui_View/images/";

                    switch (size) {
                        case (2):
                            imageURL += "2";
                            break;
                        case (3):
                            imageURL += "3";
                            break;
                        case (4):
                            imageURL += "4";
                            break;
                        case (5):
                            imageURL += "5";
                            break;
                        default:
                            System.out.println("Debug");
                    }

                    if (horizontal) {
                        imageURL += "erSchiff.png"; //todo add sunken images
                        image = new ImageView(new Image(getClass().getResourceAsStream(imageURL)));
                        image.fitWidthProperty().bind(new SimpleIntegerProperty(size * scale).asObject());
                        image.fitHeightProperty().bind(new SimpleIntegerProperty(scale).asObject());
                    } else {
                        imageURL += "erSchiffVertical.png"; //todo add sunken images
                        image = new ImageView(new Image(getClass().getResourceAsStream(imageURL)));
                        image.fitWidthProperty().bind(new SimpleIntegerProperty(scale).asObject());
                        image.fitHeightProperty().bind(new SimpleIntegerProperty(size * scale).asObject());
                    }

                    shipLabel.setGraphic(image);

                    Platform.runLater(() -> GamePlayground.getGroupEnemP().getChildren().add(shipLabel));
                }

                //END VIEW SHOW


                if ( shotResponse.isGameWin())
                {
                    ActiveGameState.setRunning(false);
                    HelpMethods. winOrLose(true);
                    logMultiplayerControlThreadShootEnemy.log(Level.INFO, "Player won against remote, closing connection");
                    if(ActiveGameState.isAmIServer()){
                        ActiveGameState.getServer().closeConnection();
                    }
                    else{
                        ActiveGameState.getClient().closeConnection();
                    }
                }
                break;

            case "timeout":
                logMultiplayerControlThreadShootEnemy.log(Level.WARNING, "Timeout appeared, closing connection");
                if ( ActiveGameState.isAmIServer()){
                    ActiveGameState.getServer().closeConnection();
                }
                else{
                    ActiveGameState.getClient().closeConnection();
                }
                ActiveGameState.setRunning(false);
                HelpMethods.connectionLost();
                return;

            default:
                logMultiplayerControlThreadShootEnemy.log(Level.WARNING, "UnexpectedMessage, closing connection");
                ActiveGameState.setRunning(false);
                return;
        }

        System.out.println( "Ein durchgang Durchgang von Shoot Enemy abgeschlossen");

        if ( cmdReceived.length == 2 ) System.out.println( cmdReceived[0] + " " + cmdReceived[1]);
        else System.out.println(Arrays.toString(cmdReceived));


        if (ActiveGameState.isRunning()) {
            //If the answer was 1 or 2, we enable the Labels (player can click/shoot again) and end the thread
            if (cmdReceived[0].equals("answer") && (Integer.parseInt(cmdReceived[1]) == 1 || Integer.parseInt(cmdReceived[1]) == 2)) {
                enemyPlayground.setAllWaterFieldsClickable();
                GamePlayground.setSaveAndCloseButtonClickable();
                HelpMethods.displayTurn(true);
                logMultiplayerControlThreadShootEnemy.log(Level.FINE, "Player hit something, his turn didn`t expire");
            }

            //If the answer was 0, its the enemy´s turn, so we end the current thread and start the getShot thread
            if (cmdReceived[0].equals("answer") && Integer.parseInt(cmdReceived[1]) == 0) {
                ActiveGameState.setYourTurn(false);
                logMultiplayerControlThreadShootEnemy.log(Level.FINE,"Player didn´t hit something, turn expired");
                HelpMethods.displayTurn(false);
                //Send next to maintain the Ping-Pong methodology
                //We are the server
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.next, "");
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.next, "");
                }
                MultiplayerControlThreadPerformEnemyAction threadGetShot = new MultiplayerControlThreadPerformEnemyAction();
                threadGetShot.start();
            }
        }
        logMultiplayerControlThreadShootEnemy.log(Level.FINE, "Closing Multiplayer Control Thread Shoot Enemy");
    }
}
