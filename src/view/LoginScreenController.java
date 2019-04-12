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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
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
import model.Appointment;
import scheduleapp.ScheduleApp;
import model.User;
import util.DataBase;

import static util.DataBase.getConnection;


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
    private Label errorMessage;

    private static Stage primaryStage;
    private ObservableList<Appointment> remindersList;


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
    private void loginLogger(User user) {
        Logger log = Logger.getLogger("log-ins.txt");
        log.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler("log-ins.txt", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            log.addHandler(fileHandler);
        } catch (Exception e) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, e);
        }
        if (user != null) {
            log.log(Level.INFO, "User " + user.getUserName() + " successfully logged in.");
        } else {
            log.log(Level.INFO, "Invalid UserName or Password.");
        }
    }

    private ScheduleApp mainApp;
    @FXML
    void login(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        ResourceBundle resourcesBundle = ResourceBundle.getBundle("language/login");
               String username = usernameText.getText();
        String password = passwordText.getText();
        if(username.length() > 0 && password.length() > 0) {
            User activeUser = validLogin(username, password);
            if (activeUser != null) {
                loginLogger(activeUser);

                if (remindersList != null) {
                    String title = remindersList.get(0).getTitle();
                    String time = remindersList.get(0).getStart().toString();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Upcomming Appointments");
                    alert.setContentText("You have an uppcomming apt " + title + " at " + time);

                    alert.showAndWait();

                }

                try {

                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentScreen.fxml"));

                    Parent sceneMain = loader.load();
                    AppointmentScreen controller = loader.<AppointmentScreen>getController();
                    controller.setUser(activeUser);

                    Scene scene = new Scene(sceneMain);
                    stage.setScene(scene);
                    stage.setTitle("Johnny Peterson Schedule App");
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                loginLogger(null);
                errorMessage.setText(resourcesBundle.getString("incorrect"));
            }

        }

    }
    
    User user = new User();

    public LoginScreenController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLogin(mainApp);


    }

    public void setLogin(ScheduleApp mainApp) {
        this.mainApp = mainApp;

        ResourceBundle resourcesBundle = ResourceBundle.getBundle("language/login");

        titleLabel.setText(resourcesBundle.getString("title"));
        usernameLabel.setText(resourcesBundle.getString("username"));
        passwordLabel.setText(resourcesBundle.getString("password"));
        cancelButton.setText(resourcesBundle.getString("cancel"));
        loginButton.setText(resourcesBundle.getString("signin"));
        

    }
    
    public User validLogin(String username, String password) throws ClassNotFoundException, SQLException {
        String sqlStatement = "SELECT * FROM user WHERE userName=" + "'" + username + "'" + "AND password=" + "'" + password + "'";
        Connection connection = getConnection();
        Statement statement;
        statement = (Statement) connection.createStatement();
        ResultSet result = statement.executeQuery(sqlStatement);
        if(result.next()) {
            user.setUserName(result.getString("userName"));
            user.setPassword(result.getString("password"));
            user.setUserId(result.getInt("userId"));
        } else {
            return null;
        }
        getReminderApt();
        return user;
    }

    private void getReminderApt() {

        LocalDateTime currentTime = LocalDateTime.now(Clock.systemUTC());



        System.out.println(currentTime);

        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.title, appointment.`start`, appointment.`end` FROM appointment, customer WHERE appointment.customerId = customer.customerId AND appointment.createdBy = ? ORDER BY `start`");
            preparedStatement.setString(1, user.getUserName());
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet.toString());


            while(resultSet.next()) {
                Integer appointmentId = resultSet.getInt("appointment.appointmentId");
                Timestamp startTimeStamp = resultSet.getTimestamp("appointment.start");
                Timestamp endTimeStamp = resultSet.getTimestamp("appointment.end");
                LocalDateTime startTime = startTimeStamp.toLocalDateTime();
                LocalDateTime endTime = endTimeStamp.toLocalDateTime();
                String title = resultSet.getString("appointment.title");


                if (startTime.isBefore(currentTime.plusMinutes(15)) && startTime.isAfter(currentTime)) {
                    Appointment appointment = new Appointment(appointmentId, title, startTime, endTime);
                    remindersList.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



}
