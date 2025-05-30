package Controller.WorkingThreads;
import Gui_View.HelpMethods;
import Gui_View.Main;
import KI.Ki;
import Network.CMD;
import GameData.ActiveGameState;
import GameData.GameMode;
import Serialize.SaveAndLoad;
import Serialize.Savegame;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This thread will:
 *
 * When we are Server:
 *      1. Send the command "size" with parameters
 *      2. Get the command "next"
 *      3. Send the command "ships" with parameters
 *      4. Get the command "done"
 *      5. Send the command "ready"
 *      6. Get the command "ready"
 *
 *      When loading:
 *      1. Send the command "load"
 *      4. Get the command "done"
 *      5. Send the command "ready"
 *      6. Get the command "ready"
 *      7. If we loaded a Game where the enemy´s
 *         turn is active send "next"
 *
 * When we are Client:
 *      1. Get the command "size" and parameters
 *      2. Send the command "next"
 *      3. Get the command "ships" and parameters
 *      4. Send the command "done"
 *      5. Get the command "ready"
 *      6. Send the command "ready"
 *
 *      When loading:
 *      1. Get the command "load"
 *      4. Send the command "done"
 *      5. Get the command "ready"
 *      6. Send the command "ready"
 *
 * In both cases:
 * The scene is set depending on the mode selected
 * player       -> place ships scene
 * ki or load   -> playground scene
 *
 */
