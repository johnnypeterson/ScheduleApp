/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author johnnypeterson
 */
public class DataBase {
    
      private static Connection connDB;
    
    public DataBase(){}
    
  
    public static void init(){
        System.out.println("Connecting to the database");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            connDB = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U03Rt2", "U03Rt2", "53688060994");;
        }catch (ClassNotFoundException ce){
            System.out.println("Cannot find the right class.  Did you remember to add the mysql library to your Run Configuration?");
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
}
    }
    
    //Returns Connection
    public static Connection getConnection(){
    
        return connDB;
    }
    
    //Closes connections
    public static void closeConnection(){
        try{
            connDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
    
}
