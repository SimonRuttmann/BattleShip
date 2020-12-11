package Controller.Handler;
import Network.CMD;
import Player.ActiveGameState;

//TODO Commentary
//TODO Create informative Exceptions for every ControlThread
/**
 * is called by GameConfig StartButton Action Event
 * vorerst in ChooseSelforKi, ist die einzige Szene bei der ich beide auf einmal aurufen kann
 */
public class MultiplayerControlThreadConfigCommunication extends Thread{
    @Override
    public void run(){
        System.out.println("Multiplayer Control Thread Config Communication");
        //Server
        //size 5
        // get next
        //ships 5 5 5
        // get done
        if (ActiveGameState.isAmIServer()){
            ActiveGameState.setYourTurn(true);

            ActiveGameState.getServer().sendCMD(CMD.size, Integer.toString(ActiveGameState.getPlaygroundSize()));
            String[] receivedCMD = ActiveGameState.getServer().getCMD();
            boolean valid = false;
            switch  (receivedCMD[0]){
                case "next":
                    valid = true;
                    break;
                case "timeout":
                    ActiveGameState.getServer().closeConnection();
                    ActiveGameState.setRunning(false);
                    break;
                default:
                    System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
            }

            if(!valid) return;

                StringBuilder ships = new StringBuilder();
                for ( int i = 0; i < ActiveGameState.getAmountShipSize2(); i++){
                    ships.append(" ");
                    ships.append("2");
                }
                for ( int i = 0; i < ActiveGameState.getAmountShipSize3(); i++){
                    ships.append(" ");
                    ships.append("3");
                }
                for ( int i = 0; i < ActiveGameState.getAmountShipSize4(); i++){
                    ships.append(" ");
                    ships.append("4");
                }
                for ( int i = 0; i < ActiveGameState.getAmountShipSize5(); i++){
                    ships.append(" ");
                    ships.append("5");
                }


                ActiveGameState.getServer().sendCMD(CMD.ships, ships.toString());



            receivedCMD =ActiveGameState.getServer().getCMD();
            valid = false;
            switch  (receivedCMD[0]){
                case "done":
                    valid = true;
                    break;
                case "timeout":
                    ActiveGameState.getServer().closeConnection();
                    ActiveGameState.setRunning(false);
                    break;
                default:
                    System.out.println("Unexpected Message from Client: " + receivedCMD[0]);
            }

            if (!valid) return;
            System.out.println("Game Configurations successfully transmitted to Client.");





        }

        //Client
        //get size
        //send next
        //get ships
        //send done

        else{
            ActiveGameState.setYourTurn(false);
            String[] receivedCMD = ActiveGameState.getClient().getCMD();

            boolean valid = false;
            switch  (receivedCMD[0]){
                case "size":
                    ActiveGameState.setPlaygroundSize(Integer.parseInt(receivedCMD[1]));
                    valid = true;
                    break;
                case "timeout":
                    ActiveGameState.getClient().closeConnection();
                    ActiveGameState.setRunning(false);
                    break;
                default:
                    System.out.println("Unexpected Message from Server: " + receivedCMD[0]);
            }

            if(!valid) return;
            ActiveGameState.getClient().sendCMD(CMD.next, "");
            receivedCMD = ActiveGameState.getClient().getCMD();

            valid = false;
            switch (receivedCMD[0]){
                case "ships":
                    int getShip;
                    int size2 = 0;
                    int size3 = 0;
                    int size4 = 0;
                    int size5 = 0;
                    for (int i = 1; i < receivedCMD.length; i++){
                        getShip = Integer.parseInt(receivedCMD[i]);
                        switch (getShip){
                            case 2: size2++;
                                    break;
                            case 3: size3++;
                                    break;
                            case 4: size4++;
                                    break;
                            case 5: size5++;
                                    break;
                            default:
                                System.out.println("Unexpected Message from Server: " + receivedCMD[i]);
                        }
                    }

                    ActiveGameState.setAmountShipSize2(size2);
                    ActiveGameState.setAmountShipSize3(size3);
                    ActiveGameState.setAmountShipSize4(size4);
                    ActiveGameState.setAmountShipSize5(size5);
                    ActiveGameState.setAmountOfShips( (size2 + size3 + size4 + size5) );
                    valid = true;
                    break;
                case "timeout":
                    ActiveGameState.getClient().closeConnection();
                    ActiveGameState.setRunning(false);
                    break;
                default:
                    System.out.println("Unexpected Message from Server: " + receivedCMD[0]);
            }
            if(!valid) return;

            ActiveGameState.getClient().sendCMD(CMD.done, "");
            System.out.println("Game Configurations from Server successfully transmitted.");

        }
    }
}
