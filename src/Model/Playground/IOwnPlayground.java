package Model.Playground;

import Model.Util.UtilDataType.Point;
import Model.Util.UtilDataType.ShotResponse;

public interface IOwnPlayground extends IPlayground{
    ShotResponse shoot(Point pos_shot);

}
