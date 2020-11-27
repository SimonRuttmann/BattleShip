package Network;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server extends Communication implements IServer{

    private ServerSocket server;

    public Server ( ) {
        try {

            server = new ServerSocket(50000);
            server.setSoTimeout(600000); //1min keine Antwort vom Client -> beendet sich selber

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeConnection(){
        if (server != null) {
            try {
                server.close();
                this.setConnected(false);
                this.closeReaderWriter();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Serversocket konnte nicht geschlossen werden");
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
            System.out.println("Connection from Server to Client established");
            // Ein- und Ausgabestrom des Sockets ermitteln
            // und als BufferedReader bzw. Writer verpacken
            // (damit man zeilen- bzw. zeichenweise statt byteweise arbeiten kann).
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
