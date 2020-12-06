package Player;

import Model.Playground.IEnemyPlayground;
import Model.Playground.IOwnPlayground;

// Hier sind alle daten drin, welche abgespeicher werden oder geladen werden
public class Savegame{
    private Long id;

    private final IOwnPlayground ownPlayground;
    private final IEnemyPlayground enemyPlayground;   // -> Drawable[][]-> Jedes Feld DrawableObject

    public final boolean multiplayer; // true = Multiplayer, false = Singleplayer
    public final boolean yourTurn;   // Wer ist an der Reihe



    // empty constructor for creating empty object -> load, will be overwritten
    public Savegame() {
        this.ownPlayground = null;
        this.enemyPlayground = null;
        this.multiplayer = false;
        this.yourTurn = false;
        this.id = 0L;
    }

    // parameterized constructor - creating "real" object
    public Savegame (IOwnPlayground ownPlayground, IEnemyPlayground enemyPlayground, boolean multiplayer, boolean yourTurn, Long id){
        this.ownPlayground = ownPlayground;
        this.enemyPlayground = enemyPlayground;
        this.multiplayer = multiplayer;
        this.yourTurn = yourTurn;
        this.id = id;
    }

}


