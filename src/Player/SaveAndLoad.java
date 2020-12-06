package Player;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveAndLoad {
//TODO Temp = Name bzw ID des Spielstandes... umbenennen
    //TODO Es wird kein Objekt übergeben, diese intern von der ActiveGameState übertragen werden
    public static boolean save(Savegame o, String temp){
        ///new Savegame(ActiveGameState.)

        try{
            Gson gson = new Gson(); // create Gson instance
            // temp = chosen name of file: e.g. temp = "test" -> output Paths.get: .savedGames/test.json
            Writer writer = Files.newBufferedWriter(Paths.get(".savedGames/"+temp+".json")); //create writer

            gson.toJson(o, writer);

            writer.close();
            return true;
        } catch (Exception ex){
            System.out.println("Saving failed");
            return false;
        }
    }

    public static Savegame load(Savegame e, String temp){
        try {
            Gson gson = new Gson(); // create Gson instance
            // temp = the complete path to the file that is intend to be loaded: e.g. temp = ".savedGames/test.json"
            Reader reader = Files.newBufferedReader(Paths.get(temp)); //create a reader
            e = gson.fromJson(reader, Savegame.class); // write File content to Savegame-Object e
            reader.close();
        } catch (Exception ex){
            System.out.println(e); //Print Error, if Error occured.
        }
        return e;
    }
}
