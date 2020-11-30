package Model.Playground;

import Model.Ship.IShip;
import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;
import javafx.scene.control.Label;

import java.util.ArrayList;

// todo -> make not abstract again, but now needed due to get game running
abstract public class EnemyPlayground extends AbstactPlayground implements IEnemyPlayground {
    private int counterShipDestroyed = 0;

    public EnemyPlayground(int playgroundsize) {
        super(playgroundsize);
    }

    /**
     * Connects all Fields with an Label
     * @param labelArray an array of objects containing labels
     */
    @Override
    public void setLabels(Object[] labelArray) {
        int i = 0;
        for ( int x = 1; x <= this.playgroundsize; x++)
        {
            for ( int y = 1; y <= this.playgroundsize; y++)
            {
                if ( Field[x][y] == null){
                    System.out.println("Error, Field is uninitialized");
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
        this.shipsplaced = IShip.getAmount();

        for (int x = 1; x <= this.playgroundsize; x++) {
            for (int y = 1; y <= this.playgroundsize; y++) {
                System.out.print("Hello");


                //Field is empty
                if (Field[x][y] == null) {
                    Field[x][y] = new Water();
                }

            }
        }


    }

    //Muss sich Schiffe gegnerische Schiffe merken können um unmögliche Felder als Wasser zu markieren, soweit sie noch nicht beschossenes Wasser sind
    //Muss zudem die gegnerisch versunkenen Schiffe zählen um ein gewonnenes Spiel auszugeben
    /**
     * Draws the shot-position in the playground depending on the shipHit, shipSunken parameters
     * Also marks all impossible positions as ShotWater, when a ship is sunken
     *
     * If the ship is sunken, the ShotResponse contains an Point-Array of impossible positions and the information if the game is won
     * In any other case the return value can be ignored
     *
     * @param pos_shot The position where the player is shooting the enemy playground
     * @param shipHit If a ship on enemy playground is hit, this variable is set to true
     * @param shipSunken If a ship on enemy playground is sunken, this variable is set to true
     * @return An object of type ShotResponse, the object contains all information
     *
     *
     */
    @Override
    public ShotResponse shoot(Point pos_shot, boolean shipHit, boolean shipSunken) {
        //Ship hit and sunken
        if (shipSunken) {
            counterShipDestroyed++;
            if (counterShipDestroyed == this.shipsplaced) {
                this.gameWon = true;
                return new ShotResponse(true, null);
            }
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", null);
            //finde heraus wie das schiff stand
            //setze alle umliegenden Felder auf ShotWater();
            int shotX = pos_shot.getX();
            int shotY = pos_shot.getY();


            Point[] destroyedShip = new Point[9];
            //Enemy Ship horizontal placed
            if (Field[shotX + 1][shotY] instanceof ShipPart || Field[shotX - 1][shotY] instanceof ShipPart) {
                //ermittle Schiffpositionen

                //Schiff is horizontal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle rechte und linke positionen
                int i = shotX;
                int count = 4;


                //  _ _ _ _ X _ _ _ _

                boolean hit = true;

                //Ermittle HitPositionen rechts davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count++;
                        i++;
                    } else {
                        hit = false;
                    }
                }

                i = shotX;
                count = 4;
                hit = true;
                //Ermittle HitPositinen links davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count--;
                        i--;
                    } else {
                        hit = false;
                    }
                }




            }

            //Enemy Ship vertical placed
            if (Field[shotX][shotY + 1] instanceof ShipPart || Field[shotX][shotY - 1] instanceof ShipPart) {

                //Schiff is vertikal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle obere und untere positionen
                int i = shotY;
                int count = 4;


                //  _
                //  _
                //  _
                //  _
                //  Y
                //  _
                //  _
                //  _
                //  _

                boolean hit = true;

                //Ermittle HitPositionen oben davon
                while (hit) {
                    if (Field[shotX][i] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count--;
                        i--;
                    } else {
                        hit = false;
                    }
                }

                i = shotY;
                count = 4;
                hit = true;

                //Ermittle HitPositinen links davon
                while (hit) {
                    if (Field[i][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(i, shotY);
                        count++;
                        i++;
                    } else {
                        hit = false;
                    }
                }


            }

            //Die Schiffsposition ist jetzt im PointArray destroyedShip
            ArrayList<Point> ImpossiblePositions = this.setAllImpossibleFieldsToWater(destroyedShip);

            return new ShotResponse(false, ImpossiblePositions);
        }

        //Ship only hit
        else if (shipHit) {

           //Change the Object to an ShipPart with shot status
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", null);
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false, null);
        }
        //No Hit
        else {

            //Change the Object to an ShotWater
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false, null);
        }

    }



    /**
     * Changes the surrounding of the ship to shotWater
     * @param destroyedShip an PointArray which contains all points to an associated ship
     * @return An ArrayList of points set to ShotWater
     */
    private ArrayList<Point> setAllImpossibleFieldsToWater(Point[] destroyedShip) {
        ArrayList<Point> ImpossiblePostions = new ArrayList<>();

        //For every Point in the destroyedShip array
        for (Point point : destroyedShip) {

                //Get all the surrounding Fields of the point and switch them to shotWater
                int x = point.getX();
                int y = point.getY();

                for ( int i = x-1; i <= x+1; i++  ){
                    for ( int j = y-1; j <= y+1; j++){
                        setImpossibleFieldToWater(i, j, ImpossiblePostions);
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
    private void setImpossibleFieldToWater ( int x, int y, ArrayList<Point> impossiblePositions){

        //Field is outside the Playground
        if ( x < 1 || x > this.playgroundsize || y < 1 || y > this.playgroundsize) return;

        //If the Field is instanceof Water change it to ShotWater and set the Label non Clickable
        if( Field[x][y] instanceof Water){
            Label label = Field[x][y].getLabel();
            Field[x][y] = new ShotWater();
            Field[x][y].setLabel(label);
            Field[x][y].setLabelNonClickable();
            Field[x][y].draw();
            impossiblePositions.add(new Point(x,y));
        }
    }
}