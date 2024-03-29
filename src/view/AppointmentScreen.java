
package view;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import util.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

/**
 * @author johnnypeterson
 */
public class AppointmentScreen implements Initializable {


    @FXML
    private TableView<Appointment> aptTableView;

    @FXML
    private TableColumn<Appointment, String> startaptColumn;

    @FXML
    private TableColumn<Appointment, String> endaptColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerColumn;

    @FXML
    private TableColumn<Appointment, String> consultantColumn;

    @FXML
    private RadioButton weekToogleButton;

    @FXML
    private RadioButton monthToggleButton;

    private User currentUser;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();
    private ToggleGroup toggleGroup;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Set up toggle group so only one button can be selected at a time.
        toggleGroup = new ToggleGroup();
        this.weekToogleButton.setToggleGroup(toggleGroup);
        this.monthToggleButton.setToggleGroup(toggleGroup);


    }


    @FXML
    void handleCustomer(ActionEvent event) {
        Parent root;
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/Customer.fxml"));
            Parent sceneMain = loader.load();
            CustomerController controller = loader.<CustomerController>getController();
            controller.setCurrentUser(currentUser);
            Scene scene = new Scene(sceneMain);
            stage.setScene(scene);
            stage.setTitle("Customer");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDelete(ActionEvent event) {
        Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Are you sure?");
            //lambda to run method to delete apt once OK is selected.
    
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                deleteAppointment(selectedAppointment);
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
        Appointment selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentEditScreen.fxml"));
                Parent sceneMain = loader.load();
                AppointmentEditScreenController controller = loader.<AppointmentEditScreenController>getController();
                controller.setUser(currentUser);
                Scene scene = new Scene(sceneMain);
                stage.setScene(scene);
                controller.setAppointment(selectedAppointment);
                stage.setTitle("Edit Appointment");
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();


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
        LocalDate localDateNow = LocalDate.now();
        LocalDate localDatePlusMonth = localDateNow.plusMonths(1);
        FilteredList<Appointment> filteredList = new FilteredList<>(getAppointmentList());
        // Use Lambda to filter and return new list while in setPredicate
        filteredList.setPredicate(row -> {
            LocalDate localDateRow = LocalDate.parse(row.getStart(), dateTimeFormatter);
            return localDateRow.isAfter(localDateNow.minusDays(1)) && localDateRow.isBefore(localDatePlusMonth);
        });
        aptTableView.setItems(filteredList);

    }


    @FXML
    void handleWeek(ActionEvent event) {

        LocalDate localDateNow = LocalDate.now();
        LocalDate localDatePlusWeek = localDateNow.plusDays(7);
        FilteredList<Appointment> filteredList = new FilteredList<>(getAppointmentList());
        // Use Lambda to filter and return new list while in setPredicate
        filteredList.setPredicate(row -> {
            LocalDate localDateRow = LocalDate.parse(row.getStart(), dateTimeFormatter);
            return localDateRow.isAfter(localDateNow.minusDays(1)) && localDateRow.isBefore(localDatePlusWeek);
        });
        aptTableView.setItems(filteredList);


    }

    @FXML
    void handleNew(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentEditScreen.fxml"));
            Parent sceneMain = loader.load();
            AppointmentEditScreenController controller = loader.<AppointmentEditScreenController>getController();
            controller.setUser(currentUser);
            Scene scene = new Scene(sceneMain);
            stage.setScene(scene);
            stage.setTitle("New Appointment");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleReports(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/ReportsScreen.fxml"));
            Parent sceneMain = loader.load();
            ReportsController controller = loader.<ReportsController>getController();

            Scene scene = new Scene(sceneMain);
            stage.setScene(scene);
            stage.setTitle("Reports");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String dateFormat(LocalDateTime localDateTime) {

        ZonedDateTime newzdtStart = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zoneId);
        return newLocalStart.format(dateTimeFormatter);
    }

    /**
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        // Refactored code from 195PreparedStatementExample provided by course mentor.  
        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT * From appointment WHERE createdBy = ? ");
            preparedStatement.setString(1, currentUser.getUserName());
            System.out.println(preparedStatement);
            ResultSet result = preparedStatement.executeQuery();


            while (result.next()) {
                Appointment appointment;

                ZonedDateTime startLocal = localTime(result.getTimestamp("start"));
                ZonedDateTime endTimeLocal = localTime(result.getTimestamp("end"));

                appointment = new Appointment(result.getInt("appointmentId"),
                        result.getInt("customerId"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("location"),
                        result.getString("contact"),
                        result.getString("url"),
                        startLocal.format(dateTimeFormatter),
                        endTimeLocal.format(dateTimeFormatter)
                );
                appointmentList.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return appointmentList;
    }

    public void showAppointments() {
        ObservableList<Appointment> list = getAppointmentList();
        customerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        consultantColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        startaptColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endaptColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        aptTableView.setItems(list);


    }

    private void deleteAppointment(Appointment appointment) {
        try {
            String query = "Delete From appointment Where appointmentId=" + appointment.getAppointmentId().toString();
            executeQuery(query);
        } catch (Exception e) {
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
        showAppointments();

    }

    public void setCustomer() {

    }

    public ZonedDateTime localTime(Timestamp timestamp) {

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(zoneId);
        return localTime;
    }


}

