package Model.Playground;

import Model.Ship.IShip;
import Model.Util.IDrawable;
import javafx.scene.control.Label;

import java.util.ArrayList;


public abstract class AbstactPlayground implements IPlayground{
    protected int playgroundsize;
    protected IDrawable[][] Field;
    protected int shipsplaced;
    protected boolean gameWon;
    protected boolean gameLost;

    public AbstactPlayground (int playgroundsize){
        this.playgroundsize = playgroundsize;
        Field = new IDrawable[playgroundsize][playgroundsize];
    }


    @Override
    public void drawPlayground(){
        for ( int x = 1; x <= this.playgroundsize; x++)
        {
            for ( int y = 1; y <= this.playgroundsize; y++)
            {
                if ( Field[x][y] == null) System.out.println("Error at drawing!");
                Field[x][y].draw();

            }
        }
    }


    @Override
    public void resetAll() {
        this.shipsplaced = 0;
            for ( int x = 1; x <= this.playgroundsize; x++)
            {
                for ( int y = 1; y <= this.playgroundsize; y++)
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
