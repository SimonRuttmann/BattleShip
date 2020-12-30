package Player;

import KI.Ki;
import Model.Playground.EnemyPlayground;
import Model.Playground.IEnemyPlayground;
import Model.Playground.IOwnPlayground;
import Model.Playground.OwnPlayground;


public class Savegame{
    public long              id;
    public GameMode          modes;
    public boolean           OwnPlayerKi;

    public OwnPlayground    ownPlayerIOwnPlayground;
    public EnemyPlayground  ownPlayerIEnemyPlayground;
    public OwnPlayground    enemyPlayerOwnPlayground;
    public EnemyPlayground  enemyPlayerEnemyPlayground;

    public  boolean          multiplayer;
    public  String           ownPlayerName;
    public  int              playgroundSize;
    public  int              amountOfShips;
    public  int              amountShipSize2;
    public  int              amountShipSize3;
    public  int              amountShipSize4;
    public  int              amountShipSize5;
    public  Ki               enemyKi;
    public  Ki               ownKi;

    public boolean           sceneIsPlaceShips;
    public boolean           sceneIsGamePlayground;
    public int               difficulty;
    public boolean           selfOrKi;
    public boolean           yourTurn;


    public Savegame (
            long id,
            GameMode modes,
            boolean OwnPlayerKi,
            OwnPlayground ownPlayerIOwnPlayground,
            EnemyPlayground ownPlayerIEnemyPlayground,
            OwnPlayground enemyPlayerOwnPlayground,
            EnemyPlayground enemyPlayerEnemyPlayground,
            boolean multiplayer,
            String ownPlayerName,
            int playgroundSize,
            int amountOfShips,
            int amountShipSize2,
            int amountShipSize3,
            int amountShipSize4,
            int amountShipSize5,
            Ki enemyKi,     //TODO Wichtig, GSON speichert null elemente nicht ab und verwendet einen properties construktor, wird ein Spiel ohne ownKI gestartet, wird die enemyKi zur ownKi und die enemyKi wird null! Diese Reihenfolge beibehalten!
            Ki ownKi,
            boolean sceneIsPlaceShips,
            boolean sceneIsGamePlayground,
            int difficulty,
            boolean selfOrKi,
            boolean yourTurn
            )
    {
        this.id = id;
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
        this.enemyKi = enemyKi;
        this.ownKi = ownKi;
        this.sceneIsPlaceShips = sceneIsPlaceShips;
        this.sceneIsGamePlayground = sceneIsGamePlayground;
        this.difficulty = difficulty;
        this.selfOrKi = selfOrKi;
        this.yourTurn = yourTurn;
    }

}


