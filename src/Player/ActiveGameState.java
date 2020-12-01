package Player;

import Model.Playground.*;
import Network.*;

/**
 * - used as cache to give controllers access to gameConfig and active Objects
 * - saving based on this class -> only necessary information are saved
 */

public class ActiveGameState {

    // Network
    private static IServer server;
    private static Client client;

    // Playground
    private static int playgroundSize;   // between 5x5 - 30x30
    private static int amountOfShips;
    private static int amountShipSize2;
    private static int amountShipSize3;
    private static int amountShipSize4;
    private static int amountShipSize5;

    private static IEnemyPlayground enemyPlayground;
    private static IOwnPlayground ownPlayground;

    private static boolean selfOrKi; //self = true; ki = false;
    private static Player ownPlayer;     // KI oder Selbst //todo -> what is Player, evtl. redunant with selfOrKi
    private static Player enemyPlayer;   // KI oder Anderer

    // Game Variables
    private static boolean yourTurn;
    private static boolean multiplayer;

    private static boolean isWon;
    private static boolean isLost;



    // Getter and Setter Methods

    // Network
    public static IServer getServer() { return server; }
    public static void setServer(IServer server) { ActiveGameState.server = server; }

    public static Client getClient() { return client; }
    public static void setClient(Client client) { ActiveGameState.client = client; }


    // Playground
    public static int getPlaygroundSize() { return playgroundSize; }
    public static void setPlaygroundSize(int playgroundSize) {
        assert ((playgroundSize >= 5) && (playgroundSize <= 30)) : "playgroundSize must be between " +
                "5 and 30, but is:" + playgroundSize;
        ActiveGameState.playgroundSize = playgroundSize;
    }

    public static int getAmountOfShips() {
        return amountOfShips;
    }
    public static void setAmountOfShips(int amountOfShips) {
        ActiveGameState.amountOfShips = amountOfShips;
    }

    public static int getAmountShipSize2() {
        return amountShipSize2;
    }
    public static void setAmountShipSize2(int amountShipSize2) {
        ActiveGameState.amountShipSize2 = amountShipSize2;
    }

    public static int getAmountShipSize3() {
        return amountShipSize3;
    }
    public static void setAmountShipSize3(int amountShipSize3) {
        ActiveGameState.amountShipSize3 = amountShipSize3;
    }

    public static int getAmountShipSize4() {
        return amountShipSize4;
    }
    public static void setAmountShipSize4(int amountShipSize4) {
        ActiveGameState.amountShipSize4 = amountShipSize4;
    }

    public static int getAmountShipSize5() {
        return amountShipSize5;
    }
    public static void setAmountShipSize5(int amountShipSize5) {
        ActiveGameState.amountShipSize5 = amountShipSize5;
    }

    public static IEnemyPlayground getEnemyPlayground() { return enemyPlayground; }
    public static void setEnemyPlayground(IEnemyPlayground enemyPlayground) {
        ActiveGameState.enemyPlayground = enemyPlayground;
    }

    public static IOwnPlayground getOwnPlayground() { return ownPlayground; }
    public static void setOwnPlayground(IOwnPlayground ownPlayground) {
        ActiveGameState.ownPlayground = ownPlayground;
    }

    public static boolean getSelfOrKi() { return selfOrKi; }
    /**Sets the variable, which one will play the game
     * @param selfOrKi self = true, ki = false;
     */
    public static void setSelfOrKi(boolean selfOrKi) {
        ActiveGameState.selfOrKi = selfOrKi;
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


    // Game Variables

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
