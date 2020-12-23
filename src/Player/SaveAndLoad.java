package Player;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveAndLoad {
//TODO Temp = Name bzw ID des Spielstandes... umbenennen
    //TODO Es wird kein Objekt übergeben, diese intern von der ActiveGameState übertragen werden
    public static boolean save(String temp){
        ///new Savegame(ActiveGameState.)

            Savegame o = constructSaveGame();

        try{
            Gson gson = new Gson(); // create Gson instance
            // temp = chosen name of file: e.g. temp = "test" -> output Paths.get: .savedGames/test.json
            Writer writer = Files.newBufferedWriter(Paths.get(".savedGames/"+temp+".json")); //create writer
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
    //TODO return boolean
    public static Savegame load(String temp){

        try {
            Gson gson = new Gson(); // create Gson instance
            // temp = the complete path to the file that is intend to be loaded: e.g. temp = ".savedGames/test.json"
            Reader reader = Files.newBufferedReader(Paths.get(temp)); //create a reader
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
                ActiveGameState.setKi(e.ki);
                ActiveGameState.setSceneIsPlaceShips(e.sceneIsPlaceShips);
                ActiveGameState.setSceneIsGamePlayground(e.sceneIsGamePlayground);
                ActiveGameState.setDifficulty(e.difficulty);
                ActiveGameState.setSelfOrKi(e.selfOrKi);
                ActiveGameState.setYourTurn(e.yourTurn);
    }

    public static Savegame constructSaveGame(){
        return  new Savegame(   ActiveGameState.getModes(),
                ActiveGameState.isOwnPlayerKi(),
                ActiveGameState.getOwnPlayerIOwnPlayground(),
                ActiveGameState.getOwnPlayerIEnemyPlayground(),
                ActiveGameState.getEnemyPlayerOwnPlayground(),
                ActiveGameState.getEnemyPlayerEnemyPlayground(),
                ActiveGameState.isMultiplayer(),
                ActiveGameState.getOwnPlayerName(),
                ActiveGameState.getPlaygroundSize(),
                ActiveGameState.getAmountOfShips(),
                ActiveGameState.getAmountShipSize2(),
                ActiveGameState.getAmountShipSize3(),
                ActiveGameState.getAmountShipSize4(),
                ActiveGameState.getAmountShipSize5(),
                ActiveGameState.getKi(),
                ActiveGameState.isSceneIsPlaceShips(),
                ActiveGameState.isSceneIsGamePlayground(),
                ActiveGameState.getDifficulty(),
                ActiveGameState.isSelfOrKi(),
                ActiveGameState.isYourTurn()
        );
    }
}
