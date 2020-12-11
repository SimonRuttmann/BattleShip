package Model.Util;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShotWater implements IDrawable {
    private Label label;
    private boolean validShipPlacementMarker = true;

    public ShotWater() {
    }

    @Override
    public void draw(){
        /*System.out.print("X");
        this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/waterHit.png"))));
        //bei .draw kann dann das label mit drawableobject.getLabel.setStyle("-fx-background-color:blue");
        //und später noch DrawableObjekt.draw.setDiabled("true")

    */

        if(!this.validShipPlacementMarker)
        {
            this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png"))));
        }
    }


    @Override
    public Label getLabel() {
        return this.label;
    }

    @Override
    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public void setLabelNonClickable() {
        this.label.setDisable(false);
    }

    @Override
    public void setValidShipPlacementMarker(boolean valid) {
        this.validShipPlacementMarker = valid;
    }

    @Override
    public boolean getValidShipPlacementMarker() {
        return this.validShipPlacementMarker;
    }
}
