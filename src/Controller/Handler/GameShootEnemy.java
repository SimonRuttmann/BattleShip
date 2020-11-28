package Controller.Handler;

import Model.Util.UtilDataType.Point;
import Player.Savegame;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameShootEnemy implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        Thread shootEnemy = new Thread(new Runnable() {
            @Override
            public void run() {
                int xPos = GridPane.getColumnIndex((Label) event.getSource()) + 1;
                int yPos = GridPane.getRowIndex((Label) event.getSource()) + 1;
                // todo Savegame has to be implemented
                //Savegame.getEnemyPlayground.shoot(new Point(xPos, yPos), getCommand);
            }
        });
    }
}
