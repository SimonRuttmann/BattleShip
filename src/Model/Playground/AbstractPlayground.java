package Model.Playground;

import Model.Util.IDrawable;
import Player.ActiveGameState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The playground is implemented as a IDrawable[][] Array. IDrawable Objects can be Water, ShotWater and ShipPart
 * Every Player got his own playground with his ships and an enemy playground
 */
public abstract class AbstractPlayground implements IPlayground{
    public static final Logger logAbstractPlayground = Logger.getLogger("parent.AbstractPlayground");
    protected int playgroundsize;
    protected IDrawable[][] Field;
    protected int shipsplaced;
    protected boolean gameLost;

    public AbstractPlayground(){
        this.playgroundsize = ActiveGameState.getPlaygroundSize();
        Field = new IDrawable[playgroundsize][playgroundsize];
        this.shipsplaced = ActiveGameState.getAmountOfShips();
    }

    @Override
    public void drawPlayground(){
        for ( int x = 0; x < this.playgroundsize; x++)
        {

            for ( int y = 0; y < this.playgroundsize; y++)
            {
                if ( Field[x][y] == null) logAbstractPlayground.log(Level.SEVERE, "Field can´t be drawn as it is not instantiated");
                Field[x][y].draw();

            }
        }
        //Uncomment to print out the playground on the console line
        /*for (int y = 0; y < this.playgroundsize; y++){
            System.out.println();
            for ( int x = 0; x < this.playgroundsize; x++){
                if (Field[x][y] instanceof ShipPart){
                    System.out.print( "S   ");
                }
                else{
                    System.out.print( "N   ");
                }
            }
        }*/
    }


    @Override
    public void resetAll() {
        this.shipsplaced = 0;
            for ( int x = 0; x < this.playgroundsize; x++)
            {
                for ( int y = 0; y < this.playgroundsize; y++)
                {
                        Field[x][y] = null;
                }
            }
    }


    @Override
    public abstract void setLabels(Object[] labelArray);

    @Override
    public abstract void buildPlayground();
}
