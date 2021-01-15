package Network;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Communication implements IServer{
    public static final Logger logServer = Logger.getLogger("parent.server");
    private ServerSocket server;

    /**
     * Constructor of the server, the port is set up to 500000
     */
    public Server ( ) {
        try {

            server = new ServerSocket(50000);
            server.setSoTimeout(600000); //1min keine Antwort vom Client -> beendet sich selber

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Closes the client or sever socket and the associated writers and readers
     */
    @Override
    public void closeConnection(){
        if (server != null) {
            try {
                server.close();
                this.setConnected(false);
                this.closeReaderWriter();
                logServer.log(Level.INFO, "Serversocket closed!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Serversocket konnte nicht geschlossen werden");
                logServer.log(Level.INFO,"Serversocket failed to close!");
            }
        }
    }

    @Override
    public String[] getAllIPAddress() {
     try {
          String localHost = InetAddress.getLocalHost().getHostName();

          String[] ips = new String[Inet4Address.getAllByName(localHost).length];
          for (int i = 0; i < Inet4Address.getAllByName(localHost).length; i++) {
              ips[i] = Arrays.toString(InetAddress.getAllByName(localHost));
          }
          return ips;
      }
      catch(UnknownHostException e){
          e.printStackTrace();
          return null;
      }
    }




    //This method is from the Internet Source: https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
    @Override
    public String getIPAddress() {
        String ip;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            return null;
        }
        return ip;
    }

    @Override
    public boolean startSeverConnection(){

        try {
            System.out.println("Waiting for Client");
            Socket server_connected  = server.accept();
            server_connected.setSoTimeout(60000);
            System.out.println("Connection from Server to Client established");

            //Set up input and output reader reading/writing form the in-/output stream of the socket
            //Set them up as buffered reader to read and write lines instead of bytes
            this.setInputReader(new BufferedReader(new InputStreamReader(server_connected.getInputStream())));

            this.setOutputWriter(new BufferedWriter (new OutputStreamWriter(server_connected.getOutputStream())));


            this.setConnected(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
