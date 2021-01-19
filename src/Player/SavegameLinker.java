package Player;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class is used to write and read form the SavegameLinker.txt, to
 * enable the user to give the saves a meaningful name
 * Use the methods in this class only in multiplayer save/load calls
 * (In singleplayer there is no ID exchange, the name can directly be used at saving and loading)
 */
public class SavegameLinker {
    public static final Logger logSavegameLinker = Logger.getLogger("parent.SavegameLinker");
    private static final String fileName = "LinkSavegames.txt";

    /**
     * Writes the savegame name and id in the LinkSavegames.txt file.
     * This method is used everytime a game in multiplayer is saved
     * @param savegamename The name of the savegame file, given by the user
     * @param id The id, representing this file, written/received to/from the remote
     * @return true, if the write process was executed right
     */
    public static boolean writeLinker(String savegamename, long id) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, true));
            fileWriter.write(savegamename + "=" + id + "\n");
            fileWriter.close();
        } catch (IOException e) {
            logSavegameLinker.log(Level.SEVERE, "IOException at writingLink");
            return false;
        }
        return true;
    }


    /**
     * Returns the name of the savegamefile referenced by the given ID
     * @param id The id from the remote
     * @return The referenced name of the savegame File
     */
    public static String readLinker(long id) {
        String nameLinkedWithID;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));

            while(true){
                String nameAndLink = fileReader.readLine();
                if (nameAndLink == null) return null;
                if (nameAndLink.contains( String.valueOf(id) )  ){
                    String[] nameAndLinkSplit = nameAndLink.split("=");
                    nameLinkedWithID = nameAndLinkSplit[0];
                    break;
                }

            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            logSavegameLinker.log(Level.SEVERE, "FileNotFound, make sure the LinkSavegames.txt isn`t removed from System");
            return null;
        } catch (IOException e) {
            logSavegameLinker.log(Level.SEVERE, "IO Exception at reading the LinkSavegames.txt");
            return null;
        }
        return nameLinkedWithID;
    }

    /**
     * Helpmethod, gets the id to the savegame name if any
     * @param savegame The savegame name
     * @return The id, if it occurs. -1 in any other case
     */
    private static long getIdFromSavegame(String savegame){
        long id;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));

            while(true){
                String nameAndLink = fileReader.readLine();
                if (nameAndLink == null) return -1;
                if (nameAndLink.contains( String.valueOf(savegame) )  ){
                    String[] nameAndLinkSplit = nameAndLink.split("=");
                    id = Long.parseLong(nameAndLinkSplit[1]);
                    break;
                }

            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            logSavegameLinker.log(Level.SEVERE, "FileNotFound, make sure the LinkSavegames.txt isn`t removed from System");
            return -1;
        } catch (IOException e) {
            logSavegameLinker.log(Level.SEVERE, "IO Exception at reading the LinkSavegames.txt");
            return -1;
        } catch (Exception e){
            logSavegameLinker.log(Level.SEVERE, "Undefined Exception at reading the LinkSavegames.txt");
            return -1;
        }
        return id;
    }

    /**
     * Removes all links between the savegame name and the id
     * Call this method, when a savegame is deleted and the id is not known
     * @param savegamename The name of the savegame
     * @return True, if the remove worked (also when nothing got removed, because no such link is present)
     */
    public static boolean removeLinker(String savegamename){
        long id = getIdFromSavegame(savegamename);
        if ( id == -1 ) logSavegameLinker.log(Level.WARNING, "Id referenced by savegame name " + savegamename + " not found");
        return removeLinker(savegamename, id);
    }



    /**
     * Removes all links between the savegame name and the id
     * Call this method, when a savegame is deleted and the id is known
     * @param savegamename The name of the savegame
     * @param id The id of the savegame
     * @return True, if the remove worked (also when nothing got removed, because no such link is present)
     */
    public static boolean removeLinker(String savegamename, long id){

        File inputFile = new File(fileName);
        File tempFile = new File("TempFile.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String removingSavegameLink = savegamename+"="+id;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                System.out.println(currentLine + "   " + removingSavegameLink);
                if (currentLine.equals(removingSavegameLink)) continue;
                System.out.println("gleich");
                writer.write(currentLine + System.getProperty("line.separator"));
            }

            writer.close();
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

        //Rename the TempFile to the LinkSavegames.txt
        //To enable the rename, the file has to be deleted
        if ( !(inputFile.delete() ) ) return false;
        return tempFile.renameTo(inputFile);
    }


    /*  //Test program
        public static void main(String[] args) {
        System.out.print(writeLinker("Das ist ein Spieeeelstand2", 872043100));
        //System.out.println(readLinker(872043100));
        //System.out.println(removeLinker("Das"));
        //System.out.println(readLinker(8720431));
        System.out.println(removeLinker("Das ist ein Spieeeelstand2", 872043100));
        //System.out.println(readLinker(8720431));

    }*/


}
