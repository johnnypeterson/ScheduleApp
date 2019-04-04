package view;


import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.User;

public class AppointmentEditScreenController implements Initializable {

    @FXML
    private TableView<?> customerTableView;

    @FXML
    private TableColumn<?, ?> CustomerNameColumn;

    @FXML
    private TableColumn<?, ?> CustomerIdColumn;

    @FXML
    private TextField titleTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField typeTextField;

    @FXML
    private ComboBox<String> startComboBox;

    @FXML
    private ComboBox<String> endComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Appointment currentAppointment;
    private final ObservableList<String> start = FXCollections.observableArrayList();
    private final ObservableList<String> end = FXCollections.observableArrayList();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();



    @FXML
    void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure? You're changes will not be saved");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent((ButtonType response) -> {
                            Platform.exit();
                            System.exit(0);
                        }
                );

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTimes();
    }




    public void setAppointment(Appointment currentAppointment) {
        this.currentAppointment = currentAppointment;

    }

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



}
