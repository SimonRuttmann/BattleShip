package Player;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SafeAndLoad implements ISafeAndLoad {

    @Override
    public void safe(Savegame o, int temp){
        try{
            Gson gson = new Gson(); // create Gson instance
            Writer writer = Files.newBufferedWriter(Paths.get(temp+".json")); //create writer

            gson.toJson(o, writer);

            writer.close();
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public Savegame load(Savegame e, int temp){
        try {
            Gson gson = new Gson(); // create Gson instance
            Reader reader = Files.newBufferedReader(Paths.get(temp+".json")); //create a reader
            e = gson.fromJson(reader, Savegame.class); // write File content to Savegame-Object e
            reader.close();
        } catch (Exception ex){
            System.out.println(e); //Print Error, if Error occured.
        }
        return e;
    }
}
