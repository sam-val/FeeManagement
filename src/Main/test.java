package Main;

import Main.model.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test extends Application {
    Stage window;

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        ChoiceBox<User> choices =new ChoiceBox<>();
        List<User> users = new ArrayList<>();
        String[] names = {"yaiba", "sam", "tuan", "natalie", "chris"};

        for (int i = 1; i <= names.length; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername(names[i-1]);
            users.add(user);
        }

        for (User user : users) {
            choices.getItems().add(user);
            System.out.println(user.getUsername());
        }

        choices.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getUsername();
            }

            @Override
            public User fromString(String s) {

                return null;
            }
        });
        choices.setValue(users.get(0));
        choices.getSelectionModel().selectedItemProperty().addListener((ob, oldVal, newVal) -> {
            if (newVal != null) {
                System.out.printf("id of user %s is %d\n", newVal.getUsername(), newVal.getId());
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(choices);

        Scene scene = new Scene(root, 300, 300);
        window.setScene(scene);
        window.setTitle("Test Stuff");
        window.show();
    }

    public static void main(String args[]) throws ParseException {
        String date1 = "1995/11/28";
        String date2 = "11-28-1995";

        SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
        Date date1Object = spf.parse(date1);

        SimpleDateFormat spf2 = new SimpleDateFormat("MM-dd-yyyy");
        Date date2Object = spf2.parse(date2);
        String converted = spf.format(date2Object);
        System.out.println(converted);

        if (date1Object.compareTo(date2Object) == 0) {
            System.out.println("same date");
        }
        launch(args);

    }
}
