package Model.Util.UtilDataType;

import Model.Ship.IShip;
import javafx.scene.control.Label;

/**
 * Objects of this class are needed for representation of ships at placeShips for Drag and Drop
 */
public class ShipLabel extends Label {
    private IShip ship;
    private Point posHead;
    private final boolean horizontal;
    private final int size;

    public ShipLabel(Point posHead, boolean horizontal, int size) {
        this.posHead = posHead;
        this.horizontal = horizontal;
        this.size = size;
    }

    public IShip getShip() { return ship; }
    public void setShip(IShip ship) { this.ship = ship; }

    public Point getPosHead() { return posHead; }
    public void setPosHead(Point posHead) {this.posHead = posHead;}

    public boolean isHorizontal() { return horizontal; }
    public int getSize() { return size; }
}