
package view;


import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DataBase;


import static util.DataBase.getConnection;
import static util.DataBase.makeConnection;

/**
 *
 * @author johnnypeterson
 */
public class AppointmentScreen implements Initializable {




    @FXML
    private TableView<Appointment> aptTableView;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startaptColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endaptColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerColumn;

    @FXML
    private TableColumn<Appointment, String> consultantColumn;
    
    private User currentUser;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();



    @FXML
    void handleCustomer(ActionEvent event) {
         Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Customer.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Johnny Peterson Schedule App");
            stage.setScene(new Scene(root, 800, 550));
            
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDelete(ActionEvent event) {
        Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment != null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Are you sure?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {deleteAppointment(selectedAppointment);
            showAppointments();
        });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an Appointment in the Table to delete");
            alert.showAndWait();

        }
    }

    @FXML
    void handleEdit(ActionEvent event) {
         Parent root;
        Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AppointmentEditScreen.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Appointment");
                stage.setScene(new Scene(root, 800, 550));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an Appointment in the Table to edit");
            alert.showAndWait();
        }

    }

    @FXML
    void handleLogOut(ActionEvent event) {
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

    @FXML
    void handleMonth(ActionEvent event) {

    }

    @FXML
    void handleNew(ActionEvent event) {
         Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AppointmentEditScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add New Appointment");
            stage.setScene(new Scene(root, 800, 550));
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleReports(ActionEvent event) {

    }

    @FXML
    void handleWeek(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showAppointments();
        } catch (Exception ex) {
            Logger.getLogger(AppointmentScreen.class.getName()).log(Level.SEVERE, null, ex);
    
         }
        
    }

    public String dateFormat(Timestamp timestamp) {

        ZonedDateTime newzdtStart = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zoneId);
        return  newLocalStart.format(dateTimeFormatter);
    }
    
    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointment";
        System.out.println(sqlStatement);
        Statement statement;
        Connection connection = getConnection();
        try {
            statement = (Statement) connection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);
            Appointment appointment = null;
            while (result.next()) {

                String startTime = dateFormat(result.getTimestamp("start"));
                String endTime = dateFormat(result.getTimestamp("end"));
                appointment = new Appointment(result.getInt("appointmentId"),
                        result.getInt("customerId"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("location"),
                        result.getString("contact"),
                        result.getString("url"),
                        startTime,
                        endTime
                        );
                appointmentList.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(appointmentList);

        return appointmentList;
    }

    public void showAppointments() {
        ObservableList<Appointment> list = getAppointmentList();
        
        customerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        consultantColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        startaptColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        endaptColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        aptTableView.setItems(list);



    }
    private  void deleteAppointment (Appointment appointment) {
        try {
            String query = "Delete From appointment Where appointmentId="+appointment.getAppointmentId().toString();
            executeQuery(query);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void executeQuery(String query) {
        Connection conn = DataBase.getConnection();
        java.sql.Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;

    }
   

}

