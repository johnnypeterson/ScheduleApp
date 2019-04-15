package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TypeCount;
import util.DataBase;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private TableView<TypeCount> typesTableView;

    @FXML
    private TableColumn<TypeCount, Integer> countColumn;

    @FXML
    private TableColumn<TypeCount, String> typesColumn;

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


}
