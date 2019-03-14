/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author johnnypeterson
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduleapp.ScheduleApp;
import scheduleapp.model.User;
import util.DataBase;

public class LoginScreenController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button cancelButton;

    @FXML
    private Button loginButton;

    @FXML
    void cancel(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to close the program");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                            Platform.exit();
                            System.exit(0);
                        }
                );

    }
    private ScheduleApp mainApp;
    @FXML
    void login(ActionEvent event) {
//               String username = usernameText.getText();
//        String password = passwordText.getText();
//        if(username.length() > 0 && password.length() > 0) {
//            User activeUser = validateLogin(username, password);
//            System.out.println("This worked" + activeUser.toString());
//        }
    Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AppointmentScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Johnny Peterson Schedule App");
            stage.setScene(new Scene(root, 800, 550));
            
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        

    }
    
    User user = new User();

    public LoginScreenController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLogin();
    }

    public void setLogin() {

        ResourceBundle resourcesBundle = ResourceBundle.getBundle("language/login");

        titleLabel.setText(resourcesBundle.getString("title"));
        usernameLabel.setText(resourcesBundle.getString("username"));
        passwordLabel.setText(resourcesBundle.getString("password"));
        cancelButton.setText(resourcesBundle.getString("cancel"));
        loginButton.setText(resourcesBundle.getString("signin"));
        

    }
    
    User validateLogin(String username, String password) {
        try{
            PreparedStatement pst = DataBase.getConnection().prepareStatement("SELECT * FROM user WHERE userName= AND password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserId(rs.getInt("userId"));
            } else {
                return null;
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }
}
