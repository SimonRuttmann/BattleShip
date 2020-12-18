package Controller.Handler;


import Player.ActiveGameState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GameShootEnemy implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {

        // In both cases, the player acts, not the Ki (the Ki can´t because it can't perform a MouseEvent)
        if (ActiveGameState.isMultiplayer()) {

            //Multiplayer Mode

            // todo: all labels must be disabled/enabled when game starts? - is this implemented?

            //This thread will:
            // 1. disable all Labels on enemy playground //todo indicate by mouse pointer? -> not clickable, todo reverse when enemy shot happended
            // 2. send the client/server an message containing all necessary information
            // 3. awaiting an answer from the client/server
            // 4. perform all necessary actions depending on the report from client/server
            // 5. Start the MultiplayerControlThreadPerformEnemyAction Thread

            //TODO Yannick DISPLAY NO TURN
            MultiplayerControlThreadShootEnemy multiplayerControlThreadShootEnemy = new MultiplayerControlThreadShootEnemy(event);
            multiplayerControlThreadShootEnemy.start();
        }
        else{

            //Singleplayer Mode, the player plays against an KI, we are already Ingame..
            SingleplayerControlThreadShootEnemy singleplayerControlThreadShootEnemy = new SingleplayerControlThreadShootEnemy(event);
            singleplayerControlThreadShootEnemy.start();
        }
    }
}
