package Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NewClientBox {


    public static void display() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NewClientBox.class.getResource("fxml/clientAddition.fxml"));

        Parent root = loader.load();


        window.setTitle("Manage Client...");

        window.setScene(new Scene(root));
        window.showAndWait();

    }
}
