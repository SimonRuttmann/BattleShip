package KI;

import Model.Playground.*;
import Model.Ship.*;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import GameData.ActiveGameState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


//There are two instances, enemyAI and ownAI, which are stored in ActiveGameState as KI, they are needed for the getShot method
public class Ki implements IKi{
    public enum Difficulty {undefined, normal, hard}

    public static final Logger logKi = Logger.getLogger("parent.Ki");

    enum NextLocation { nextTop, nextBottom, nextLeft, nextRight, noDestination}    //shows where the nect shot will/can be
    enum DestroyStatus {notFound, destroying}                                       //mode of destroying or not destorying a ship

    private DestroyStatus destroyStatus = DestroyStatus.notFound;
    private NextLocation nextLocation;                                              //next position that is going to get shot
    int rangeToShot = 1;

    transient ShotResponse shotResponseFromKI;


    /**
     * Convenience Method of checkArrayList
     * @param x coordinate x axis
     * @param y coordinate y axis
     * @return True, if the previous list contains a point with the same x and y coordinates
     */
    public boolean isNextShotInPreviousList(int x, int y){
        for ( Point point : previousShots){
            if ( point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }

    protected int Playgroundsize;
    private final Difficulty difficulty;
    public Ki(Ki.Difficulty difficulty){
        this.difficulty = difficulty;
        Playgroundsize = ActiveGameState.getPlaygroundSize();
    }


    private int counterToRestartPlaceShip = 0;

    /**
     * Backtrack algorithm to place ships randomly
     *
     * Algorithm:
     *
     * 1. If all ships are already placed return the passed shipList
     *
     * 2. As long, as no valid position is found, where all ships above (via recursive call) can be added or all possible positions have been tried:
     *      1. Get a random point
     *      2. If the random point is not already in use
     *             1. Add the point to the occupied dots list
     *      3. Try to place the ship via the method getPlacementStyle
     *
     *      4. If the placement is possible:
     *              1. Add the ship to the new Ships list
     *              2. Add all points of the ship and all surroundings to the list
     *              3. Execute the placeShips method (recursive call), with the new occupied dots list and an incremented value of currentShipToPlace
     *
     *              4. If the called placeShips Method returns a list, containing the ships
     *                      1. All ships above could be added to the playground, therefore return the newShipsList
     *
     *              5.Else, revert the list to the origin value, except the point added in
     *
     *    5. Else, repeat the do-while loop
     *
     *    End While
     *
     * 3. If no position was found return null
     * 4. Else: A valid position (placementStyle > 0) was found, return the shipList
     *
     * @param occupiedDotsList ArrayList of all points, occupied by the previous ship placements
     * @param kiShips ArrayList of ships, which need to be placed
     * @param newShips ArrayList of already placed Ships
     * @param playground The playground, where the ships have to be placed in
     * @param currentShipToPlace An integer used as index for the ArrayList kiShips. The ArrayList with the index currentShipToPlace returns the ship, which needs to be placed next
     * @return An ArrayList of the new placed ships
     */

    public ArrayList<IShip> placeShip(ArrayList<Point> occupiedDotsList, ArrayList<IShip> kiShips, ArrayList<IShip> newShips, IOwnPlayground playground, int currentShipToPlace){

        if (this.counterToRestartPlaceShip > 10000){
            return null;
        }
        this.counterToRestartPlaceShip++;

        if ( currentShipToPlace >= ActiveGameState.getAmountOfShips()){
            return newShips;
        }

        //Create random point pool
        ArrayList<Point> randomPointPool = new ArrayList<>();
        for (int x = 0; x < ActiveGameState.getPlaygroundSize(); x++){
            for (int y = 0; y < ActiveGameState.getPlaygroundSize(); y++){
                randomPointPool.add(new Point(x, y));
            }
        }

        //Remove values from the Set
        for(Point point : occupiedDotsList){
            randomPointPool.removeIf(point2 -> point.getX() == point2.getX() && point.getY() == point2.getY());
        }

        int random_x;
        int random_y;
        int placementStyle = -1;
        int counter = 0;

        do {

            IShip kiShip = kiShips.get(currentShipToPlace);

            Point point;

            Random random = new Random();

            //no existing element
            if (randomPointPool.size() == 0) return null;
            //element existing -> (random.nextInt needs a number > 0)
            if (randomPointPool.size() == 1) {
                point = randomPointPool.get(0);
            }
            //choose element from remaining amount
            else{
                point = randomPointPool.get(random.nextInt(randomPointPool.size()));
            }

            if ( point == null) return null; // Every single Point tested
            random_x = point.getX();
            random_y = point.getY();

            randomPointPool.remove(point);

            placementStyle = getPlacementStyle(new Point(random_x, random_y), kiShip.getSize(), ActiveGameState.getPlaygroundSize(), occupiedDotsList);
            counter++;

            //do recursion for every valid placement
            if ( placementStyle >= 0){
                Point newEndPos;

                //get ship positions
                switch(placementStyle){
                    case 0: newEndPos = new Point ( random_x ,                        random_y-kiShip.getSize()+1); break;      //top
                    case 1: newEndPos = new Point ( random_x + kiShip.getSize()-1,    random_y); break;                         //right
                    case 2: newEndPos = new Point ( random_x ,                        random_y+kiShip.getSize()-1); break;      //bottom
                    case 3: newEndPos = new Point ( random_x - kiShip.getSize()+1,    random_y); break;                         //left
                    default:
                        throw new IllegalStateException("Unexpected value: " + placementStyle);
                }

                //add ships with the positions

                IShip shipToPlace = new Ship(new Point(random_x, random_y), newEndPos, playground);
                newShips.add(shipToPlace);

                //ship positions + surroundings
                ArrayList<Point> shipPositions = new ArrayList<>();
                shipPositions = markShipDots(point, kiShip.getSize(), placementStyle, shipPositions);

                ArrayList<Point> shipPositionsAndSurroundings = surroundShipDots(shipPositions);
                shipPositionsAndSurroundings.addAll(shipPositions);

                //ship positions from earlier recursive steps + ship positions + surroundings from actual recursive step
                ArrayList<Point> newOccupiedDotsList = new ArrayList<Point>(occupiedDotsList);
                newOccupiedDotsList.addAll(shipPositionsAndSurroundings);

                currentShipToPlace++;
                ArrayList<IShip>  result = placeShip(newOccupiedDotsList, kiShips, newShips, playground, currentShipToPlace);
                currentShipToPlace--;

                // there is no placement for the rest of the following ships
                if ( result == null){
                    placementStyle = -404;
                    newShips.remove(shipToPlace);
                }
            }

        } while (placementStyle < 0 && counter < ActiveGameState.getPlaygroundSize()*ActiveGameState.getPlaygroundSize());
        //recursive resolution, wasnt able to place the ship
        if ( placementStyle < 0){
            return null;
        }

        //recursion succeeded
        return newShips;
    }

    public ArrayList<IShip> placeships(IOwnPlayground playground) {

        ArrayList<Point> occupiedDotsList = new ArrayList<>();
        ArrayList<IShip> kiShips = new ArrayList<>();
        ArrayList<IShip> newShips = new ArrayList<>();

        //all the different ship types are going to get stored/saved for the following processing
        for(int u = 0; u < ActiveGameState.getAmountShipSize5(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 4), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u < ActiveGameState.getAmountShipSize4(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 3), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u < ActiveGameState.getAmountShipSize3(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 2), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u < ActiveGameState.getAmountShipSize2(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 1), playground);
            kiShips.add(ship);
        }

        for(IShip ship : kiShips){
            System.out.println(ship.getPosStart() +  " " +ship.getPosEnd());
        }

        this.counterToRestartPlaceShip = 0;
        ArrayList<IShip> shipList;
        shipList = placeShip(occupiedDotsList, kiShips, newShips, playground, 0);
        while ( shipList == null){
            this.counterToRestartPlaceShip = 0;
            shipList = placeShip(occupiedDotsList, kiShips, newShips, playground, 0);
        }
        System.out.println(counterToRestartPlaceShip);
        return shipList;
    }

    //marks the surrounding dots of the ship
    protected ArrayList<Point> surroundShipDots(ArrayList<Point> currentShipDots) {
        ArrayList<Point> surrDots = new ArrayList<>();
        for(int i = 0 ; i < currentShipDots.size() ; i++) {
            Point p = new Point(currentShipDots.get(i).getX(), currentShipDots.get(i).getY());
            for(int z = 0; z < 9 ; z ++){
                switch(z){
                    case 0:
                        if(!checkArrayList(surrDots, new Point(p.getX(), p.getY() - 1)) && !checkArrayList(currentShipDots, new Point(p.getX(), p.getY() - 1)))
                            surrDots.add(new Point(p.getX(), p.getY() - 1));
                        break;
                    case 1:
                        if(!checkArrayList(surrDots, new Point(p.getX() + 1 , p.getY() - 1)) && !checkArrayList(currentShipDots, new Point(p.getX() + 1, p.getY() - 1)))
                            surrDots.add(new Point(p.getX() + 1, p.getY() - 1));
                        break;
                    case 2:
                        if(!checkArrayList(surrDots, new Point(p.getX() + 1 , p.getY())) && !checkArrayList(currentShipDots, new Point(p.getX() + 1, p.getY())))
                            surrDots.add(new Point(p.getX() + 1, p.getY()));
                        break;
                    case 3:
                        if(!checkArrayList(surrDots, new Point(p.getX() + 1 , p.getY() + 1)) && !checkArrayList(currentShipDots, new Point(p.getX() + 1, p.getY() + 1)))
                            surrDots.add(new Point(p.getX() + 1, p.getY() + 1));
                        break;
                    case 4:
                        if(!checkArrayList(surrDots, new Point(p.getX(), p.getY() + 1)) && !checkArrayList(currentShipDots, new Point(p.getX(), p.getY() + 1)))
                            surrDots.add(new Point(p.getX(), p.getY() + 1));
                        break;
                    case 5:
                        if(!checkArrayList(surrDots, new Point(p.getX() - 1, p.getY() + 1)) && !checkArrayList(currentShipDots, new Point(p.getX() - 1, p.getY() + 1)))
                            surrDots.add(new Point(p.getX() - 1, p.getY() + 1));
                        break;
                    case 6:
                        if(!checkArrayList(surrDots, new Point(p.getX() - 1 , p.getY())) && !checkArrayList(currentShipDots, new Point(p.getX() - 1, p.getY())))
                            surrDots.add(new Point(p.getX() - 1, p.getY()));
                        break;
                    case 7:
                        if(!checkArrayList(surrDots, new Point(p.getX() - 1 , p.getY() - 1)) && !checkArrayList(currentShipDots, new Point(p.getX() - 1, p.getY() - 1)))
                            surrDots.add(new Point(p.getX() - 1, p.getY() - 1));
                        break;
                }
            }
        }
        return surrDots;
    }


    //chekcs if the point is in the list
    protected boolean checkArrayList(ArrayList<Point> list, Point p){
        for( Point point : list){
            if (point.getX() == p.getX() && point.getY() == p.getY()) return true;
        }
        return false;
    }

    //marks the dots of the current ship
    protected ArrayList<Point> markShipDots(Point p, int size, int style, ArrayList<Point> list) {
        switch (style) {
            case 0:
                for(int i = 0; i < size; i++) {
                    list.add(new Point(p.getX(), p.getY() - i));
                }
                return list;

            case 1:
                for(int i = 0; i < size; i++) {
                    list.add(new Point(p.getX() + i, p.getY()));
                }
                return list;

            case 2:
                for(int i = 0; i < size; i++) {
                    list.add(new Point(p.getX(), p.getY() + i));
                }
                return list;

            case 3:
                for(int i = 0; i < size; i++) {
                    list.add(new Point(p.getX() - i, p.getY()));
                }
                return list;

        }
        return null;
    }

    //checks if there is a valid placementstyle at the current point for the current ship
    protected int getPlacementStyle(Point p, int size, int playgrounds, ArrayList<Point> list){
        int style;
        int count = 0;
        boolean placeable;
        style = getRandomInt(0 , 4);
        do{
            placeable = true;
            switch (style){
                case 0:     //Top
                    for(int i = 0; i < size; i++){
                        if(checkArrayList(list, new Point(p.getX(), p.getY() - i))){
                            placeable = false;
                        }
                    }
                    if(p.getY() - size >= 0 && placeable) return 0;
                    else placeable = false;
                    break;

                case 1:     //Right
                    for(int i = 0; i < size; i++){
                        if(checkArrayList(list, new Point(p.getX() + i, p.getY()))){
                            placeable = false;
                        }
                    }
                    if(p.getX() + size < playgrounds && placeable) return 1;
                    else placeable = false;
                    break;

                case 2:     //Down
                    for(int i = 0; i < size; i++){
                        if(checkArrayList(list, new Point(p.getX(), p.getY() + i))){
                            placeable = false;
                        }
                    }
                    if(p.getY() + size < playgrounds && placeable) return 2;
                    else placeable = false;
                    break;

                case 3:    //Left
                    for(int i = 0; i < size; i++){
                        if(checkArrayList(list, new Point(p.getX() - i, p.getY()))){
                            placeable = false;
                        }
                    }
                    if(p.getX() - size >= 0 && placeable) return 3;
                    else placeable = false;
            }

            style = (style + 1) % 4;
            count++;
        }while(!placeable && count < 4);
        return -1;
    }


    /**
     * @param min min value of the random
     * @param max max value of the random exclusive
     * @return  a random int between min (inclusive) and max (exclusive)
     */

    private static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min in KI min:" + min + "max:" + max);
        }
        Random r = new Random();
        return r.nextInt((max - min) ) + min;
    }

    //returns the point which is going to get shot
    @Override
    public ShotResponse getShot(IOwnPlayground playground) {
        System.out.println("Schwierigkeitsgrad: " + this.difficulty);
        //difficulty = normal
        if(this.difficulty == Difficulty.normal){
            boolean success = normaleKi(playground);
            if ( !success ) return shotResponseFromKI;
            return this.shotResponseFromKI;
        }
        //difficulty = hard or more likley with tactic
        if(this.difficulty == Difficulty.hard){
            boolean success = schwereKi(playground);
            if ( !success ) return shotResponseFromKI;

            return this.shotResponseFromKI;
        }
        return null;
    }

    protected boolean isHitFlag; //default value is false
    protected Set<Point> previousShots = new HashSet<>();
    protected Point firstHit;
    protected boolean startDestroy;
    private boolean needRandomLocation;

    /**
     * Algorithm: normale AI
     *  1. if the destroy status is inactive
     *      1.1 Get a random point, that did not get shot before
     *
     *      1.2 if the shot at this point was a hit
     *          1.2.1 destroy status will switch to active and the algorithm will start the destroy- mode and will destroy the found ship
     *
     *      1.3 if the ship is destroyed, destroy status will switch to inactive
     *
     * @param playground The playground, where the ships have to be placed in
     * @return An boolean, if the call of normaleKi, to get a new point to shoot was successful
     */

    protected boolean normaleKi(IOwnPlayground playground){
        ShotResponse answerofShot;
        int random_x;
        int random_y;


        if(this.destroyStatus == DestroyStatus.notFound){
            //as long the random point got already shot it will search for a new one
            do {
                random_x = getRandomInt(0, ActiveGameState.getPlaygroundSize() );
                random_y = getRandomInt(0, ActiveGameState.getPlaygroundSize() );
            }while (isNextShotInPreviousList(random_x,random_y));

            Point currentShot = new Point(random_x, random_y);

            answerofShot = shootPlayground(currentShot, playground);
            if ( shotResponseFromKI.isUnhandled() ) return false;

            this.shotResponseFromKI = answerofShot;
            shotResponseFromKI.setShotPosition(currentShot);
            previousShots.add(currentShot);

            if(answerofShot.isHit()){
                destroyStatus = DestroyStatus.destroying;
                nextLocation = NextLocation.nextTop;
                needRandomLocation = true;

                isHitFlag = true; //a ship is found
                firstHit = new Point(random_x,random_y); //first hit of the ship is getting stored
                startDestroy = true;
                shiptoDestroy.add(currentShot);
            }
            else{
                this.destroyStatus = DestroyStatus.notFound;
            }
        }

        else if(destroyStatus == DestroyStatus.destroying){

            return destroyShip(firstHit, playground);
        }

        return true;
    }

    /**
     * Algorith: Hard AI
     *  1. Checks if the destroy status is inactive
     *      1.1 if inactive -> currentShot method will be called to get a tactical and valid point to shoot
     *
     *      1.2 If the shot was a hit
     *          1.2.1 destroy status will switch to active and the algorithm will start the destroy- mode and will destroy the found ship
     *
     *      1.3 if the ship is destroyed, destroy status will switch to inactive
     *
     * @param playground The playground, where the ships have to be placed in
     * @return An boolean, if the call of schwereKi, to get a new point to shoot was successful
     */
    //hard AI
    protected boolean schwereKi(IOwnPlayground playground){
        ShotResponse answerofShot;

        if(this.destroyStatus == DestroyStatus.notFound){

            Point currentShot = hardKiShot();

            answerofShot = shootPlayground(currentShot, playground);
            if ( shotResponseFromKI.isUnhandled()) return false;

            this.shotResponseFromKI = answerofShot;
            shotResponseFromKI.setShotPosition(currentShot);

            if(answerofShot.isHit()){
                destroyStatus = DestroyStatus.destroying;
                nextLocation = NextLocation.nextTop;
                needRandomLocation = true;
                shipRecon = 1;

                shiptoDestroy.add(currentShot);

                isHitFlag = true; //new ship got hit
                firstHit = new Point(currentShot.getX(), currentShot.getY()); //first hit of the ship is getting stored
                previousShots.add(new Point(currentShot.getX(), currentShot.getY()));
                startDestroy = true;
            }
            else{
                this.destroyStatus = DestroyStatus.notFound;
                previousShots.add(new Point(currentShot.getX(), currentShot.getY())); //no hit and the point is getting stored
            }
        }
        else if(destroyStatus == DestroyStatus.destroying){
            return destroyShip(firstHit, playground);
        }
        return true;
    }


    //Variabls for hardKishot- method
    private final ArrayList<Point> takticalDots = new ArrayList<>();
    private boolean searchAnewShip = true;

    //return the point were the hard AI will shoot at
    private Point hardKiShot(){

        //fill Search-Array-List with every Dot that makes sense to shoot except the ones that are already shot
        int searchedShip;
        if(searchAnewShip && ShipsWithSize5 != 0){
            searchedShip = 5;

            fillTacticalDotsList(searchedShip);
        }
        if(searchAnewShip && ShipsWithSize4 != 0){
            searchedShip = 4;

            fillTacticalDotsList(searchedShip);
        }
        if(searchAnewShip && ShipsWithSize3 != 0){
            searchedShip = 3;
            fillTacticalDotsList(searchedShip);
        }
        if(searchAnewShip && ShipsWithSize2 != 0){
            searchedShip = 2;
            fillTacticalDotsList(searchedShip);
        }

        int randy = getRandomInt(0, takticalDots.size() ) ;
        Point newShot = takticalDots.get(randy);
        //remove shifts any subsequent elements to the left
        takticalDots.remove(randy);
        return newShot;
    }

    private void fillTacticalDotsList(int searchedShip) {
        for (int y = 0; y < ActiveGameState.getPlaygroundSize(); y++){
            for (int x = searchedShip - y - 1; x < ActiveGameState.getPlaygroundSize(); x = x + searchedShip){
                if(!isNextShotInPreviousList(x,y) && checkIfTacticMakesSense(new Point(x, y), searchedShip)){
                    takticalDots.add(new Point(x,y));
                }
            }
        }
        searchAnewShip = false;
    }

    private int ShipsWithSize5 = ActiveGameState.getAmountShipSize5();
    private int ShipsWithSize4 = ActiveGameState.getAmountShipSize4();
    private int ShipsWithSize3 = ActiveGameState.getAmountShipSize3();
    private int ShipsWithSize2 = ActiveGameState.getAmountShipSize2();

    private void incrementDestroyedShipFromTotalShipList(int shipSize){

        switch(shipSize){
            case 5:
                ShipsWithSize5--;
                break;
            case 4:
                ShipsWithSize4--;
                break;
            case 3:
                ShipsWithSize3--;
                break;
            case 2:
                ShipsWithSize2--;
                break;
        }
    }

    private boolean checkIfTacticMakesSense(Point p, int size){
        //Point is not in the playground
        if ( !( p.getX() >= 0 && p.getX() < ActiveGameState.getPlaygroundSize() &&
                p.getY() >= 0 && p.getY() < ActiveGameState.getPlaygroundSize()) ) return false;

        int spaceCountHorizontal = 0;
        int spaceCountVertical = 0;

        //check if there is space = shipLength - 1 in any direction

        //right
        for(int i = 1; i<size; i++){
            if(!isNextShotInPreviousList(p.getX() + i , p.getY()) && (p.getX() + i) < ActiveGameState.getPlaygroundSize() ){
                spaceCountVertical++;
            }else{
                break;
            }
        }

        //left
        for(int i = 1; i<size; i++){
            if(!isNextShotInPreviousList(p.getX() - i , p.getY() ) && (p.getX() - i) >= 0){
                spaceCountVertical++;
            }else{
                break;
            }
        }

        //bot
        for(int i = 0; i<size; i++){
            if(!isNextShotInPreviousList(p.getX()  , p.getY() + i ) && (p.getY() + i) < ActiveGameState.getPlaygroundSize()){
                spaceCountHorizontal++;
            }else{
                break;
            }
        }

        //top
        for(int i = 0; i<size; i++){
            if(!isNextShotInPreviousList(p.getX() , p.getY()  - i ) && (p.getY() - i) >= 0){
                spaceCountHorizontal++;
            }else{
                break;
            }
        }

        return spaceCountHorizontal >= size - 1 || spaceCountVertical >= size - 1;
    }

    protected ArrayList<Point> shiptoDestroy = new ArrayList<>();

    //random searchstyle for every hit after the firstHit
    public NextLocation getRandomLocation(){
        ArrayList<NextLocation> nextLocationArrayList = new ArrayList<>();
        nextLocationArrayList.add(NextLocation.nextTop);
        nextLocationArrayList.add(NextLocation.nextBottom);
        nextLocationArrayList.add(NextLocation.nextRight);
        nextLocationArrayList.add(NextLocation.nextLeft);

        Random random = new Random();
        return nextLocationArrayList.get(   random.nextInt(nextLocationArrayList.size()-1)   );
    }
    //normal shoot method and some communication stuff
    public ShotResponse shootPlayground(Point nextPositionToShoot, IOwnPlayground playground){
        if ( !ActiveGameState.isMultiplayer()) {
            shotResponseFromKI = playground.shoot(nextPositionToShoot);
        }
        else{
            shotResponseFromKI = getShootOverCommunication(nextPositionToShoot);
            if ( shotResponseFromKI.isUnhandled() ) return shotResponseFromKI;
        }

        shotResponseFromKI.setShotPosition(nextPositionToShoot);
        return  shotResponseFromKI;

    }


    /**
     * Send shot, and receives a command
     * @param posToShot The position to shot at
     * @return ShotResponse, with the information: hit and destroyed when unhandled is false
     *         When unhandled is false, the shotResponseObject contains the received CMD and the shotResponse Object has to be returned
     *         The unhandled shotResponseObject will get handled at the MultiplayerControlThreadKiShootsEnemy
     */
    public ShotResponse getShootOverCommunication(Point posToShot){
        String cmdParameter = (posToShot.getX()+1) + " " + (posToShot.getY()+1);
        String[] cmdReceived;
        if ( ActiveGameState.isAmIServer()){
            ActiveGameState.getServer().sendCMD(CMD.shot, cmdParameter);
            cmdReceived = ActiveGameState.getServer().getCMD();
        }
        else{
            ActiveGameState.getClient().sendCMD(CMD.shot, cmdParameter);
            cmdReceived = ActiveGameState.getClient().getCMD();
        }

        if ("answer".equals(cmdReceived[0])) {
            ShotResponse shotResponse = new ShotResponse();
            switch (Integer.parseInt(cmdReceived[1])) {
                case 0:
                    shotResponse.setHit(false);
                    shotResponse.setShipDestroyed(false);
                    break;
                case 1:
                    shotResponse.setHit(true);
                    shotResponse.setShipDestroyed(false);
                    break;
                case 2:
                    shotResponse.setHit(true);
                    shotResponse.setShipDestroyed(true);
            }
            return shotResponse;
        }
        return new ShotResponse(true, cmdReceived[0]);

    }

    private int shipRecon = 0;

    //will destroy the found ship
    private boolean destroyShip(Point firstHit, IOwnPlayground playground){

        if (this.needRandomLocation) nextLocation = getRandomLocation();
        this.needRandomLocation = false;

        Point nextPositionToShoot;

        int counter = 0;
        while (counter <= 2) {
            counter++;

            switch (this.nextLocation) {
                case nextTop:
                    nextPositionToShoot = new Point(firstHit.getX(), firstHit.getY() - rangeToShot);
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getY() >= 0)) {

                        previousShots.add(nextPositionToShoot);

                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI.isUnhandled() ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            //part for hard AI
                            shipRecon++;
                            incrementDestroyedShipFromTotalShipList(shipRecon);
                            searchAnewShip = true;
                            takticalDots.clear();

                            logKi.log(Level.FINE, "Ship, which was killed: " + shipRecon);

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));

                            shiptoDestroy.clear();

                            shipRecon = 0;
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextTop;

                            shiptoDestroy.add(nextPositionToShoot);
                            shipRecon++;
                            this.rangeToShot++;
                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextBottom;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    //shotposition is not valid, switch in to next case
                    rangeToShot = 1;
                case nextBottom:
                    nextPositionToShoot = new Point(firstHit.getX(), firstHit.getY() + rangeToShot);
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getY() < ActiveGameState.getPlaygroundSize())) {

                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI.isUnhandled() ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            //part for hard AI
                            shipRecon++;
                            incrementDestroyedShipFromTotalShipList(shipRecon);
                            searchAnewShip = true;
                            takticalDots.clear();

                            logKi.log(Level.FINE, "Ship, which was killed: " + shipRecon);

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));

                            shiptoDestroy.clear();

                            shipRecon = 0;
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextBottom;
                            this.rangeToShot++;

                            shiptoDestroy.add(nextPositionToShoot);
                            shipRecon++;
                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextTop;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    //shotposition is not valid, switch in to next case
                    rangeToShot = 1;
                case nextRight:
                    nextPositionToShoot = new Point(firstHit.getX() + rangeToShot, firstHit.getY());
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getX() < ActiveGameState.getPlaygroundSize())) {

                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI.isUnhandled() ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            //part for hard AI
                            shipRecon++;
                            incrementDestroyedShipFromTotalShipList(shipRecon);
                            searchAnewShip = true;
                            takticalDots.clear();


                            logKi.log(Level.FINE, "Ship, which was killed: " + shipRecon);


                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));

                            shiptoDestroy.clear();

                            shipRecon = 0;

                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextRight;
                            this.rangeToShot++;


                            shiptoDestroy.add(nextPositionToShoot);
                            shipRecon++;

                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextLeft;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    //shotposition is not valid, switch in to next case
                    rangeToShot = 1;
                case nextLeft:
                    nextPositionToShoot = new Point(firstHit.getX() - rangeToShot, firstHit.getY());
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getX() >= 0)) {

                        //Schiessposition erlaubt
                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI.isUnhandled() ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            //part for hard AI
                            shipRecon++;
                            incrementDestroyedShipFromTotalShipList(shipRecon);
                            searchAnewShip = true;
                            takticalDots.clear();

                            logKi.log(Level.INFO, "Ship, which was killed: " + shipRecon);

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));

                            shiptoDestroy.clear();

                            shipRecon = 0;
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextLeft;
                            this.rangeToShot++;
                            shiptoDestroy.add(nextPositionToShoot);
                            shipRecon++;
                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextTop;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    rangeToShot = 1;
                    this.nextLocation = NextLocation.nextTop;
            }
        }
        return false;
    }

}
