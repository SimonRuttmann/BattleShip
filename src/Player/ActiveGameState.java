package Player;

import KI.Ki;
import Model.Playground.*;
import Network.*;

/**
 * - used as cache to give controllers access to gameConfig and active Objects
 * - saving based on this class -> only necessary information are saved
 */

public class ActiveGameState {
    public static boolean newView = false;

    // Modes
    /**
     *
     * Es gibt folgende Modi:
     *
     *
     * playerVsRemote       Spieler      vs anderen Spieler/KI wir haben ein eigenes Spielfeld und ein gegnerisches Spielfeld             wie bisher
     *
     * KiVsRemote           Ki (unsere)  vs anderen Spieler/KI wir haben ein eigenes Spielfeld und ein gegnerisches Spielfeld
     *
     * playerVsKi           Spieler      vs eigene KI          wir haben ein eigenes Spielfeld und ein gegnerisches Spielfeld
     *                                     -> Die KI hat auch ein eigenes Spielfeld und ein gegnerisches Spielfeld
     *
     * KivsKi eigene KI    vs eigene KI          Die KI 1 hat auch ein eigenes Spielfeld und ein gegnerisches Spielfeld
     *                                      Die KI 2 hat auch ein eigenes Spielfeld und ein gegnerisches Spielfeld
     */
    private static GameMode modes;

    private static boolean OwnPlayerKi; //<- Das ist die KI, welche bei Modus 2 und 3 benötigt wird
    private static IOwnPlayground ownPlayerIOwnPlayground;
    private static IEnemyPlayground ownPlayerIEnemyPlayground;

    // You only use the enemy player in singleplayer, where the enemy is always a KI
    private static IOwnPlayground enemyPlayerOwnPlayground;
    private static IEnemyPlayground enemyPlayerEnemyPlayground;


    //Network
    private static boolean amIServer;
    private static IServer server;
    private static Client client;

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        ActiveGameState.running = running;
    }

    private static boolean running;

    // Game Configuration
    private static boolean multiplayer;  // true = multiplayer, false = singleplayer
    private static String ownPlayerName;
    private static int playgroundSize;   // between 5x5 - 30x30
    private static int amountOfShips;
    private static int amountShipSize2;
    private static int amountShipSize3;
    private static int amountShipSize4;
    private static int amountShipSize5;
    private static Ki ki;

    private static boolean sceneIsPlaceShips;

    //Hinzugefügt für Drawable Objekte -> Draw Methode
    public static boolean isSceneIsGamePlayground() {
        return sceneIsGamePlayground;
    }

    public static void setSceneIsGamePlayground(boolean sceneIsGamePlayground) {
        ActiveGameState.sceneIsGamePlayground = sceneIsGamePlayground;
    }

    private static boolean sceneIsGamePlayground = false;

    public static int getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(int difficulty) {
        ActiveGameState.difficulty = difficulty;
    }

    private static int difficulty; //wählt die Schwierigkeit aus, 0 = normal, 1 = schwer
    //TODO evtl enum

    // Game Variables
// -> Gestrichen -> neuer Zugriff über ActiveGameState.getOwnPlayer.get[Own][Enemy]Playground
//    private static IEnemyPlayground enemyPlayground;
//    private static IOwnPlayground ownPlayground;

    private static boolean selfOrKi; //self = true; ki = false;


    private static boolean yourTurn;
    private static boolean isWon;
    private static boolean isLost;



    // ----------------------------------------
    // Getter and Setter Methods

    // Network
    public static boolean isAmIServer() {
        return amIServer;
    }

    public static void setAmIServer(boolean amIServer) {
        ActiveGameState.amIServer = amIServer;
    }

    public static IServer getServer() { return server; }
    public static void setServer(IServer server) { ActiveGameState.server = server; }

    public static Client getClient() { return client; }
    public static void setClient(Client client) { ActiveGameState.client = client; }


    // Game Configuration
    public static boolean isMultiplayer() {
        return multiplayer;
    }
    public static void setMultiplayer(boolean multiplayer) {
        ActiveGameState.multiplayer = multiplayer;
    }

    public static String getOwnPlayerName() { return ownPlayerName; }
    public static void setOwnPlayerName(String ownPlayerName) {
        ActiveGameState.ownPlayerName = ownPlayerName;
    }

    public static int getPlaygroundSize() { return playgroundSize; }

    /**
     * The size must be between 5 and 30
     * @param playgroundSize the size of the playground
     */
    public static void setPlaygroundSize(int playgroundSize) {
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

    public static Ki getKi() { return ki; }
    public static void setKi(Ki ki) { ActiveGameState.ki = ki; }

    public static boolean isSceneIsPlaceShips() {return sceneIsPlaceShips;};
    public static void setSceneIsPlaceShips(boolean sceneIsPlaceShips) {ActiveGameState.sceneIsPlaceShips = sceneIsPlaceShips;};


    // Game Variables

    public static boolean getSelfOrKi() { return selfOrKi; }
    /**Sets the variable, which one will play the game
     * @param selfOrKi self = true, ki = false;
     */
    public static void setSelfOrKi(boolean selfOrKi) {
        ActiveGameState.selfOrKi = selfOrKi;
    }


    public static boolean isYourTurn() {
        return yourTurn;
    }
    public static void setYourTurn(boolean yourTurn) {
        ActiveGameState.yourTurn = yourTurn;
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


    public static GameMode getModes() {
        return modes;
    }

    public static void setModes(GameMode modes) {
        ActiveGameState.modes = modes;
    }

    public static boolean isOwnPlayerKi() {
        return OwnPlayerKi;
    }

    public static void setOwnPlayerKi(boolean ownPlayerKi) {
        OwnPlayerKi = ownPlayerKi;
    }

    public static IOwnPlayground getOwnPlayerIOwnPlayground() {
        return ownPlayerIOwnPlayground;
    }

    public static void setOwnPlayerIOwnPlayground(IOwnPlayground ownPlayerIOwnPlayground) {
        ActiveGameState.ownPlayerIOwnPlayground = ownPlayerIOwnPlayground;
    }

    public static IEnemyPlayground getOwnPlayerIEnemyPlayground() {
        return ownPlayerIEnemyPlayground;
    }

    public static void setOwnPlayerIEnemyPlayground(IEnemyPlayground ownPlayerIEnemyPlayground) {
        ActiveGameState.ownPlayerIEnemyPlayground = ownPlayerIEnemyPlayground;
    }

    public static IOwnPlayground getEnemyPlayerOwnPlayground() {
        return enemyPlayerOwnPlayground;
    }

    public static void setEnemyPlayerOwnPlayground(IOwnPlayground enemyPlayerOwnPlayground) {
        ActiveGameState.enemyPlayerOwnPlayground = enemyPlayerOwnPlayground;
    }

    public static IEnemyPlayground getEnemyPlayerEnemyPlayground() {
        return enemyPlayerEnemyPlayground;
    }

    public static void setEnemyPlayerEnemyPlayground(IEnemyPlayground enemyPlayerEnemyPlayground) {
        ActiveGameState.enemyPlayerEnemyPlayground = enemyPlayerEnemyPlayground;
    }

    public static boolean isSelfOrKi() {
        return selfOrKi;
    }

}
