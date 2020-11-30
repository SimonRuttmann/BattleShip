package Model.Playground;

import Model.Ship.IShip;
import Model.Ship.Ship;
import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;
import javafx.scene.control.Label;

public class OwnPlayground extends AbstactPlayground implements IOwnPlayground{
    public OwnPlayground(int playgroundsize) {
        super(playgroundsize);
    }

    /**
     * Connects all Fields with an Label
     * Sets all Fields non clickable
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
                    Field[x][y].setLabelNonClickable();
                    i++;
                }

            }
        }
    }





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
    public ShotResponse shoot(Point pos_shot) {

        //Hit
        if (Field[pos_shot.getX()][pos_shot.getY()] instanceof ShipPart){

            //Decrease Hit Points of the Ship
            ShipPart hitShipPart = (ShipPart)Field[pos_shot.getX()][pos_shot.getY()];
            IShip hitShip = hitShipPart.getOwner();
            hitShip.sethitPoints( hitShip.gethitPoints() -1 );

            //Mark shipPart as destroyed
            hitShipPart.setPart("Destroyed");
            hitShipPart.setShot(true);
            hitShipPart.setLabelNonClickable();
            hitShipPart.draw();

            //Ship sunken
            if ( hitShip.gethitPoints() <= 0){
                IShip.getShipList().remove(hitShip);

                //Game won
                if ( IShip.getShipList().size() == 0){
                    this.gameLost = true;
                    return new ShotResponse (true,true,true);
                }

                //Game still running
                return new ShotResponse(false, true, true);

            }
            //Ship still alive
            else{
                return new ShotResponse( false, true,false);
            }

        }

        //Water
        if (Field[pos_shot.getX()][pos_shot.getY()] instanceof Water){
            ShotResponse response = new ShotResponse(false,false,false);

            //Set ShotWater
            Label label = Field[pos_shot.getX()][pos_shot.getY()].getLabel();
            Field[pos_shot.getX()][pos_shot.getY()] = new ShotWater();
            Field[pos_shot.getX()][pos_shot.getY()].setLabel(label);
            Field[pos_shot.getX()][pos_shot.getY()].draw();
            return response;
        }
        return null;
    }



    /**
     * Restriction: You can only build the Playground if all Ships are created and the placement of the ships are valid
     * Creates the Playground, sets all ships-parts on the Field (if it is the own Field) and fills the rest of the fields with water
     */
    @Override
    public void buildPlayground() {
        this.shipsplaced = IShip.getAmount();

        //For every Ship in the ShipList insert the affiliated ship parts and fill the left fields with water

        for ( IShip Element : IShip.getShipList()) {

            //Position returns two Points, Start and End
            Point[] shipposition = Element.getPosition();
            int xStart = shipposition[0].getX();
            int yStart = shipposition[0].getY();

            int xEnd = shipposition[1].getX();
            int yEnd = shipposition[1].getY();

            //Insert Ship parts
            //Ship vertical
            if (xStart == xEnd) {
                Field[xStart][yStart] = new ShipPart("start vertical", Element);
                Field[xStart][yEnd] = new ShipPart("end vertical", Element);
                for (int i = yStart+1; i < yEnd; i++) {
                    Field[xStart][i] = new ShipPart("middle vertical", Element);
                }
            }
            //Ship horizontal
            else {
                Field[xStart][yStart] = new ShipPart("start horizontal", Element);
                Field[xEnd][yStart] = new ShipPart("end horizontal", Element);
                for (int i = xStart+1; i < xEnd; i++) {
                    Field[i][yStart] = new ShipPart("middle horizontal", Element);
                }
            }

        }

            //Fills the Rest with Water
            for ( int x = 1; x <= this.playgroundsize; x++)
            {
                for ( int y = 1; y <= this.playgroundsize; y++)
                {
                    System.out.print("Hello");


                    //Field is empty
                    if (Field[x][y] == null){
                        Field[x][y] = new Water();
                    }

                }
            }


    }


    /**
     * Checks if the ship represented by these two points is valid
     * if the ship placement is valid the method creates the ship and adds it to the shipList itself
     *
     * @param startPoint The start point of the ship, which needs to be checked
     * @param endPoint  The end point of the ship, which needs to be checked
     * @return True if the placement is valid. In any other case false
     */
    @Override
    public boolean isShipPlacementValid(Point startPoint, Point endPoint) {
        int startX = startPoint.getX();
        int startY = startPoint.getY();

        int endX = endPoint.getX();
        int endY = endPoint.getY();

        int size = this.playgroundsize;

        //Checks if any coordinates are out of the field
        if ( startX < 0 || startY < 0 || endX < 0 || endY < 0
                || startX >= this.playgroundsize|| startY >= this.playgroundsize ||
                endX >= this.playgroundsize || endY >= this.playgroundsize)
                return false;


        for ( int x = 0; x < size; x++){
            for ( int y = 0; y < size; y++){

            }
        }
        //Wenn ein schiff konstruiert wird und später nicht plazierbar ist, MUSS es wieder aus der Schiffsliste (automatisch beim konstruieren hinzugefügt) entfernt werden!
        return false;
    }

    /**
     * Use this method, when the player wants to switch the position of his ship on the selection
     * @param shipToMove The current ship the player wants to move to another position
     * @param newStartPoint The startPosition, where the player wants to move the ship
     * @param newEndpoint The endPosition, where the player wants to move the ship
     * @return true, if the ship movement was correct, false if the movement is not allowed
     */
    @Override
    public boolean moveShip(Ship shipToMove, Point newStartPoint, Point newEndpoint) {
        return false;
    }

}
