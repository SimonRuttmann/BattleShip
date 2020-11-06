package Model.Playground;

import Model.Ship.IShip;
import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;
import Model.Util.Water;

public class EnemyPlayground extends AbstactPlayground implements IEnemyPlayground{
    private int counterShipDestroyed;

    public EnemyPlayground(int playgroundsize) {
        super(playgroundsize);
    }

    @Override
    public void buildPlayground() {

            for ( int x = 1; x <= this.playgroundsize; x++)
            {
                for ( int y = 1; y <= this.playgroundsize; y++)
                {
                    System.out.print("Hello");


                    //Field is empty
                    if (Field[x][y] == null){
                        Field[x][y] = new Water();
                    }

                }
            }


    }

//Muss sich Schiffe gegnerische Schiffe merken können um unmögliche Felder als Wasser zu markieren, soweit sie noch nicht beschossenes Wasser sind
//Muss zudem die gegnerisch versunkenen Schiffe zählen um ein gewonnenes Spiel auszugeben
    @Override
    public ShotResponse shoot(Point pos_shot, boolean shipSunken) {
        this.counterShipDestroyed = 0;
        return null;
    }
}
