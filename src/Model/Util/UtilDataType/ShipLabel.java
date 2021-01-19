package Model.Util.UtilDataType;

/**
 * Objects of type ShipLabel contain information needed by the controller`s to display
 * the images of the ships, when the game is loaded
 * An arrayList containing all shipLabel Objects will be saved
 */
public class ShipLabel {
    private final Point posHead;
    private final boolean horizontal;
    private final int size;
    private final boolean living;

    public ShipLabel(Point posHead, boolean horizontal, int size, boolean living) {
        this.posHead = posHead;
        this.horizontal = horizontal;
        this.size = size;
        this.living = living;
    }

    public Point getPosHead() {
        return posHead;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getSize() {
        return size;
    }

    public boolean isLiving() {
        return living;
    }
}
