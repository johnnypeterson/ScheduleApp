package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.TypeCount;
import util.DataBase;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private TableView<TypeCount> typesTableView;

    @FXML
    private TableColumn<TypeCount, Integer> countColumn;

    @FXML
    private TableColumn<TypeCount, String> typesColumn;

    @FXML
    private TableView<Appointment> scheduleTableView;

    @FXML
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerColumn;

    @FXML
    private ComboBox<String> consultantComboBox;

    @FXML
    private TableView<Appointment> scheduleTableView1;

    @FXML
    private TableColumn<Appointment, String> startColumn1;

    @FXML
    private TableColumn<Appointment, String> endColumn1;

    @FXML
    private TableColumn<Appointment, String> titleColumn1;

    @FXML
    private TableColumn<Appointment, String> typeColumn1;

    @FXML
    private TableColumn<Appointment, Integer> customerColumn1;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    void handleTitleSearch(ActionEvent event) {
        String searchTerm = searchTextField.getText();
        if (searchTerm != null) {
            setTitleTableView(searchTerm);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an City drop down.");
            alert.showAndWait();
        }


    }

    private void setTitleTableView(String title) {

            ObservableList<Appointment> list = getTitleAppointmentList(title);


        titleColumn1.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        customerColumn1.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        startColumn1.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endColumn1.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        typeColumn1.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        scheduleTableView1.setItems(list);

    }

    private ObservableList<Appointment> getTitleAppointmentList(String title) {

            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
            try {
                PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                        "SELECT * From appointment WHERE title like ?");
                preparedStatement.setString(1, "%" + title + "%");
                System.out.println(preparedStatement);
                ResultSet result = preparedStatement.executeQuery();


                setAppointmentList(appointmentList, result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return appointmentList;
        }

    private void setAppointmentList(ObservableList<Appointment> appointmentList, ResultSet result) throws SQLException {
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
    }



    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);


    @FXML
    void handleComboBox(ActionEvent event) {
        String consultent = consultantComboBox.getSelectionModel().getSelectedItem();
        if (consultent != null) {
            setTableView(consultent);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an Consultant drop down.");
            alert.showAndWait();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTypesTableView();
        consultantComboBox.setItems(getContacts());



    }



    //Query to get the count of the different types of appointments.  Type is stored as the description in the database.
    private ObservableList<TypeCount> getTypeReport() {
        ObservableList<TypeCount> typeObservableList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "Select description, count(description) as count from appointment group by description order by count desc "
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                TypeCount typeCount;
                typeCount = new TypeCount(resultSet.getInt("count"),
                        resultSet.getString("description"));
                typeObservableList.add(typeCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeObservableList;
    }

    public void setTypesTableView() {
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        typesColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typesTableView.setItems(getTypeReport());
    }

    // Quuery to get apt by contact which is the DB field used as consultant in the UI.
    public ObservableList<Appointment> getAppointmentList(String contact) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT * From appointment WHERE contact = ? ");
            preparedStatement.setString(1, contact);
            System.out.println(preparedStatement);
            ResultSet result = preparedStatement.executeQuery();


            setAppointmentList(appointmentList, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

        public ZonedDateTime localTime(Timestamp timestamp) {

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(zoneId);
            return localTime;
        }

    private void setTableView(String contact){
        ObservableList<Appointment> list = getAppointmentList(contact);


        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        scheduleTableView.setItems(list);

    }



    private  ObservableList<String> getContacts() {
        ObservableList<String> contactList = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT contact from appointment group by contact;"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                contactList.add(resultSet.getString("contact"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }


    }
