package Main.Controller;

import Main.App;
import Main.ConfirmBox;
import Main.model.EmpUser;
import Main.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    private App app;
    private User loggedUser;
    private EmpUser user = null;
    private Pane currentPane;


    @FXML
    private VBox pnItems = null;
    @FXML
    private Button btnDashBoard;

    @FXML
    private Button btnHistory;

    @FXML
    private Button btnOnTasks;

    @FXML
    private Button btnComTasks;

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlComTasks;

    @FXML
    private Pane pnlDetails;

    @FXML
    private Pane pnlOnTasks;

    @FXML
    private Pane pnlDashBoard;

    @FXML
    private Pane pnlHistory;

    @FXML
    private Label empName;

    public void setApplication(App application) {
        this.app = application;
        loggedUser = app.getLoggedUser();
        System.out.println("You signed in as " + loggedUser.getUsername());
        user = DatabaseController.findEmployee(loggedUser.getId());
        empName.setText( user.getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPane = pnlDashBoard;

        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(EmployeeController.class.getResource("../fxml/item.fxml"));

                nodes[i] = loader.load();
                //give the items some effect

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                pnItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void userLogOut(ActionEvent actionEvent) throws IOException {
        boolean a = ConfirmBox.display("Logging Out", "Do you want to sign out?");
        if (a) {
            if (app != null) {
                app.logOut();
            }
        }
    }

    public void handleClicks(ActionEvent event) {
        if (event.getSource() == btnDetails) {
           setCurrentPane(pnlDetails);
        }
        if (event.getSource() == btnDashBoard) {
            setCurrentPane(pnlDashBoard);
        }
        if (event.getSource() == btnHistory) {
            setCurrentPane(pnlHistory);
        }
        if (event.getSource() == btnComTasks) {
            setCurrentPane(pnlComTasks);
        }
        if (event.getSource() == btnOnTasks) {
            setCurrentPane(pnlOnTasks);
        }
    }

    private void setCurrentPane(Pane pane) {
        currentPane.setVisible(false);
        currentPane = pane;
        currentPane.toFront();
        currentPane.setVisible(true);

    }
}
