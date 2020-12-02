package Model.Playground;

import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

public interface IEnemyPlayground {
    /**
     * Draws the shot-position in the playground depending on the shipHit, shipSunken parameters
     * Also marks all impossible positions as ShotWater, when a ship is sunken
     *
     * If the ship is sunken, the ShotResponse contains an Point-Array of impossible positions and the information if the game is won
     * In any other case the return value can be ignored
     *
     *
     * @param pos_shot The position where the player is shooting the enemy playground
     * @param answer The answer from the connection partner
     *               0: shipHit false, shipSunken false
     *               1: shipHit true, shipSunken false
     *               2: shipHit true, shipSunken true
     * shipHit If a ship on enemy playground is hit, this variable is set to true
     * shipSunken If a ship on enemy playground is sunken, this variable is set to true
     *
     * @return An object of type ShotResponse, the object contains all information
     *
     *
     */
    ShotResponse shoot(Point pos_shot, int answer); //Muss sich Schiffe gegnerische Schiffe merken können um unmögliche Felder als Wasser zu markieren, soweit sie noch nicht beschossenes Wasser sind

    /**
     * Sets all labels of the enemy playground non clickable
     * Use this method, after the player clicked on an Label, so that he cant accidentally try to shoot 2 times at once
     */
    void setAllLabelsNonClickable();

    /**
     * Sets the disabled status to all labels of the fields which should be able to get clicked (which are only water) on true
     */
    void setAllWaterFieldsClickable();

}
