package Model.Util;

import Model.Ship.IShip;

public class ShipPart implements IDrawable{

    private final String part;
    private final IShip owner;

    public ShipPart(String part, IShip owner) {
        this.part = part;
        this.owner = owner;
    }

    public IShip getOwner() {
        return owner;
    }

    @Override
    public void draw() {
        switch (part){
            case "start vertical"    : System.out.print("^");break;
            case "start horizontal"  : System.out.print("<");break;
            case "end vertical"      : System.out.print("v");break;
            case "end horizontal"    : System.out.print(">");break;
            case "middle vertical"   : System.out.print("||");break;
            case "middle horizontal" : System.out.print("=");break;
            default: System.out.println("Position not found");
        }
    }

}
