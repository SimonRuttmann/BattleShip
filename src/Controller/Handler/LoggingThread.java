package Controller.Handler;

import Player.NetworkLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class LoggingThread extends Thread{
    String name;
    public LoggingThread(String name){
        this.name = name;
    }
    public void run() {
        System.out.println("Geben sie das Logging Level ein. Standart ist ALL.");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        while (input != null) {
            try {
                input = reader.readLine();
                switch (input){
                    case "ALL":
                        NetworkLogger.setLevel(Level.ALL);
                        break;
                    case "CONFIG":
                        NetworkLogger.setLevel(Level.CONFIG);
                        break;
                    case "FINE":
                        NetworkLogger.setLevel(Level.FINE);
                        break;
                    case "FINER":
                        NetworkLogger.setLevel(Level.FINER);
                        break;
                    case "FINEST":
                        NetworkLogger.setLevel(Level.FINEST);
                        break;
                    case "INFO":
                        NetworkLogger.setLevel(Level.INFO);
                        break;
                    case "OFF":
                        NetworkLogger.setLevel(Level.OFF);
                        break;
                    case "SEVERE":
                        NetworkLogger.setLevel(Level.SEVERE);
                        break;
                    case "WARNING":
                        NetworkLogger.setLevel(Level.WARNING);
                        break;
                    default:
                        System.out.println("Doesn't match keyword");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }
