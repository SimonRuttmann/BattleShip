package Model.Util;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Water implements IDrawable {
    private Label label;
    private boolean validShipPlacementMarker = true;

    public Water() {
    }

    @Override
    public void draw(){
        System.out.print("*water*");
        this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/water.png"))));
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
