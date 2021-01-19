package Model.Playground;

import Model.Ship.IShip;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

import java.util.ArrayList;

public interface IOwnPlayground extends IPlayground{
    /**
     * Use this method when the enemy shoots at our playground
     * Manages all necessary actions, when and shot occurs on our playground
     *
     * @param pos_shot The position, where the enemy wants to hit our playground
     * @return  Returns an ShotResponseObject containing three booleans:
     * gameLost:        True if the game is over, because we lost all our ships
     * hit:             True one of our ships got hit
     * shipDestroyed:   True, if on of our ships got hit and got destroyed
     */
    ShotResponse shoot(Point pos_shot);

    /**
     * Checks if the ship represented by these two points is valid
     * if the ship placement is valid the method creates the ship and adds it to the shipList itself
     *
     * @param startPoint The start point of the ship, which needs to be checked
     * @param endPoint  The end point of the ship, which needs to be checked
     * @return The ship if the placement is valid. In any other case null
     */
    IShip isShipPlacementValid(Point startPoint, Point endPoint);

    /**
     * Use this method, when the player wants to switch the position of his ship on the selection
     * @param shipToMove The current ship the player wants to move to another position
     * @param newStartPoint The startPosition, where the player wants to move the ship
     * @param newEndpoint The endPosition, where the player wants to move the ship
     * @return true, if the ship movement was correct, false if the movement is not allowed
     */
    boolean moveShip(IShip shipToMove, Point newStartPoint, Point newEndpoint);


    /**
     * This method only checks whether the ship (represented by start and endpoint) can be placed or not
     * In both cases no ship is created
     *
     * @param startPoint The start point of the ship
     * @param endPoint  The end point of the ship
     * @return true, if placement is allowed
     */
    boolean isValidPlacement(Point startPoint, Point endPoint);


    ArrayList<IShip> getShipListOfThisPlayground();

    void setShipListOfThisPlayground(ArrayList<IShip> shipListOfThisPlayground);


}
