package KI;

import Model.Playground.*;
import Model.Ship.*;
import Model.Util.UtilDataType.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Ki implements IKi{

    protected int Playgroundsize;
    protected AbstactPlayground ap;
    protected int Listsize;
    protected Ship s;
    protected static ArrayList<IShip> ShipList = null;


    /*  Der Ki wird der erstellte Playground übergeben, um ihr mitzuteilen welche Eigenschaften das SPielfeld hat
        wie beispielsweisse die Playgroundsize
        Ihr wird ein Objekt vom Typ Ship übergeben um auf die ShipList zuzugreifen, um so die Positionen der Schiffe zu ändern*/

    public Ki(AbstactPlayground ap){
        this.ap = ap;
        this.Playgroundsize = ap.getPlaygroundsize();
        this.Listsize = Ship.getAmount();
    }

    //TODO besprechen wie die KI auf die Shipliste zugreifen soll, ob über eine Instanz der Klasse Ships oder als übergabeparameter

    public ArrayList placeships(ArrayList<IShip> mainList) {
        IShip dummy;
        int x_Coordinate;
        int y_Coordinate;
        int ship_size;
        int random_x;
        int random_y;
        ArrayList<Point> occupiedDotsList = new ArrayList<Point>();
        ArrayList<Point> currentShipDots;
        ArrayList<Point> surrDots;

        //TODO rausfinden wie bei der Erstellung des Spielfelds vorgegangen wird und bei der random erstellung der int werte evlt auf von 0 bis playgroundsize
        //TODO auf 1 bis playgroundsize -1 ändern

        for(int i = 0; i <= this.Listsize; i++){
            dummy = ShipList.get(i);
            ship_size = dummy.getSize();
            Point start = dummy.getPosStart();
            Point end = dummy.getPosEnd();
            int placementStyle;
            //In der do-While Schleife wird ein zufälliger Punkt erzeugt und überprüft ob es für ihn einen Style/ eine Ausrichtung gibt die gültig bzw. möglich ist
            do {
                random_x = getRandomInt(0, this.Playgroundsize);
                random_y = getRandomInt(0, this.Playgroundsize);
                placementStyle = getPlacementStyle(new Point(random_x, random_y), ship_size, this.Playgroundsize, occupiedDotsList);
            }while (placementStyle < 0);
            //wurde ein gültiger Style gefunden, dann werden die Flächen des Schiffs gespeichert
            occupiedDotsList = markShipDots(new Point(random_x, random_y), ship_size, placementStyle, occupiedDotsList); //die Punkte des Schiffs werden in die Liste gespeichert
            currentShipDots = getcurrShipDots(occupiedDotsList, ship_size); //das Aktuelle Schiff wird in eine extra Liste gespeichert
            surrDots = surroundShipDots(currentShipDots);


            occupiedDotsList.addAll(surrDots);
            //die SurroundingDotListe wieder auf Null setzen damit die surr dots des alten Schiffs nicht mehr übergeben werden sondern nur die des neuen Schiffs
            surrDots = null;

            //TODO die posStart und posEnde der einzelnen Ship objekte müssen noch auf den neuen Wert geändert werden, aber erst klären wie auf die einzelnen Objekte zugegriffen werden soll
        }

        return mainList;
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



    @Override
    public int shoot() {
        return 0;
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
}
