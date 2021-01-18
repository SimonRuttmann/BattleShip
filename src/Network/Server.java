package Network;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Communication implements IServer{
    public enum ConnectionStatus {Connected, Timeout, ManualClose, ioException}

    public static final Logger logServer = Logger.getLogger("parent.server");

    private ServerSocket server;
    private Socket server_connected;
    /**
     * Constructor of the server, the port is set up to 500000
     * Timeout time is set to 1 min
     */
    public Server ( ) {
        try {

            server = new ServerSocket(50000);
            server.setSoTimeout(60000); //1min keine Antwort vom Client -> beendet sich selber

        } catch (IOException e) {
            logServer.log(Level.SEVERE,"IO Exception at creating the server socket");
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
                if ( server_connected != null ) server_connected.close();
                this.setConnected(false);
                this.closeReaderWriter();
                logServer.log(Level.INFO, "Serversocket closed!");
            } catch (IOException e) {
                logServer.log(Level.SEVERE,"Serversocket failed to close!");
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
          logServer.log(Level.SEVERE, "Connection over datagrammSocket impossible, no IP can be shown");
          return null;
      }
    }


    /**
     * Creates a DatagrammSocket, to determine the local address that would be used to connect to the specified remote host
     * This method is based on: https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     * @return A String of the currently preferred IP-Address (Chosen by the OS)
     */
    @Override
    public String getIPAddress() {
        String ip;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();

        } catch (UnknownHostException | SocketException e) {

            logServer.log(Level.SEVERE, "Connection over datagrammSocket impossible, no IP can be shown");
            return null;
        }
        return ip;
    }

    @Override
    public Server.ConnectionStatus startSeverConnection(){

        try {

            logServer.log(Level.INFO, "Waiting for Client to connect");

            Socket server_connected  = server.accept();
            server_connected.setSoTimeout(60000);

            logServer.log(Level.INFO, "Connection from Server to Client established");
            this.server_connected = server_connected;

            //Set up input and output reader reading/writing form the in-/output stream of the socket
            //Set them up as buffered reader to read and write lines instead of bytes
            this.setInputReader(new BufferedReader(new InputStreamReader(server_connected.getInputStream())));

            this.setOutputWriter(new BufferedWriter (new OutputStreamWriter(server_connected.getOutputStream())));


            this.setConnected(true);
            logServer.log(Level.FINE,"Reader set and connected set to true");
            return ConnectionStatus.Connected;
        }

        //This exception occurs, when no socket connected to the server for 1 min
        catch (SocketTimeoutException e){
            logServer.log(Level.WARNING, "Timeout at offering connection");
            return ConnectionStatus.Timeout;
        }
        //This exception is thrown by closing the server,
        //if the server.accept() didn`t already connection the remote socket
        //No actions are needed for handling
        catch (SocketException e){
            logServer.log(Level.INFO, "Manual close at offering connection");
            return ConnectionStatus.ManualClose;
        }
        //All other standard IO Exception cases
        catch (IOException e) {
            logServer.log(Level.SEVERE, "IOException by offering connection");
            return ConnectionStatus.ioException;
        }
    }
}
