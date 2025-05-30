package LoggingNetwork;

import GameData.ActiveGameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoggingThread extends Thread{
    String name;
    public LoggingThread(String name){
        this.name = name;
    }
    public void run() {

            System.out.println("Please type in your desired logging level. Default is ALL.");
            System.out.println("Following keywords are allowed: \n" +
                "\t 'ALL'     \n" +
                "\t 'FINE'    \n" +
                "\t 'INFO'    \n" +
                "\t 'WARNING' \n" +
                "\t 'SEVERE'  \n" +
                "\t 'OFF'     \n" +
                "\t 'CLOSE'   \n");

            final Socket s;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            ActiveGameState.setLoggingReader(reader);
            String input = "";
            while (ActiveGameState.isLogging() && !input.equals("Close")) {
                try {

                    input = interruptibleReadLine(reader);
                    //This one isn`t interruptible
                    //input = reader.readLine();

                    if (input.contains("FINE")) input = "FINE";
                    if (input.contains("INFO")) input = "INFO";
                    if (input.contains("OFF")) input = "OFF";
                    if (input.contains("SEVERE")) input = "SEVERE";
                    if (input.contains("WARNING")) input = "WARNING";
                    if (input.contains("CLOSE")) input = "CLOSE";

                    switch (input) {
                        case "ALL":
                            NetworkLogger.setLevel(Level.ALL);
                            System.out.println("Changed to ALL");
                            break;
                        case "FINE":
                            NetworkLogger.setLevel(Level.FINE);
                            System.out.println("Changed to FINE");
                            break;
                        case "INFO":
                            NetworkLogger.setLevel(Level.INFO);
                            System.out.println("Changed to INFO");
                            break;
                        case "OFF":
                            NetworkLogger.setLevel(Level.OFF);
                            System.out.println("Changed to OFF");
                            break;
                        case "SEVERE":
                            NetworkLogger.setLevel(Level.SEVERE);
                            System.out.println("Changed to SEVERE");
                            break;
                        case "WARNING":
                            NetworkLogger.setLevel(Level.WARNING);
                            System.out.println("Changed to WARNING");
                            break;
                        case "CLOSE":
                            reader.close();
                            break;
                        case "\n":
                        case " ":
                        case "": break;
                        default:
                            System.out.println("Doesn't match keyword, following keywords are allowed: \n" +
                                                "\t 'ALL'     \n" +
                                                "\t 'FINE'    \n" +
                                                "\t 'INFO'    \n" +
                                                "\t 'WARNING' \n" +
                                                "\t 'SEVERE'  \n" +
                                                "\t 'OFF'     \n" +
                                                "\t 'CLOSE'   \n");
                    }
                } catch (IOException e) {
                    ActiveGameState.setLogging(false);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

    }


    /**
     * Implementation of an "interruptible" Reader, reads only if chars are available on the console
     * https://stackoverflow.com/questions/3595926/how-to-interrupt-bufferedreaders-readline?noredirect=1&lq=1
     * @param reader Buffered reader, which reads the next line
     * @return The string read from the inputStream from the reader
     */
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
