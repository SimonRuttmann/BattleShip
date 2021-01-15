package Player;

import Controller.Handler.LoggingThread;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;
import java.io.IOException;

public class NetworkLogger {
    private static Logger logger = Logger.getLogger("parent");
    Formatter formatter;

    public NetworkLogger() throws IOException{
        LoggingThread loggingThread = new LoggingThread("loggingThread");
        loggingThread.start();
        Logger logger = Logger.getLogger("parent");
        logger.setUseParentHandlers(false);
        FileHandler fileHandler = new FileHandler("src/Logs/log.txt", true);
        ConsoleHandler consoleHandler = new ConsoleHandler();

        fileHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat DateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Date a = new Date(record.getMillis());
                //String logLines = a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record);
                return a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record) + '\n';

            }
        });
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat DateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                Date a = new Date(record.getMillis());
                //String logLines = a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record);
                return a + " [ " + record.getLevel() + " ] " + "[ " + record.getClass() + " ] " + "[ " + record.getSourceMethodName() + " ] " + this.formatMessage(record) + '\n';

            }
        });
        logger.addHandler(fileHandler);
        logger.addHandler(consoleHandler);
        logger.log(Level.INFO,"Es lebt!");

    }

    public static void setLevel(Level input){
        logger.setLevel(input);
    }
    public static void terminateLogging(){
        //Handler[] fileHandler = logger.getHandlers();
        for (java.util.logging.Handler filehandler : logger.getHandlers()){
            filehandler.close();
        }
    }
}


