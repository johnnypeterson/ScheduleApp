package util;

import sun.rmi.runtime.Log;

import java.io.*;
import java.sql.Timestamp;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoginLogger {
    private final static Logger loginLogger = Logger.getLogger("log-ins.txt");
    private static FileHandler fileHandler = null;
    private static  PrintWriter printWriter;


    public static void init() {

        try {
            fileHandler = new FileHandler("log-ins.txt",true);
//            printWriter = new PrintWriter(new FileOutputStream(new File("log-ins.txt")), true);
//            Logger logger = Logger.getLogger("");
            fileHandler.setFormatter(new SimpleFormatter());
            loginLogger.addHandler(fileHandler);
            loginLogger.setLevel(Level.INFO);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        printWriter.append("sucessfull login");
//        printWriter.close();


    }
}
