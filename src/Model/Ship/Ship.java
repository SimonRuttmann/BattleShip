package Model.Ship;

import Model.Util.UtilDataType.Point;

import java.util.ArrayList;


public class Ship implements IShip {
    private int hitPoints;
    private final Point posStart;
    private final Point posEnd;
    private int size;

    private static final ArrayList<IShip> ShipList = new ArrayList<>();
    private static int amount  = 0;

    public Ship ( Point posStart, Point posEnd){
        this.posStart = posStart;
        this.posEnd = posEnd;
        getSizeOfShip (posStart, posEnd);
        this.hitPoints = this.size;
        ShipList.add(this);
        amount = ShipList.size();
    }

    /**
     * Calculates the size of the ship
     * @param posStart the start position of the ship
     * @param posEnd the end position of the ship
     */
    private void getSizeOfShip(Point posStart, Point posEnd){
        //Vertical placed
        if (posStart.getX() == posEnd.getX()){
            this.size = posEnd.getY() - posStart.getY() +1;
        }
        //Horizontal placed
        else{
            this.size = posEnd.getX() - posStart.getX() +1;
        }
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

}
