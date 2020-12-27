package Model.Util.UtilDataType;

import javafx.scene.control.Label;

import java.util.ArrayList;

public class ShotResponse {
    private boolean gameLost;
    private boolean hit;
    private boolean shipDestroyed;

    private boolean gameWin;
    private ArrayList<Point> impossiblePositions;

    //Used by Ki, when communicating with remote
    public ShotResponse(){
    };

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


    private Label label;
    private boolean horizontal;

    public Label getLabel() {
        return label;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public void setShipDestroyed(boolean shipDestroyed) {
        this.shipDestroyed = shipDestroyed;
    }

    public boolean getAlignment() {
        return horizontal;
    }

    public int getSizeOfSunkenShip() {
        return sizeOfSunkenShip;
    }

    private int sizeOfSunkenShip;

    //label = shotResponse.getLabel();
    //horizontal = shotResponse.getAlignment();
    //size = shotResponse.getSize();
    //Constructor, when we hit the enemy´s Playground and we sunk a ship
    public ShotResponse (boolean gameWin, ArrayList<Point> impossiblePositions, Label label, boolean horizontal, int sizeOfSunkenShip){
        this.gameWin = gameWin;
        this.impossiblePositions = impossiblePositions;
        this.label = label;
        this.horizontal = horizontal;
        this.sizeOfSunkenShip = sizeOfSunkenShip;
    }

    public boolean isGameWin() {
        return gameWin;
    }

    public ArrayList<Point> getImpossiblePositions() {
        return impossiblePositions;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isShipDestroyed() {
        return shipDestroyed;
    }

    private Point shotPosition;

    public Point getShotPosition() {
        return shotPosition;
    }

    public void setShotPosition(Point shotPosition) {
        this.shotPosition = shotPosition;
    }
}
