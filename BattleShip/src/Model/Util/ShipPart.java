package Model.Util;

import GameData.ActiveGameState;
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

    //ShipsParts in our Field have an associated Ship, they can be marked as shot
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

    //ShipPart specific getter and setter
    public boolean isShot (){ return this.shot; }
    public void setShot (boolean shot){ this.shot = shot; }
    public IShip getOwner() { return owner; }

    //IDrawable getter and setter

    @Override
    public Label getLabel(){ return this.label; }

    @Override
    public void setLabel(Label label){ this.label = label; }



    @Override
    public void setValidShipPlacementMarker( boolean valid) { this.validShipPlacementMarker = valid; }

    @Override
    public boolean getValidShipPlacementMarker() { return this.validShipPlacementMarker; }

    //IDrawable methods

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


    /**
     * Use this method, to represent the shipPart on the playground, it will be displayed as destroyed or not hit, depending of it´s status
     */
    @Override
    public void draw() {
        if ( this.label == null) return;
        Platform.runLater( () -> {
            if (shot) {ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png")));

                // making ships resizeable, so they fit to the current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);
            }
            else {

                ImageView image;
                if (ActiveGameState.isSceneIsPlaceShips())
                    image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/water.png")));
                else
                    image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/ship.png")));

                this.label.setGraphic(image);

                // making ships resizeable, so they fit to the current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());

            }
        });

    }

}
