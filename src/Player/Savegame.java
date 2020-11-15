package Player;

import Model.Playground.IEnemyPlayground;
import Model.Playground.IOwnPlayground;

import java.io.Serializable;

public class Savegame implements Serializable {
    private Long id;

    private final IOwnPlayground ownPlayground;
    private final IEnemyPlayground enemyPlayground;   // -> Drawable[][]-> Jedes Feld DrawableObject

    public final boolean  yourTurn;   //Wer ist an der Reihe

    private final Player ownIPlayer;     // KI oder Selbst
    private final Player enemyIPlayer;   // KI oder Anderer


    public Savegame (IOwnPlayground ownPlayground, IEnemyPlayground enemyPlayground, boolean yourTurn, Player ownIPlayer, Player enemyIPlayer, Long id){
        this.ownPlayground = ownPlayground;
        this.enemyPlayground = enemyPlayground;
        this.yourTurn = yourTurn;
        this.ownIPlayer = ownIPlayer;
        this.enemyIPlayer = enemyIPlayer;
        this.id = id;
    }
}
