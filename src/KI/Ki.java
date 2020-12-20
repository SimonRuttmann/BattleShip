package KI;

import Model.Playground.*;
import Model.Ship.*;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Player.ActiveGameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;

public class Ki implements IKi{

    protected int Playgroundsize;

    public Ki(){
        Playgroundsize = ActiveGameState.getPlaygroundSize();
    }

    public ArrayList<IShip> placeships(OwnPlayground playground) {

        int random_x;
        int random_y;
        ArrayList<Point> occupiedDotsList = new ArrayList<>();
        ArrayList<IShip> kiShips = new ArrayList<>();
        ArrayList<IShip> newShips = new ArrayList<>();


        //Alles Schifftypen werden in eine Arraylist gespeichert damit sie unabhängig bearbeitet werden können
        for(int u = 0; u <= ActiveGameState.getAmountShipSize2(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 1), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize3(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 2), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize4(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 3), playground);
            kiShips.add(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize5(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 4), playground);
            kiShips.add(ship);
        }

        //TODO PLaygroubndsize ist von 0 bis playgroundsize - 1, kann sein dass dabei noch irgendwo ein fehler auftreten kann
        /*Ablauf:
        1. Zufälliger Punkt wird erstellt und ein dazu eine gültige und passende Ausrichtung des Schiffs
        2. Die Schiffspunkte werden in eine Arrayliste gespeichert
        3. Das Schiff wird surrounded und die Punkte ebenfalls in die Arrayliste gespeichert
        4. Die start und endposition des Schiffs wird gespeichert und eine Liste mit den Schiffen zurückgegeben
         */
        for (IShip kiShip : kiShips) {

            ArrayList<Point> currentShipDots;
            int placementStyle;
            //In der do-While Schleife wird ein zufälliger Punkt erzeugt und überprüft ob es für ihn einen Style/ eine Ausrichtung gibt die gültig bzw. möglich ist
            do {
                random_x = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
                random_y = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
                placementStyle = getPlacementStyle(new Point(random_x, random_y), kiShip.getSize(), ActiveGameState.getPlaygroundSize(), occupiedDotsList);
            } while (placementStyle < 0);
            //wurde ein gültiger Style gefunden, dann werden die Flächen des Schiffs gespeichert
            occupiedDotsList.addAll(markShipDots(new Point(random_x, random_y), kiShip.getSize(), placementStyle, occupiedDotsList)); //die Punkte des Schiffs werden in die Liste gespeichert
            //das Aktuelle Schiff wird in eine extra Liste gespeichert
            currentShipDots = markShipDots(new Point(random_x, random_y), kiShip.getSize(), placementStyle, occupiedDotsList);
            //die umgebenden Schiffspunkte in die occupiedDotsList hinzufügen
            occupiedDotsList.addAll(surroundShipDots(currentShipDots));

            //ändern des start und endwertes der Position des Schiffs
            Point newEndPos = currentShipDots.get(currentShipDots.size() - 1);
            newShips.add(new Ship(new Point(random_x, random_y), newEndPos, playground));
        }
        return newShips;
    }

    //Markiert alle Punkte um das Schiff herum
    protected ArrayList<Point> surroundShipDots(ArrayList<Point> currentShipDots) {
        ArrayList<Point> surrDots = null;
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
        if(list == null) {
            return false;
        }
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getX() == p.getX() && list.get(i).getY() == p.getY()){
               return true;
            }
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
        boolean check = false;
        do{
            style = getRandomInt(0 , 3);
            if(style == 0){
                for(int i = 0; i < size; i++){
                    if(checkArrayList(list, new Point(p.getX(), p.getY() - i))){
                        check = true;
                    }
                }
                if(p.getY() - size >= 0 && !check)
                    return 0;
                else
                    check = true;
            }else if(style == 1){
                for(int i = 0; i < size; i++){
                    if(checkArrayList(list, new Point(p.getX() + i, p.getY()))){
                        check = true;
                    }
                }
                if(p.getX() + size < playgrounds && !check)
                    return 1;
                else
                    check = true;
            }else if(style == 2){
                for(int i = 0; i < size; i++){
                    if(checkArrayList(list, new Point(p.getX(), p.getY() + i))){
                        check = true;
                    }
                }
                if(p.getY() + size < playgrounds && !check)
                    return 2;
                else
                    check = true;
            }else if(style == 3){
                for(int i = 0; i < size; i++){
                    if(checkArrayList(list, new Point(p.getX() - i, p.getY()))){
                        check = true;
                    }
                }
                if(p.getX() - size >= 0 && !check)
                    return 3;
                else
                    check = true;
            }
            count++;
        }while(check && count <= 20);
       return -1;
    }


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
    public Point getShot(OwnPlayground playground) {
        //die Ki besitzt die Schwierigkeit = normal
        if(ActiveGameState.getDifficulty() == 0){
            Point returnShot;
            returnShot = normaleKi(playground);
            return returnShot;
            //TODO die normale KI unbedingt zuerst Testen sobald möglich !!
            // die Ki besitzt die Schwierigkeit = schwer
        }else{

        }
        return null;
    }

    protected boolean isHitFlag; //default Wert ist false
    protected ArrayList<Point> previousShots;
    protected Point firstHit;
    protected boolean startDestroy;
    protected Point normalKiShot;

    protected Point normaleKi(OwnPlayground playground){
        ShotResponse answerofShot;
        int random_x;
        int random_y;

        //wird beim ersten Aufruf betreten, da der Default Wert von isHitFlag = false ist
        if(!isHitFlag && !startDestroy){
            //solange der Punkt schon beschossen wurde wird ein neuer Punkt gesucht
            do {
                random_x = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
                random_y = getRandomInt(0, ActiveGameState.getPlaygroundSize() - 1);
            }while (checkArrayList(previousShots, new Point(random_x,random_y)));
            answerofShot = playground.shoot(new Point(random_x,random_y));

            if(answerofShot.isHit()){
            isHitFlag = true; //bei suche nach schiff wurde ein Treffer gelanded
            firstHit = new Point(random_x,random_y); //der erste Punkt des Schiffs der getroffen wurde wird gespeichert
            previousShots.add(new Point(random_x, random_y));//der Punkt wird gespeichert
            startDestroy = true;
            return firstHit;
        }else{
            previousShots.add(new Point(random_x, random_y)); //es ist kein Treffer und der Punkt wird gespeichert und zuückgegeben
            return new Point(random_x,random_y);
        }
    }
        if(startDestroy){
           try{
               normalKiShot = destroyShip(firstHit,playground);
           }catch (Exception e){
               System.out.println("Fehler in der destroyShip Methode");
               e.printStackTrace();
           }
        }
        return normalKiShot;
    }

    //Alle Variablen die für die destroyShipNotonEdge gebraucht werden
    protected boolean vertFlagOben;
    protected boolean horizFlagRechts;
    protected boolean vertFlagUnten;
    protected boolean horizFlagLinks;
    protected boolean waslastShotaHit = true;
    protected int countDestroyShots = -1;
    protected boolean isShipcomDestroyed;
    protected ArrayList<Point> shiptoDestroy;

    //TODO stand jz sollte die Ki zufällig ein Schiff finden und wenn gefunden auch zerstören (ohne große Taktik)
    //soll das gefundene Schiff zerstören
    private Point destroyShip(Point firstHit,OwnPlayground playground ){
        ShotResponse answerofShot;
        //wenn Schiff zerstört wurde wird Ausgangszustand wieder hergestellt
        if(isShipcomDestroyed){
            vertFlagOben = false;
            horizFlagRechts = false;
            horizFlagLinks = false;
            countDestroyShots = -1;
            isShipcomDestroyed = false;
        }

        if(waslastShotaHit){
            countDestroyShots++;
        }

        //es wird vom firstHit aus nach oben geschossen, wenn der Rand erreicht ist und das Schiff nicht zerstört, dann wird nach unten vom firstHit aus geschossen bis es zerstört ist
        //wird ein leeres Feld beim beschießen der Felder nach oben getroffen, wird nach unten geschossen bis das Schiffzerstört ist
        if( !horizFlagRechts  && !vertFlagUnten  && !horizFlagLinks ){
            if(firstHit.getY() - countDestroyShots - 1 >= 0){
                Point newHit = new Point(firstHit.getX(), firstHit.getY() - countDestroyShots - 1);
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = false;
                    vertFlagOben = true;
                    horizFlagLinks = false;
                    horizFlagRechts = false;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = true;
                    horizFlagLinks = false;
                    horizFlagRechts = false;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }else{      //wenn die Schüsse nach oben den Rand erreichen und das Schiff nicht zerstört ist, wird nach unten geschossen bis es zerstört ist (vom firsthit punkt aus)
                Point newHit = new Point(firstHit.getX(), firstHit.getY() + countDestroyShots + 1);
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = true;
                    vertFlagOben = false;
                    horizFlagLinks = false;
                    horizFlagRechts = false;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = false;
                    horizFlagRechts = true;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }
        }

        //ist das Feld über dem firstHit kein Treffer gewesen, wird nach unten beschossen
        if( !horizFlagRechts  && !vertFlagOben  && !horizFlagLinks ){
            if(firstHit.getY() + countDestroyShots + 1 <= ActiveGameState.getPlaygroundSize() - 1){
                Point newHit = new Point(firstHit.getX(), firstHit.getY() + countDestroyShots + 1);
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = true;
                    vertFlagOben = false;
                    horizFlagLinks = false;
                    horizFlagRechts = false;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = false;
                    horizFlagRechts = true;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }

        }else{
            Point newHit = new Point(firstHit.getX(), firstHit.getY() - countDestroyShots - 1);
            answerofShot = playground.shoot(newHit);
            if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                previousShots.add(newHit);
                shiptoDestroy.add(newHit);
                vertFlagUnten = false;
                vertFlagOben = true;
                horizFlagLinks = false;
                horizFlagRechts = false;
                waslastShotaHit = true;
                return newHit;
            }
            else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                previousShots.add(newHit);
                shiptoDestroy.add(newHit);
                previousShots.addAll(surroundShipDots(shiptoDestroy));
                shiptoDestroy.clear();
                isShipcomDestroyed = true;
                startDestroy = false;
                return newHit;
            }else{
                vertFlagOben = false;
                vertFlagUnten = true;
                horizFlagLinks = false;
                horizFlagRechts = false;
                countDestroyShots = -1;
                previousShots.add(newHit);
                return newHit;
            }
        }

        //wenn über und unterhalb des firstHits kein Treffer ist, dann wird rechts vom firstHit geschossen
        if( !vertFlagOben  && !vertFlagUnten  && !horizFlagLinks ){
            if(firstHit.getX() + countDestroyShots + 1 <= ActiveGameState.getPlaygroundSize() - 1){
                Point newHit = new Point(firstHit.getX() + countDestroyShots + 1, firstHit.getY() );
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = false;
                    vertFlagOben = false;
                    horizFlagLinks = false;
                    horizFlagRechts = true;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = true;
                    horizFlagRechts = false;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }else{      //wenn die Schüsse nach rechts den Rand erreichen und das Schiff nicht zerstört ist, wird nach links geschossen bis es zerstört ist (vom firsthit punkt aus)
                Point newHit = new Point(firstHit.getX() - countDestroyShots - 1, firstHit.getY());
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = false;
                    vertFlagOben = false;
                    horizFlagLinks = true;
                    horizFlagRechts = false;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = true;
                    horizFlagRechts = false;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }
        }

        if( !horizFlagRechts  && !vertFlagOben  && !vertFlagUnten){
            if(firstHit.getX() - countDestroyShots - 1 >= 0){
                Point newHit = new Point(firstHit.getX() - countDestroyShots - 1, firstHit.getY());
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = true;
                    vertFlagOben = false;
                    horizFlagLinks = false;
                    horizFlagRechts = false;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = false;
                    horizFlagRechts = true;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
            }
        }else{
                Point newHit = new Point(firstHit.getX() + countDestroyShots + 1, firstHit.getY() );
                answerofShot = playground.shoot(newHit);
                if (answerofShot.isHit() && !answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    vertFlagUnten = false;
                    vertFlagOben = false;
                    horizFlagLinks = false;
                    horizFlagRechts = true;
                    waslastShotaHit = true;
                    return newHit;
                }
                else if (answerofShot.isHit() && answerofShot.isShipDestroyed()){
                    previousShots.add(newHit);
                    shiptoDestroy.add(newHit);
                    previousShots.addAll(surroundShipDots(shiptoDestroy));
                    shiptoDestroy.clear();
                    isShipcomDestroyed = true;
                    startDestroy = false;
                    return newHit;
                }else{
                    vertFlagOben = false;
                    vertFlagUnten = false;
                    horizFlagLinks = true;
                    horizFlagRechts = false;
                    countDestroyShots = -1;
                    previousShots.add(newHit);
                    return newHit;
                }
        }
        return null;
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
