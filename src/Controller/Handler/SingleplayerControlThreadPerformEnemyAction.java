package Controller.Handler;


import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;

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

    @Override
    public void run(){

        while ( !ActiveGameState.isYourTurn()){
            //Get the position, where the enemy wants to shoot
            Point posToShoot = null;
            posToShoot = ActiveGameState.getKi().getShot(ActiveGameState.getOwnPlayerIOwnPlayground());
            //Shoot the Players ownPlayground
            ShotResponse shotResponse = ActiveGameState.getEnemyPlayerOwnPlayground().shoot(posToShoot);

            // The player lost
            if (shotResponse.isGameLost()){
                HelpMethods.winOrLose(false);
                ActiveGameState.setRunning(false);
            }

            int answerFromPlayer;
            if (shotResponse.isShipDestroyed()){
                answerFromPlayer = 2;
            }
            else if (shotResponse.isHit()){
                answerFromPlayer = 1;
            }
            else {
                answerFromPlayer = 0;
            }

            //Mark it at the EnemyPlayground of the Ki
            ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(posToShoot, answerFromPlayer );

            if ( answerFromPlayer == 1 || answerFromPlayer == 2 ) {
                ActiveGameState.setYourTurn(false);
                //TODO Yannick diplay Player's Turn
            }
            //Enemy Turn expired
            else{
                ActiveGameState.setYourTurn(true);
                //TODO Yannick display Enemy´s Turn
            }
        }

        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
    }

}
