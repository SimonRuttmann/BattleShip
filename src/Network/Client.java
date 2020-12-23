package Network;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Communication {

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

        } catch (IOException e) {
            e.printStackTrace();
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

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Client konnte nicht geschlossen werden");
            }
        }
    }
}

