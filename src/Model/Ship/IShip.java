package Model.Ship;

import Model.Util.UtilDataType.Point;

import java.util.ArrayList;


public interface IShip {
    int gethitPoints();
    void sethitPoints(int hitPoints);
    Point[] getPosition();
    int getSize();
    static int getAmount() {
        return Ship.getAmount();
    }

    //TODO not clean... but get from AbstractShip is undesirable and should be avoided
     static ArrayList<IShip> getShipList(){
         return Ship.getShipList();
     }

    void setPosStart();
    void setPosEnd();

    public Point getPosStart();

    public Point getPosEnd();
}
