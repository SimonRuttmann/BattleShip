package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SingleplayerControlThreadKiVsKi extends Thread {
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

    @Override
    public void run() {
        while (ActiveGameState.isRunning()) {
            ActiveGameState.getEnemyPlayerOwnPlayground().drawPlayground();
            //Own KI turn
            while (ActiveGameState.isYourTurn()) {

                System.out.println("Our Turn");
               try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //unsere Ki beschießt gegnerisches Spielfeld
                ShotResponse shotResponse = ActiveGameState.getOwnKi().getShot(ActiveGameState.getEnemyPlayerOwnPlayground());

                System.out.println( "Eigene Ki schießt auf: " +shotResponse.getShotPosition() + "  Getroffen: " + shotResponse.isHit() + "  Zerstört: "+  shotResponse.isShipDestroyed());
                System.out.println("Shoots at: " + shotResponse.getShotPosition().getX() + " " +shotResponse.getShotPosition().getY());
                //mark the Own KI´s enemyPlayground

                //The Ki lost the game, so the player won the game
                if (shotResponse.isGameLost()) {
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
                        //TODO Yannick diplay Player(KI)'s Turn
                }
                //Enemy Turn expired
                else {
                    ActiveGameState.setYourTurn(false);
                    //Mark our enemy Playground
                    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),0);
                    //TODO Yannick display Enemy(KI)´s Turn
                }

            }
            //Own KI turn end

            //Enemy KI turn
            while (!ActiveGameState.isYourTurn()) {

                System.out.println("Enemy Turn");
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //gegnerische Ki beschießt mein Spielfeld
                ShotResponse shotResponse = ActiveGameState.getEnemyKi().getShot(ActiveGameState.getOwnPlayerIOwnPlayground());
                System.out.println("Shoots at: " + shotResponse.getShotPosition().getX() + " " +shotResponse.getShotPosition().getY());
                // The player lost
                if (shotResponse.isGameLost()) {
                    HelpMethods.winOrLose(false);
                    ActiveGameState.setRunning(false);
                    break;
                }

                //Mark it at the EnemyPlayground of the Ki
               // ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(shotResponse.getShotPosition(), 2 );

                if (shotResponse.isHit()) {
                    ActiveGameState.setYourTurn(false);
                    //TODO Yannick diplay Enemy(KI)'s Turn
                    //Mark our enemy Playground
                    if (shotResponse.isShipDestroyed()) {
                       // ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(shotResponse.getShotPosition(), 2 );
                       // ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 2);
                    }
                    else {
                     //   ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(shotResponse.getShotPosition(), 1 );
                    //    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 1);
                    }



                }
                //Enemy Turn expired
                else {
                    ActiveGameState.setYourTurn(true);
                  //  ActiveGameState.getEnemyPlayerEnemyPlayground().shoot(shotResponse.getShotPosition(), 2 );
                  //  ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),0);
                    //TODO Yannick display Players(KI)´s Turn
                }
            }
            //Enemy KI turn end

        }
        //Game finished

    }
}

