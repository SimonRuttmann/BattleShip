package Player;

import Controller.MusicController;
import KI.Ki;
import Model.Playground.*;
import Network.*;
import java.io.BufferedReader;

/**
 * - used as cache to give controllers access to gameConfig and active Objects
 * - saving based on this class -> only necessary information are saved
 */

public class ActiveGameState {

    public static boolean newView = false;

    //Loading related
    public enum Loading {singleplayer, multiplayer, noLoad}

    private static Loading loading = Loading.noLoad;
        public static Loading getLoading() { return loading; }
        public static void setLoading(Loading loading) { ActiveGameState.loading = loading; }

    private static boolean loadWithNext = false;
        public static boolean isLoadWithNext() { return loadWithNext; }
        public static void setLoadWithNext(boolean loadWithNext) { ActiveGameState.loadWithNext = loadWithNext; }

    private static long loadId;
        public static long getLoadId() { return loadId; }
        public static void setLoadId(long loadId) { ActiveGameState.loadId = loadId; }


    //Logging related
    private static volatile boolean logging = true;
        public static boolean isLogging() { return logging; }
        public static void setLogging(boolean logging){ ActiveGameState.logging = logging; }

    private static Thread loggingThread;
        public static Thread getLoggingThread() { return loggingThread; }
        public static void setLoggingThread(Thread loggingThread) { ActiveGameState.loggingThread = loggingThread; }

    private static BufferedReader loggingReader;
        public static BufferedReader getLoggingReader() { return loggingReader; }
        public static void setLoggingReader(BufferedReader loggingReader) { ActiveGameState.loggingReader = loggingReader; }



    //General Options
    private static MusicController musicController;
        public static MusicController getMusicController() { return musicController; }
        public static void setMusicController(MusicController musicController) { ActiveGameState.musicController = musicController; }

    private static int musicVolume = 50;
        public static int getMusicVolume() { return musicVolume; }
        public static void setMusicVolume(int musicVolume) { ActiveGameState.musicVolume = musicVolume; }

    public enum Language {english, german}

    private static Language language = Language.english;
        public static Language getLanguage() { return language; }
        public static void setLanguage(Language language) { ActiveGameState.language = language; }

    private static int aiVelocity = 1;
        public static int getAiVelocity() { return aiVelocity; }
        public static void setAiVelocity(int aiVelocity) { ActiveGameState.aiVelocity = aiVelocity; }

    private static String ownPlayerName; //currently not in use
        public static String getOwnPlayerName() { return ownPlayerName; }
        public static void setOwnPlayerName(String ownPlayerName) { ActiveGameState.ownPlayerName = ownPlayerName; }


    //Game Modes
    private static GameMode modes;
        public static GameMode getModes() { return modes; }
        public static void setModes(GameMode modes) { ActiveGameState.modes = modes; }

    private static boolean OwnPlayerKi;
        public static boolean isOwnPlayerKi() { return OwnPlayerKi; }
        public static void setOwnPlayerKi(boolean ownPlayerKi) { OwnPlayerKi = ownPlayerKi; }

    private static boolean multiplayer;
        public static boolean isMultiplayer() { return multiplayer; }
        public static void setMultiplayer(boolean multiplayer) { ActiveGameState.multiplayer = multiplayer; }

    private static boolean yourTurn;
        public static boolean isYourTurn() { return yourTurn; }
        public static void setYourTurn(boolean yourTurn) { ActiveGameState.yourTurn = yourTurn; }

    private static boolean sceneIsGamePlayground;
        public static boolean isSceneIsGamePlayground() { return sceneIsGamePlayground; }
        public static void setSceneIsGamePlayground(boolean sceneIsGamePlayground) { ActiveGameState.sceneIsGamePlayground = sceneIsGamePlayground; }

    private static boolean sceneIsPlaceShips;
        public static boolean isSceneIsPlaceShips() {return sceneIsPlaceShips;}
        public static void setSceneIsPlaceShips(boolean sceneIsPlaceShips) {ActiveGameState.sceneIsPlaceShips = sceneIsPlaceShips;}

    //Game Variables
    private static IOwnPlayground ownPlayerIOwnPlayground;
        public static IOwnPlayground getOwnPlayerIOwnPlayground() { return ownPlayerIOwnPlayground; }
        public static void setOwnPlayerIOwnPlayground(IOwnPlayground ownPlayerIOwnPlayground) { ActiveGameState.ownPlayerIOwnPlayground = ownPlayerIOwnPlayground; }

    private static IEnemyPlayground ownPlayerIEnemyPlayground;
        public static IEnemyPlayground getOwnPlayerIEnemyPlayground() { return ownPlayerIEnemyPlayground; }
        public static void setOwnPlayerIEnemyPlayground(IEnemyPlayground ownPlayerIEnemyPlayground) { ActiveGameState.ownPlayerIEnemyPlayground = ownPlayerIEnemyPlayground; }

