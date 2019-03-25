/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.DataBase;

import view.LoginScreenController;

/**
 *
 * @author johnnypeterson
 */
public class ScheduleApp extends Application {
    
    
   
    static Stage primaryStage;
    Locale locale = Locale.getDefault();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginScreen();
    
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DataBase.makeConnection();
                    launch(args);
        DataBase.closeConnection();
        }  catch (Exception ex) {
            Logger.getLogger(ScheduleApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public void showLoginScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/LoginScreen.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
