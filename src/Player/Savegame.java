package Player;

import KI.Ki;
import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;

/**
 * This class contains all information saved as JSON-File
 * The constructor is called by SaveAndLoad by using the save methods
 * The load methods in SaveAndLoad creates an Savegame-Object by reading the JSON-File
 *
 * Warning: The order of the attributes SHOULD NOT BE CHANGED UNDER ANY CIRCUMSTANCES.
 *          This can affect GSON-internally used constructor by creating the Savegame-Object,
 *          which could result into null-Objects at the wrong spots.
 *
 *          E.g. enemyKi not instantiated -> null
 *               ownKI not null
 *
 *               Save -> Load:  EnemyKI = ownKI
 *                              ownKI = null
 */
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
    public Ki.Difficulty     enemyDifficulty;
    public Ki.Difficulty     ownDifficulty;
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
            Ki enemyKi,
            Ki ownKi,
            boolean sceneIsPlaceShips,
            boolean sceneIsGamePlayground,
            Ki.Difficulty enemyDifficulty,
            Ki.Difficulty ownDifficulty,
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
        this.enemyDifficulty = enemyDifficulty;
        this.ownDifficulty = ownDifficulty;
        this.yourTurn = yourTurn;
    }

}


