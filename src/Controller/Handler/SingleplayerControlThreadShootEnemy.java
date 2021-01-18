package Controller.Handler;

import Controller.GamePlayground;
import Gui_View.HelpMethods;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *  This thread will:
 *          1. disable all Labels on enemy playground
 *          2. get the position of the clicked label
 *          3. call the method, to shoot the enemy´s playground. For the Ki the shot playground is his ownPlayground
 *          4. call the method, to shoot the Players enemyPlayground
 *          5. a) If the player hit an ship, or sunk it, he is allowed to shoot again, so the Labels are enabled
 *          5. b) Enemy turn started, starting the SingleplayerControlThreadPerformEnemyAction Thread
 */
public class SingleplayerControlThreadShootEnemy extends Thread{

    public static final Logger logSingleplayerControlThreadShootEnemy = Logger.getLogger("parent.SingleplayerControlThreadShootEnemy");
    public Event event;
    public SingleplayerControlThreadShootEnemy(Event event){
        this.event = event;
    }


    @Override
    public void run(){
        //1.
        logSingleplayerControlThreadShootEnemy.log(Level.FINE, "Starting Singleplayer Control Thread Shoot Enemy");

        ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();
        GamePlayground.setSaveAndCloseButtonNonClickable();

        //2.
        int xPos = GridPane.getColumnIndex((Label) event.getSource());
        int yPos = GridPane.getRowIndex((Label) event.getSource());
        Point shootPosition = new Point(xPos, yPos);


        //3. We are now in the Enemy´s sight of view, enemy KI shoots at our playground
        ShotResponse shotResponse = ActiveGameState.getEnemyPlayerOwnPlayground().shoot(shootPosition);

        logSingleplayerControlThreadShootEnemy.log(Level.INFO, "Player shoots at: (" + xPos +"|" + yPos + ")"+ "\n"+
                "\t Hit: " + shotResponse.isHit() + " \t Destroyed: " + shotResponse.isShipDestroyed());


        //Ki lost the game, so the player won the game
        if(shotResponse.isGameLost()){
            logSingleplayerControlThreadShootEnemy.log(Level.INFO, "Player won against AI");
            ActiveGameState.setRunning(false);
            HelpMethods.winOrLose(true);
        }

        int answerFromKi;
        if ( shotResponse.isShipDestroyed()){
            answerFromKi = 2;
        }
        else if(shotResponse.isHit()){
            answerFromKi = 1;
        }
        else{
            answerFromKi = 0;
        }

        //4 ! We are now on the Players sight of view, and received the answerFromKi !
        ActiveGameState.getOwnPlayerIEnemyPlayground().shoot(shootPosition, answerFromKi);

        //5 a) If the player hit an ship, or sunk it, he is allowed to shoot again
        if (answerFromKi == 1 || answerFromKi == 2){
            ActiveGameState.setYourTurn(true);
            HelpMethods.displayTurn(true);

            ActiveGameState.getOwnPlayerIEnemyPlayground().setAllWaterFieldsClickable();
            GamePlayground.setSaveAndCloseButtonClickable();
        }
        // 5 b) Enemy turn started
        else{
            ActiveGameState.setYourTurn(false);
            HelpMethods.displayTurn(false);

            SingleplayerControlThreadPerformEnemyAction singleplayerControlThreadPerformEnemyAction = new SingleplayerControlThreadPerformEnemyAction();
            singleplayerControlThreadPerformEnemyAction.start();
        }

    }
}
