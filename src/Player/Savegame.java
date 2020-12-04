package Player;

import Model.Playground.IEnemyPlayground;
import Model.Playground.IOwnPlayground;


public class Savegame{
    private Long id;

    private final IOwnPlayground ownPlayground;
    private final IEnemyPlayground enemyPlayground;   // -> Drawable[][]-> Jedes Feld DrawableObject

    public final boolean multiplayer; // true = Multiplayer, false = Singleplayer
    public final boolean yourTurn;   // Wer ist an der Reihe

    private final Player ownIPlayer;     // KI oder Selbst
    private final Player enemyIPlayer;   // KI oder Anderer

    // empty constructor for creating empty object -> load, will be overwritten
    public Savegame() {
        this.ownPlayground = null;
        this.enemyPlayground = null;
        this.multiplayer = false;
        this.yourTurn = false;
        this.ownIPlayer = null;
        this.enemyIPlayer = null;
        this.id = 0L;
    }

    // parameterized constructor - creating "real" object
    public Savegame (IOwnPlayground ownPlayground, IEnemyPlayground enemyPlayground, boolean multiplayer, boolean yourTurn, Player ownIPlayer, Player enemyIPlayer, Long id){
        this.ownPlayground = ownPlayground;
        this.enemyPlayground = enemyPlayground;
        this.multiplayer = multiplayer;
        this.yourTurn = yourTurn;
        this.ownIPlayer = ownIPlayer;
        this.enemyIPlayer = enemyIPlayer;
        this.id = id;
    }

}


