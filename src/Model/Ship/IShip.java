package Model.Ship;

import Model.Util.UtilDataType.Point;

import java.util.ArrayList;


public interface IShip {

    /**
     * Every ship got a number of hit-points, which is initialized with the size of the ship
     * @return The number of the remaining hit-points
     */
    int gethitPoints();

    /**
     * Every ship got a number of hit-points, which is initialized with the size of the ship
     * @param hitPoints Set the number of the hit-points left on the selected ship
     */
    void sethitPoints(int hitPoints);

    /**
     * The position of a ship is represented by a start-point and a end-point
     * @return The start- and endpoint of the ship
     */
    Point[] getPosition();

    /**
     * Gets the size of the selected ship, valid sizes are 2, 3, 4 and 5
     * @return The size of the ship
     */
    int getSize();
/*
    /**
     * Every ship constructed is saved in an Ship-List
     * Returns the size of the Ship-List
     * @return The amount of ships in the Ship-List
     */
//    static int getAmount() {
//        return Ship.getAmount();
//    }

/*
    /**
     * All constructed ships, which are valid are added in the ship-List
     * @return A ArrayList of placed ships
     */
//    static ArrayList<IShip> getShipList(){
//         return Ship.getShipList();
//     }

    /**
     * Calculates all points, where this ship is represented
     * @return Point Array with all coordiantes of the ship
     */
    Point[] getCoordinates();



    /**
     *
     * @return An Array of Points representing the Fields which got marked as not placeable
     */
     ArrayList<Point> getPlacementMarkers();

    /**
     *
     * @param placementMarkers An Array of Points representing the Fields which got marked as not placeable
     */
    void setPlacementMarkers(ArrayList<Point> placementMarkers);
}
