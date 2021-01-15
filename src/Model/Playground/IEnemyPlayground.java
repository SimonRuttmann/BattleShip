package Model.Playground;

import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

public interface IEnemyPlayground extends IPlayground{
    /**
     * Call this method on the enemy playground when you shot the enemy and you know, what you hit
     *
     * Draws the shot-position in the playground depending on the answer parameter
     * Also marks all impossible positions as ShotWater, when a ship is sunken
     *
     * If the ship is sunken, the ShotResponse contains following information:
     *      1. The information if the game is won
     *      2. An Point-Array of impossible positions
     *      3. The label of the topLeft position, where the sunken ship was placed
     *      4. The information if the ship was placed horizontal or vertical
     *      5. The size of the Ship
     *
     * In any other case the return value can be ignored
     *
     *
     * @param pos_shot The position where the player is shooting the enemy playground
     * @param answer The answer from the connection partner
     *               0: shipHit false, shipSunken false
     *               1: shipHit true, shipSunken false
     *               2: shipHit true, shipSunken true
     *
     * @return An object of type ShotResponse, the object contains all necessary information
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
