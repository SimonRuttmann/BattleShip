package Model.Ship;

import Model.Playground.OwnPlayground;
import Model.Util.UtilDataType.Point;

import java.util.ArrayList;


public class Ship implements IShip {
    private int hitPoints;
    private final Point posStart;
    private final Point posEnd;
    private int size;

    //private static final ArrayList<IShip> ShipList = new ArrayList<>();
    //private static int amount  = 0;

    private ArrayList<Point> placementMarkers;

    public Ship ( Point posStart, Point posEnd, OwnPlayground ownPlayground){
        this.posStart = posStart;
        this.posEnd = posEnd;
        getSizeOfShip (posStart, posEnd);
        this.hitPoints = this.size;
        ownPlayground.getShipListOfThisPlayground().add(this);
        //ShipList.add(this);
        //amount = ShipList.size();
    }

    /**
     *
     * @return An Array of Points representing the Fields which got marked as not placeable
     */
    @Override
    public ArrayList<Point> getPlacementMarkers() {
        return placementMarkers;
    }

    /**
     *
     * @param placementMarkers An Array of Points representing the Fields which got marked as not placeable
     */
    @Override
    public void setPlacementMarkers(ArrayList<Point> placementMarkers) {
        this.placementMarkers = placementMarkers;
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

    /**
     * Calculates all points, where this ship is represented
     * @return Point Array with all coordiantes of the ship
     */
    @Override
    public Point[] getCoordinates(){
        Point[] coordinates = new Point[this.size];
        //vertical placed
        if ( this.posStart.getX() == this.posEnd.getX()){
            for ( int i = 0; i < this.size; i++){
                coordinates[i] = new Point(posStart.getX(), posStart.getY() + i);
            }
        }
        else{
            for ( int i = 0; i < this.size; i++){
                //coordinates[i] = new Point(posStart.getY(), posStart.getX() + i);
                coordinates[i] = new Point(posStart.getX()+i, posStart.getY());
            }
        }
        return coordinates;
    }

    //public static int getAmount(){
    //    return amount;
    //}

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
    //public static ArrayList<IShip> getShipList() {
    //    return ShipList;
    //}

    @Override
    public Point[] getPosition(){
        return new Point[]{this.posStart, this.posEnd};
    }

}
