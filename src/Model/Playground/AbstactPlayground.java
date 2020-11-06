package Model.Playground;

import Model.Ship.IShip;
import Model.Util.IDrawable;

import java.util.ArrayList;


public abstract class AbstactPlayground implements IPlayground{
    protected int playgroundsize;
    protected IDrawable[][] Field;

    public AbstactPlayground (int playgroundsize){
        this.playgroundsize = playgroundsize;
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
            for ( int x = 1; x <= this.playgroundsize; x++)
            {
                for ( int y = 1; y <= this.playgroundsize; y++)
                {
                        Field[x][y] = null;
                }
            }
    }

   public abstract void buildPlayground();
}
