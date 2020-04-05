package Main.Controller;

import Main.SignUpBox;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterAccountController implements Initializable {
    public AdminController adControl;
    private SignUpBox signUpBox;
    public TextField userField;
    public PasswordField passwordField;
    public Button createAccountBtn;
    public Label announcement;
    public TextField employeeField;
    public TextField phoneField;
    public TextField skillsField;
    public TextField emailField;


    public void setAdControl(AdminController adControl) {
        this.adControl = adControl;
    }
    public void changeScene() {

    }

    public void CreateAccount(ActionEvent actionEvent) {
        String user = userField.getText();
        String email = emailField.getText();
        String name = employeeField.getText();
        String skills = skillsField.getText();
        String phone = phoneField.getText();
        int phoneNum;
        char[] password = passwordField.getText().toCharArray();
        if (user.isEmpty() || (passwordField.getText().isEmpty()) || name.isEmpty()) {
            announcement.setText("Fields should not be empty");
            return;
        }
        if (!phone.isBlank()) {
           if (!phone.matches("^\\d+$")) {
                announcement.setText("Phone number is invalid");
                return;
            }
            phoneNum = Integer.parseInt(phone);
        } else  {
            phoneNum = 0;
        }
        if (DatabaseController.userExists(user)) {
            announcement.setText("User exists");
            return;
        }
        DatabaseController.insertEmployee(user, password, name, email, phoneNum, skills);

        if (adControl != null) {
            System.out.println("works");
            adControl.pendingTable.getItems().add(DatabaseController.findEmployee(DatabaseController.of(user).getId()));
        }


        announcement.setText("Created Successfully");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //
        createAccountBtn.setDefaultButton(true);
        //

    }
}
