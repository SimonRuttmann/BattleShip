package KI;

import Model.Playground.*;
import Model.Ship.*;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Network.CMD;
import Player.ActiveGameState;
import com.sun.nio.sctp.IllegalReceiveException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//Wäre whr sinnvoll die Ki in Klassen zu unterteilen, die Ki, bei der placeShips aufgerufen wird, ist die KI als  placementKi in ActiveGamestate gespeichert
//Beim beschießen gibt es 2 instanzen, enemyKi und ownKi, welche im ActiveGameState auch als KI gespeichert sind, diese brauchen die getShot methode
public class Ki implements IKi{


    /**
     * Bugfixed
     * Diese Enumerationen erstzen all deine Booleans:
     *    Alle Variablen die für die destroyShipNotonEdge gebraucht werden
     *
     *
     *
     *     protected boolean vertFlagOben;
     *     protected boolean horizFlagRechts;
     *     protected boolean vertFlagUnten;
     *     protected boolean horizFlagLinks;
     *     protected boolean waslastShotaHit = true;
     *     protected int countDestroyShots = -1;
     *     protected boolean isShipcomDestroyed;
     *
     * Dadurch das die Booleans wegfallen, hat man in den if elseif elseif else Anweisungen nur noch 1 Argument
     * Dass kann man dann mit einem Switch Case sehr einfach machen -> 80% des Codes fällt weg
     *
     * Eine kleine Änderung musste ich noch wegen dem return machen, da du die shoot-Methode bereits aufrufst, darf ich sie im Controller nicht nochmal aufrufen,
     * weshalb nun nicht der punkt, sondern das ergebnis (ShotResponse) des Aufrufs mit playground.shoot(punkt) übergeben wird
     *
     * Codetechnisch/Logiktechnisch ist alles gleich geblieben, außer das zusätzlich noch geprüft wird, ob der Punkt, bei dem man schließen möchte schon als shotWater markiert wurde (dann darf man dort hin nicht schießen)
     *
     *
     * Das hattest du Teilweise schon drin, jedoch wird bei einer ArrayListe arrayList.contains(new Point(x,y)) immer false kommen, da referenzen überprüft werden und
     * intern nicht die equals-Methode (die wir btw garnicht haben :D).
     * Dafür hab ich die Methode isNextShotInPreviousList geschrieben, diese überprüft, ob es in der Liste schon einen ANDEREN Punkt (andere Referenz) gibt mit den gleichen
     * X und Y Koordinaten gibt
     *
     * Hinzugefügt:
     * Kommunikationszeugs (2 Methoden), muss hier leider rein, damit ich die Ki auch für den Modus KI vs Remote verwenden kann
     * Bei der PlaceShips, die RandomX, RandomY durch die Methode(RandomPointExceptSet) ersetzt. Die Methode holt sich nen Punkt aus der verbleibenden Menge an Punkten -> Führt sonst zu (nahezu) Endlosschleifen, wenn nur noch wenige Punkte vorhanden sind
     *
     */

    enum NextLocation { nextTop, nextBottom, nextLeft, nextRight, noDestination}    //Zeigt an, wo man als nächstes hinschießt
    enum DestroyStatus {notFound, destroying}                                       //Gibt an, ob man gerade im Modus Schiff zerstören oder Random schießen ist

    private DestroyStatus destroyStatus = DestroyStatus.notFound;
    private NextLocation nextLocation;                              //nächste position auf die Geschossen wird
    int rangeToShot = 1;                                            //1. Schuss (treffer) nach oben -> rangeToShot wird auf 2 erhöht
                                                                    //2. Schuss (treffer) nach oben -> rangeToShot wird auf 3 erhöht
                                                                    //3. Schuss  (kein treffer) rangeToShot wird auf 1 gesetzt                  und die position ist jetzt nextBottom
                                                                    //1. Schuss (treffer) nach unten -> rangeToShot wird auf 2 erhöht
                                                                    //2. Schuss (kein treffer) nach unten -> rangeToShot wird auf 1 gesetzt     und die position ist jetzt nextleft
                                                                    //...
    ShotResponse shotResponseFromKI;


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

    public Ki(){
        Playgroundsize = ActiveGameState.getPlaygroundSize();
    }

