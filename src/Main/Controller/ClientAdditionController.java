package Main.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientAdditionController implements Initializable {

    @FXML
    public Label announcement;

    @FXML
    public Button deleteBtn;

    @FXML
    public Button addBtn;

    @FXML
    public GridPane root;

    @FXML
    public TextField clientNameField;

    @FXML
    public TextField idField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addClient(ActionEvent event) {

        String name = clientNameField.getText().trim();
        if (name.isBlank()) {
            announcement.setText("name is enpty");
            return;
        }
        boolean result = DatabaseController.addClient(name);
        if (result) {
            announcement.setText("add successfully!");
            AdminController.allClients = FXCollections.observableArrayList(DatabaseController.getAllClients());
        } else {
            announcement.setText("addition failed");
        }
    }

    public void deleteClient(ActionEvent event) {
        String id = idField.getText().trim();
        if (id.isBlank()) {
            announcement.setText("id is enpty");
            return;
        }
        if (!id.matches("^\\d+$")) {
            announcement.setText("Id must be a integer");
            return;
        }
        boolean result = DatabaseController.deleteClient(id);
        if (result) {
            announcement.setText("delete successfully!");
            AdminController.allClients = FXCollections.observableArrayList(DatabaseController.getAllClients());
        } else {
            announcement.setText("deletion failed!");
        }
    }
}
