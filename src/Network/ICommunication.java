package Network;

public interface ICommunication {

//    /**
//     * This method needs to be started, before commands can be send or received
//     * Starts the communication Thread to enable the transmission
//     */
//    void startListening();
    /**
     * Sends a command to the connection partner (e.g. sendCMD(CMD.ships,"5 5 5"))
     * @param command Keyword of the Command, accessible via the CMD enumeration
     * @param parameter the following parameters for this command, starting without a space
     */
    void sendCMD (CMD command, String parameter);

    /**
     * Gets the next command written by the connected partner
     * The command is already separated into strings for the keyword and every single parameter if any
     * @return a valid command form the connected partner
     */
    String[] getCMD ();

    /**
     * Closes the Client or SeverSocket and the associated Writers and Readers
     */
    void closeConnection();
}
