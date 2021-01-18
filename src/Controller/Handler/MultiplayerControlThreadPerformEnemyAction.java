package Controller.Handler;

import Controller.GamePlayground;
import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;
import Player.GameMode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread will:
 *
 *  As long as the enemy turn is present or the connection didn't collapse (also when the game is lost) :
 *      1. Get the command form the remote socket
 *      2. Perform the requested Action
 *      3. Send the command next/done if necessary
 *      4. Set YourTurn true if enemyTurn is expired
 *
 *  5. If it is the Player's turn again, set SaveButton (if Player is involved) and the water fields on the enemy playground clickable
 */
public class MultiplayerControlThreadPerformEnemyAction extends Thread{
    public static final Logger logMultiplayerControlThreadPerformEnemyAction = Logger.getLogger("parent.MultiplayerControlThreadPerformEnemyAction");

    @Override
    public void run() {

        logMultiplayerControlThreadPerformEnemyAction.log(Level.FINE, "Starting Multiplayer Perform Enemy Action Thread");

        //Your Turn can be true, when the game is loaded by server and the client turn didn`t expire
        ActiveGameState.setYourTurn(false);
        while( !ActiveGameState.isYourTurn() && ActiveGameState.isRunning()) {

        //1
        String[] cmdReceived;

        //We are the server
        if (ActiveGameState.isAmIServer()) {
            cmdReceived = ActiveGameState.getServer().getCMD();
        }
        //We are the client
        else {
            cmdReceived = ActiveGameState.getClient().getCMD();
        }

        //2,3,4
        String answerToEnemyAction;

        switch (cmdReceived[0]) {
            case "shot":
                ShotResponse shotResponse = ActiveGameState.getOwnPlayerIOwnPlayground().shoot(new Point(Integer.parseInt(cmdReceived[1]) -1, Integer.parseInt(cmdReceived[2])-1) );
                if (shotResponse.isGameLost()) {
                    ActiveGameState.setRunning(false);

                    //Game ended, send the finish command, to enable the client to execute
                    //and close the reader, writer and sockets

                    logMultiplayerControlThreadPerformEnemyAction.log(Level.INFO, "Lost game against remote");
                    if(ActiveGameState.isAmIServer()){
                        ActiveGameState.getServer().sendCMD(CMD.answer, "2");
                        ActiveGameState.getServer().closeConnection();
                    }
                    else{
                        ActiveGameState.getClient().sendCMD(CMD.answer, "2");
                        ActiveGameState.getClient().closeConnection();
                    }

                    HelpMethods.winOrLose(false);
                }
                if (shotResponse.isShipDestroyed()) {
                    answerToEnemyAction = "2";
                } else if (shotResponse.isHit()) {
                    answerToEnemyAction = "1";
                } else {
                    answerToEnemyAction = "0";
                    //The next answer from the Remote will be "next",
                    //therefore we continue, get the next command and
                    //end the enemy turn at "next"

                }

                //Send the answer to the remote socket
                //We are the server
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.answer, answerToEnemyAction);

                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.answer, answerToEnemyAction);
                }
                break;

            case "save":
                logMultiplayerControlThreadPerformEnemyAction.log(Level.INFO, "Remote requested saving, sending done and close connection");
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.done, "");
                    ActiveGameState.setRunning(false);
                    ActiveGameState.getServer().closeConnection();
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.done, "");
                    ActiveGameState.setRunning(false);
                    ActiveGameState.getClient().closeConnection();
                }
                //Saving game and return
                HelpMethods.saveRequest(Long.parseLong(cmdReceived[1]), false);
                return;
            case "next":
                                ActiveGameState.setYourTurn(true);
                                break;
            case "timeout":     logMultiplayerControlThreadPerformEnemyAction.log(Level.WARNING, "Timeout appeared, closing connection");
                                HelpMethods.connectionLost();
                                if (ActiveGameState.isAmIServer()) {
                                    ActiveGameState.getServer().closeConnection();
                                }
                                else {
                                    ActiveGameState.getClient().closeConnection();
                                }
                                ActiveGameState.setRunning(false);
                                return;

            default:            logMultiplayerControlThreadPerformEnemyAction.log(Level.WARNING, "UnexpectedMessage, closing connection");
                                HelpMethods.unexceptedMessage();
                                if (ActiveGameState.isAmIServer()) {
                                    ActiveGameState.getServer().closeConnection();
                                }
                                else {
                                    ActiveGameState.getClient().closeConnection();
                                }
                                ActiveGameState.setRunning(false);

                                ActiveGameState.setRunning(false);
                                return;
        }
    }


        logMultiplayerControlThreadPerformEnemyAction.log(Level.FINE, "GameMode at leaving the Perform Enemy Action Thread" + ActiveGameState.getModes());

        //In the Mode Ki vs Remote the KI Shoots Enemy Thread is called.
        if( ActiveGameState.getModes() == GameMode.kiVsRemote){
            MultiplayerControlThreadKiShootsEnemy multiplayerControlThreadKiShootsEnemy = new MultiplayerControlThreadKiShootsEnemy();
            multiplayerControlThreadKiShootsEnemy.start();
            return;
        }

        //5
        //When the player plays, the labels and the save button will be enabled
        logMultiplayerControlThreadPerformEnemyAction.log(Level.FINE, "Enable Playground");

        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
        ActiveGameState.getOwnPlayerIEnemyPlayground().drawPlayground();
        GamePlayground.setSaveAndCloseButtonClickable();

        logMultiplayerControlThreadPerformEnemyAction.log(Level.FINE, "Leaving Multiplayer Perform Enemy Action");

    }
}
