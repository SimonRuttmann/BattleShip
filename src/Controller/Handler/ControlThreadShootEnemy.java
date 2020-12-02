package Controller.Handler;

import Gui_View.HelpMethods;
import Model.Playground.IEnemyPlayground;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ControlThreadShootEnemy extends Thread{

    public Event event;
    public ControlThreadShootEnemy(Event event){
        this.event = event;
    }

    @Override
    public void run() {

        //This thread will:
        // 1. disable all Labels on enemy playground
        // 2. send the client/server an message containing all necessary information
        // 3. awaiting an answer from the client/server
        // 4. perform all necessary actions depending on the report from client/server

        //1.
        ActiveGameState.getEnemyPlayground().setAllLabelsNonClickable();

        //2. and 3.
        int xPos = GridPane.getColumnIndex((Label) event.getSource());
        int yPos = GridPane.getRowIndex((Label) event.getSource());
        Point shootPosition = new Point(xPos, yPos);

        //Preparing command which has to be send
        String cmdParameter = xPos + " " + yPos;

        //Report from the remoteSocket
        String[] cmdReceived;

        //We are the server
        if ( ActiveGameState.isAmIServer()){
            ActiveGameState.getServer().sendCMD(CMD.answer, cmdParameter);
            cmdReceived = ActiveGameState.getServer().getCMD();
        }
        //We are the client
        else{
            ActiveGameState.getClient().sendCMD(CMD.answer, cmdParameter);
            cmdReceived = ActiveGameState.getClient().getCMD();
        }


        //4 determine the response and act depending on it
        IEnemyPlayground enemyPlayground = ActiveGameState.getEnemyPlayground();
        switch (cmdReceived[0]){
            case "answer":
                ShotResponse shotResponse = enemyPlayground.shoot(shootPosition, Integer.parseInt(cmdReceived[1]));
                if ( shotResponse.isGameWin())
                {
                    HelpMethods.winOrlose(true);
                }

            case "Timeout":  //TODO Timeout ? Response?
                if ( ActiveGameState.isAmIServer()){
                    ActiveGameState.getServer().closeConnection();
                }
                else{
                    ActiveGameState.getClient().closeConnection();
                }
                //TODO POPUP für Timeout setzen
            default:
                System.out.println("Unexpected message from connection partner");
        }


        //If the answer was 1 or 2, we enable the Labels (player can click shoot again) and end the thread
        enemyPlayground.setAllWaterFieldsClickable();
        //TODO Display Your Turn

        //If the answer was 0, its the enemy´s turn, so we end the current thread and start the getShot thread
        if (cmdReceived[0].equals("answer") && Integer.parseInt(cmdReceived[1]) == 0){
            ControlThreadGetShot threadGetShot = new ControlThreadGetShot();
            threadGetShot.start();
        }
        //TODO Display Enemy Turn
    }
}
