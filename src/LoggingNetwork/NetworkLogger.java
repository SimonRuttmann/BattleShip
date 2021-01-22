package LoggingNetwork;

import GameData.ActiveGameState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import java.io.IOException;

public class NetworkLogger {
    private static Logger logger = Logger.getLogger("parent");
    Formatter formatter;

    /**
     * NetworkLogger Constructor, needs to be called once at main
     * Starts the loggingThread and sets up formatter
     */
    public NetworkLogger() throws IOException{
        LoggingThread loggingThread = new LoggingThread("loggingThread");
        ActiveGameState.setLoggingThread(loggingThread);
        loggingThread.start();

        Logger logger = Logger.getLogger("parent");
        logger.setUseParentHandlers(false);
        FileHandler fileHandler = new FileHandler("Logs/log.txt", false);
        ConsoleHandler consoleHandler = new ConsoleHandler();

        fileHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat DateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Date a = new Date(record.getMillis());
                //String logLines = a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record);
                return a + " [ " + record.getLevel() + " ] " + "[ " + record.getSourceClassName() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record) + '\n';

            }
        });
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat DateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Date a = new Date(record.getMillis());
                //String logLines = a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record);
                return a + " [ " + record.getLevel() + " ] " + "[ " + record.getSourceClassName() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record) + '\n';

            }
        });
        logger.addHandler(fileHandler);
        logger.addHandler(consoleHandler);
        logger.log(Level.INFO,"Logging started");

    }

    public static void setLevel(Level input){
        logger.setLevel(input);
        for (java.util.logging.Handler filehandler : logger.getHandlers()){
            filehandler.setLevel(input);
        }
    }

    /**
     * Closes all FileHandlers and closes the terminal reader
     */
    public static void terminateLogging(){

        for (java.util.logging.Handler filehandler : logger.getHandlers()){
            filehandler.close();
        }

        try {
            ActiveGameState.getLoggingReader().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


