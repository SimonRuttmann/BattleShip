package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread will:
 * Labels werden nicht gesetzt
 * Solange bis das Spiel vorbei ist:
 * 1. Unsere Ki schießt
 * wenn getroffen -> nochmal
 * <p>
 * 2. Gegnerische Ki schießt
 * wenn getroffen -> nochmal
 */
public class SingleplayerControlThreadKiVsKi extends Thread {
    public static final Logger logSingleplayerControlThreadKiVsKi = Logger.getLogger("parent.SingleplayerControlThreadKiVsKi");

    @Override
    public void run() {
        logSingleplayerControlThreadKiVsKi.log(Level.FINE, "Starting .SingleplayerControlThreadKiVsKi");
        while (ActiveGameState.isRunning()) {

            //Drawing the playground, currently not necessary
            //ActiveGameState.getEnemyPlayerOwnPlayground().drawPlayground();

            //Own KI turn
            while (ActiveGameState.isYourTurn() && ActiveGameState.isRunning()) {

                logSingleplayerControlThreadKiVsKi.log(Level.FINE, "Own Ai`s turn");
               try {
                    logSingleplayerControlThreadKiVsKi.log(Level.FINE, "Waiting" +ActiveGameState.getAiVelocity() + "seconds till next shot");
                    sleep(ActiveGameState.getAiVelocity()*1000);
                } catch (InterruptedException e) {
                    logSingleplayerControlThreadKiVsKi.log(Level.SEVERE, "Thread interrupted by waiting");
                }


                //Our KI shoots at the enemy playground
                ShotResponse shotResponse = ActiveGameState.getOwnKi().getShot(ActiveGameState.getEnemyPlayerOwnPlayground());

                logSingleplayerControlThreadKiVsKi.log(Level.INFO, "Own AI shoots at: (" + shotResponse.getShotPosition().getX() +"|" + shotResponse.getShotPosition().getY() + ")"+ "\n"+
                                                                            "\t Hit: " + shotResponse.isHit() + " \t Destroyed: " + shotResponse.isShipDestroyed());

                //The Ki lost the game, so the player won the game
                if (shotResponse.isGameLost()) {
                    logSingleplayerControlThreadKiVsKi.log(Level.INFO, "Our Ai won against enemy AI");
                    //Mark our enemy playground
                    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),2);
                    HelpMethods.winOrLose(true);
                    ActiveGameState.setRunning(false);
                    break;
                }
                if (shotResponse.isHit()) {
                    //Player is allowed to shoot again
                    ActiveGameState.setYourTurn(true);

                    //Mark our enemy Playground
                    if (shotResponse.isShipDestroyed()) {
                        ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 2);
                    }
                    else {
                        ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 1);
                    }
                    HelpMethods.displayTurn(true);
                }
                //Own Turn expired
                else {
                    ActiveGameState.setYourTurn(false);

                    //Mark our enemy Playground
                    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),0);

                    HelpMethods.displayTurn(false);
                }

            }
            //Own KI turn end

            //Enemy KI turn
            while (!ActiveGameState.isYourTurn() && ActiveGameState.isRunning()) {

                logSingleplayerControlThreadKiVsKi.log(Level.FINE, "Enemy Ai`s turn");

                try {
                    logSingleplayerControlThreadKiVsKi.log(Level.FINE, "Waiting" +ActiveGameState.getAiVelocity() + "seconds till next shot");
                    sleep(ActiveGameState.getAiVelocity()*1000);
                } catch (InterruptedException e) {
                    logSingleplayerControlThreadKiVsKi.log(Level.SEVERE, "Thread interrupted by waiting");
                }

                //Enemy Ki is shooting at our playground
                ShotResponse shotResponse = ActiveGameState.getEnemyKi().getShot(ActiveGameState.getOwnPlayerIOwnPlayground());

                logSingleplayerControlThreadKiVsKi.log(Level.INFO, "Enemy AI shoots at: (" + shotResponse.getShotPosition().getX() +"|" + shotResponse.getShotPosition().getY() + ")"+ "\n"+
                        "\t Hit: " + shotResponse.isHit() + " \t Destroyed: " + shotResponse.isShipDestroyed());


                // The player lost
                if (shotResponse.isGameLost()) {
                    logSingleplayerControlThreadKiVsKi.log(Level.INFO, "Our Ai lost against enemy AI");
                    HelpMethods.winOrLose(false);
                    ActiveGameState.setRunning(false);
                    break;
                }

                if (shotResponse.isHit()) {
                    ActiveGameState.setYourTurn(false);
                    HelpMethods.displayTurn(false);
                }
                //Enemy Turn expired
                else {
                    ActiveGameState.setYourTurn(true);
                    HelpMethods.displayTurn(true);
                }
            }
            //Enemy KI turn end

        }
        //Game finished

    }
}

