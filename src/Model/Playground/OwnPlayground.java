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

public class OwnPlayground extends AbstactPlayground implements IOwnPlayground{
    public OwnPlayground() {
        super();
    }


    private ArrayList<IShip> shipListOfThisPlayground = new ArrayList<>();

    public void setShipListOfThisPlayground(ArrayList<IShip> shipListOfThisPlayground) {
        this.shipListOfThisPlayground = shipListOfThisPlayground;
    }

    public ArrayList<IShip> getShipListOfThisPlayground() {
        return shipListOfThisPlayground;
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
                                            //IShip.getShipList().remove(hitShip);
                this.shipListOfThisPlayground.remove(hitShip);

                //Game won
                                            //if ( IShip.getShipList().size() == 0){
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

                                                        //for ( IShip Element : IShip.getShipList()) {
        for ( IShip Element : this.shipListOfThisPlayground){
           this.addShipToPlayground(Element);
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
            MarkedPositions.add(new Point(x,y));
        }
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

        /*
        1. Set the fields, where the ship was to water, the Water Fields got by default an valid placement-marker
        2. Get all surrounding coordinates and mark them as valid if there is no ship next to
        3. Check if the new placement is valid
            a) False -> Revert all actions                      -> return false
            b) True  -> The movement of the ship was correct    -> return true
         */


        //Get the coordinates of the ship
        Point[] coordinatesOfShip = shipToMove.getCoordinates();

        //Save the ShipParts in a cache, it's necessary if the shipPlacement wasn't valid and we have to revert the action
        ArrayList<ShipPart> cache = new ArrayList<>();

        //1
        for ( Point point : coordinatesOfShip ){
            //Set the Fields to Water, the Water Fields got by default an valid placement-marker
            cache.add((ShipPart)Field[point.getX()][point.getY()]);

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
            return false;
        }
        else{
        // b)
            return true;
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
                    surroundingCoordinates.add(new Point(x, y));
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
                    if ( x < 0 || x >= this.playgroundsize || y < 0 || y >= this.playgroundsize) continue;
                    if ( Field[x][y] instanceof ShipPart){
                        isShipNext = true;
                        break;
                    }
                }
            }
            changedCoordinates.add(new Point(x,y));
            Field[x][y].setValidShipPlacementMarker(!isShipNext);
        }
    }

    //TODO wenn später Bilder eingefügt werden, und das Shippart das entsprechende Bild zeigen soll, muss das hier überarbeitet werden
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
                Field[xStart][yStart] = new ShipPart("start vertical", ship);
                Field[xStart][yEnd] = new ShipPart("end vertical", ship);
                for (int i = yStart+1; i < yEnd; i++) {
                    Field[xStart][i] = new ShipPart("middle vertical", ship);
                }
            }
            //Ship horizontal
            else {
                Field[xStart][yStart] = new ShipPart("start horizontal", ship);
                Field[xEnd][yStart] = new ShipPart("end horizontal", ship);
                for (int i = xStart+1; i < xEnd; i++) {
                    Field[i][yStart] = new ShipPart("middle horizontal", ship);
                }
            }

    }
}
