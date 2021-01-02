package Model.Playground;

import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;
import javafx.scene.control.Label;

import java.util.ArrayList;


public class EnemyPlayground extends AbstactPlayground implements IEnemyPlayground {
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
        //this.shipsplaced = IShip.getAmount();

        for (int x = 0; x < this.playgroundsize; x++) {
            for (int y = 0; y < this.playgroundsize; y++) {


                //Field is empty
                if (Field[x][y] == null) {
                    Field[x][y] = new Water();
                }

            }
        }


    }
    //TODO Bug wenn man ein schiff nach oben zerstört, tritt nur manchmal auf, whr auch der Fehler für KIvsKI modus, zeichnet nicht alle positionen, sondern nur die oberste und findet die unteren position nicht
    //Muss sich Schiffe gegnerische Schiffe merken können um unmögliche Felder als Wasser zu markieren, soweit sie noch nicht beschossenes Wasser sind
    //Muss zudem die gegnerisch versunkenen Schiffe zählen um ein gewonnenes Spiel auszugeben
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
        System.out.println( "shoot mit: "+ pos_shot.getX() + pos_shot.getY());
        //Kein Treffer!
        boolean shipHit = false;
        boolean shipSunken = false;


        //Schiff getroffen
        if ( answer == 1){
            shipHit = true;
            shipSunken = false;
        }

        //Schiff versenkt!
        if ( answer == 2){
            shipHit = true;
            shipSunken = true;
        }

        //Ship hit and sunken
        if (shipSunken) {
            counterShipDestroyed++;


            //Labelaustausch und neues zerstörtes Schiffsteil nicht klickbar machen

            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", true);                              //--------------------- Ship Sunk ------------------------//
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();

            //finde heraus wie das schiff stand
            //setze alle umliegenden Felder auf ShotWater();
            int shotX = pos_shot.getX();
            int shotY = pos_shot.getY();


            Point[] destroyedShip = new Point[9];

            //Enemy Ship horizontal placed

                boolean horizontal = false;
                //Feld rechts oder links ist ein Schiffsteil
                if ( (   (shotX+1 < playgroundsize) && (Field[shotX + 1][shotY] instanceof ShipPart)   ) || (  (shotX -1 >= 0) && (Field[shotX - 1][shotY] instanceof ShipPart)  ) ) {
                //ermittle Schiffpositionen
                horizontal = true;


                //Schiff is horizontal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle rechte und linke positionen
                int currX = shotX;
                int count = 4;


                //  _ _ _ _ X _ _ _ _

                boolean hit = true;

                //Ermittle HitPositionen rechts davon

                //Starte bei Position 5 (beginnend bei 0)

                while (hit) {
                    //Spielfeld wird durch i nach links unterschritten
                    if ( currX >= playgroundsize|| currX < 0 || count < 0 || count > 8) break;

                    if (Field[currX][shotY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(currX, shotY);
                        count++;
                        currX++;
                    } else {
                        hit = false;
                    }
                }

                //Auf Anfangswerte zurücksetzen
                currX = shotX;
                count = 4;

                //Beginne bei Position 4 (beginnend bei 0)

                hit = true;
                //Ermittle HitPositinen links davon

                while (hit) {

                    //Spielfeld wird durch i nach rechts übertreten
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

            //Enemy Ship vertical placed
            //Feld darunter oder darüber ein Schiffsteil

            if ( (   (shotY+1 < playgroundsize) && (Field[shotX][shotY+1] instanceof ShipPart)   ) || (  (shotY -1 >= 0) && (Field[shotX][shotY-1] instanceof ShipPart)  ) ){
                horizontal = false;
                //Schiff is vertikal plaziert und ist untergegangen -> Ermittle die positionen des Schiffs
                //Schiffsgröße 2 3 4 5
                //zähle obere und untere positionen
                int currY = shotY;
                int count = 4;


                //  _ 0
                //  _ 1
                //  _ 2
                //  _ 3
                //  Y 4
                //  _ 5
                //  _ 6
                //  _ 7
                //  _ 8

                boolean hit = true;

                //Ermittle HitPositionen oben davon
                while (hit) {
                    //Spielfeld wird durch i nach oben überschritten
                    if ( currY >= playgroundsize|| currY < 0 || count < 0 || count > 8) break;

                    if (Field[shotX][currY] instanceof ShipPart) {
                        destroyedShip[count] = new Point(shotX, currY);
                        count--;
                        currY--;
                    } else {
                        hit = false;
                    }
                }

                currY = shotY;
                count = 4;
                hit = true;

                //Ermittle HitPositinen unten davon
                while (hit) {
                    //Spielfeld wrid durch i nach unten überschritten
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

            //Die Schiffsposition ist jetzt im PointArray destroyedShip
            ArrayList<Point> ImpossiblePositions = this.setAllImpossibleFieldsToWater(destroyedShip);

            //Informationen bereitstellen:
            //Head = Position oben links
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
                System.out.println( "Couldn't find the Head of the Ship");
                return null;
            }
            else {
                Label headLabel = Field[positionHead.getX()][positionHead.getY()].getLabel();
                //public ShotResponse (boolean gameWin, ArrayList<Point> impossiblePositions, Label label, boolean horizontal, int sizeOfSunkenShip)

                                    //Counter == Ship Destroyed -> Spiel gewonnen -> sonst verloren
                return new ShotResponse((counterShipDestroyed==this.shipsplaced), ImpossiblePositions, headLabel, horizontal, size);
            }


        }

        //Ship only hit
        else if (shipHit) {

           //Change the Object to an ShipPart with shot status
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            //Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", null);
            Field[pos_shot.getX()][pos_shot.getY()] = new ShipPart("Destroyed", true);
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false);
        }
        //No Hit
        else {

            //Change the Object to an ShotWater
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();
            Field[pos_shot.getX()][pos_shot.getY()].setLabelNonClickable();
            return new ShotResponse(false);
        }

    }

    @Override
    public void setAllLabelsNonClickable() {
        for ( int x = 0; x < this.playgroundsize; x++)
        {
            for ( int y = 0; y < this.playgroundsize; y++)
            {
                Field[x][y].setLabelNonClickable();
                //Field[x][y].getLabel().setDisable(false);
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
                    //Field[x][y].getLabel().setDisable(false);
                }

            }
        }
    }


    /**
     * Changes the surrounding of the ship to shotWater
     * @param destroyedShip an PointArray which contains all points to an associated ship
     * @return An ArrayList of points set to ShotWater
     */
    private ArrayList<Point> setAllImpossibleFieldsToWater(Point[] destroyedShip) {
        ArrayList<Point> ImpossiblePostions = new ArrayList<>();

        //bsp Point-Array: [null, null, point, point, point, point, null, null, null]
        //For every Point in the destroyedShip array
        for (Point point : destroyedShip) {
                if (point == null) continue;
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
        if ( x < 0 || x >= this.playgroundsize || y < 0 || y >= this.playgroundsize) return;

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