package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;
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
                ShotResponse shotResponse = ActiveGameState.getOwnPlayerIOwnPlayground().shoot(new Point(Integer.parseInt(cmdReceived[1]), Integer.parseInt(cmdReceived[2])));
                if (shotResponse.isGameLost()) {
                    ActiveGameState.setRunning(false);
                    HelpMethods.winOrLose(true);
                }
                if (shotResponse.isShipDestroyed()) {
                    answerToEnemyAction = "2";
                } else if (shotResponse.isHit()) {
                    answerToEnemyAction = "1";
                } else {
                    answerToEnemyAction = "0";
                    //If nothing got hit, we send the answer and our turn started
                    ActiveGameState.setYourTurn(true);
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

                //TODO Save And Load methods have to be adjusted
            case "save":
                //TODO Yannick Save the game and CONTINUE playing, don't stop!
                SaveAndLoad.save(new Savegame(), cmdReceived[1]);
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.done, "");
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.done, "");
                }
            case "load":
                SaveAndLoad.load(new Savegame(), cmdReceived[1]);
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().sendCMD(CMD.done, "");
                }
                //We are the client
                else {
                    ActiveGameState.getClient().sendCMD(CMD.done, "");
                }
            case "timeout":
                if (ActiveGameState.isAmIServer()) {
                    ActiveGameState.getServer().closeConnection();
                } else {
                    ActiveGameState.getClient().closeConnection();
                }
                ActiveGameState.setRunning(false);
                //TODO Yannick POPUP für Timeout setzen
            default:
                System.out.println("Unexpected message from connection partner");
                ActiveGameState.setRunning(false);
        }
    }
        //5
        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
    }
}
