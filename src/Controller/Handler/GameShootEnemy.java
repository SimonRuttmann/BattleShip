package Controller.Handler;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class GameShootEnemy implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {

        //This thread will:
        // 1. disable all Labels on enemy playground
        // 2. send the client/server an message containing all necessary information
        // 3. awaiting an answer from the client/server
        // 4. perform all necessary actions depending on the report from client/server

        //TODO DISPLAY NO TURN
        ControlThreadShootEnemy controlThreadShootEnemy = new ControlThreadShootEnemy(event);
        controlThreadShootEnemy.start();

    }
}
