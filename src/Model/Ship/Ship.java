package Model.Ship;

import Model.Util.UtilDataType.Point;

import java.util.ArrayList;


public class Ship implements IShip {

    private int hitPoints;
    private Point posStart;
    private Point posEnd;
    private final int size;

    private static final ArrayList<IShip> ShipList = new ArrayList<>();
    private static int amount  = 0;

    public Ship ( Point posStart, Point posEnd, int size){
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.size = size;
        this.hitPoints = size;
        ShipList.add(this);
        amount = amount+1;
    }


    public static int getAmount(){
        return amount;
    }

    @Override
    public int gethitPoints() {
        return hitPoints;
    }

    @Override
    public void sethitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    @Override
    public int getSize() {
        return size;
    }

    //Is called by IShip
    public static ArrayList<IShip> getShipList() {
        return ShipList;
    }



    @Override
    public Point[] getPosition(){
        return new Point[]{this.posStart, this.posEnd};
    }

    public Point getPosStart() { return this.posStart; }

    public Point getPosEnd() { return this.posEnd; }

    @Override
    public void setPosStart() {
        this.posStart = posStart;
    }

    @Override
    public void setPosEnd() {
        this.posEnd = posEnd;
    }
}
