package Model.Util;

import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Water implements IDrawable {
    private transient Label label;
    private boolean validShipPlacementMarker = true;

    public Water() {
    }

    @Override

    public void draw() {
        if ( this.label == null )return;
       // System.out.print("*water*");
        // Wasser = keine Grafik
      
      //TODO Erstmal mit, und später ohne Testen -> wenns ohne geht rausschmeisen -> (setGraphic wurde nicht in jedem Fall in der Gui angezeigt) -> Whr löschen
        //this.label.setGraphic(null);
        Platform.runLater(() -> {
            //Das ist für die PlaceShips Scene
            if (!validShipPlacementMarker && ActiveGameState.isSceneIsPlaceShips()) {
                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/notValidPlacementMarker.png")));
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);
            }
            //HINZUGEFÜGT, für die Spiel Scene ( die Szene auf der gegeneinadner gespielt wird )
            if (ActiveGameState.isSceneIsGamePlayground()){
                ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/water.png")));
                // making ships resizeable -> fitting to current label size
                image.fitWidthProperty().bind(label.widthProperty());
                image.fitHeightProperty().bind(label.heightProperty());
                this.label.setGraphic(image);
            }
        });

  /*  public void draw(){
        /*
        System.out.print("*water*");
        this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/water.png"))));
    */
  /*
        if(!this.validShipPlacementMarker)
        {
            this.label.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/shipHit.png"))));
        }
  */

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
        if (this.label == null) return;
        Platform.runLater(()-> this.label.setDisable(true));
    }

    @Override
    public void setLabelClickable() {
        if ( this.label == null) return;
        Platform.runLater(()-> this.label.setDisable(false));
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
