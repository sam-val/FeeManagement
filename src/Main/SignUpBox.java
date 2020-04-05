package Main;

import Main.Controller.AdminController;
import Main.Controller.RegisterAccountController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpBox {
    private AdminController adminController;

    public static void display() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Account Registering");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SignUpBox.class.getResource("fxml/RegisterAccount.fxml"));
        RegisterAccountController reControl = loader.getController();

        Parent root = loader.load();
        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();
    }

    public void displayForAdmin() throws IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Account Registering");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SignUpBox.class.getResource("fxml/RegisterAccount.fxml"));
        Parent root = loader.load();
        RegisterAccountController reControl = (RegisterAccountController) loader.getController();


        if (adminController != null) {

            reControl.setAdControl(adminController);
        }

        Scene scene = new Scene(root);

        window.setScene(scene);
        window.showAndWait();
    }



    public void setAdControl(AdminController adminController) {
        this.adminController = adminController;

    }
}
