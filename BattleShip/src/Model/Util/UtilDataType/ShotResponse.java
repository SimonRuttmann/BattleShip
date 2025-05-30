package Model.Util.UtilDataType;

import javafx.scene.control.Label;
import java.util.ArrayList;

/**
 * Objects of this class contain all necessary information, when shoot`s are made
 * The information, an ShotResponse object can have strongly depends on the use case
 */
public class ShotResponse {

    //Information, the object can contain, when we call the shoot method of our own playground
    private boolean gameLost;
    private boolean hit;
    private boolean shipDestroyed;

    //Information, the object can contain additionally, when the ki shoots our own playground
    //Information, used by KI shoot call, when the shot can`t be handled there cause of invalid answer from remote
    private String unhandledCMD;
    private boolean unhandled;
    private Point shotPosition;

    //Information, the object can contain, when we call the shoot method of our enemy playground
    private boolean gameWin;
    private ArrayList<Point> impossiblePositions;
    private Label label;
    private boolean horizontal;
    private int sizeOfSunkenShip;


    //Constructors, used by Ki when communicating with remote
    public ShotResponse(){
    }

    public ShotResponse (boolean unhandled, String unhandledCMD){
        this.unhandled = unhandled;
        this.unhandledCMD = unhandledCMD;
    }

    //Constructor, when our Playground got hit
    public ShotResponse(boolean gameLost, boolean hit, boolean shipDestroyed) {
        this.gameLost = gameLost;
        this.hit = hit;
        this.shipDestroyed = shipDestroyed;
    }

    //Constructor, when we hit the enemy´s Playground and a ship didn't sink
    public ShotResponse(boolean gameWin) {
        this.gameWin = gameWin;
    }

    //Constructor, when we hit the enemy´s Playground and we sunk a ship
    public ShotResponse (boolean gameWin, ArrayList<Point> impossiblePositions, Label label, boolean horizontal, int sizeOfSunkenShip){
        this.gameWin = gameWin;
        this.impossiblePositions = impossiblePositions;
        this.label = label;
        this.horizontal = horizontal;
        this.sizeOfSunkenShip = sizeOfSunkenShip;
    }

    //Getter
    public String getUnhandledCMD() { return unhandledCMD; }
    public boolean isUnhandled() { return unhandled; }
    public Label getLabel() { return label; }
    public boolean getAlignment() { return horizontal; }
    public int getSizeOfSunkenShip() { return sizeOfSunkenShip; }
    public boolean isGameWin() { return gameWin; }
    public ArrayList<Point> getImpossiblePositions() { return impossiblePositions; }
    public boolean isGameLost() { return gameLost; }
    public boolean isHit() { return hit; }
    public boolean isShipDestroyed() { return shipDestroyed; }
    public Point getShotPosition() { return shotPosition; }

    //Setter
    public void setHit(boolean hit) { this.hit = hit; }
    public void setShipDestroyed(boolean shipDestroyed) { this.shipDestroyed = shipDestroyed; }
    public void setShotPosition(Point shotPosition) { this.shotPosition = shotPosition; }
}
