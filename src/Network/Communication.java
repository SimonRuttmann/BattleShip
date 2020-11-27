package Network;

import com.sun.javaws.Main;

import java.io.*;

public abstract class Communication implements ICommunication{
    private boolean connected = false;

    //private String sendCMD
    //private final Object sendFlag = new Object();

    //private String getCMD;
    //private final Object getFlag = new Object();




    // Ein- und Ausgabestrom des Sockets ermitteln
    // und als BufferedReader bzw. Writer verpacken.

    @Override
    public void sendCMD(CMD command, String parameter) {
        String sendCMD = command.toString() + " " + parameter;

        if (!connected) return;
        try {
            outputWriter.write(sendCMD);
            outputWriter.flush();
            // flush sorgt dafür, dass der Writer garantiert alle Zeichen
            // in den unterliegenden Ausgabestrom schreibt.
        } catch (IOException e) {
            System.out.println("Error while writing!" + sendCMD);
            e.printStackTrace();
        }
/*            //Gibt CommThread frei -> schreibt Befehl
            sendFlag.notify();
*/
    }

    @Override
    public String[] getCMD() {
        if (!connected) return null;
        try {
            String cmd = inputReader.readLine();

            if(validDataReceived(cmd)){
                return cmd.split(" ");
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read next Command!");
            return null;
        }


    /*
        //Gibt CommThread frei -> Liest Befehl
        // -> Befehl wird von CommThread in getCMD gespeichert, bis dahin darf der MainThread noch nicht die ReturnZeile ausführen -> er wird deshalb erstmal angehalten und vom CommThread wieder erweckt
        //Problematik: ComThread is so schnell, dass er Main bereits erweckt, bevor dieser schläft
        try {
            getFlag.notify();
            wait(); // Dieses Wait kann erst nach dem Notify des CommThreads augeführt werden //UMGEHUNG!?

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.getCMD.split(" ");

   */
    }

    private BufferedReader inputReader;
    private Writer outputWriter;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setInputReader(BufferedReader inputReader) {
        this.inputReader = inputReader;
    }

    public void setOutputWriter(BufferedWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

/*
  / //Thread CommThread starten
    @Override
    public void startListening() {
        Thread CommThread = new Thread(new Runnable() {


            @Override
            public void run(){


                while ( connected ){
                //Nachricht lesen
                    //Nachricht checken -> break -> conneted = false
                //einen Befehl schicken -> erst dann wenn wir die Methode send Message -> attribute auf true -> befehl schicken

                        // Solange warten bis sendFlag.notify (von sendCMD) augerufen wird -> wait wird beendet -> CMD wird geschrieben
                        if (ourTurn) {
                            try {
                                sendFlag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                outputWriter.write(sendCMD);
                                outputWriter.flush();
                                // flush sorgt dafür, dass der Writer garantiert alle Zeichen
                                // in den unterliegenden Ausgabestrom schreibt.
                            } catch (IOException e) {
                                System.out.println("Error while writing!" + sendCMD);
                                e.printStackTrace();
                            }
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ourTurn = false;
                        }

                        // Solange warten, bis getFlag.notify (von getCMD) augerufen wird -> wait wird beendet -> CMD wird gelesen

                        try {
                            getFlag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //Nachricht lesen
                        try {
                            String temp = inputReader.readLine();
                            if (validDataReceived(temp)){
                                getCMD = temp;
                            }
                            connected = false;

                            notifyAll(); // weckt Mainthread auf

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Error at reading !");
                        }

                        ourTurn = true;
                }


            }
        });
        CommThread.start();
    }
*/


    private boolean validDataReceived(String receivedDate){
        try{
            return this.validDataChecker(receivedDate);
        }catch(NumberFormatException e){
            return false;
        }
    }

    // Ships 5 5 5 5
    private boolean validDataChecker(String receivedData) throws NumberFormatException{
        String[] splitData = receivedData.split(" ");
        //Keyword klein Schreiben und dann prüfen, die befehle sind nicht Groß/Kleinschreibsensitiv
        String keyword = splitData[0].toLowerCase();

        switch(keyword){
            case "size":
                if (splitData.length == 2){
                    int playgroundsize = Integer.parseInt(splitData[1]);
                    return 5 <= playgroundsize && playgroundsize <= 30;
                }
                return false;

            case "ships":   //ships 5 5 5 3 3 3 -> 3 5er Schiffe und 3 3er Schiffe
                boolean isValid = false;

                if (splitData.length >= 2){
                    for (int i = 1; i < splitData.length; i++){
                        int shipsize = Integer.parseInt(splitData[i]);
                        if ( 2 <= shipsize && shipsize <= 5 ){
                            isValid = true;
                        }
                        else{
                            return false;
                        }
                    }
                }
                return isValid;

            case "shot": //shop 4 2
                if ( splitData.length == 3){
                    int row = Integer.parseInt(splitData[1]);
                    int column = Integer.parseInt(splitData[2]);
                    if (!(5<=row && row <= 30)) return false;
                    if (!(5<=column && column <= 30)) return false;
                    return true;
                }
                return false;

            case "answer": //answer [0,1,2]
                if ( splitData.length == 2){
                    int arg = Integer.parseInt(splitData[1]);
                    return arg == 0 || arg == 1 || arg == 2;
                }
                return false;

            case "save": //save 34234
            case "load": //load 23432
                if (splitData.length == 2){
                    long id = Long.parseLong(splitData[1]);
                    return true;
                }
                return false;

            case "next":
            case "done":
            case "ready":
                return splitData.length == 1;


            //Optionale Kommandos
            case "timeout":
                if (splitData.length == 2){
                    long time = Long.parseLong(splitData[1]);
                    //min 10 Sekunden muss Zeit gegeben werden
                    return time > 10000;
                }
                return false;

            case "name":
            case "repeat":
            case "firstShot":
                return splitData.length == 2;
            default: return false;
        }

    }

    public void closeReaderWriter(){

        try {
            this.inputReader.close();
            this.outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Reader and Writer cant be closed!");
        }
    }

}
