package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.TypeCount;
import util.DataBase;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private TableView<?> scheduleTableView;

    @FXML
    private TableColumn<?, ?> startColumn;

    @FXML
    private TableColumn<?, ?> endColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> CustomerColumn;

    @FXML
    private ComboBox<?> consultantComboBox;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();

    @FXML
    void handleComboBox(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTypesTableView();


    }




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

    public ObservableList<Appointment> getAppointmentList(String contact) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT * From appointment WHERE contact = ? ");
            preparedStatement.setString(1, contact);

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
    }

        public ZonedDateTime localTime(Timestamp timestamp) {

            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime localTime = zonedDateTime.withZoneSameInstant(zoneId);
            return localTime;
        }

    private void setTableView(String user){

    }

    private  ObservableList<String> getContacts(){
        
    }



    }
