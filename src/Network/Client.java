package Network;

import Gui_View.HelpMethods;
import Player.ActiveGameState;
import Player.NetworkLogger;

import java.io.*;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Communication  {
    public static final Logger logClient = Logger.getLogger("parent.client");
    private Socket client;

    /**
     * Constructor of the client, call it with the address of remote, the port is set up to 50000
     * @param ipAddress the address of remote
     */
    public Client(String ipAddress) {
        try {

            //Create an connected socket
            client = new Socket(ipAddress, 50000);

            //Set the TimeoutTime to 1 min, after one minute closes the client socket
            client.setSoTimeout(60000);
            this.setConnected(true);

            //Set up input and output reader reading/writing form the in-/output stream of the socket
            //Set them up as buffered reader to read and write lines instead of bytes
            this.setInputReader(new BufferedReader(new InputStreamReader(client.getInputStream())));

            this.setOutputWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
            ActiveGameState.setRunning(true);
            logClient.log(Level.INFO, "Client connected to Server.");
        } catch (UnknownHostException | ConnectException e){
            //TODO Host is now known
            ActiveGameState.setRunning(false);
            //HelpMethods.connectionFailed();
        } catch (IOException e) {
            e.printStackTrace();
            ActiveGameState.setRunning(false);
            logClient.log(Level.SEVERE,"Failed to connect to Server.");
        }

    }

    /**
     * Closes the client or sever socket and the associated writers and readers
     */
    @Override
    public void closeConnection(){
        if (client != null){
            try {
                client.close();
                this.setConnected(false);
                this.closeReaderWriter();
                logClient.log(Level.INFO,"Client connection closed!");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Client konnte nicht geschlossen werden");
                logClient.log(Level.SEVERE,"Failed to close Client connection");
            }
        }
    }
}

