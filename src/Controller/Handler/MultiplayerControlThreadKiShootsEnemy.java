package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO
 */
public class MultiplayerControlThreadKiShootsEnemy extends Thread{
    public static final Logger logMultiplayerControlThreadKiShootsEnemy = Logger.getLogger("parent.MultiplayerControlThreadKiShootsEnemy");
    @Override
    public void run(){
        while (ActiveGameState.isYourTurn() && ActiveGameState.isRunning()){
            try {
                logMultiplayerControlThreadKiShootsEnemy.log(Level.FINE, "Waiting" +ActiveGameState.getAiVelocity() + "seconds till next shot");
                sleep(ActiveGameState.getAiVelocity()*1000);
            } catch (InterruptedException e) {
                logMultiplayerControlThreadKiShootsEnemy.log(Level.SEVERE, "Thread interrupted by waiting");
            }

            logMultiplayerControlThreadKiShootsEnemy.log(Level.FINE, "Calling (own) KI shoot, Difficulty: " + ActiveGameState.getOwnKiDifficulty());
            ShotResponse shotResponse = ActiveGameState.getOwnKi().getShot(ActiveGameState.getEnemyPlayerOwnPlayground());


            //KI received an unhandled command
            if ( shotResponse.isUnhandled()){

                //close connection
                if ( ActiveGameState.isAmIServer()) { ActiveGameState.getServer().closeConnection(); }
                else { ActiveGameState.getClient().closeConnection(); }

                ActiveGameState.setRunning(false);

                if(shotResponse.getUnhandledCMD().equals("timeout")){
                    logMultiplayerControlThreadKiShootsEnemy.log(Level.WARNING, "Timeout appeared, closing connection");
                    HelpMethods.connectionLost();
                }
                else{
                    logMultiplayerControlThreadKiShootsEnemy.log(Level.WARNING, "UnexpectedMessage, closing connection");
                    System.out.println("Unexpected Message from Remote: " + shotResponse.getUnhandledCMD());
                    HelpMethods.unexceptedMessage();
                }
                break;
            }

            //The Ki lost the game, so the player won the game
            ShotResponse shotResponseFromOwnEnemyPlayground;
            if (shotResponse.isHit()) {
                //Player is allowed to shoot again
                ActiveGameState.setYourTurn(true);
                //Mark our enemy Playground
                if (shotResponse.isShipDestroyed()) {
                    shotResponseFromOwnEnemyPlayground = ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 2);
                    if ( shotResponseFromOwnEnemyPlayground.isGameWin() ){
                        logMultiplayerControlThreadKiShootsEnemy.log(Level.INFO, "Game against KI won");
                        HelpMethods.winOrLose(true);
                        ActiveGameState.setRunning(false);
                        break;
                    }

                }
                else {
                    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 1);
                }
                logMultiplayerControlThreadKiShootsEnemy.log(Level.FINE, "Turn: Own");
                HelpMethods.displayTurn(true);
            }
            //Our Turn expired
            else {
                ActiveGameState.setYourTurn(false);

                //Mark our enemy Playground
                ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),0);

                logMultiplayerControlThreadKiShootsEnemy.log(Level.FINE, "Turn: Remote");
                HelpMethods.displayTurn(false);
            }


        }
        if ( !ActiveGameState.isRunning()) return;

        //Send next to maintain the Ping-Pong methodology
        //We are the server
        if (ActiveGameState.isAmIServer()) {
            ActiveGameState.getServer().sendCMD(CMD.next, "");
        }
        //We are the client
        else {
            ActiveGameState.getClient().sendCMD(CMD.next, "");
        }

        MultiplayerControlThreadPerformEnemyAction multiplayerControlThreadPerformEnemyAction = new MultiplayerControlThreadPerformEnemyAction();
        multiplayerControlThreadPerformEnemyAction.start();

        //End While
    }
}
