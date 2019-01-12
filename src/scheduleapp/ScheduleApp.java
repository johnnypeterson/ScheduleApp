/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scheduleapp.view.LoginController;

/**
 *
 * @author johnnypeterson
 */
public class ScheduleApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
    ;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocacation(ScheduleApp.class.getResource("scheduleapp.view.LoginController"));
            AnchorPane loginScreen = (AnchorPane) loader.load();
            
            LoginController controller = loader.getController();
            controller.setLogin(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