    /**
     * Checks if the values of the hand over point is contained in the arrayList
     * @param pointSet The set
     * @param checkPoint The point to check
     * @return True, if the list contains a point with the same x and y coordinates
     */
    public boolean isPointValueInSet( Set<Point> pointSet, Point checkPoint){
        for ( Point point : pointSet ){
            if (point.getX() == checkPoint.getX() && point.getY() == checkPoint.getY()) return true;
        }
        return false;
    }
private int debugg = 0;

    /**
     * Diese methode hat im schlimmsten fall quadratische Laufzeit !!!
     * Stochastisch jetzt deterministisch, (keine Random abbrüche mehr)
     * DEUTLICHES Optimierungspotenzial möglich -> ArrayListe wir einmalig initialisiert und elemente werden einzeln abgezogen, hier overkill, jedes mal neu initialisieren und eine Menge an Punkten (im durchschnitt ~ 70) abziehen
     * @param usedPoints The Set, where the Points should not come from
     * @return Point from the Playground except from the Set usedPoints
     */
    private Point randomPointInPlaygroundExceptSet(Set<Point> usedPoints){

        //Create random point pool
        ArrayList<Point> randomPointPool = new ArrayList<>();
        for (int x = 0; x < ActiveGameState.getPlaygroundSize(); x++){
            for (int y = 0; y < ActiveGameState.getPlaygroundSize(); y++){
                randomPointPool.add(new Point(x, y));
            }
        }

        //Remove values from the Set
        for(Point point : usedPoints){
            randomPointPool.removeIf(point1 -> point.getX() == point1.getX() && point.getY() == point1.getY());
        }

        Random random = new Random();
        //Kein Element mehr vorhanden
        if (randomPointPool.size() == 0) return null;
        //Ein Element vorhanden (random.nextInt braucht eine Zahl > 0)
        if (randomPointPool.size() == 1) return randomPointPool.get(0);
        //Element aus der verbleibenden Menge auswählen
        return randomPointPool.get(random.nextInt(randomPointPool.size()-1));
    }

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

        /*Ablauf (alt):
        1. Zufälliger Punkt wird erstellt und ein dazu eine gültige und passende Ausrichtung des Schiffs
        2. Die Schiffspunkte werden in eine Arrayliste gespeichert
        3. Das Schiff wird surrounded und die Punkte ebenfalls in die Arrayliste gespeichert
        4. Die start und endposition des Schiffs wird gespeichert und eine Liste mit den Schiffen zurückgegeben
         */

        //Rekursionsauflösung alle Schiffe plaziert
        if ( currentShipToPlace >= kiShips.size()){
            return newShips;
        }


        int random_x;
        int random_y;
        int placementStyle;
        int counter = 0;
        Set<Point> checkedPointsSet = new HashSet<>(occupiedDotsList);

