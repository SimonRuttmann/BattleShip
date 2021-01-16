package Network;


import Player.NetworkLogger;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Communication implements ICommunication{
    public static final Logger logCommunciation = Logger.getLogger("parent.communication");

    private boolean connected = false;
    private BufferedReader inputReader;
    private BufferedWriter outputWriter;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setInputReader(BufferedReader inputReader) {
        this.inputReader = inputReader;
    }

    public void setOutputWriter(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }


    /**
     * Sends a command to the connection partner (e.g. sendCMD(CMD.ships,"5 5 5"))
     * @param command Keyword of the Command, accessible via the CMD enumeration
     * @param parameter the following parameters for this command, starting without a space
     */
    @Override
    public void sendCMD(CMD command, String parameter) {
        //Building up the String
        String sendCMD;
        if (!parameter.isEmpty()) {
            sendCMD = command.toString() + " " + parameter;
        }
        else{
            sendCMD = command.toString();
        }

        if (!connected) return;
        try {
            logCommunciation.log(Level.INFO,"Send CMD " +  String.format("%s%n",sendCMD));

            //Necessary to write the command and a new line
            outputWriter.write(String.format("%s%n",sendCMD));

            //The outputWriter is a bufferedWriter,
            //therefore we need to flush the buffer respectively
            //to write any saved characters
            outputWriter.flush();

        } catch (IOException e) {
            logCommunciation.log(Level.SEVERE,"Error while writing!" + sendCMD);
        }
    }

    /**
     * Gets the next command written by the connected partner
     * The command is already separated into strings for the keyword and every single parameter if any
     * @return a valid command form the connected partner
     */
    @Override
    public String[] getCMD() {
        if (!connected) return null;
        logCommunciation.log(Level.FINE,"Connection status is true");

        String[] timeout = new String[1];
        timeout[0] = "timeout";
        try {


            String cmd = inputReader.readLine();
            logCommunciation.log(Level.INFO,"Socket receives: " + cmd);

            //Check if the data received is valid
            if(cmd != null && validDataReceived(cmd)){
                //If the data is valid, return the split command
                String[] cmdSplit = cmd.split(" ");
                cmdSplit[0] = cmdSplit[0].toLowerCase();
                return cmdSplit;
            }
            if (cmd == null) {
                logCommunciation.log(Level.WARNING,"Remote sent null, planned timeout occurred");
                return timeout;
            }

            logCommunciation.log(Level.WARNING,"The received cmd: " + cmd + "is not valid");
            String[] invalid = new String[1];
            invalid[0] = "invalid";
            return invalid;
    //TODO SocketTimoutException Read timed out -> bei .readLine();
            //TODO SocketException Connection Reset
        }
        //Exception handling
        //In those cases the handler´s, calling this method need the command timeout

        //This exception will occur, when the remote closes the connection,
        //after 1 min (time set up) the readerLine() method will throw an
        //SocketTimeoutException
        catch( SocketTimeoutException e){

            logCommunciation.log(Level.WARNING,"Socket Timout Exception at reading the next command");
            return timeout;
        }

        catch(SocketException e){

            logCommunciation.log(Level.WARNING,"Connection reset");
            return timeout;
        }

        catch (IOException e) {

            logCommunciation.log(Level.SEVERE,"IO Eception at reading the next command");
            return timeout;

        }


    }


    /**
     * This method checks if the data we received
     * @param receivedDate The string to check
     * @return True, if the date received is valid
     */
    private boolean validDataReceived(String receivedDate){
        try{
            return this.validDataChecker(receivedDate);
        }catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * HelpMethod to check the string
     * @param receivedData The string to check
     * @return True, if the data received is valid
     *         False or NumberFormat exception, when the data is invalid
     * @throws NumberFormatException The exception can be thrown, if the parameters aren´t numbers
     */
    private boolean validDataChecker(String receivedData) throws NumberFormatException{
        String[] splitData = receivedData.split(" ");

        //No definition of lower- or uppercase commands
        //therefore, transform the chars into lower case
        String keyword = splitData[0].toLowerCase();

        switch(keyword){
            case "size":
                if (splitData.length == 2){
                    int playgroundsize = Integer.parseInt(splitData[1]);
                    return 5 <= playgroundsize && playgroundsize <= 30;
                }
                return false;

            case "ships":
                boolean isValid = false;

                if (splitData.length >= 2){
                    for (int i = 1; i < splitData.length; i++){
                        int shipsize = Integer.parseInt(splitData[i]);
                        if ( 2 <= shipsize && shipsize <= 5 ){
                            isValid = true;
                        }
                        else{
                            return false;
                        }
                    }
                }
                return isValid;

            case "shot":
                if ( splitData.length == 3){
                    int row = Integer.parseInt(splitData[1]);
                    int column = Integer.parseInt(splitData[2]);
                    return (1 <= row && row <= 30) && (1 <= column && column <= 30);
                }
                return false;

            case "answer":
                if ( splitData.length == 2){
                    int arg = Integer.parseInt(splitData[1]);
                    return arg == 0 || arg == 1 || arg == 2;
                }
                return false;

            case "save":
            case "load":
                if (splitData.length == 2){
                    //id never used, but necessary to throw an number format exception
                    long id = Long.parseLong(splitData[1]);
                    return true;
                }
                return false;

            case "next":
            case "done":
            case "ready":
                return splitData.length == 1;


            //optional commands for the extended mode
            case "timeout":
                if (splitData.length == 2){
                    long time = Long.parseLong(splitData[1]);
                    return time > 10000;
                }
                return false;

            case "name":
            case "repeat":
            case "firstShot":
                return splitData.length == 2;
            default: return false;
        }

    }

    /**
     * This method is called by the closeConnection method in Server.java and Client.java
     * Closes the readers and writers
     */
    protected void closeReaderWriter(){

        try {
            this.inputReader.close();
            this.outputWriter.close();
        } catch (IOException | NullPointerException e) {
            logCommunciation.log(Level.FINE,"Reader and Writer can`t be closed");
        }
    }

}
