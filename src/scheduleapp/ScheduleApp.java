/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;


import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import util.DataBase;

import view.AppointmentScreen;

/**
 *
 * @author johnnypeterson
 */
public class ScheduleApp extends Application {
    
    
   
    static Stage primaryStage;

    private static AnchorPane mainLayout;

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
    public void showAppointmentScreen (User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleApp.class.getResource("view/AppointmentScreen.fxml"));
    mainLayout = loader.load();

    AppointmentScreen controller = loader.getController();
        controller.setUser(user);

    Scene scene = new Scene(mainLayout, 800, 550 );
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
}

    
    
}
