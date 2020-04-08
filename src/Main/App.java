package Main;

import Main.Controller.AdminController;
import Main.Controller.DatabaseController;
import Main.Controller.EmployeeController;
import Main.Controller.LoginController;
import Main.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    Stage window;
    private User loggedUser;
    public DatabaseController dbController = new DatabaseController();

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        dbController.connect();
        window = stage;
        goToLogin();


        // set up :
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        window.setMinHeight(300);
        window.setMinWidth(300);
        window.show();

    }

    private void goToLogin() {
        try {
            LoginController login = (LoginController) changeScene("fxml/login.fxml", "Log In");
            login.setApplication(this);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setSize(int i, int i1) {
        window.setWidth(i);
        window.setHeight(i1);
    }

    public void goToEmployee() {
        try {
            EmployeeController employee = (EmployeeController) changeScene("fxml/employee.fxml", "Employee Window");
            employee.setApplication(this);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void goToAdmin() {
        try {
            AdminController admin = (AdminController) changeScene("fxml/admin_s.fxml", "Admin Window");
            admin.setApp(this);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void logOut() {
        loggedUser = null;
        goToLogin();
    }

    public Initializable changeScene(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = App.class.getResourceAsStream(fxml);
        loader.setLocation(getClass().getResource(fxml));
        Parent root;

        try {
            root = loader.load(in);
        } finally {
            in.close();
        }


        window.setScene(new Scene(root));
        window.setTitle(title);

        return (Initializable) loader.getController();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void closeProgram() {
        try {
            boolean answer = ConfirmBox.display("Closing Application", "Do you want to quit?");
            if (answer) {
                dbController.close();
                window.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void minSize() {
        window.setMinWidth(window.getWidth());
        window.setMinHeight(window.getHeight());
    }
}
