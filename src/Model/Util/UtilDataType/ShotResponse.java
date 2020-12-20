package Model.Util.UtilDataType;

import javafx.scene.control.Label;

import java.util.ArrayList;

public class ShotResponse {
    private boolean gameLost;
    private boolean hit;
    private boolean shipDestroyed;

    private boolean gameWin;
    private ArrayList<Point> impossiblePositions;

    //Constructor, when our Playground got hit
    public ShotResponse(boolean gameLost, boolean hit, boolean shipDestroyed) {
        this.gameLost = gameLost;
        this.hit = hit;
        this.shipDestroyed = shipDestroyed;
    }

    //Constructor, when we hit the enemy´s Playground
    public ShotResponse(boolean gameWin, ArrayList<Point> impossiblePositions) {
        this.gameWin = gameWin;
        this.impossiblePositions = impossiblePositions;
    }

    //Constructor, when we hit the enemy´s Playground and we sunk a ship


    private Label label;
    private boolean horizontal;

    public Label getLabel() {
        return label;
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

}
