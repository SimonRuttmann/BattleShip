package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;

public class MultiplayerControlThreadKiShootsEnemy extends Thread{

    @Override
    public void run(){
        while (ActiveGameState.isYourTurn() && ActiveGameState.isRunning()){
            try {
                sleep(ActiveGameState.getAiVelocity()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(ActiveGameState.getOwnKi());

            ShotResponse shotResponse = ActiveGameState.getOwnKi().getShot(ActiveGameState.getEnemyPlayerOwnPlayground());



            //Timeout oder anderer Fehler
            if ( shotResponse.isUnhandled()){

                //Sockets schließen
                if ( ActiveGameState.isAmIServer()) { ActiveGameState.getServer().closeConnection(); }
                else { ActiveGameState.getClient().closeConnection(); }

                ActiveGameState.setRunning(false);

                if(shotResponse.getUnhandledCMD().equals("timeout")){ HelpMethods.connectionLost(); }
                else{
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
                        HelpMethods.winOrLose(true);
                        ActiveGameState.setRunning(false);
                        break;
                    }

                }
                else {
                    ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(), 1);
                }
                //TODO Yannick diplay Player(KI)'s Turn
            }
            //Our Turn expired
            else {
                ActiveGameState.setYourTurn(false);
                //Mark our enemy Playground
                ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shotResponse.getShotPosition(),0);
                //TODO Yannick display Enemy(KI)´s Turn
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
