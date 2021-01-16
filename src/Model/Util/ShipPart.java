package Model.Util;

import Model.Ship.IShip;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShipPart implements IDrawable{

    private transient Label label;
    private IShip owner = null;
    private boolean shot;
    private boolean validShipPlacementMarker = true;

    //ShipsParts in our Field have an associated Ship
    public ShipPart(IShip owner) {
        this.owner = owner;
    }

    public ShipPart(IShip owner, boolean shot){
        this.owner = owner;
        this.shot = shot;
    }

    //ShipParts on the enemy Field don't have an associated Ship, but can be marked as shot immediately
    public ShipPart(boolean shot) {
        this.shot = shot;
    }

    public boolean isShot (){
        return this.shot;
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
        if ( this.label == null ) return;
        Platform.runLater(()-> this.label.setDisable(true));
    }

    @Override
    public void setLabelClickable() {
        if ( this.label == null ) return;
        Platform.runLater(()-> this.label.setDisable(false));
    }

    @Override
    public void setValidShipPlacementMarker( boolean valid) {
        this.validShipPlacementMarker = valid;
    }

    @Override
    public boolean getValidShipPlacementMarker() {
        return this.validShipPlacementMarker;
    }

    public IShip getOwner() {
        return owner;
    }



    @Override
    public void draw() {
        if ( this.label == null) return;
      //  // Was machst du hier Yannick?
      //  if(!this.validShipPlacementMarker)
      //  {
    //
    //        this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png"))));
    //    }


        /*
        switch (part){
            case "start vertical"    : System.out.print("^");break;
            case "start horizontal"  : System.out.print("<");break;
            case "end vertical"      : System.out.print("v");break;
            case "end horizontal"    : System.out.print(">");break;
            case "middle vertical"   : System.out.print("||");break;
            case "middle horizontal" : System.out.print("=");break;
            case "destroyed"         : System.out.print("H");
            default: System.out.println("Alignment not found");


        } //todo hit&sunk
            */
        // todo test if platfrom run later works here
        Platform.runLater( () -> {
            if (shot) {ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png")));
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);
            }
            else {
                // default way
                /*ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/ship.png")));
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);*/

                // for test: draw ships "right"

                // ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipStartVertical.png")));
                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/ship.png")));
                this.label.setGraphic(image);
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
              /*  switch (part) {
                    case "start vertical"    : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipStartVertical.png"))); this.label.setGraphic(image); break;
                    case "start horizontal"  : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipStartHorizontal.png"))); this.label.setGraphic(image); break;
                    case "end vertical"      : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipEndVertical.png")));  this.label.setGraphic(image); break;
                    case "end horizontal"    : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipEndHorizontal.png")));  this.label.setGraphic(image); break;
                    case "middle vertical"   : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipMiddleVertical.png")));  this.label.setGraphic(image); break;
                    case "middle horizontal" : image.setImage(new Image(getClass().getResourceAsStream("/Gui_View/images/shipMiddleHorizontal.png")));  this.label.setGraphic(image); break;
                    case "destroyed"         : image.setImage(new Image(getClass().getResourceAsStream("Gui_View/images/shipHit.png"))); this.label.setGraphic(image); break;
                    default: System.out.println("Alignment not found");
                }

               */
            }
        });


        /*

         */
        //if ( shot ){
        //    this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png"))));
        //}
        //else{
        //    this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/ship.png"))));
        //}



    }

}
