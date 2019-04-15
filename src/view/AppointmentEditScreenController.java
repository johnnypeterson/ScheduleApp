package view;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Customer;
import model.User;
import util.DataBase;

import static util.DataBase.getConnection;

public class AppointmentEditScreenController implements Initializable {

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TextField titleTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;


    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Appointment currentAppointment;
    private Customer customer;
    private final ObservableList<String> start = FXCollections.observableArrayList();
    private final ObservableList<String> end = FXCollections.observableArrayList();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private User currentUser;


    @FXML
    void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure? You're changes will not be saved");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                            showAppointmentScreen(event);
                        }
                );

    }

    @FXML
    void handleSave(ActionEvent event) {
        if  (currentAppointment != null) {
            updateApt();
        } else {
            saveNewApt();
        }
        showAppointmentScreen(event);


    }

    private void showAppointmentScreen(ActionEvent event) {
        try {

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/AppointmentScreen.fxml"));

            Parent sceneMain = loader.load();
            AppointmentScreen controller = loader.<AppointmentScreen>getController();
            controller.setUser(currentUser);

            Scene scene = new Scene(sceneMain);
            stage.setScene(scene);
            stage.setTitle("Johnny Peterson Schedule App");
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showCustomers();
        setTimes();


    }


    public void setAppointment(Appointment currentAppointment) {
        this.currentAppointment = currentAppointment;
        titleTextField.setText(currentAppointment.getTitle());
        descriptionTextField.setText(currentAppointment.getType());
        LocalDateTime startTime = LocalDateTime.parse(currentAppointment.getStart(), dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(currentAppointment.getEnd(), dateTimeFormatter);
        ZonedDateTime end = startTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime start = endTime.atZone(ZoneId.of("UTC"));
        String startString = start.format(dateTimeFormatter);
        String endString = end.format(dateTimeFormatter);
        LocalDateTime endComboTime = LocalDateTime.parse(endString, dateTimeFormatter);
        LocalDateTime startComboTime = LocalDateTime.parse(startString, dateTimeFormatter);
        System.out.println(endComboTime);
        startComboBox.getSelectionModel().select(startComboTime.format(timeFormatter));
        endComboBox.getSelectionModel().select(endComboTime.format(timeFormatter));
        datePicker.setValue(LocalDate.parse(startString, dateTimeFormatter));
    }

    /** This prevents scheduling outside buisness hours
     *  do to the only selectable times being thoses in buisness hours.
     */
    public void setTimes() {
        LocalTime time = LocalTime.of(8, 0);
        do {
            start.add(time.format(timeFormatter));
            end.add(time.format(timeFormatter));
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(17, 15)));
        start.remove(start.size() - 1);
        end.remove(0);
        startComboBox.setItems(start);
        endComboBox.setItems(end);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerObservableListList = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM customer";
        Statement statement;
        Connection connection = getConnection();
        try {
            statement = (Statement) connection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);
            Customer customer = null;
            while (result.next()) {
                customer = new Customer(result.getInt("customerId"),
                        result.getString("customerName"),
                        result.getInt("addressId")
                );
                customerObservableListList.add(customer);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerObservableListList;
    }

    public void showCustomers() {
    ObservableList<Customer> list = getCustomerList();
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        customerTableView.setItems(list);

    }

    public void saveNewApt(){
            LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeFormatter);
            LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeFormatter);
            LocalDate date = datePicker.getValue();

            Timestamp startTimeStamp = timeConvertor(date, startTime);
            Timestamp endTimeStamp = timeConvertor(date, endTime);
                try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "INSERT into appointment (customerId, contact, title, description, start, end, createdBy, createDate, lastUpdate, lastUpdateBy, location, url)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?)"
            );
            preparedStatement.setInt(1, customerTableView.getSelectionModel().getSelectedItem().getCustomerId());
            preparedStatement.setString(2, customerTableView.getSelectionModel().getSelectedItem().getCustomerName());
            preparedStatement.setString(3, titleTextField.getText());
            preparedStatement.setString(4, descriptionTextField.getText());
            preparedStatement.setTimestamp(5, startTimeStamp);
            preparedStatement.setTimestamp(6, endTimeStamp);
            preparedStatement.setString(7, currentUser.getUserName());
            preparedStatement.setString(8, currentUser.getUserName());
            preparedStatement.setString(9, "");
            preparedStatement.setString(10, "");

                    System.out.println(preparedStatement);
            Integer result = preparedStatement.executeUpdate();

        } catch (Exception e) {
                    e.printStackTrace();
                    exceptionAlert(e);
                }
    }
    // Convert times from view to UTC timestamp for Data Base
    public Timestamp timeConvertor(LocalDate date, LocalTime time) {

        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        ZonedDateTime utcTime = localDateTime.atZone(zoneId).withZoneSameInstant(ZoneId.of("UTC"));
        Timestamp timestamp = Timestamp.valueOf(utcTime.toLocalDateTime());

        return timestamp;

    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void updateApt() {
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeFormatter);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeFormatter);
        LocalDate date = datePicker.getValue();

        Timestamp startTimeStamp = timeConvertor(date, startTime);
        Timestamp endTimeStamp = timeConvertor(date, endTime);
        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement("UPDATE appointment SET customerId = ?, title = ?, description = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? WHERE appointmentId = ?");

            preparedStatement.setInt(1, customerTableView.getSelectionModel().getSelectedItem().getCustomerId());
            preparedStatement.setString(2, titleTextField.getText());
            preparedStatement.setString(3, descriptionTextField.getText());
            preparedStatement.setTimestamp(4, startTimeStamp);
            preparedStatement.setTimestamp(5, endTimeStamp);
            preparedStatement.setString(6, currentUser.getUserName());
            preparedStatement.setInt(7, currentAppointment.getAppointmentId());



            System.out.println(preparedStatement);
            Integer result = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionAlert(e);
        }
    }

    private void exceptionAlert(Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Failed To Save");
        alert.setContentText("Unable to save with data provided." + e.toString());
        alert.showAndWait();
    }
}
