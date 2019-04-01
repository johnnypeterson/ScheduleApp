package util;

import sun.rmi.runtime.Log;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoginLogger {
    private final static Logger loginLogger = Logger.getLogger(LoginLogger.class.getName());
    private static FileHandler fileHandler = null;

    public static void init() {
        try {
            fileHandler = new FileHandler("log-ins.txt",1024 * 1024, 10,true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
    }
}