        //Plaziere Schiff an der Stelle laufvariable in der Liste
        do {
            System.out.println( "1: " +debugg++);                                       //Kritischer Bereich Anfang

            IShip kiShip = kiShips.get(currentShipToPlace);
            //random_x = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
            //random_y = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
            Point point = this.randomPointInPlaygroundExceptSet(checkedPointsSet);
            if ( point == null) return null; // Every single Point tested
            random_x = point.getX();
            random_y = point.getY();
            //Point point = new Point ( random_x, random_y);
            placementStyle = -1;
            System.out.println( "1.a: " + debugg++);

            if (isPointValueInSet(checkedPointsSet, point)) continue;                      //Kritischer Bereich Ende


            System.out.println( "1.b: " + debugg++);
            checkedPointsSet.add(point);
            placementStyle = getPlacementStyle(new Point(random_x, random_y), kiShip.getSize(), ActiveGameState.getPlaygroundSize(), occupiedDotsList);
            counter++;

            // Für jede gültige Platzierung, führe die Rekursion aus
            if ( placementStyle >= 0){
                Point newEndPos;

                //Ermittle Schiffspositionen
                switch(placementStyle){
                    case 0: newEndPos = new Point ( random_x ,                        random_y-kiShip.getSize()+1); break;      //Oben
                    case 1: newEndPos = new Point ( random_x + kiShip.getSize()-1,    random_y); break;                         //Rechts
                    case 2: newEndPos = new Point ( random_x ,                        random_y+kiShip.getSize()-1); break;      //Unten
                    case 3: newEndPos = new Point ( random_x - kiShip.getSize()+1,    random_y); break;                         //Links
                    default:
                        throw new IllegalStateException("Unexpected value: " + placementStyle);
                }

                //Füge Schiff mit den Positionen hinzu
                newShips.add(new Ship(new Point(random_x, random_y), newEndPos, playground));

                System.out.println("2 :" + debugg++);
                //Schiffspositionen + Umgebungspositionen
                ArrayList<Point> shipPositions = new ArrayList<>();
                shipPositions = markShipDots(point, kiShip.getSize(), placementStyle, shipPositions);

                ArrayList<Point> shipPositionsAndSurroundings = surroundShipDots(shipPositions);
                shipPositionsAndSurroundings.addAll(shipPositions);


                //Bishige Schiffspositionen (aus früheren Rekursionsschritten + Schiffspostion + Umgebungsposition von aktuellem Rekursionschritt)
                ArrayList<Point> newOccupiedDotsList = new ArrayList<Point>(occupiedDotsList);
                newOccupiedDotsList.addAll(shipPositionsAndSurroundings);

                currentShipToPlace++;
                ArrayList<IShip>  result = placeShip(newOccupiedDotsList, kiShips, newShips, playground, currentShipToPlace);
                currentShipToPlace--;

                // Für diese Plazierung des Schiffs gibt es keine Möglichkeit die restlichen zu plazieren
                if ( result == null){
                    placementStyle = -404;
                    newShips.remove(kiShip);
                }
            }

            if ( currentShipToPlace == 0) System.out.println( counter);
        } while (placementStyle < 0 && counter < ActiveGameState.getPlaygroundSize()*ActiveGameState.getPlaygroundSize());
        System.out.println( "3: "+  debugg++);
        //Rekursionsauflösung, Schiff kann nicht plaziert werden
        if ( placementStyle < 0){
            return null;
        }

