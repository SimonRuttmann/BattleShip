package KI;

import Model.Playground.*;
import Model.Ship.*;
import Model.Util.UtilDataType.Point;
import Player.ActiveGameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Ki implements IKi{

    protected int Playgroundsize;


    /*  Der Ki wird der erstellte Playground übergeben, um ihr mitzuteilen welche Eigenschaften das SPielfeld hat
        wie beispielsweisse die Playgroundsize
        Ihr wird ein Objekt vom Typ Ship übergeben um auf die ShipList zuzugreifen, um so die Positionen der Schiffe zu ändern*/

    public Ki(){
        Playgroundsize = ActiveGameState.getPlaygroundSize();
    }



    public ArrayList<IShip> placeships() {

        int random_x;
        int random_y;
        ArrayList<Point> occupiedDotsList = new ArrayList<>();
        ArrayList<IShip> kiShips = new ArrayList<>();
        ArrayList<IShip> newShips = new ArrayList<>();


        //Alles Schifftypen werden in eine Arraylist gespeichert und die unabhängig bearbeitet werden können
        for(int u = 0; u <= ActiveGameState.getAmountShipSize2(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 1));
            kiShips.add(ship);
            Ship.getShipList().remove(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize3(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 2));
            kiShips.add(ship);
            Ship.getShipList().remove(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize4(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 3));
            kiShips.add(ship);
            Ship.getShipList().remove(ship);
        }
        for(int u = 0; u <= ActiveGameState.getAmountShipSize5(); u++){
            Ship ship = new Ship(new Point(0,0), new Point(0, 4));
            kiShips.add(ship);
            Ship.getShipList().remove(ship);
        }

        //TODO PLaygroubndsize ist von 0 bis playgroundsize - 1, kann sein dass dabei noch irgendwo ein fehler auftreten kann

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
            newShips.add(new Ship(new Point(random_x, random_y), newEndPos));
        }

        return newShips;
    }

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

    //Hilfsmethode um Point array zu durchsuchen
    protected boolean PointCheck(Point[] array, Point p){
        for(int i = 0; i <= array.length; i++){
            if(array[i].getX() == p.getX() && array[i].getY() == p.getY())
                return true;
        }
        return false;
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
    public int shoot() {
        //die Ki besitzt die Schwierigkeit = normal
        if(ActiveGameState.getDifficulty() == 0){


            //die Ki besitzt die Schwierigkeit = schwer
        }else{

        }
        return 0;
    }


}
