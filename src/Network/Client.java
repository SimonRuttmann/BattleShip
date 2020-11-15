package Network;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Communication {
    private Socket client;

    public Client(String ipAddress) {
        try {

            client = new Socket(ipAddress, 50000);
            client.setSoTimeout(600000); //1min keine Antwort vom Client -> beendet sich selber

            // Ein- und Ausgabestrom des Sockets ermitteln
            // und als BufferedReader bzw. Writer verpacken
            // (damit man zeilen- bzw. zeichenweise statt byteweise arbeiten kann).
            this.setInputReader(new BufferedReader(new InputStreamReader(client.getInputStream())));

            this.setOutputWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

