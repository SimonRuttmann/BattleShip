package Model.Util;

import javafx.application.Platform;
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

       // System.out.print("*shotWater*");
        // -> Gui should update when drawing shotWater
        Platform.runLater(() -> {
            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/waterHit.png")));
            // making ships resizeable -> fitting to current label size
            image.fitWidthProperty().bind(label.widthProperty());
            image.fitHeightProperty().bind(label.heightProperty());
            this.label.setGraphic(image);
        });
        //this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/waterHit.png"))));

        /*System.out.print("X");
        this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/waterHit.png"))));

        //bei .draw kann dann das label mit drawableobject.getLabel.setStyle("-fx-background-color:blue");
        //und später noch DrawableObjekt.draw.setDiabled("true")

    */

        /* was only for testing - can be fully dropped out probably
        if(!this.validShipPlacementMarker)
        {
            this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png"))));
        }*/
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
        Platform.runLater(()->{
            this.label.setDisable(true);
        });

    }

    @Override
    public void setLabelClickable() {
        Platform.runLater(()->{
            this.label.setDisable(false);
        });

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