        //Es hat funktioniert
        return newShips;
    }

    public ArrayList<IShip> placeships(IOwnPlayground playground) {


        ArrayList<Point> occupiedDotsList = new ArrayList<>();
        ArrayList<IShip> kiShips = new ArrayList<>();
        ArrayList<IShip> newShips = new ArrayList<>();

        //Alles Schifftypen werden in eine Arraylist gespeichert damit sie unabhängig bearbeitet werden können
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

        //TODO PLaygroubndsize ist von 0 bis playgroundsize - 1, kann sein dass dabei noch irgendwo ein fehler auftreten kann
        /*Ablauf:
        1. Zufälliger Punkt wird erstellt und ein dazu eine gültige und passende Ausrichtung des Schiffs
        2. Die Schiffspunkte werden in eine Arrayliste gespeichert
        3. Das Schiff wird surrounded und die Punkte ebenfalls in die Arrayliste gespeichert
        4. Die start und endposition des Schiffs wird gespeichert und eine Liste mit den Schiffen zurückgegeben
         */


       return placeShip(occupiedDotsList, kiShips, newShips, playground, 0);


    }

    //Markiert alle Punkte um das Schiff herum
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

    protected ArrayList<Point> getcurrShipDots(ArrayList<Point> ocuList, int shipsize) {
        ArrayList<Point> currShip = null;

        for(int i = ocuList.size() - shipsize ; i < ocuList.size() ; i++){
            currShip.add(ocuList.get(i));
        }
        return currShip;
    }

    //prüft ob der Punkt in der übergebenen Arrayliste vorhanden ist
   protected boolean checkArrayList(ArrayList<Point> list, Point p){
        for( Point point : list){
            if (point.getX() == p.getX() && point.getY() == p.getY()) return true;
        }
        return false;

   }

    //Markiert die Punkte des aktuelle Schiffs
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


        /*Prüft für den random Punkt, ob das Ship von ihm aus ins Feld Passt und eine zufällige richtung, wird in 20 versuchen kein Ergebnis gefunden, wird in der
        do -while-Schleife in der placeships- Methode ein neuer zufälliger Punkt erstellt  und für diesen dann diese Überprüfung erneut gemacht*/
        //TODO abchecken wie die Playgroundgröße vorgegeben ist, dann eventl die vergleiche < <= und so anpasssen
    protected int getPlacementStyle(Point p, int size, int playgrounds, ArrayList<Point> list){
        int style;
        int count = 0;
        boolean placeable;
        style = getRandomInt(0 , 3);
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

    /*
    if(style == 0){
                for(int i = 0; i < size; i++){
                    if(isPointValueInList(list, new Point(p.getX(), p.getY() - i))){
                        check = true;
                    }
                }
                if(p.getY() - size >= 0 && !check)
                    return 0;
                else
                    check = true;
            }else if(style == 1){
                for(int i = 0; i < size; i++){
                    if(isPointValueInList(list, new Point(p.getX() + i, p.getY()))){
                        check = true;
                    }
                }
                if(p.getX() + size < playgrounds && !check)
                    return 1;
                else
                    check = true;
            }else if(style == 2){
                for(int i = 0; i < size; i++){
                    if(isPointValueInList(list, new Point(p.getX(), p.getY() + i))){
                        check = true;
                    }
                }
                if(p.getY() + size < playgrounds && !check)
                    return 2;
                else
                    check = true;
            }else if(style == 3){
                for(int i = 0; i < size; i++){
                    if(isPointValueInList(list, new Point(p.getX() - i, p.getY()))){
                        check = true;
                    }
                }
                if(p.getX() - size >= 0 && !check)
                    return 3;
                else
                    check = true;
            }
     */


    //erstellt zufällig eine int zahl in einer vorgegebenen range also von bis
    private static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min in KI");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    //gibt einen Punkt zurück
    @Override
    public ShotResponse getShot(IOwnPlayground playground) {
        //die Ki besitzt die Schwierigkeit = normal
        if(ActiveGameState.getDifficulty() == 0){
            Point returnShot;
            boolean success = normaleKi(playground);
            if ( !success ) return null;
            return this.shotResponseFromKI;
            //TODO die normale KI unbedingt zuerst Testen sobald möglich !!
            // die Ki besitzt die Schwierigkeit = schwer
        }else{
            //TODO schwere KI
        }
        return null;
    }

    protected boolean isHitFlag; //default Wert ist false
    protected ArrayList<Point> previousShots = new ArrayList<>();
    protected Point firstHit;
    protected boolean startDestroy;
    protected Point normalKiShot;
    private boolean needRandomLocation;

    protected boolean normaleKi(IOwnPlayground playground){
        ShotResponse answerofShot;
        int random_x;
        int random_y;

        //wird beim ersten Aufruf betreten, da der Default Wert von isHitFlag = false ist
        if(this.destroyStatus == DestroyStatus.notFound){
            //solange der Punkt schon beschossen wurde wird ein neuer Punkt gesucht
            do {
                random_x = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
                random_y = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
            }while (isNextShotInPreviousList(random_x,random_y));
            System.out.println("first shoot call");

            Point currentShot = new Point(random_x, random_y);

            answerofShot = shootPlayground(currentShot, playground);
            if ( shotResponseFromKI == null ) return false;


            this.shotResponseFromKI = answerofShot;
            shotResponseFromKI.setShotPosition(currentShot);
            previousShots.add(currentShot);


            System.out.println(random_x);
            System.out.println(random_y);

            if(answerofShot.isHit()){
                destroyStatus = DestroyStatus.destroying;
                nextLocation = NextLocation.nextTop;
                needRandomLocation = true;

                isHitFlag = true; //bei suche nach schiff wurde ein Treffer gelanded
                firstHit = new Point(random_x,random_y); //der erste Punkt des Schiffs der getroffen wurde wird gespeichert
                //previousShots.add(new Point(random_x, random_y));//der Punkt wird gespeichert
                startDestroy = true;

            }
            else{
                this.destroyStatus = DestroyStatus.notFound;
                //previousShots.add(new Point(random_x, random_y)); //es ist kein Treffer und der Punkt wird gespeichert und zuückgegeben

            }
        }

        else if(destroyStatus == DestroyStatus.destroying){

            return destroyShip(firstHit, playground);


        }

        return true;
    }

    //Alle Variablen die für die destroyShipNotonEdge gebraucht werden
   /*

    All diese booleans kann man durch die Enum nextLocation ersetzten, wodurch die Abfragebedingungen wegfallen, sowie die initialisierungs if´s und rücksetz if´s wegfallen

    protected boolean vertFlagOben;
    protected boolean horizFlagRechts;
    protected boolean vertFlagUnten;
    protected boolean horizFlagLinks;
    protected boolean waslastShotaHit = true;
    protected int countDestroyShots = -1;
    protected boolean isShipcomDestroyed;
    */

    protected ArrayList<Point> shiptoDestroy = new ArrayList<>();

    //Kleine Erweiterung, damit die Ki nicht immer oben anfängt
    public NextLocation getRandomLocation(){
        ArrayList<NextLocation> nextLocationArrayList = new ArrayList<>();
        nextLocationArrayList.add(NextLocation.nextTop);
        nextLocationArrayList.add(NextLocation.nextBottom);
        nextLocationArrayList.add(NextLocation.nextRight);
        nextLocationArrayList.add(NextLocation.nextLeft);

        Random random = new Random();
        return nextLocationArrayList.get(   random.nextInt(nextLocationArrayList.size()-1)   );
    }
    //Normale Shoot Methode mit Communication zeugs
    public ShotResponse shootPlayground(Point nextPositionToShoot, IOwnPlayground playground){
        if ( !ActiveGameState.isMultiplayer()) {
            shotResponseFromKI = playground.shoot(nextPositionToShoot);
        }
        else{
            shotResponseFromKI = getShootOverCommunication(nextPositionToShoot);
            if ( shotResponseFromKI == null ) return null;
        }

        shotResponseFromKI.setShotPosition(nextPositionToShoot);
        return  shotResponseFromKI;

    }

    //communication zeugs
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

        if ( cmdReceived[0].equals("timeout")){
            return null;
        }

        if (! cmdReceived[0].equals("answer") ){
            throw new IllegalReceiveException();
        }
        else{
            ShotResponse shotResponse = new ShotResponse();
            switch (Integer.parseInt(cmdReceived[1])){
                case 0: shotResponse.setHit(false);
                        shotResponse.setShipDestroyed(false);
                        break;
                case 1: shotResponse.setHit(true);
                        shotResponse.setShipDestroyed(false);
                        break;
                case 2: shotResponse.setHit(true);
                        shotResponse.setShipDestroyed(true);
            }
            return shotResponse;
        }
    }

    private int debug= 0;

    //TODO stand jz sollte die Ki zufällig ein Schiff finden und wenn gefunden auch zerstören (ohne große Taktik)
    //soll das gefundene Schiff zerstören
    private boolean destroyShip(Point firstHit,IOwnPlayground playground ){

        if (this.needRandomLocation) nextLocation = getRandomLocation();
        this.needRandomLocation = false;
        System.out.println("Next Position to shoot: " + this.nextLocation + " rangeToShot: " + rangeToShot);

        Point nextPositionToShoot;

        while (true) {
            System.out.println( "while ture durchlauf"); //TODO Endlosschleife
            System.out.println("Next Position to shoot: " + this.nextLocation + " rangeToShot: " + rangeToShot + "Schießursprung: " + firstHit.getX() + " " + firstHit.getY());
            switch (this.nextLocation) {
                case nextTop:
                    nextPositionToShoot = new Point(firstHit.getX(), firstHit.getY() - rangeToShot);
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getY() >= 0)) {

                        //Schiessposition erlaubt
                        previousShots.add(nextPositionToShoot);

                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI == null ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextTop;
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
                    //Schiessposition nicht erlaubt, gehe in den nächsten case
                    rangeToShot = 1;
                case nextBottom:
                    nextPositionToShoot = new Point(firstHit.getX(), firstHit.getY() + rangeToShot);
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getY() < ActiveGameState.getPlaygroundSize())) {

                        //Schiessposition erlaubt
                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI == null ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextBottom;
                            this.rangeToShot++;
                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextRight;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    //Schiessposition nicht erlaubt, gehe in den nächsten case
                    rangeToShot = 1;
                case nextRight:
                    nextPositionToShoot = new Point(firstHit.getX() + rangeToShot, firstHit.getY());
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getX() < ActiveGameState.getPlaygroundSize())) {

                        //Schiessposition erlaubt
                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI == null ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));
                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextRight;
                            this.rangeToShot++;
                        }
                        //no Hit
                        else {
                            this.nextLocation = NextLocation.nextLeft;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    //Schiessposition nicht erlaubt, gehe in den nächsten case
                    rangeToShot = 1;
                case nextLeft:
                    nextPositionToShoot = new Point(firstHit.getX() - rangeToShot, firstHit.getY());
                    if ((!isNextShotInPreviousList(nextPositionToShoot.getX(), nextPositionToShoot.getY()))
                            && (nextPositionToShoot.getX() >= 0)) {

                        //Schiessposition erlaubt
                        previousShots.add(nextPositionToShoot);
                        shotResponseFromKI = shootPlayground(nextPositionToShoot, playground);
                        if ( shotResponseFromKI == null ) return false;

                        //Hit and Destroyed
                        if (shotResponseFromKI.isShipDestroyed()) {
                            this.nextLocation = NextLocation.noDestination;
                            this.rangeToShot = 1;
                            this.destroyStatus = DestroyStatus.notFound;

                            shiptoDestroy.add(nextPositionToShoot);
                            previousShots.addAll(surroundShipDots(shiptoDestroy));

                        }
                        //Hit
                        else if (shotResponseFromKI.isHit()) {
                            this.nextLocation = NextLocation.nextLeft;
                            this.rangeToShot++;
                            shiptoDestroy.add(nextPositionToShoot);
                        }
                        //no Hit
                        else {
                            System.out.println("Can't be, shoot at left and all other positions are cleared");
                            this.nextLocation = NextLocation.nextTop;
                            this.rangeToShot = 1;
                        }

                        //Leave the switch case, as we have already shot the enemy
                        return true;
                    }
                    rangeToShot = 1;
                    //Notwendig, damit von oben aus alle Punkte wieder erreicht werden können
                    //Bsp. Start at nextLeft -> NextLeft aber nicht möglich -> NextLeft -> NextLeft -> NextLeft -> Endlos
                    this.nextLocation = NextLocation.nextTop;
            }
        }

        //TODO die KI soll nicht systematisch um den firstHit herum schießen -> mit random arbeiten
        //es wird vom firstHit aus nach oben geschossen, wenn der Rand erreicht ist und das Schiff nicht zerstört, dann wird nach unten vom firstHit aus geschossen bis es zerstört ist
        //wird ein leeres Feld beim beschießen der Felder nach oben getroffen, wird nach unten geschossen bis das Schiffzerstört ist

    }

    protected Point getLastShot(ArrayList<Point> prevShots){
        return prevShots.get(prevShots.size() - 1);
    }

    //checkt ob der Letze Treffer am Rand des Spielfelds war und falls ja an welchem Rand, 0 = oben 1 = rechts 2 = unten 3 = links,
    //4 = linksoben, 5 = rechtsoben, 6 = rechtsunten, 7 = linksunten
    protected int checkifLastHitwasAtEdge(Point lastHit){
        if(lastHit.getX() - 1 < 0 && lastHit.getY() + 1 > ActiveGameState.getPlaygroundSize() - 1){
            return 7;
        }
        if(lastHit.getX() + 1 > ActiveGameState.getPlaygroundSize() - 1 && lastHit.getY() + 1 > ActiveGameState.getPlaygroundSize() - 1){
            return 6;
        }
        if(lastHit.getX() + 1 > ActiveGameState.getPlaygroundSize() - 1 && lastHit.getY() - 1 < 0 ){
            return 5;
        }
        if(lastHit.getX() - 1 < 0 && lastHit.getY() - 1 < 0){
            return 4;
        }
        if(lastHit.getX() - 1 < 0){
            return 3;
        }
        if(lastHit.getY() + 1 > ActiveGameState.getPlaygroundSize() - 1){
            return 2;
        }
        if(lastHit.getX() + 1 > ActiveGameState.getPlaygroundSize() - 1){
            return 1;
        }
        if(lastHit.getY() - 1 < 0){
            return 0;
        }
        return -1;
    }

}
