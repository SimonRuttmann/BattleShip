package Model.Playground;

import Model.Ship.IShip;
import Model.Ship.Ship;
import Model.Util.ShipPart;
import Model.Util.ShotWater;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;
import javafx.scene.control.Label;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnPlayground extends AbstractPlayground implements IOwnPlayground {
    public static final Logger logOwnPlayground = Logger.getLogger("parent.OwnPlayground");

    public OwnPlayground() {
        super();
    }

    private ArrayList<IShip> shipListOfThisPlayground = new ArrayList<>();

    @Override
    public void setShipListOfThisPlayground(ArrayList<IShip> shipListOfThisPlayground) {
        this.shipListOfThisPlayground = shipListOfThisPlayground;
    }

    @Override
    public ArrayList<IShip> getShipListOfThisPlayground() {
        return shipListOfThisPlayground;
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
                    logOwnPlayground.log(Level.SEVERE,"Field can't be connected with label as it is not instantiated");
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
        if ( pos_shot.getY() < 0 || pos_shot.getY() >= this.playgroundsize || pos_shot.getX() < 0 || pos_shot.getX() >= this.playgroundsize ){
            logOwnPlayground.log(Level.SEVERE, "The Point: " + pos_shot.getX() + " " + pos_shot.getY() + " is outside the playground");
        }

        //Hit
        if (Field[pos_shot.getX()][pos_shot.getY()] instanceof ShipPart){



            //Decrease Hit Points of the Ship
            ShipPart hitShipPart = (ShipPart)Field[pos_shot.getX()][pos_shot.getY()];
            IShip hitShip = hitShipPart.getOwner();
            hitShip.sethitPoints( hitShip.gethitPoints() -1 );


            //Mark shipPart as destroyed
            hitShipPart.setShot(true);
            hitShipPart.draw();

            //Ship sunken
            if ( hitShip.gethitPoints() <= 0){
                this.shipListOfThisPlayground.remove(hitShip);

                //Game won
                if ( this.shipListOfThisPlayground.size() == 0){
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

        if ( Field[pos_shot.getX()][pos_shot.getY()] instanceof ShotWater) {
            logOwnPlayground.log(Level.SEVERE,"The Field on Position:  " + pos_shot.getX() + " " + pos_shot.getY() + " is a shotWater Field");
        }
        else {
            logOwnPlayground.log(Level.SEVERE,"The Field on Position:  " + pos_shot.getX() + " " + pos_shot.getY() + " is not instantiated");
        }
        return null;
    }




    /**
     * This method builds the Playground, if there are no Ships in the ShipList, it will create an playground filled all Fields with water
     * If there are Ships in the List it will place them as shipParts
     * Creates the Playground, sets all ships-parts on the Field (if it is the own Field) and fills the rest of the fields with water
     */
    @Override
    public void buildPlayground() {


        //For every Ship in the ShipList insert the affiliated ship parts and fill the left fields with water
        for ( IShip Element : this.shipListOfThisPlayground){
           this.addShipToPlayground(Element);
        }

        //Fills the rest with Water
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
     * This method only checks whether the ship (represented by start and endpoint) can be placed or not
     * In both cases no ship is created
     *
     * @param startPoint The start point of the ship
     * @param endPoint  The end point of the ship
     * @return true, if placement is allowed
     */
    public boolean isValidPlacement(Point startPoint, Point endPoint){
        int startX = startPoint.getX();
        int startY = startPoint.getY();

        int endX = endPoint.getX();
        int endY = endPoint.getY();

        //Checks if any coordinates are out of the field
        if ( startX < 0 || startY < 0 || endX < 0 || endY < 0
                || startX >= this.playgroundsize|| startY >= this.playgroundsize ||
                endX >= this.playgroundsize || endY >= this.playgroundsize)
            return false;

        IShip ship = new Ship(startPoint, endPoint, this);

        Point[] coordinates = ship.getCoordinates();

        //Checks if the placement is valid
        for (Point point : coordinates ){
            int x = point.getX();
            int y = point.getY();
            if (!Field[x][y].getValidShipPlacementMarker()){
                //IShip.getShipList().remove(ship);
                this.shipListOfThisPlayground.remove(ship);
                return false;
            }
        }
        this.shipListOfThisPlayground.remove(ship);
        return true;
    }


    /**
     * Checks if the ship represented by these two points is valid
     * if the ship placement is valid the method creates the ship and adds it to the shipList itself
     *
     * @param startPoint The start point of the ship, which needs to be checked
     * @param endPoint  The end point of the ship, which needs to be checked
     * @return The ship if the placement is valid. In any other case null
     */
    @Override
    public IShip isShipPlacementValid(Point startPoint, Point endPoint) {
        int startX = startPoint.getX();
        int startY = startPoint.getY();

        int endX = endPoint.getX();
        int endY = endPoint.getY();

        //Checks if any coordinates are out of the field
        if ( startX < 0 || startY < 0 || endX < 0 || endY < 0
                || startX >= this.playgroundsize|| startY >= this.playgroundsize ||
                endX >= this.playgroundsize || endY >= this.playgroundsize)
                return null;

        IShip ship = new Ship(startPoint, endPoint, this);

        Point[] coordinates = ship.getCoordinates();

        //Checks if the placement is valid
        for (Point point : coordinates ){
            int x = point.getX();
            int y = point.getY();
            if (!Field[x][y].getValidShipPlacementMarker()){
                                                //IShip.getShipList().remove(ship);
                this.shipListOfThisPlayground.remove(ship);
                return null;
            }
        }

        //If the placement was valid then the fields surrounding the ship have to be marked
        //An ArrayList containing these points are saved for this ship
        ship.setPlacementMarkers(setPlacementMarkerToSurroundingFields(coordinates));

        //Adding the Ship to the Playground
        this.addShipToPlayground(ship);

        for ( Point point : coordinates){
            setPlacementMarkerToField(point.getX(),point.getY(), null);
        }

        return ship;
    }


    /**
     * Help-Method for the isShipPlacement-Method
     * @param coordinatesToMark The coordinates, which surroundings have to be marked
     * @return An ArrayList of all marked positions
     */
    private ArrayList<Point> setPlacementMarkerToSurroundingFields(Point[] coordinatesToMark) {
        ArrayList<Point> MarkedPositions = new ArrayList<>();
        //For every Point where the Ship will be placed
        for (Point point : coordinatesToMark) {

            //Get all the surrounding Fields of the point and switch them to marked
            int x = point.getX();
            int y = point.getY();

            for ( int i = x-1; i <= x+1; i++  ){
                for ( int j = y-1; j <= y+1; j++){
                    setPlacementMarkerToField (i, j, MarkedPositions);
                }
            }

        }
        return MarkedPositions;
    }


    /**
     * Help-Method for the setPlacementMarkerToSurroundingFields
     * @param x the x coordinate which has to be marked
     * @param y the y coordinate which has to be marked
     * @param MarkedPositions The ArrayList where the marked positions get added
     */
    private void setPlacementMarkerToField ( int x, int y, ArrayList<Point> MarkedPositions){

        //Field is outside the Playground
        if ( x < 0 || x >= this.playgroundsize || y < 0 || y >= this.playgroundsize) return;

        //Set the placementMarker of the Field to true
        if( Field[x][y] != null){
            Field[x][y].setValidShipPlacementMarker(false);
            if ( MarkedPositions!= null) MarkedPositions.add(new Point(x,y));
        }
    }




    /**
     * Use this method, when the player wants to switch the position of his ship on the selection
     * @param shipToMove The current ship the player wants to move to another position
     * @param newStartPoint The startPosition, where the player wants to move the ship
     * @param newEndpoint The endPosition, where the player wants to move the ship
     * @return IShip, if the ship movement was correct, null if the movement is not allowed
     */
    @Override
    public IShip moveShip(IShip shipToMove, Point newStartPoint, Point newEndpoint) {

        /*
        1. Set the fields, where the ship was to water, the Water Fields got by default an valid placement-marker
        2. Get all surrounding coordinates and mark them as valid if there is no ship next to
        3. Check if the new placement is valid
            a) null     -> Revert all actions                      -> return false
            b) notnull  -> The movement of the ship was correct    -> return true
         */


        //Get the coordinates of the ship
        Point[] coordinatesOfShip = shipToMove.getCoordinates();

        //Save the ShipParts in a cache, it's necessary if the shipPlacement wasn't valid and we have to revert the action
        ArrayList<ShipPart> cache = new ArrayList<>();

        //1
        for ( Point point : coordinatesOfShip ){
            //Set the Fields to Water, the Water Fields got by default an valid placement-marker
            System.out.println("Points of ship" + point.getX() + point.getY());
            System.out.println(Field[point.getX()][point.getY()].getClass());
            System.out.println("Error!");
            cache.add((ShipPart)Field[point.getX()][point.getY()]);
            System.out.println("Error?");
            Label label = Field[point.getX()][point.getY()].getLabel();
            Field[point.getX()][point.getY()] = new Water();
            Field[point.getX()][point.getY()].setLabel(label);
        }

        //2
        ArrayList<Point> changedCoordinates = checkSurroundingsOfMarkedPositions(coordinatesOfShip);

        //3
        IShip placedShip = isShipPlacementValid(newStartPoint,newEndpoint);

        //new Placement is not valid
        if (placedShip == null){
        // a)
            //replace the shipParts
            for ( Point point : coordinatesOfShip ){
                Field[point.getX()][point.getY()] = cache.get(0);
            }
            //remark the fields
            for (Point point : changedCoordinates){
                Field[point.getX()][point.getY()].setValidShipPlacementMarker(false);
            }
            return null;
        }
        else{
        // b)
            this.getShipListOfThisPlayground().remove(shipToMove);
            return placedShip;
        }

    }

    /**
     * Calculates the surrounding Coordinates of a Point
     * This method calls foreach surrounding coordinate the checkIfNextToShip method
     * @param coordinates The coordinates of the ship
     * @return ArrayList of Points with changed coordinates
     */
    private ArrayList<Point> checkSurroundingsOfMarkedPositions(Point[] coordinates){
        ArrayList<Point> changedCoordinates = new ArrayList<>();

        for (Point point : coordinates) {

            //Get all the surrounding Fields
            int x = point.getX();
            int y = point.getY();

            ArrayList<Point> surroundingCoordinates = new ArrayList<>();
            for ( int i = x-1; i <= x+1; i++  ){
                for ( int j = y-1; j <= y+1; j++){
                    surroundingCoordinates.add(new Point(i, j));
                }
            }
            checkIfNextToShip(surroundingCoordinates, changedCoordinates);
        }
        return changedCoordinates;
    }

    /**
     * When a surroundingCoordinates is not next to a ship is sets the ValidPlacementMarker to true
     * @param surroundingCoordinates The surrounding Coordinates which have to be checked if a ship is next to
     */
    private void checkIfNextToShip(ArrayList<Point> surroundingCoordinates, ArrayList<Point> changedCoordinates){
        //Foreach point the in the surroundingCoordinates list, check if a ship is NextTo
        for ( Point point : surroundingCoordinates){

            int x = point.getX();
            int y = point.getY();
            boolean isShipNext = false;

            for ( int i = x-1; i <= x+1; i++  ){
                for ( int j = y-1; j <= y+1; j++){
                    // Point is outside the playground
                    if ( i < 0 || i >= this.playgroundsize || j < 0 || j >= this.playgroundsize) continue;
                    if ( Field[i][j] instanceof ShipPart){
                        isShipNext = true;
                        break;
                    }
                }
            }
            changedCoordinates.add(new Point(x,y));
            Field[x][y].setValidShipPlacementMarker(!isShipNext);
        }
    }

    /**
     * Help method to add an ship to the playground, as loading exists, it is necessary to check if it is hit
     * @param ship The ship, which contains an start and end position
     */
    private void addShipToPlayground(IShip ship){
            //Position returns two Points, Start and End
            Point[] shipposition = ship.getPosition();
            int xStart = shipposition[0].getX();
            int yStart = shipposition[0].getY();

            int xEnd = shipposition[1].getX();
            int yEnd = shipposition[1].getY();

            //Insert Ship parts
            //Ship vertical
            if (xStart == xEnd) {
                for ( int i = yStart; i <= yEnd; i++)
                if ( Field[xStart][i] instanceof ShipPart && ((ShipPart)Field[xStart][i]).isShot()){
                    Field[xStart][i] = new ShipPart(ship, true);
                }
                else{
                    Field[xStart][i] = new ShipPart(ship, false);
                }
            }
            //Ship horizontal
            else {
                for ( int i = xStart; i <= xEnd; i++)
                    if ( Field[i][yStart] instanceof ShipPart && ((ShipPart)Field[i][yStart]).isShot()){
                        Field[i][yStart] = new ShipPart(ship, true);
                    }
                    else{
                        Field[i][yStart] = new ShipPart(ship, false);
                    }

            }

    }


}
