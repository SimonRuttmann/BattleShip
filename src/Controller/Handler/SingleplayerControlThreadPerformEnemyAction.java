package Controller.Handler;


import Controller.GamePlayground;
import Gui_View.HelpMethods;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread will:
 *
 *  As long as the enemy turn is present or the game isn't lost:
 *      1. Get the position, where the Ki wants to shoot
 *      2. Perform the shoot on the Players ownPlayground
 *      3. Perform the shoot on the Ki´s enemyPlayground
 *      4. Set YourTurn true if enemyTurn is expired
 *
 *  5. If it is the Player's turn again, set the water fields on the enemy playground clickable
 */
public class SingleplayerControlThreadPerformEnemyAction extends Thread{
    public static final Logger logSingleplayerControlThreadPerformEnemyAction = Logger.getLogger("parent.SingleplayerControlThreadPerformEnemyAction");

    @Override
    public void run(){
        logSingleplayerControlThreadPerformEnemyAction.log(Level.FINE, "Starting .SingleplayerControlPerformEnemyAction");
        while ( !ActiveGameState.isYourTurn()){

            logSingleplayerControlThreadPerformEnemyAction.log(Level.FINE, "Enemy Ai`s turn");

            //Waiting for the next KI shot
            try {
                logSingleplayerControlThreadPerformEnemyAction.log(Level.FINE, "Waiting" +ActiveGameState.getAiVelocity() + "seconds till next shot");
                sleep(ActiveGameState.getAiVelocity()*1000);
            } catch (InterruptedException e) {
                logSingleplayerControlThreadPerformEnemyAction.log(Level.SEVERE, "Thread interrupted by waiting");
            }

            //Let enemy KI shoot my own playground
            ShotResponse shotResponse = ActiveGameState.getEnemyKi().getShot(ActiveGameState.getOwnPlayerIOwnPlayground());

            logSingleplayerControlThreadPerformEnemyAction.log(Level.INFO, "Enemy AI shoots at: (" + shotResponse.getShotPosition().getX() +"|" + shotResponse.getShotPosition().getY() + ")"+ "\n"+
                    "\t Hit: " + shotResponse.isHit() + " \t Destroyed: " + shotResponse.isShipDestroyed());


            // The player lost
            if (shotResponse.isGameLost()){
                HelpMethods.winOrLose(false);
                ActiveGameState.setRunning(false);
                break;
            }

            //Mark it at the EnemyPlayground of the Ki, not necessary as the enemy players playground isn`t shown in the GUI
            //ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(posToShoot, answerFromPlayer );

            if ( shotResponse.isHit() ) {
                ActiveGameState.setYourTurn(false);
                HelpMethods.displayTurn(true);
            }
            //Enemy Turn expired
            else{
                ActiveGameState.setYourTurn(true);
                HelpMethods.displayTurn(false);
            }
        }

        //Player is allowed to click labels/buttons, because enemy`s turn expired
        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
        GamePlayground.setSaveAndCloseButtonClickable();
    }

}
