package Model.Util;

import javafx.scene.control.Label;

public interface IDrawable {

    /**
     * Draws the Object using his referenced Label
     */
    void draw();

    /**
     * @return The Label referenced to the Object
     */
    Label getLabel();

    /**
     * @param label The Label, which shall represent the Object using the draw() method
     */
    void setLabel(Label label);

    /**
     * Sets the label referenced to the Object to the disabled status
     */
    void setLabelNonClickable();

    /**
     * Sets an marker which signals, that a ship placement on this field is valid
     */
    void setValidShipPlacementMarker(boolean valid);

    /**
     * Gets an marker which signals, that a ship placement on this field is valid
     */
    boolean getValidShipPlacementMarker();
}
