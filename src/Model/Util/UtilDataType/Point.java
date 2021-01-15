package Model.Util.UtilDataType;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int i, int i1) {
        this.x = i;
        this.y = i1;
    }
}
