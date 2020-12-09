package Model.Util;

import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Water implements IDrawable {
    private Label label;
    private boolean validShipPlacementMarker = true;

    public Water() {
    }

    @Override
    public void draw() {
        System.out.print("*water*");
        // Wasser = keine Grafik
        this.label.setGraphic(null);
        Platform.runLater(() -> {
            if (!validShipPlacementMarker && ActiveGameState.isSceneIsPlaceShips()) {
                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/notValidPlacementMarker.png")));
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);
            }
        });
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
