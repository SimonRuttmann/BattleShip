package Model.Playground;

import Model.Ship.IShip;
import Model.Util.IDrawable;
import Model.Util.Water;
import Player.ActiveGameState;
import javafx.scene.control.Label;

import java.util.ArrayList;


public abstract class AbstactPlayground implements IPlayground{
    protected int playgroundsize;
    protected IDrawable[][] Field;
    protected int shipsplaced;
    protected boolean gameWon;
    protected boolean gameLost;

    public AbstactPlayground (){
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
                if ( Field[x][y] == null) System.out.println("Error at drawing!");
                Field[x][y].draw();

            }
        }
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
