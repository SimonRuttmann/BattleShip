package Model.Playground;
import javafx.scene.control.Label;

public interface IPlayground {


    /**
     * This method builds the Playground, if there are no Ships in the ShipList, it will create an playground filled all Fields with water
     * If there are Ships in the List it will place them as shipParts
     * Creates the Playground, sets all ships-parts on the Field (if it is the own Field) and fills the rest of the fields with water
     */
    void buildPlayground();


    /**
     * Restriction: You can only draw the Playground if it is built
     * Draws all Fields of the Playground
     */
    void drawPlayground();

    /**
     * Sets all Field in the Drawable Field[][] to null
     * sets the Amount of Ships placed in the Playground to 0
     * After the Playground is reset, it needs to be instantiated and build again
     */
    void resetAll();


    /**
     * Restriction: You can only set the Labels to the Objects in the Playground, when it is build
     * Connects all Drawable Objects of the selected Playground with an Label shown at the GUI
     * @param labelArray an array of objects containing labels
     */
    void setLabels(Object[] labelArray);
}
