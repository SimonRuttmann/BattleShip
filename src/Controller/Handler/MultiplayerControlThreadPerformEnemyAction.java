package Controller.Handler;

import Controller.GamePlayground;
import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;
import Player.GameMode;
import Player.SaveAndLoad;
import Player.Savegame;

/**
 * This thread will:
 *
 *  As long as the enemy turn is present or the connection didn't collapse (also when the game is lost) :
 *      1. Get the command form the remote socket
 *      2. Perform the requested Action
 *      3. Send the command next/done if necessary
 *      4. Set YourTurn true if enemyTurn is expired
 *
 *  5. If it is the Player's turn again, set the water fields on the enemy playground clickable
 */
public class MultiplayerControlThreadPerformEnemyAction extends Thread{
    @Override
    public void run() {

        System.out.println( "Starte Multiplayer Perform Enemy Action");
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
            System.out.println( "Received the Command " + cmdReceived );
        //2,3,4
        String answerToEnemyAction;

        switch (cmdReceived[0]) {
            case "shot":
                ShotResponse shotResponse = ActiveGameState.getOwnPlayerIOwnPlayground().shoot(new Point(Integer.parseInt(cmdReceived[1]) -1, Integer.parseInt(cmdReceived[2])-1) );
                if (shotResponse.isGameLost()) {
                    ActiveGameState.setRunning(false);

                    //Game ended, send the finish command, to enable the client to execute
                    //and close the reader, writer and sockets
                    if(ActiveGameState.isAmIServer()){
                        ActiveGameState.getServer().sendCMD(CMD.answer, "2");
                        ActiveGameState.getServer().closeConnection();
                    }
                    else{
                        ActiveGameState.getClient().sendCMD(CMD.answer, "2");
                        ActiveGameState.getClient().closeConnection();
                    }

                    HelpMethods.winOrLose(false);
                    System.out.println("Game lost");
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

                //TODO Save And Load methods have to be adjusted
            case "save":
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.done, "");
                    ActiveGameState.setRunning(false);
                    ActiveGameState.getServer().closeConnection();                  //TODO beenden nach speichern
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.done, "");
                    ActiveGameState.setRunning(false);
                    ActiveGameState.getClient().closeConnection();                  //TODO Beenden nach speichern
                }
                //Saving game and return
                HelpMethods.saveRequest(Long.parseLong(cmdReceived[1]), false);
                return;
           /* case "load":                                                          //TODO
                SaveAndLoad.load( cmdReceived[1]);
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.done, "");
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.done, "");
                }
                break;*/
            case "next":
                                ActiveGameState.setYourTurn(true);
                                break;
            case "timeout":
                                HelpMethods.connectionLost();
                                if (ActiveGameState.isAmIServer()) {
                                    ActiveGameState.getServer().closeConnection();
                                }
                                else {
                                    ActiveGameState.getClient().closeConnection();
                                }
                                ActiveGameState.setRunning(false);
                                return;

            default:
                                HelpMethods.unexceptedMessage();
                                if (ActiveGameState.isAmIServer()) {
                                    ActiveGameState.getServer().closeConnection();
                                }
                                else {
                                    ActiveGameState.getClient().closeConnection();
                                }
                                ActiveGameState.setRunning(false);

                                System.out.println("Unexpected message from connection partner");
                                ActiveGameState.setRunning(false);
                                return;
        }
    }
        //TODO Kommentieren
        //wenn ki vs Remote -> wieder den KI shoot Thread starten -> ping pong
        if( ActiveGameState.getModes() == GameMode.kiVsRemote){
            MultiplayerControlThreadKiShootsEnemy multiplayerControlThreadKiShootsEnemy = new MultiplayerControlThreadKiShootsEnemy();
            multiplayerControlThreadKiShootsEnemy.start();
            System.out.println( "Your turn sollte true sein: " + ActiveGameState.isYourTurn());
            return;
        }

        //5
        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
        GamePlayground.setSaveAndCloseButtonClickable();
        System.out.println( "Beende Multiplayer Perform Enemy Action ");
    }
}
