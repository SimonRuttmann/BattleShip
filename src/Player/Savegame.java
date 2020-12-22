package Player;

import KI.Ki;
import Model.Playground.IEnemyPlayground;
import Model.Playground.IOwnPlayground;


public class Savegame{

    public GameMode          modes;
    public boolean           OwnPlayerKi;

    public IOwnPlayground    ownPlayerIOwnPlayground;
    public IEnemyPlayground  ownPlayerIEnemyPlayground;
    public IOwnPlayground    enemyPlayerOwnPlayground;
    public IEnemyPlayground  enemyPlayerEnemyPlayground;

    public  boolean          multiplayer;
    public  String           ownPlayerName;
    public  int              playgroundSize;
    public  int              amountOfShips;
    public  int              amountShipSize2;
    public  int              amountShipSize3;
    public  int              amountShipSize4;
    public  int              amountShipSize5;
    public  Ki               ki;

    public boolean           sceneIsPlaceShips;
    public boolean           sceneIsGamePlayground;
    public int               difficulty;
    public boolean           selfOrKi;
    public boolean           yourTurn;


    public Savegame (
            GameMode modes,
            boolean OwnPlayerKi,
            IOwnPlayground ownPlayerIOwnPlayground,
            IEnemyPlayground ownPlayerIEnemyPlayground,
            IOwnPlayground enemyPlayerOwnPlayground,
            IEnemyPlayground enemyPlayerEnemyPlayground,
            boolean multiplayer,
            String ownPlayerName,
            int playgroundSize,
            int amountOfShips,
            int amountShipSize2,
            int amountShipSize3,
            int amountShipSize4,
            int amountShipSize5,
            Ki ki,
            boolean sceneIsPlaceShips,
            boolean sceneIsGamePlayground,
            int difficulty,
            boolean selfOrKi,
            boolean yourTurn
            )
    {
        this.modes = modes;
        this.OwnPlayerKi = OwnPlayerKi;
        this.ownPlayerIOwnPlayground = ownPlayerIOwnPlayground;
        this.ownPlayerIEnemyPlayground = ownPlayerIEnemyPlayground;
        this.enemyPlayerOwnPlayground = enemyPlayerOwnPlayground;
        this.enemyPlayerEnemyPlayground = enemyPlayerEnemyPlayground;
        this.multiplayer = multiplayer;
        this.ownPlayerName =  ownPlayerName;
        this.playgroundSize = playgroundSize;
        this.amountOfShips = amountOfShips;
        this.amountShipSize2 = amountShipSize2;
        this.amountShipSize3 = amountShipSize3;
        this.amountShipSize4 = amountShipSize4;
        this.amountShipSize5 = amountShipSize5;
        this.ki = ki;
        this.sceneIsPlaceShips = sceneIsPlaceShips;
        this.sceneIsGamePlayground = sceneIsGamePlayground;
        this.difficulty = difficulty;
        this.selfOrKi = selfOrKi;
        this.yourTurn = yourTurn;
    }

}


