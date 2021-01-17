package Controller.Handler;


import Player.ActiveGameState;
import Player.GameMode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GameShootEnemy implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {

        //We don't need to make label´s disabled in KI modes, when we return instantly
        if ( ActiveGameState.getModes() == GameMode.kiVsRemote || ActiveGameState.getModes() == GameMode.kiVsKi) return;

        // In both cases, the player acts, not the Ki (the Ki can´t because it can't perform a MouseEvent)
        if (ActiveGameState.isMultiplayer()) {
            if ( !ActiveGameState.isYourTurn()) ActiveGameState.getOwnPlayerIEnemyPlayground().setAllLabelsNonClickable();

            //Multiplayer Mode


            //This thread will:
            // 1. disable all Labels on enemy playground
            // 2. send the client/server an message containing all necessary information
            // 3. awaiting an answer from the client/server
            // 4. perform all necessary actions depending on the report from client/server
            // 5. Start the MultiplayerControlThreadPerformEnemyAction Thread


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
