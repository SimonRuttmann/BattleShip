package Player;

import Model.Playground.EnemyPlayground;
import Model.Playground.OwnPlayground;
import Model.Ship.IShip;
import Model.Util.IDrawable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAndLoad {
    public static final Logger logSaveAndLoad = Logger.getLogger("parent.SaveAndLoad");

    /**
     * Call this method in multiplayer mode, the Name and ID are referenced to each other
     * @param nameOfSavegame The name of the Savegame, which has to be saved
     * @param id The id, send to the remote. This id is written down in the SavegameLinker-File.
     * @return true, if the save worked, false otherwise
     */
    public static boolean save(String nameOfSavegame, long id){
        if ( SavegameLinker.writeLinker(nameOfSavegame, id) ) {
            return save(nameOfSavegame);
        }
        return false;
    }

    /**
     * Saves a game with a name given by the User
     * @param nameOfSavegame The name of the Savegame, which has to be saved
     * @return true, if the save worked, false otherwise
     */
    public static boolean save(String nameOfSavegame){

            Savegame o = constructSaveGame();

        try{

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IDrawable.class, new InterfaceAdapterForPlayground());
            builder.registerTypeAdapter(IShip.class, new InterfaceAdapterForPlayground());
            Gson gson = builder.create();

            Writer writer;
            if (!ActiveGameState.isMultiplayer()) {
                writer = Files.newBufferedWriter(Paths.get(".singleplayerGames/" + nameOfSavegame + ".json")); //create writer
            }
            else{
                writer = Files.newBufferedWriter(Paths.get(".multiplayerGames/" + nameOfSavegame + ".json")); //create writer
            }

            gson.toJson(o, writer);

            writer.close();

            logSaveAndLoad.log(Level.INFO, "Game successfully saved");

            return true;
        } catch (Exception ex){

            logSaveAndLoad.log(Level.SEVERE, "Couldn`t save game");

            return false;
        }
    }


    /**
     * Use this method in multiplayer mode, when we have to load a game with a specified id
     * @param id The id, given by the remote
     * @return The Savegame-Object if load worked, in any other case null
     */
    public static Savegame load (long id){
        String nameOfSavegame = SavegameLinker.readLinker(id);

        logSaveAndLoad.log(Level.INFO, "Found the name of the savegame: "+ nameOfSavegame+ "ReferredID: " + id );

        return load(nameOfSavegame);
    }


    /**
     * Use this method, when the player loads a game
     * @param nameOfSavegame The name of the gamefile.
     * @return The Savegame-Object if load worked, in any other case null
     */
    public static Savegame load (String nameOfSavegame){

        try {

            if ( ActiveGameState.isMultiplayer() && !(nameOfSavegame.contains(".multiplayer"))){
                nameOfSavegame = ".multiplayerGames/" + nameOfSavegame + ".json";
            }


            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IDrawable.class, new InterfaceAdapterForPlayground());
            builder.registerTypeAdapter(IShip.class, new InterfaceAdapterForPlayground());
            Gson gson = builder.create();


            Reader reader = Files.newBufferedReader(Paths.get(nameOfSavegame)); //create a reader
            Savegame e = gson.fromJson(reader, Savegame.class); // write File content to Savegame-Object e
            reader.close();
            setSavegameToActiveGameState(e);

            logSaveAndLoad.log(Level.INFO, "Loaded game: " + nameOfSavegame);
            return e;
        } catch (Exception ex){

            logSaveAndLoad.log(Level.SEVERE, "Load didn`t work, File may be corrupted");
            return null;
        }

    }

    public static void setSavegameToActiveGameState( Savegame e ){
                ActiveGameState.setLoadId(e.id);
                ActiveGameState.setModes(e.modes);
                ActiveGameState.setOwnPlayerKi(e.OwnPlayerKi);
                ActiveGameState.setOwnPlayerIOwnPlayground(e.ownPlayerIOwnPlayground);
                ActiveGameState.setOwnPlayerIEnemyPlayground(e.ownPlayerIEnemyPlayground);
                ActiveGameState.setEnemyPlayerOwnPlayground(e.enemyPlayerOwnPlayground);
                ActiveGameState.setEnemyPlayerEnemyPlayground(e.enemyPlayerEnemyPlayground);
                ActiveGameState.setMultiplayer(e.multiplayer);
                ActiveGameState.setOwnPlayerName(e.ownPlayerName);
                ActiveGameState.setPlaygroundSize(e.playgroundSize);
                ActiveGameState.setAmountOfShips(e.amountOfShips);
                ActiveGameState.setAmountShipSize2(e.amountShipSize2);
                ActiveGameState.setAmountShipSize3(e.amountShipSize3);
                ActiveGameState.setAmountShipSize4(e.amountShipSize4);
                ActiveGameState.setAmountShipSize5(e.amountShipSize5);
                ActiveGameState.setEnemyKi(e.enemyKi);
                ActiveGameState.setOwnKi(e.ownKi);
                ActiveGameState.setSceneIsPlaceShips(e.sceneIsPlaceShips);
                ActiveGameState.setSceneIsGamePlayground(e.sceneIsGamePlayground);
                ActiveGameState.setEnemyKiDifficulty(e.enemyDifficulty);
                ActiveGameState.setOwnKiDifficulty(e.ownDifficulty);
                ActiveGameState.setSelfOrKi(e.selfOrKi);
                ActiveGameState.setYourTurn(e.yourTurn);
    }

    public static Savegame constructSaveGame(){
        return  new Savegame(
                ActiveGameState.getLoadId(),
                ActiveGameState.getModes(),
                ActiveGameState.isOwnPlayerKi(),
                (OwnPlayground)ActiveGameState.getOwnPlayerIOwnPlayground(),
                (EnemyPlayground)ActiveGameState.getOwnPlayerIEnemyPlayground(),
                (OwnPlayground)ActiveGameState.getEnemyPlayerOwnPlayground(),
                (EnemyPlayground)ActiveGameState.getEnemyPlayerEnemyPlayground(),
                ActiveGameState.isMultiplayer(),
                ActiveGameState.getOwnPlayerName(),
                ActiveGameState.getPlaygroundSize(),
                ActiveGameState.getAmountOfShips(),
                ActiveGameState.getAmountShipSize2(),
                ActiveGameState.getAmountShipSize3(),
                ActiveGameState.getAmountShipSize4(),
                ActiveGameState.getAmountShipSize5(),
                ActiveGameState.getEnemyKi(),
                ActiveGameState.getOwnKi(),
                ActiveGameState.isSceneIsPlaceShips(),
                ActiveGameState.isSceneIsGamePlayground(),
                ActiveGameState.getEnemyKiDifficulty(),
                ActiveGameState.getOwnKiDifficulty(),
                ActiveGameState.isSelfOrKi(),
                ActiveGameState.isYourTurn()
        );
    }
}
