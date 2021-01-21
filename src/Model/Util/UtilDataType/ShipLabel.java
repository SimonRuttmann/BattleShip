package Model.Util.UtilDataType;

import GameData.ActiveGameState;
import Gui_View.HelpMethods;
import Model.Ship.IShip;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Objects of this class are needed for representation of ships at placeShips for Drag and Drop
 */
public class ShipLabel extends Label {
    private IShip ship;
    private Point posHead;
    private boolean horizontal;
    private final int size;

    public ShipLabel(Point posHead, boolean horizontal, int size) {
        this.posHead = posHead;
        this.horizontal = horizontal;
        this.size = size;
        this.setImage();
    }

    public IShip getShip() { return ship; }
    public void setShip(IShip ship) { this.ship = ship; }

    public Point getPosHead() { return posHead; }
    public void setPosHead(Point posHead) {this.posHead = posHead;}

    public boolean isHorizontal() { return horizontal; }
    public void setHorizontal(boolean horizontal) {this.horizontal = horizontal;};

    public int getSize() { return size; }


    /** setImage sets the Image for the ship label.
     *  1. Called when initialized: adds living ship for own Playground + dead ship for enemy Playground
     *  2. Called when own ship dies: changes from living ship to dead ship when ship got shot
     */
    public void setImage() {
        String imageURL;
        ImageView image;
        // adding the right image to the ship label
        if (horizontal) {
            imageURL = "/Gui_View/images/" + size + "erSchiff" + ".png";
            image = new ImageView(new Image(HelpMethods.class.getResourceAsStream(imageURL)));
            image.fitWidthProperty().bind(new SimpleIntegerProperty(size * ActiveGameState.getPlaygroundScale()).asObject());
            image.fitHeightProperty().bind(new SimpleIntegerProperty(ActiveGameState.getPlaygroundScale()).asObject());
        } else {
            imageURL = "/Gui_View/images/" + size + "erSchiffVertical" + ".png";
            image = new ImageView(new Image(HelpMethods.class.getResourceAsStream(imageURL)));
            image.fitWidthProperty().bind(new SimpleIntegerProperty(ActiveGameState.getPlaygroundScale()).asObject());
            image.fitHeightProperty().bind(new SimpleIntegerProperty(size * ActiveGameState.getPlaygroundScale()).asObject());
        }
        Platform.runLater(() -> this.setGraphic(image));
    }

}