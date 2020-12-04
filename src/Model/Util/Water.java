package Model.Util;

import javafx.scene.control.Label;

public class Water implements IDrawable {
    private Label label;
    private boolean validShipPlacementMarker = true;

    public Water() {
    }

    @Override
    public void draw(){
        System.out.print("*");
        this.label.setStyle("-fx-background-aqua");
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
