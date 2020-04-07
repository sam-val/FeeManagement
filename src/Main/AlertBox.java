package Main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String s, String title) {
        Stage win = new Stage();
        win.initModality(Modality.APPLICATION_MODAL);

        Label prompt = new Label(s);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.getStyleClass().add("prompt");

        ImageView image = new ImageView("Main/images/skull.png");
        image.setFitHeight(40);
        image.setFitWidth(40);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(image, prompt);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);



        BorderPane border = new BorderPane();
        border.setCenter(vbox);
        border.getStyleClass().add("prompt-window");

        Scene scene = new Scene(border);
        scene.getStylesheets().add("Main/css/style.css");

        win.setScene(scene);
        win.setHeight(150);
        win.setWidth(250);
        win.setTitle(title);

        win.showAndWait();
    }
}
