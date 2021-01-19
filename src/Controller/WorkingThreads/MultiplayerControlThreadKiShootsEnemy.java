package Controller.WorkingThreads;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import GameData.ActiveGameState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  This thread will:
 *      As long as our KI`s turn is active:
 *          1. Wait for the set time (selected by Game Options -> AI Velocity)
 *          2. Call the KI`s shoot method, which calculates a point and shoots
 *             The KI sends the command "shoot x y" and receives an answer
 *             If the answer from remote is not "answer 0|1|2", the KI can`t handle it
 *
 *          3.a If the Ki didn`t handle remote`s answer, the sockets are closed and the right popup is shown
 *          3.b In any other case we mark our enemy playground with the shot result
 *       When our turn expired (no hit) the "next" cmd is send to remote
 *       and the MultiplayerControlThreadPerformEnemyAction Thread gets started
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
