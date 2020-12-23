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
            //Point posToShoot = null;
            System.out.println( "Enemy Turn");

            //ich übergebe der KI mein eigenes spielfeld, die KI beschießt damit mein eigenes Spielfeld
            ShotResponse shotResponse = ActiveGameState.getKi().getShot(ActiveGameState.getOwnPlayerIOwnPlayground());

            //System.out.println( "Enemy is shooting at " + posToShoot.getX() + posToShoot.getY());


            //Shoot the Players ownPlayground
            //Ich lasse die Ki mein eigenes spielfeld beschießen, Problem ist, das die KI schon mein eigenes Spielfeld beschießt
            //ShotResponse shotResponse = ActiveGameState.getOwnPlayerIOwnPlayground().shoot(posToShoot);

            // The player lost
            if (shotResponse.isGameLost()){
                HelpMethods.winOrLose(false);
                ActiveGameState.setRunning(false);
                break;
            }

            //Mark it at the EnemyPlayground of the Ki
           //ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(posToShoot, answerFromPlayer );

            if ( shotResponse.isHit() ) {
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
