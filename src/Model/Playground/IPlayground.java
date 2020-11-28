package Model.Playground;
import javafx.scene.control.Label;

public interface IPlayground {

    //You can only build the Playground if the Ships are created
    //The Amount of Ships placed in the Playground is saved
    void buildPlayground();


    //You can only draw the Playground if the Playground is built
    void drawPlayground();

    //Sets all Fields in Drawable Field [][] to null
    //Sets the Amount of Ships placed in the Playground to 0
    //After the Playground is reset it needs to be instantiated and build again.
    void resetAll();


    /**
     *
     * @param labelArray an array of objects containing labels
     *
     */
    void setLabels(Object[] labelArray);
    //TODO alle drawable-objekte in den playgrounds mit den labels aus dem label array verknüpfen
    //bei .draw kann dann das label mit drawableobject.getLabel.setStyle("-fx-background-color:blue");
    //und später noch DrawableObjekt.draw.setDiabled("true");

    //-> Vertikale linie zuerst 0 ist oben links, 1 ist oben links 2.reihe -> 30 bei size 30 ist dann oben links 1.Reihe 2.spalte
}
