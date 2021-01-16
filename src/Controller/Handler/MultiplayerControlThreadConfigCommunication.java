package Controller.Handler;
import Gui_View.HelpMethods;
import Gui_View.Main;
import Network.CMD;
import Player.ActiveGameState;
import Player.SaveAndLoad;
import Player.Savegame;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;



//TODO S load .....
//TODO C done

/**
 * This thread will:
 *
 *  When we are Server:
 *      1. Send the command "size" with parameters
 *      2. Get the command "next"
 *      3. Send the command "ships" with parameters
 *      4. Get the command "done"
 *
 *      5. Send the command ready
 *      6. Get the command ready
 * When we are Client:
 *      1. Get the command "size" and parameters
 *      2. Send the command "next"
 *      3. Get the command "ships" and parameters
 *      4. Send the command "done"
 *
 *      5. Get the command ready
 *      6. Send the command ready
 *
 * In both cases:
 * The scene is set depending on the mode selected
 * player   -> place ships scene
 * ki       -> playground scene
 *
 * Send/received parameters are either received or stored form the ActiveGameState.java
 * load  s
 * done  c
 * ready s
 * ready c
 */
public class MultiplayerControlThreadConfigCommunication extends Thread{
    @Override
    public void run(){
        System.out.println("Multiplayer Control Thread Config Communication");
        System.out.println("An der Reihe: " + ActiveGameState.isYourTurn());
        //Server

        if (ActiveGameState.isAmIServer()){


            if ( ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer) ActiveGameState.setLoadWithNext(!ActiveGameState.isYourTurn());

            ActiveGameState.setYourTurn(true);
            String[] receivedCMD;
            if (!(ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer)) {
                ActiveGameState.getServer().sendCMD(CMD.size, Integer.toString(ActiveGameState.getPlaygroundSize()));
                receivedCMD = ActiveGameState.getServer().getCMD();
                switch (receivedCMD[0]) {
                    case "next":        break;
                    case "timeout":     HelpMethods.connectionLost();
                                        ActiveGameState.getServer().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;

                    default:            HelpMethods.unexceptedMessage();
                                        ActiveGameState.getServer().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
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

                ActiveGameState.getServer().sendCMD(CMD.load, String.valueOf(ActiveGameState.getLoadId()));
               /* receivedCMD = ActiveGameState.getServer().getCMD();
                switch (receivedCMD[0]) {
                    case "done":
                        break;
                    case "timeout":
                        ActiveGameState.getServer().closeConnection();
                        ActiveGameState.setRunning(false);
                        return;
                    default:
                        System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
                        return;
                }*/

            }
            //Get done
            receivedCMD = ActiveGameState.getServer().getCMD();

            switch  (receivedCMD[0]){
                case "done": break;
                case "timeout":     HelpMethods.connectionLost();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;

                default:            HelpMethods.unexceptedMessage();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
                                    return;
            }



            //send ready get ready
            ActiveGameState.getServer().sendCMD(CMD.ready, "");
            receivedCMD = ActiveGameState.getServer().getCMD();


            switch (receivedCMD[0]){
                case "ready": break;
                case "timeout":     HelpMethods.connectionLost();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    return;
                default:            HelpMethods.unexceptedMessage();
                                    ActiveGameState.getServer().closeConnection();
                                    ActiveGameState.setRunning(false);
                                    System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
                                    return;
            }


            System.out.println("Game Configurations successfully transmitted to Client.");

            //Send next, as we loaded a game where the enemy turn is active
            if (ActiveGameState.isLoadWithNext()){
                ActiveGameState.getServer().sendCMD(CMD.next,"");
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

                case "load":    Savegame savegame = SaveAndLoad.load(Long.parseLong(receivedCMD[1]));
                                if ( savegame == null) {
                                    HelpMethods.noGameFile();
                                    return;
                                }
                                load = true;
                                ActiveGameState.setLoading(ActiveGameState.Loading.multiplayer);
                                break;

                case "timeout": HelpMethods.connectionLost();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;
                default:
                                HelpMethods.unexceptedMessage();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                System.out.println("Unexpected Message from Server: " + receivedCMD[0]);
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
                                default:
                                    System.out.println("Unexpected Message from Server: " + receivedCMD[i]);
                            }
                        }

                        ActiveGameState.setAmountShipSize2(size2);
                        ActiveGameState.setAmountShipSize3(size3);
                        ActiveGameState.setAmountShipSize4(size4);
                        ActiveGameState.setAmountShipSize5(size5);
                        ActiveGameState.setAmountOfShips((size2 + size3 + size4 + size5));
                        break;
                    case "timeout":     HelpMethods.connectionLost();
                                        ActiveGameState.getClient().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        return;
                    default:
                                        HelpMethods.unexceptedMessage();
                                        ActiveGameState.getClient().closeConnection();
                                        ActiveGameState.setRunning(false);
                                        System.out.println("Unexpected Message from Server: " + receivedCMD[0]);
                                        return;
                }
            }

            ActiveGameState.getClient().sendCMD(CMD.done, "");



            //get ready, send ready
            receivedCMD = ActiveGameState.getClient().getCMD();


            switch (receivedCMD[0]){
                case "ready": break;
                case "timeout": HelpMethods.connectionLost();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                return;

                default:        HelpMethods.unexceptedMessage();
                                ActiveGameState.getClient().closeConnection();
                                ActiveGameState.setRunning(false);
                                System.out.println("Unexpected Message from Server: " + receivedCMD[0]);
                                return;
            }

            ActiveGameState.getClient().sendCMD(CMD.ready, "");

            System.out.println("Game Configurations from Server successfully transmitted.");

        }

        if(ActiveGameState.newView){
            Platform.runLater(()->{
                try{
                    //Switch scene, depending on ki selection
                    switch (ActiveGameState.getModes()){
                        case playerVsRemote:
                                                //Wenn nicht laden und Spieler vs Remote-> place Ships..., wenn laden oder ki vs remote -> direkt gamePlayground
                                                if (!(ActiveGameState.getLoading() == ActiveGameState.Loading.multiplayer) ) {
                                                    Parent placeShips = FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/placeShips.fxml"));
                                                    Main.primaryStage.setScene(new Scene(placeShips));
                                                    Main.primaryStage.show();
                                                    break;
                                                }

                        case kiVsRemote:            Parent gamePlayground =  FXMLLoader.load(getClass().getResource("/Gui_View/fxmlFiles/gamePlayground.fxml"));
                                                    Main.primaryStage.setScene(new Scene(gamePlayground));
                                                    Main.primaryStage.show();
                                                    break;
                    }
                }catch(IOException e){
                    System.out.println("Couldn't load the Scene");
                }
            });
        }


    }
    //end run
}