        // You only use the enemy player in singleplayer, where the enemy is always a KI
    private static IOwnPlayground enemyPlayerOwnPlayground;
        public static IOwnPlayground getEnemyPlayerOwnPlayground() { return enemyPlayerOwnPlayground; }
        public static void setEnemyPlayerOwnPlayground(IOwnPlayground enemyPlayerOwnPlayground) { ActiveGameState.enemyPlayerOwnPlayground = enemyPlayerOwnPlayground; }

    private static IEnemyPlayground enemyPlayerEnemyPlayground;
        public static IEnemyPlayground getEnemyPlayerEnemyPlayground() { return enemyPlayerEnemyPlayground; }
        public static void setEnemyPlayerEnemyPlayground(IEnemyPlayground enemyPlayerEnemyPlayground) { ActiveGameState.enemyPlayerEnemyPlayground = enemyPlayerEnemyPlayground; }

        //Playground Config
    private static int playgroundSize;   // between 5x5 - 30x30
    public static int getPlaygroundSize() { return playgroundSize; }

    private static int playgroundScale;  // set automatically by choosing playground size, only getter
    public static int getPlaygroundScale() { return playgroundScale; }

    private static int amountOfShips;
        public static int getAmountOfShips() { return amountOfShips; }
        public static void setAmountOfShips(int amountOfShips) { ActiveGameState.amountOfShips = amountOfShips; }

    private static int amountShipSize2;
        public static int getAmountShipSize2() { return amountShipSize2; }
        public static void setAmountShipSize2(int amountShipSize2) { ActiveGameState.amountShipSize2 = amountShipSize2; }

    private static int amountShipSize3;
        public static int getAmountShipSize3() { return amountShipSize3; }
        public static void setAmountShipSize3(int amountShipSize3) { ActiveGameState.amountShipSize3 = amountShipSize3; }

    private static int amountShipSize4;
        public static int getAmountShipSize4() { return amountShipSize4; }
        public static void setAmountShipSize4(int amountShipSize4) { ActiveGameState.amountShipSize4 = amountShipSize4; }

    private static int amountShipSize5;
        public static int getAmountShipSize5() { return amountShipSize5; }
        public static void setAmountShipSize5(int amountShipSize5) { ActiveGameState.amountShipSize5 = amountShipSize5; }

        //Ki
    private static Ki EnemyKi = new Ki(Ki.Difficulty.undefined);
        public static Ki getEnemyKi() { return EnemyKi; }
        public static void setEnemyKi(Ki enemyKi) { ActiveGameState.EnemyKi = enemyKi; }

    private static Ki OwnKi = new Ki(Ki.Difficulty.undefined);
        public static Ki getOwnKi() { return OwnKi; }
        public static void setOwnKi(Ki ownKi) { OwnKi = ownKi; }

    private static Ki placementKi;
        public static Ki getPlacementKi() { return placementKi; }
        public static void setPlacementKi(Ki placementKi) { ActiveGameState.placementKi = placementKi; }

    private static Ki.Difficulty ownKiDifficulty;
        public static Ki.Difficulty getOwnKiDifficulty() { return ownKiDifficulty; }
        public static void setOwnKiDifficulty(Ki.Difficulty ownKiDifficulty) { ActiveGameState.ownKiDifficulty = ownKiDifficulty; }


    private static Ki.Difficulty enemyKiDifficulty;
        public static Ki.Difficulty getEnemyKiDifficulty() { return enemyKiDifficulty; }
        public static void setEnemyKiDifficulty(Ki.Difficulty enemyKiDifficulty) { ActiveGameState.enemyKiDifficulty = enemyKiDifficulty; }

    //Network
    private static volatile boolean running;
        public static boolean isRunning() { return running; }
        public static void setRunning(boolean running) { ActiveGameState.running = running; }

    private static boolean amIServer;
        public static boolean isAmIServer() { return amIServer; }
        public static void setAmIServer(boolean amIServer) { ActiveGameState.amIServer = amIServer; }

    private static IServer server;
        public static IServer getServer() { return server; }
        public static void setServer(IServer server) { ActiveGameState.server = server; }

    private static Client client;
        public static Client getClient() { return client; }
        public static void setClient(Client client) { ActiveGameState.client = client; }



    /**
     * The size must be between 5 and 30
     * setting the size will automatically set the scale for the view
     * @param playgroundSize the size of the playground
     */
    public static void setPlaygroundSize(int playgroundSize) {
        ActiveGameState.playgroundSize = playgroundSize;
        if (5 <= playgroundSize && playgroundSize <= 10) {
            ActiveGameState.playgroundScale = 45;
        } else if (11 <= playgroundSize && playgroundSize <= 15) {
            ActiveGameState.playgroundScale = 35;
        } else if (16 <= playgroundSize && playgroundSize <= 20) {
            ActiveGameState.playgroundScale = 25;
        } else if (21 <= playgroundSize && playgroundSize <= 25) {
            ActiveGameState.playgroundScale = 20;
        } else if (26 <= playgroundSize && playgroundSize <= 30) {
            ActiveGameState.playgroundScale = 15;
        }
    }




}
