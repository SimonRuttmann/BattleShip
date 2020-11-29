package Model.Util;

import Model.Ship.IShip;
import javafx.scene.control.Label;

public class ShipPart implements IDrawable{

    private Label label;
    private String part;
    private IShip owner = null;
    private boolean shot;

    //ShipsParts in our Field have an associated Ship
    public ShipPart(String part, IShip owner) {
        this.part = part;
        this.owner = owner;
    }

    //ShipParts on the enemy Field don't have an associated Ship, but can be marked as shot immediately
    public ShipPart(String part, boolean shot) {
        this.part = part;
        this.shot = shot;
    }

    public void setShot (boolean shot){
        this.shot = shot;
    }

    @Override
    public Label getLabel(){
        return this.label;
    }

    @Override
    public void setLabel(Label label){
        this.label = label;
    }

    @Override
    public void setLabelNonClickable() {
        this.label.setDisable(false);
    }

    public IShip getOwner() {
        return owner;
    }

    public void setPart(String part) {
        this.part = part;
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
            case "destroyed"         : System.out.print("H");
            default: System.out.println("Alignment not found");

        }
        if ( shot ){
            this.label.setStyle("-fx-background-darkred");
        }
        else{
            this.label.setStyle("-fx-background-darkgreen");
        }
    }

}
