package Controller.Handler;

import Player.ActiveGameState;
import Player.NetworkLogger;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoggingThread extends Thread{
    String name;
    public LoggingThread(String name){
        this.name = name;
    }
    public void run() {

            System.out.println("Geben sie das Logging Level ein. Standard ist ALL.");

            final Socket s;
         //   s = new Socket("localhost", 49999);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


            ActiveGameState.loggingReader = reader;
            String input = "";
            while (ActiveGameState.isLogging() && !input.equals("Close")) {
                try {
                    //TODO .readline -> Läuft bereits -> muss abgebrochen werden -> geht nicht mittels .close, da readLine bereits aufgerufen wurde
              //      input = ActiveGameState.loggingReader.readLine();
                    input = interruptibleReadLine(reader);
                    System.out.println( input);
                    switch (input) {
                        case "ALL":
                            NetworkLogger.setLevel(Level.ALL);
                            break;
                        case "CONFIG":
                            NetworkLogger.setLevel(Level.CONFIG);
                            break;
                        case "FINE":
                            NetworkLogger.setLevel(Level.FINE);     //genau ausgabe
                            break;
                        case "FINER":
                            NetworkLogger.setLevel(Level.FINER);
                            break;
                        case "FINEST":
                            NetworkLogger.setLevel(Level.FINEST);
                            break;
                        case "INFO":
                            NetworkLogger.setLevel(Level.INFO);     //standard ausgabe
                            break;
                        case "OFF":
                            NetworkLogger.setLevel(Level.OFF);
                            break;
                        case "SEVERE":
                            NetworkLogger.setLevel(Level.SEVERE);   //Ausgabe von nicht behandelbaren Fehlern
                            break;
                        case "WARNING":
                            NetworkLogger.setLevel(Level.WARNING);  //Ausgabe von behandelten Fehlern
                            break;
                        case "Close":
                            reader.close();
                            break;
                        default:
                            System.out.println("Doesn't match keyword");
                    }
                } catch (IOException e) {
                    ActiveGameState.setLogging(false);
                    System.out.println( "Reader Closed");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

    }

    //https://stackoverflow.com/questions/3595926/how-to-interrupt-bufferedreaders-readline?noredirect=1&lq=1
    private String interruptibleReadLine(BufferedReader reader)
            throws InterruptedException, IOException {
        Pattern line = Pattern.compile("^(.*)\\R");
        Matcher matcher;
        boolean interrupted = false;

        StringBuilder result = new StringBuilder();
        int chr = -1;
        do {
            if (reader.ready()) chr = reader.read();
            if (chr > -1) result.append((char) chr);
            matcher = line.matcher(result.toString());
            interrupted = Thread.interrupted(); // resets flag, call only once
        } while (!interrupted && !matcher.matches());
        if (interrupted) throw new InterruptedException();
        return (matcher.matches() ? matcher.group(1) : "");
    }


    }