public class MultiplayerControlThreadConfigCommunication extends Thread{
    public static final Logger logConfigCommunicationThread = Logger.getLogger("parent.MultiplayerControlThreadConfigCommunication");
    @Override
    public void run(){

        logConfigCommunicationThread.log(Level.INFO, "Multiplayer Control Thread Config Communication");
        logConfigCommunicationThread.log(Level.FINE, "Players turn: " + ActiveGameState.isYourTurn());

        //Server

        if (ActiveGameState.isAmIServer()){
            logConfigCommunicationThread.log(Level.FINE, "Server sends configuration");

            if ( ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer) ActiveGameState.setLoadWithNext(!ActiveGameState.isYourTurn());

            ActiveGameState.setYourTurn(true);
            String[] receivedCMD;
            if (!(ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer)) {
                logConfigCommunicationThread.log(Level.FINE, "Send player's set configuration");
                ActiveGameState.getServer().sendCMD(CMD.size, Integer.toString(ActiveGameState.getPlaygroundSize()));
                receivedCMD = ActiveGameState.getServer().getCMD();
                switch (receivedCMD[0]) {
                    case "next":        break;
                    case "timeout":     logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                        HelpMethods.connectionLost();
                                        ActiveGameState.getServer().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;

                    default:            logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Client, closing connection");
                                        HelpMethods.unexceptedMessage();
                                        ActiveGameState.getServer().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;
                }



                StringBuilder ships = new StringBuilder();
                for (int i = 0; i < ActiveGameState.getAmountShipSize2(); i++) {
                    ships.append("2");
                    ships.append(" ");
                }
                for (int i = 0; i < ActiveGameState.getAmountShipSize3(); i++) {
                    ships.append("3");
                    ships.append(" ");
                }
                for (int i = 0; i < ActiveGameState.getAmountShipSize4(); i++) {
                    ships.append("4");
                    ships.append(" ");
                }
                for (int i = 0; i < ActiveGameState.getAmountShipSize5(); i++) {
                    ships.append("5");
                    ships.append(" ");
                }

                //ships example:    5 5 5 5 2 3 2 3
                //with CMD.ships:   ships 5 5 5 5 2 3 2 3
                ActiveGameState.getServer().sendCMD(CMD.ships, ships.toString());

            }
            //Load Game
            else{
                logConfigCommunicationThread.log(Level.FINE, "Send loaded configuration");
                ActiveGameState.getServer().sendCMD(CMD.load, String.valueOf(ActiveGameState.getLoadId()));
            }
            //Get done
            receivedCMD = ActiveGameState.getServer().getCMD();

            switch  (receivedCMD[0]){
                case "done":        break;
                case "timeout":     logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                    HelpMethods.connectionLost();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;

                default:            logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Client, closing connection");
                                    HelpMethods.unexceptedMessage();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;
            }



            //send ready get ready
            ActiveGameState.getServer().sendCMD(CMD.ready, "");
            receivedCMD = ActiveGameState.getServer().getCMD();


            switch (receivedCMD[0]){
                case "ready": break;
                case "timeout":     logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                    HelpMethods.connectionLost();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;
                default:            logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Client, closing connection");
                                    HelpMethods.unexceptedMessage();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;
            }

            logConfigCommunicationThread.log(Level.INFO, "Game Configurations successfully transmitted to Client.");

            //Send next, as we loaded a game where the enemy turn is active
            if (ActiveGameState.isLoadWithNext()){
                ActiveGameState.getServer().sendCMD(CMD.next,"");
                logConfigCommunicationThread.log(Level.FINE, "Game is loaded and the client turn is active");
            }

        }

        //Client

        else{
            ActiveGameState.setYourTurn(false);
            String[] receivedCMD = ActiveGameState.getClient().getCMD();
            boolean load = false;
            switch  (receivedCMD[0]){
                case "size":    ActiveGameState.setPlaygroundSize(Integer.parseInt(receivedCMD[1]));
                                break;

                case "load":    ActiveGameState.setLoading(ActiveGameState.Loading.multiplayer);
                                Savegame savegame = SaveAndLoad.load(Long.parseLong(receivedCMD[1]));
                                if ( savegame == null) {
                                    logConfigCommunicationThread.log(Level.WARNING, "From server requested file couldn`t be loaded");
                                    HelpMethods.noGameFile();
                                    return;
                                }
                                logConfigCommunicationThread.log(Level.INFO, "Loaded file, requested from server");
                                load = true;
                                ActiveGameState.setLoading(ActiveGameState.Loading.multiplayer);
                                break;

                case "timeout": logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                HelpMethods.connectionLost();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;
                default:        logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Server, closing connection");
                                HelpMethods.unexceptedMessage();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;
            }

            if ( !load) {
                ActiveGameState.getClient().sendCMD(CMD.next, "");
                receivedCMD = ActiveGameState.getClient().getCMD();


                switch (receivedCMD[0]) {
                    case "ships":
                        int getShip;
                        int size2 = 0;
                        int size3 = 0;
                        int size4 = 0;
                        int size5 = 0;
                        for (int i = 1; i < receivedCMD.length; i++) {
                            getShip = Integer.parseInt(receivedCMD[i]);
                            switch (getShip) {
                                case 2:     size2++;    break;
                                case 3:     size3++;    break;
                                case 4:     size4++;    break;
                                case 5:     size5++;    break;
                                default:        logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Server at sending ships, closing connection");
                                                HelpMethods.unexceptedMessage();
                                                ActiveGameState.getClient().closeConnection();
                                                ActiveGameState.setRunning(false);
                                                return;
                            }
                        }

                        ActiveGameState.setAmountShipSize2(size2);
                        ActiveGameState.setAmountShipSize3(size3);
                        ActiveGameState.setAmountShipSize4(size4);
                        ActiveGameState.setAmountShipSize5(size5);
                        ActiveGameState.setAmountOfShips((size2 + size3 + size4 + size5));
                        if (ActiveGameState.getModes() == GameMode.kiVsRemote) ActiveGameState.setOwnKi(new Ki(ActiveGameState.getOwnKiDifficulty()));
                        break;
                    case "timeout":     logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                        HelpMethods.connectionLost();
                                        ActiveGameState.getClient().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;
                    default:            logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Server, closing connection");
                                        HelpMethods.unexceptedMessage();
                                        ActiveGameState.getClient().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;
                }
            }

            ActiveGameState.getClient().sendCMD(CMD.done, "");

            //get ready, send ready
            receivedCMD = ActiveGameState.getClient().getCMD();


            switch (receivedCMD[0]){
                case "ready": break;
                case "timeout": logConfigCommunicationThread.log(Level.WARNING, "Timeout appeared, closing connection");
                                HelpMethods.connectionLost();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;

                default:        logConfigCommunicationThread.log(Level.WARNING, "UnexpectedMessage from Server, closing connection");
                                HelpMethods.unexceptedMessage();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;
            }

            ActiveGameState.getClient().sendCMD(CMD.ready, "");



            logConfigCommunicationThread.log(Level.INFO, "Game Configurations successfully transmitted to Server.");

        }

        if (ActiveGameState.getModes() == GameMode.playerVsRemote){
            logConfigCommunicationThread.log(Level.INFO, "\nSettings: " + "\n" +
                    "\t Multiplayer Player vs Remote" + "\n" +
                    "\t Playgroundsize: " + ActiveGameState.getPlaygroundSize() + "\n" +
                    "\t Amount of Ships 2: " + ActiveGameState.getAmountShipSize2() + "\n" +
                    "\t Amount of Ships 3: " + ActiveGameState.getAmountShipSize3() + "\n" +
                    "\t Amount of Ships 4: " + ActiveGameState.getAmountShipSize4() + "\n" +
                    "\t Amount of Ships 5: " + ActiveGameState.getAmountShipSize5()
            );
        }else if ( ActiveGameState.getModes() == GameMode.kiVsRemote){
            logConfigCommunicationThread.log(Level.INFO, "\nSettings: " + "\n" +
                    "\t Multiplayer AI vs Remote" + "\n" +
                    "\t Playgroundsize: " + ActiveGameState.getPlaygroundSize() + "\n" +
                    "\t Amount of Ships 2: " + ActiveGameState.getAmountShipSize2() + "\n" +
                    "\t Amount of Ships 3: " + ActiveGameState.getAmountShipSize3() + "\n" +
                    "\t Amount of Ships 4: " + ActiveGameState.getAmountShipSize4() + "\n" +
                    "\t Amount of Ships 5: " + ActiveGameState.getAmountShipSize5() + "\n" +
                    "\t Own AI Difficulty: " + ActiveGameState.getOwnKiDifficulty()
            );
        }

        if(ActiveGameState.newView){
            Platform.runLater(()->{
                try{
                    //Switch scene, depending on ki selection
                    switch (ActiveGameState.getModes()){
                        case playerVsRemote:
                                                //Not loading and player vs remote -> place ships
                                                if (!(ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer) ) {
                                                    logConfigCommunicationThread.log(Level.INFO, "Switching Scene to Place Ships");
                                                    Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
                                                    Main.primaryStage.setScene(new Scene(placeShips));
                                                    Main.primaryStage.show();
                                                    break;
                                                }

                        case kiVsRemote:            logConfigCommunicationThread.log(Level.INFO, "Switching Scene to Game Playground.");
                                                    Parent gamePlayground =  FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                                                    Main.primaryStage.setScene(new Scene(gamePlayground));
                                                    Main.primaryStage.show();
                                                    break;
                    }
                }catch(IOException e){
                    logConfigCommunicationThread.log(Level.SEVERE, "IO Exception at loading Scene");
                }
            });
        }


    }
    //end run
}
