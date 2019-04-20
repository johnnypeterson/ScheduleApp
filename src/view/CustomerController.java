package view;


import com.mysql.jdbc.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import util.DataBase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
        setCustomerView();
        setCityTableView();

    }

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, String> nameTableRow;

    @FXML
    private TableColumn<Customer, Integer> idTableRow;

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
    private TableView<City> cityTableView;

    @FXML
    private TableColumn<City, Integer> idColum;

    @FXML
    private TableColumn<City, String> cityColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    public User currentUser;

    @FXML
    void handleEdit(ActionEvent event) {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            setCustomerEditView(customer);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an Customer in the Table to edit");
            alert.showAndWait();
        }

    }

    @FXML
    void handleDelete(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Are you sure?");
            alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
                deleteCustomer(selectedCustomer);
                setCustomerView();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an Customer in the Table to delete");
            alert.showAndWait();

        }

    }

    @FXML
    void handleSave(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            saveCustomer();
            setCustomerView();
        } else {
            updateCustomer(selectedCustomer);
            setCustomerView();

        }
    }
    
  

    private void updateCustomer(Customer customer) {
        City selectedCity = cityTableView.getSelectionModel().getSelectedItem();

        if (selectedCity != null) {
            try {
                PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                        "UPDATE address, customer, city, country SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ?WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId"
                );
                preparedStatement.setString(1, lineOneTextField.getText());
                preparedStatement.setString(2, lineTwoTextField.getText());
                preparedStatement.setInt(3, selectedCity.getCityId());
                preparedStatement.setString(4, zipTextField.getText());
                preparedStatement.setString(5, phoneTextField.getText());
                preparedStatement.setString(6, currentUser.getUserName());
                preparedStatement.setInt(7, customer.getCustomerId());

                Integer result = preparedStatement.executeUpdate();

                PreparedStatement preparedStatement2 = DataBase.getConnection().prepareStatement(
                        "UPDATE customer, address, city SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId"
                );
                preparedStatement2.setString(1, nameTextField.getText());
                preparedStatement2.setString(2, currentUser.getUserName());
                preparedStatement2.setInt(3, customer.getCustomerId());


                Integer result2 = preparedStatement2.executeUpdate();


                saveFailedAlert(result2);

            } catch (Exception e) {
                exceptionAlert(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an City in the City Table");
            alert.showAndWait();
        }
    }


    private void clearTextFields() {
        nameTextField.clear();
        lineOneTextField.clear();
        lineTwoTextField.clear();
        zipTextField.clear();
        phoneTextField.clear();
    }


    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                    "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.postalCode, address.addressId, city.cityId, city.city, country.country, address.phone FROM customer, address, city, country WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId ORDER BY customer.customerName"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Customer customer;
                Address address;
                City city;
                Country country;
                city = new City(resultSet.getInt("city.cityId"),
                        resultSet.getString("city.city"));
                country = new Country(resultSet.getString("country.country"));
                address = new Address(resultSet.getInt("address.addressId"),
                        resultSet.getString("address.address"),
                        resultSet.getString("address.address2"),
                        resultSet.getInt("city.cityId"),
                        resultSet.getString("address.postalCode"),
                        resultSet.getString("address.phone"));

                customer = new Customer(
                        resultSet.getInt("customer.customerId"),
                        resultSet.getString("customer.customerName"),
                        resultSet.getInt("address.addressId"),
                        address,
                        city,
                        country);
                customerObservableList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerObservableList;
    }

    private void setCustomerView() {
        nameTableRow.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idTableRow.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableView.getItems().setAll(getCustomerList());

    }

    private void setCityTableView() {
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        idColum.setCellValueFactory(new PropertyValueFactory<>("cityId"));
        cityTableView.getItems().setAll(getCityList());
    }

    private void setCustomerEditView(Customer customer) {
        nameTextField.setText(customer.getCustomerName());
        lineOneTextField.setText(customer.getAddress().getAddress());
        lineTwoTextField.setText(customer.getAddress().getAddress2());
        zipTextField.setText(customer.getAddress().getPostalCode());
        phoneTextField.setText(customer.getAddress().getPhone());

    }

    private void saveCustomer() {

        City selectedCity = cityTableView.getSelectionModel().getSelectedItem();

        if (selectedCity != null) {
            try {
                PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement(
                        "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, lineOneTextField.getText());
                preparedStatement.setString(2, lineTwoTextField.getText());
                preparedStatement.setInt(3, selectedCity.getCityId());
                preparedStatement.setString(4, zipTextField.getText());
                preparedStatement.setString(5, phoneTextField.getText());
                preparedStatement.setString(6, currentUser.getUserName());
                preparedStatement.setString(7, currentUser.getUserName());
                Integer addressId = null;
                Boolean resultBool = preparedStatement.execute();
                ResultSet resultKeys = preparedStatement.getGeneratedKeys();

                if (resultKeys.next()) {
                    addressId = resultKeys.getInt(1);
                }

                PreparedStatement preparedStatement2 = DataBase.getConnection().prepareStatement(
                        "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)"
                );
                preparedStatement2.setString(1, nameTextField.getText());
                preparedStatement2.setInt(2, addressId);
                preparedStatement2.setInt(3, 1);
                preparedStatement2.setString(4, currentUser.getUserName());
                preparedStatement2.setString(5, currentUser.getUserName());

                Integer result = preparedStatement2.executeUpdate();

                saveFailedAlert(result);

            } catch (Exception e) {
                exceptionAlert(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nothing Selected");
            alert.setContentText("Please select an City in the City Table");
            alert.showAndWait();
        }
    }
        //if try throws and exception I call this to let the user know the customer data is not valid.
    private void exceptionAlert(Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Failed To Save");
        alert.setContentText("Unable to save with data provided." + e.toString());
        alert.showAndWait();
    }

    private void saveFailedAlert(Integer result) {
        if (result == 1) {
            clearTextFields();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Failed To Save");
            alert.setContentText("Unable to save with data provided.");
            alert.showAndWait();
        }
    }

    private ObservableList<City> getCityList() {
        ObservableList<City> cityList = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = DataBase.getConnection().prepareStatement("SELECT * from city");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cityList.add(new City(resultSet.getInt("cityId"), resultSet.getString("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;

    }

    private void deleteCustomer(Customer customer) {
        try {
            String query = "Delete From customer Where customerId=" + customer.getCustomerId();
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


}
