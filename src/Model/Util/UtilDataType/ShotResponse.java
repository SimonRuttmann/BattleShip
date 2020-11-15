package Model.Util.UtilDataType;

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
