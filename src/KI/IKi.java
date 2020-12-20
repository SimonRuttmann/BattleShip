package KI;

import Model.Playground.EnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.IPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Model.Util.UtilDataType.Point;

import java.util.ArrayList;

public interface IKi {
    Point getShot(OwnPlayground playground);

    ArrayList<IShip> placeships(OwnPlayground playground);
}
