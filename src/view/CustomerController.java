package view;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author johnny.peterson
 */
public class CustomerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> nameTableRow;

    @FXML
    private TableColumn<?, ?> idTableRow;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField lineOneTextField;

    @FXML
    private TextField lineTwoTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField zipTextField;

    @FXML
    private ComboBox<?> stateComboBox;

    @FXML
    private TextField cityTextField;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    void handleAdd(ActionEvent event) {

    }

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleSave(ActionEvent event) {

    }



}
