/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author johnnypeterson
 */
public class DataBase {
    
    private static final String databaseName = "U03Rt2";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109:3306/U03Rt2";
    private static final String username = "U03Rt2";
    private static final String password = "53688060994";
    private static final String driver = "com.mysql.jdbc.Driver";
    
    public static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, username, password);
        System.out.println("Connection Successful");
    }
        public static void closeConnection() throws ClassNotFoundException, SQLException {
             conn.close();
             System.out.println("Connection Closed");
        }
}
