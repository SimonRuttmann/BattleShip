package Player;

import Model.Playground.*;
import Network.*;

/**
 * Used as Cache to get access to the active objects from the controllers
 */
public class ActiveGameState {

    //Network
    private static Server server;
    private static Client client;

    //Playground
    private static EnemyPlayground enemyPlayground;
    private static OwnPlayground ownPlayground;

    private static Player ownPlayer;     // KI oder Selbst
    private static Player enemyPlayer;   // KI oder Anderer

    //Game Variables
    private static boolean yourTurn;
    private static boolean multiplayer;

    private static boolean isWon;
    private static boolean isLost;



    //Getter and Setter

    public static Server getServer() {
        return server;
    }

    public static void setServer(Server server) {
        ActiveGameState.server = server;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        ActiveGameState.client = client;
    }

    public static EnemyPlayground getEnemyPlayground() {
        return enemyPlayground;
    }

    public static void setEnemyPlayground(EnemyPlayground enemyPlayground) {
        ActiveGameState.enemyPlayground = enemyPlayground;
    }

    public static OwnPlayground getOwnPlayground() {
        return ownPlayground;
    }

    public static void setOwnPlayground(OwnPlayground ownPlayground) {
        ActiveGameState.ownPlayground = ownPlayground;
    }

    public static Player getOwnPlayer() {
        return ownPlayer;
    }

    public static void setOwnPlayer(Player ownPlayer) {
        ActiveGameState.ownPlayer = ownPlayer;
    }

    public static Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public static void setEnemyPlayer(Player enemyPlayer) {
        ActiveGameState.enemyPlayer = enemyPlayer;
    }

    public static boolean isYourTurn() {
        return yourTurn;
    }

    public static void setYourTurn(boolean yourTurn) {
        ActiveGameState.yourTurn = yourTurn;
    }

    public static boolean isMultiplayer() {
        return multiplayer;
    }

    public static void setMultiplayer(boolean multiplayer) {
        ActiveGameState.multiplayer = multiplayer;
    }

    public static boolean isIsWon() {
        return isWon;
    }

    public static void setIsWon(boolean isWon) {
        ActiveGameState.isWon = isWon;
    }

    public static boolean isIsLost() {
        return isLost;
    }

    public static void setIsLost(boolean isLost) {
        ActiveGameState.isLost = isLost;
    }
}
