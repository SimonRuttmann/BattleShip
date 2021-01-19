package Model.Ship;

import Model.Playground.IOwnPlayground;
import Model.Util.UtilDataType.Point;
import java.util.ArrayList;


public class Ship implements IShip {

    private int hitPoints;
    private final Point posStart;
    private final Point posEnd;
    private int size;

    //Points, which got marked as not valid for placement, due to this ship
    private ArrayList<Point> placementMarkers;

    public Ship ( Point posStart, Point posEnd, IOwnPlayground ownPlayground){

        //If the position got switched accidentally, switch them
        //Vertical
        if ( posStart.getX() == posEnd.getX()){
            if ( posStart.getY() > posEnd.getY()){
                Point temp;
                temp = posEnd;
                posEnd = posStart;
                posStart = temp;
            }
        }
        //Horizontal
        if ( posStart.getY() == posEnd.getY()){
            if(posStart.getX() > posEnd.getX()){
                Point temp;
                temp = posEnd;
                posEnd = posStart;
                posStart = temp;
            }
        }

        this.posStart = posStart;
        this.posEnd = posEnd;
        getSizeOfShip (posStart, posEnd);
        this.hitPoints = this.size;
        ownPlayground.getShipListOfThisPlayground().add(this);

    }

    /**
     * Get points which have been marked as not valid for placement, due to this ship
     * @return An Array of Points representing the Fields which got marked as not placeable
     */
    @Override
    public ArrayList<Point> getPlacementMarkers() {
        return placementMarkers;
    }

    /**
     * Set points which have been marked as not valid for placement, due to this ship
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
     * @return Point Array with all coordinates of the ship
     */
    @Override
    public Point[] getCoordinates(){
        Point[] coordinates = new Point[this.size];
        //vertical placed
        if (this.posStart.getX() == this.posEnd.getX()){
            for ( int i = 0; i < this.size; i++){
                coordinates[i] = new Point(posStart.getX(), posStart.getY() + i);
            }
        }
        else{
            for ( int i = 0; i < this.size; i++){

                coordinates[i] = new Point(posStart.getX()+i, posStart.getY());

            }
        }
        return coordinates;
    }

    //Getters

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

    @Override
    public Point[] getPosition(){
        return new Point[]{this.posStart, this.posEnd};
    }

    @Override
    public Point getPosStart() {
        return posStart;
    }

    @Override
    public Point getPosEnd() {
        return posEnd;
    }

}
