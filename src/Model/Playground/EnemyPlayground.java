package Model.Playground;

import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;
import GameData.ActiveGameState;
import GameData.GameMode;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EnemyPlayground extends AbstractPlayground implements IEnemyPlayground {
    public static final Logger logEnemyPlayground = Logger.getLogger("parent.EnemyPlayground");
    private int counterShipDestroyed = 0;

    public EnemyPlayground() {
        super();
    }

    /**
     * Connects all Fields with an Label
     * @param labelArray an array of objects containing labels
     */
    @Override
    public void setLabels(Object[] labelArray) {
        int i = 0;
        for ( int x = 0; x < this.playgroundsize; x++)
        {
            for ( int y = 0; y < this.playgroundsize; y++)
            {
                if ( Field[x][y] == null){
                    logEnemyPlayground.log(Level.SEVERE, "Field is uninitialized");
                    return;
                }
                else{
                    Label label = (Label)labelArray[i];
                    Field[x][y].setLabel(label);
                    i++;
                }

            }
        }
    }


    /**
     * Restriction: You can only build the Playground if all Ships are created and the placement of the ships are valid
     * Creates the Playground, as it is the enemy playground, all Fields are filled with Water Objects
     */
    @Override
    public void buildPlayground() {
        for (int x = 0; x < this.playgroundsize; x++) {
            for (int y = 0; y < this.playgroundsize; y++) {


                //Field is empty
                if (Field[x][y] == null) {
                    Field[x][y] = new Water();
                }

            }
        }


    }

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
    @Override
    public ShotResponse shoot(Point pos_shot, int answer) {

        //No hit
        boolean shipHit = false;
        boolean shipSunken = false;


        //Ship hit
        if ( answer == 1){
            shipHit = true;
            shipSunken = false;
        }

        //Ship sunken
        if ( answer == 2){
            shipHit = true;
            shipSunken = true;
        }

        //Ship hit and sunken
        if (shipSunken) {
            counterShipDestroyed++;



            //Switching labels
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart(true);
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();

            //Only Display Labels as disabled, when the the player has to shoot
            if (ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.playerVsRemote)
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();


            //The following code will find out, how the ship was standing, by setting the shipPart coordinates into an array with length 9
            //Afterwards it will mark all surrounding fields with shotWater

            int shotX = pos_shot.getX();
            int shotY = pos_shot.getY();

            Point[] destroyedShip = new Point[9];

            //Enemy Ship horizontal placed

                boolean horizontal = false;

                //If on the left or right position is a shipPart the ship is placed horizontal
                if ( (   (shotX+1 < playgroundsize) && (Field[shotX + 1][shotY] instanceof ShipPart)   ) || (  (shotX -1 >= 0) && (Field[shotX - 1][shotY] instanceof ShipPart)  ) ) {
                horizontal = true;

                // Possible ship points
                //  _ _ _ _ X _ _ _ _

                //1. Get the shipParts right form the X
                int currX = shotX;
                int count = 4;

                boolean hit = true;

                //Starting at position 4 (in the array)
                while (hit) {
                    //Check if playground is exceeded
                    if ( currX >= playgroundsize|| currX < 0 || count < 0 || count > 8) break;

                    if (Field[currX][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(currX, shotY);
                        count++;
                        currX++;
                    } else {
                        hit = false;
                    }
                }

                //2. Get the shipParts left form the X
                currX = shotX;
                count = 4;

                hit = true;

                //Starting at position 4 (in the array)
                while (hit) {
                    //Check if playground is exceeded
                    if ( currX >= playgroundsize|| currX < 0 || count < 0 || count > 8) break;

                    if (Field[currX][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(currX, shotY);
                        count--;
                        currX--;
                    } else {
                        hit = false;
                    }
                }


            }
            //End horizontal

            //Enemy Ship vertical placed

            //If on the top or bottom position is a shipPart the ship is placed vertical
            if ( (   (shotY+1 < playgroundsize) && (Field[shotX][shotY+1] instanceof ShipPart)   ) || (  (shotY -1 >= 0) && (Field[shotX][shotY-1] instanceof ShipPart)  ) ){
                horizontal = false;

                //Possible ship points
                //  _ 0
                //  _ 1
                //  _ 2
                //  _ 3
                //  Y 4
                //  _ 5
                //  _ 6
                //  _ 7
                //  _ 8

                //1. Get the shipParts above the Y

                int currY = shotY;
                int count = 4;

                boolean hit = true;

                //Starting position is 4 (in the array)
                while (hit) {
                    //Check if playground is exceeded
                    if ( currY >= playgroundsize|| currY < 0 || count < 0 || count > 8) break;

                    if (Field[shotX][currY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(shotX, currY);
                        count--;
                        currY--;
                    } else {
                        hit = false;
                    }
                }

                //2. Get the shipParts beyond the Y

                currY = shotY;
                count = 4;
                hit = true;

                //Starting position is 4 (in the array)
                while (hit) {
                    //Check if playground is exceeded
                    if ( currY >= playgroundsize|| currY < 0 || count < 0 || count > 8) break;

                    if (Field[shotX][currY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(shotX, currY);
                        count++;
                        currY++;
                    } else {
                        hit = false;
                    }
                }


            }
            //End vertical

            //The ship positions now in the PointArray destroyedShip, call of the helpMethod, to set all surrounding fields to shotWater
            ArrayList<Point> ImpossiblePositions = this.setAllImpossibleFieldsToShotWater(destroyedShip);

            //Providing additional information to set up the shipLabels in the Gui
            //Searching the "Head" (top, left) position of the ship
            Point positionHead = null;
            int size = 0;

            //Get first not null Point and get amount of not null points
            for ( Point point : destroyedShip){
                if ( point != null) {
                    size++;
                    if (positionHead  == null) positionHead = point;
                }
            }


            if (positionHead == null) {
                logEnemyPlayground.log(Level.SEVERE, "Couldn't find the Head of the Ship");
                return null;
            }
            else {
                Label headLabel = Field[positionHead.getX()][positionHead.getY()].getLabel();
                return new ShotResponse((counterShipDestroyed==this.shipsplaced), ImpossiblePositions, headLabel, horizontal, size);
            }

        }
        //End ship sunken

        //Ship only hit
        else if (shipHit) {

           //Change the Object to an ShipPart with shot status and exchange label
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart( true);
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();

            //Only Display Labels as disabled, when the the player has to shoot
            if (ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.playerVsRemote)
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false);
        }
        //No Hit
        else {

            //Change the Object to an ShotWater and exchange Label
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();

            //Only Display Labels as disabled, when the the player has to shoot
            if (ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.playerVsRemote)
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false);
        }

    }


    /**
     * Changes the surrounding of the ship to shotWater
     * @param destroyedShip an PointArray which contains all points to an associated ship
     * @return An ArrayList of points set to ShotWater
     */
    private ArrayList<Point> setAllImpossibleFieldsToShotWater(Point[] destroyedShip) {
        ArrayList<Point> ImpossiblePostions = new ArrayList<>();

        //e.g. Point-Array: [null, null, point, point, point, point, null, null, null]
        //For every Point in the destroyedShip array
        for (Point point : destroyedShip) {
                if (point == null) continue;

                //Get all the surrounding Fields of the point and switch them to shotWater
                int x = point.getX();
                int y = point.getY();

                for ( int i = x-1; i <= x+1; i++  ){
                    for ( int j = y-1; j <= y+1; j++){
                        setImpossibleFieldToShotWater(i, j, ImpossiblePostions);
                    }
                }

        }
        return ImpossiblePostions;
    }


    /**
     * Exchanges an object of type Water to an object of type ShotWater, including the label reference
     * Adds all changed objects to the impossiblePositions ArrayList
     * @param x Position x im Playground
     * @param y Position y im Playground
     */
    private void setImpossibleFieldToShotWater(int x, int y, ArrayList<Point> impossiblePositions){

        //Field is outside the Playground
        if ( x < 0 || x >= this.playgroundsize || y < 0 || y >= this.playgroundsize) return;

        //If the Field is instanceof Water change it to ShotWater
        if( Field[x][y] instanceof Water){
            Label label = Field[x][y].getLabel();
            Field[x][y] = new ShotWater();
            Field[x][y].setLabel(label);

            //Only Display Labels as disabled, when the the player has to shoot
            if (ActiveGameState.getModes() == GameMode.playerVsKi || ActiveGameState.getModes() == GameMode.playerVsRemote)
            Field[x][y].setLabelNonClickable();

            Field[x][y].draw();
            impossiblePositions.add(new Point(x,y));
        }
    }

    @Override
    public void setAllLabelsNonClickable() {
        for ( int x = 0; x < this.playgroundsize; x++)
        {
            for ( int y = 0; y < this.playgroundsize; y++)
            {
                Field[x][y].setLabelNonClickable();
            }
        }
    }

    @Override
    public void setAllWaterFieldsClickable() {
        for ( int x = 0; x < this.playgroundsize; x++)
        {
            for ( int y = 0; y < this.playgroundsize; y++)
            {
                if ( Field[x][y] instanceof Water)
                {
                    Field[x][y].setLabelClickable();
                }

            }
        }
    }
}