package Model.Playground;

import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

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
     * @param StartPoint The start point of the ship, which needs to be checked
     * @param EndPoint  The end point of the ship, which needs to be checked
     * @return True if the placement is valid. In any other case false
     */
    boolean isShipPlacementValid(Point StartPoint, Point EndPoint);

}
