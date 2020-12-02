package Player;

import Model.Playground.*;
import Network.*;

/**
 * Used as Cache to get access to the active objects from the controllers
 */
public class ActiveGameState {

    //Network
    private static boolean amIServer;

    private static IServer server;
    private static Client client;

    //Playground
    private static IEnemyPlayground enemyPlayground;
    private static IOwnPlayground ownPlayground;

    private static Player ownPlayer;     // KI oder Selbst
    private static Player enemyPlayer;   // KI oder Anderer

    //Game Variables
    private static boolean yourTurn;
    private static boolean multiplayer;

    private static boolean isWon;
    private static boolean isLost;

    private static int amountOfShips;
    private static int amountShipSize2;
    private static int amountShipSize3;
    private static int amountShipSize4;
    private static int amountShipSize5;


    private static boolean selfOrKi; //self = true; ki = false;



    public static boolean isAmIServer() {
        return amIServer;
    }

    public static void setAmIServer(boolean amIServer) {
        ActiveGameState.amIServer = amIServer;
    }


    public static boolean isSelfOrKi() {
        return selfOrKi;
    }

    /**
     * Sets the variable, which one will play the game
     * @param selfOrKi self = true, ki = false;
     */
    public static void setSelfOrKi(boolean selfOrKi) {
        ActiveGameState.selfOrKi = selfOrKi;
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
//Getter and Setter

    public static IServer getServer() {
        return server;
    }

    public static void setServer(IServer server) {
        ActiveGameState.server = server;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        ActiveGameState.client = client;
    }

    public static IEnemyPlayground getEnemyPlayground() {
        return enemyPlayground;
    }

    public static void setEnemyPlayground(IEnemyPlayground enemyPlayground) {
        ActiveGameState.enemyPlayground = enemyPlayground;
    }

    public static IOwnPlayground getOwnPlayground() {
        return ownPlayground;
    }

    public static void setOwnPlayground(IOwnPlayground ownPlayground) {
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
