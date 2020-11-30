package Controller.Handler;

import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;
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
                //TODO WHILE TRUE (OR IS RUNNING) mit gegner beschiessen beschossen, abbruch mit den Befehlen speichern, abbruch, win/lose oder timeout <- Gehört woanders hin
                //Reaction es wird am ende eine methode aufgerufen, die REAKTION heist, welche auf den behehl wartet getCMD()
                int xPos = GridPane.getColumnIndex((Label) event.getSource()) + 1;
                int yPos = GridPane.getRowIndex((Label) event.getSource()) + 1;

                //TODO SEND CMD

                //TODO andere Befehle abchecken (hier nur abbruch und save)
                String[] cmd = ActiveGameState.getServer().getCMD();
                int param = Integer.parseInt(cmd[1]);


                //Kein Treffer!
                boolean shipHit = false;
                boolean shipSunk = false;


                //Schiff getroffen
                if ( param == 1){
                    shipHit = true;
                    shipSunk = false;
                }

                //Schiff versenkt!
                if ( param == 2){
                    shipHit = true;
                    shipSunk = true;
                }
                ActiveGameState.getEnemyPlayground().shoot(new Point(xPos, yPos), shipHit, shipSunk);
                //GameGetShot();
            }
        });
    }
}
