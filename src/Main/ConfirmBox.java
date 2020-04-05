package Main;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmBox {
    public static Stage window;
    public static boolean answer;

    public static boolean display(String title, String info) throws IOException {
        window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);

        Font f = Font.font("SansSerif", FontWeight.BOLD, 16);
//        System.out.println(Font.getFamilies());

        Button yesBtn = new Button("Yes");
        yesBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        yesBtn.setPrefSize(60, 40);
        yesBtn.setOnAction(e -> {
            answer = true;
            window.close();
        });

        Button noBtn = new Button("No");
        noBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        noBtn.setPrefSize(60, 40);
        noBtn.setOnAction(e -> {
            answer = false;
            window.close();
        });

        Label content = new Label(info);
        content.setAlignment(Pos.CENTER);
        content.setTextAlignment(TextAlignment.CENTER);
        content.setFont(f);
        BorderPane border = new BorderPane();
        border.setCenter(content);


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(10);
        grid.setGridLinesVisible(false);
        grid.setHalignment(yesBtn, HPos.LEFT);
        grid.setHalignment(noBtn, HPos.RIGHT);
        grid.add(yesBtn, 0, 0);
        grid.add(noBtn, 1, 0);


        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(border, grid);

        window.setScene(new Scene(root, 400,200));

        window.showAndWait();
        return answer;
    }


    public void noClicked() {
        answer = false;
        window.close();
    }
    public void yesClicked() {
        answer = true;
        window.close();
    }

}
