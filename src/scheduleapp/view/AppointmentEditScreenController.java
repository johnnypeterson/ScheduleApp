package scheduleapp.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AppointmentEditScreenController {

    @FXML
    private TableView<?> customerTableView;

    @FXML
    private TableColumn<?, ?> CustomerNameColumn;

    @FXML
    private TableColumn<?, ?> CustomerIdColumn;

    @FXML
    private Label appointmentTitle;

    @FXML
    private TextField titleTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<?> typeComboBox;

    @FXML
    private ComboBox<?> startComboBox;

    @FXML
    private ComboBox<?> endComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }

}
