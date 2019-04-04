package util;

import sun.rmi.runtime.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoginLogger {
    private final static Logger loginLogger = Logger.getLogger(LoginLogger.class.getName());
    private static FileHandler fileHandler = null;

    public static void init() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            fileHandler = new FileHandler("log-ins.txt",true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger("");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);

    }
}
