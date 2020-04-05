package Main.Controller;

import Main.App;
import Main.SignUpBox;
import Main.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController extends GridPane implements Initializable {
    public GridPane grid;
    public Label announcement;
    public Button logInButton;
    public TextField userNameField;
    public PasswordField passwordField;
    private App application;
    private boolean logInProcess;
    public Label nameLabel;
    public Label pwLabel;

    public void setApplication(App application) {
        this.application = application;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //...
        logInButton.setDefaultButton(true);
        logInProcess = false;

        nameLabel.setLabelFor(userNameField);
        pwLabel.setLabelFor(passwordField);


    }

    public void logIn(ActionEvent actionEvent) throws IOException {
        String user = userNameField.getText();
        char[] password = passwordField.getText().toCharArray();
        if (user.isEmpty() || (passwordField.getText().isEmpty())) {
            announcement.setText("Fields should not be empty");
            return;
        }
        if (DatabaseController.validate(user, password)) {
            if (this.application != null) {
                User loggedUser = application.dbController.of(user);
                if ((loggedUser.getType() != 0) && (loggedUser.getApproved() != 1)) {
                    announcement.setText("Account isn't yet approved");
                    return;
                }
                waitForLogIn();
                loggingIn(loggedUser);
            } else {
                System.out.println("application is null");
            }
        } else {
            announcement.setText("Wrong username or password");
        }
    }

    public void ShowSignUp() throws IOException {
       SignUpBox.display();
    }


    public void waitForLogIn() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            StringBuilder text = new StringBuilder("Logging You In");
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        grid.setAlignment(Pos.BASELINE_CENTER);
                        announcement.setPadding(new Insets(0,0,0,30));
                        announcement.setText(text.toString());
                        text.append(".");
                        if (logInProcess == true) {
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 800);
    }

    private void loggingIn(User loggedUser) {
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        application.setLoggedUser(loggedUser);
                        if (loggedUser.getType() == 1) {
                                application.goToEmployee();
                        } else if (loggedUser.getType() == 0) {
                            application.goToAdmin();
                        }
                        logInProcess = true;
                        timer2.cancel();
                    }
                });
            }
        }, 2000);
    }


    public void handleMouseClicks(MouseEvent event) {
        if (event.getSource() == nameLabel) {

        }
        if (event.getSource() == pwLabel) {

        }
    }

}
