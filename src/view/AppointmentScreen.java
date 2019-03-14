
package view;


import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduleapp.model.User;

/**
 *
 * @author johnnypeterson
 */
public class AppointmentScreen implements Initializable {




    @FXML
    private TableView<?> aptTableView;

    @FXML
    private TableColumn<?, ?> startaptColumn;

    @FXML
    private TableColumn<?, ?> endaptColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> customerColumn;

    @FXML
    private TableColumn<?, ?> consultantColumn;

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
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleEdit(ActionEvent event) {
         Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("view/AppointmentEditScreen.fxml"));
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

    }

    @FXML
    void handleReports(ActionEvent event) {

    }

    @FXML
    void handleWeek(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
   

}

