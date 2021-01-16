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

public class SaveAndLoad {


    public static boolean save(String nameOfSavegame, long id){
        if ( SavegameLinker.writeLinker(nameOfSavegame, id) ) {
            return save(nameOfSavegame);
        }
        return false;
    }


    public static boolean save(String nameOfSavegame){
        ///new Savegame(ActiveGameState.)

            Savegame o = constructSaveGame();

        try{

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IDrawable.class, new InterfaceAdapterForPlayground());
            builder.registerTypeAdapter(IShip.class, new InterfaceAdapterForPlayground());
            Gson gson = builder.create();
          //  Gson gson = new Gson(); // create Gson instance
            // temp = chosen name of file: e.g. temp = "test" -> output Paths.get: .savedGames/test.json
            Writer writer;
            if (!ActiveGameState.isMultiplayer()) {
                writer = Files.newBufferedWriter(Paths.get(".singleplayerGames/" + nameOfSavegame + ".json")); //create writer
            }
            else{
                writer = Files.newBufferedWriter(Paths.get(".multiplayerGames/" + nameOfSavegame + ".json")); //create writer
            }
//getClass.getRessourceAsStream("/.savedGame")
            gson.toJson(o, writer);

            writer.close();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Saving failed");
            return false;
        }
    }


    public static Savegame load (long id){
        String nameOfSavegame = SavegameLinker.readLinker(id);
        System.out.println("Found the name of the savegame: "+ nameOfSavegame+ "ReferredID: " + id);
        return load(nameOfSavegame);
    }



    public static Savegame load (String nameOfSavegame){

        try {

            System.out.println(nameOfSavegame);
            if ( ActiveGameState.isMultiplayer() && !(nameOfSavegame.contains(".multiplayer"))){
                nameOfSavegame = ".multiplayerGames\\" + nameOfSavegame + ".json";
            }
           // if ( ActiveGameState.isMultiplayer() && !(nameOfSavegame.contains(".multiplayer/"))){
           //     nameOfSavegame = ".multiplayerGames/" + nameOfSavegame + ".json";
           // }
            //Singleplayer wird direkt der richtige pfad übergeben
            //else{
            //    nameOfSavegame = ".singleplayerGames/" + nameOfSavegame + ".json";
            //}

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IDrawable.class, new InterfaceAdapterForPlayground());
            builder.registerTypeAdapter(IShip.class, new InterfaceAdapterForPlayground());
            Gson gson = builder.create();

           // Gson gson = new Gson(); // create Gson instance
            // temp = the complete path to the file that is intend to be loaded: e.g. temp = ".savedGames/test.json"
            System.out.println(nameOfSavegame);
            Reader reader = Files.newBufferedReader(Paths.get(nameOfSavegame)); //create a reader
            Savegame e = gson.fromJson(reader, Savegame.class); // write File content to Savegame-Object e
            reader.close();
            setSavegameToActiveGameState(e);
            return e;
        } catch (Exception ex){
            ex.printStackTrace(); //Print Error, if Error occured.
            System.out.println("Load didn't work");
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
