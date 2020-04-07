package Main.Controller;

import Main.App;
import Main.ConfirmBox;
import Main.FeeProposalsBox;
import Main.model.Database;
import Main.model.EmpUser;
import Main.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    private App app;
    private User loggedUser;
    private EmpUser user = null;
    private Pane currentPane;
    private Button currentBtn;


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

    @FXML
    public TextField usernameDetails;

    @FXML
    private TextField emailDetails;

    @FXML
    private TextField phoneDetails;

    @FXML
    private TextArea skillsDetails;

    @FXML
    private TextField fullnameDetails;

    @FXML
    private TextField dateDetails;

    @FXML
    private Label detailsPrompt;


    @FXML
    private TextField comTasksSearch;

    @FXML
    private TextField onTasksSearch;

    @FXML
    private TableView<Main.model.Task> comTasksTable;

    @FXML
    private TableView<Main.model.Task> onTasksTable;

    @FXML
    private Label tasksDoneStats;


    @FXML
    private Label proposalsStats;


    @FXML
    private Label tasksAssStats;


    @FXML
    private Label payStats;

    @FXML
    private TextField DSearchField;

    public void setApplication(App application) {
        this.app = application;
        loggedUser = app.getLoggedUser();
        System.out.println("You signed in as " + loggedUser.getUsername());
        user = DatabaseController.findEmployee(loggedUser.getId());
        empName.setText( user.getName());

        tasksDoneStats.setText(Integer.toString(DatabaseController.countDoneTasks(loggedUser.getId())) );
        payStats.setText(Integer.toString(DatabaseController.totalPay(loggedUser.getId())));
        proposalsStats.setText(Integer.toString(DatabaseController.countProposals(loggedUser.getId())));
        tasksAssStats.setText(Integer.toString(DatabaseController.countAssTasks(loggedUser.getId())));


        listUnAssTasks("");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPane = pnlDashBoard;
        currentBtn = btnDashBoard;
        currentBtn.getStyleClass().add("current-btn");

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
           setCurrentPane(pnlDetails, btnDetails);

            EmpUser user = DatabaseController.findEmployee(loggedUser.getId());
            usernameDetails.setText(user.getUsername());
            fullnameDetails.setText(user.getName());
            emailDetails.setText(user.getEmail());
            phoneDetails.setText(Integer.toString(user.getPhone()));
            skillsDetails.setText(user.getSkills());
            dateDetails.setText(user.getDate());

        }
        if (event.getSource() == btnDashBoard) {
            setCurrentPane(pnlDashBoard, btnDashBoard);
            DSearchField.setText("");
            listUnAssTasks("");

            tasksDoneStats.setText(Integer.toString(DatabaseController.countDoneTasks(loggedUser.getId())) );
            payStats.setText(Integer.toString(DatabaseController.totalPay(loggedUser.getId())));
            proposalsStats.setText(Integer.toString(DatabaseController.countProposals(loggedUser.getId())));
            tasksAssStats.setText(Integer.toString(DatabaseController.countAssTasks(loggedUser.getId())));
        }
        if (event.getSource() == btnHistory) {
            setCurrentPane(pnlHistory, btnHistory);
        }
        if (event.getSource() == btnComTasks) {
            setCurrentPane(pnlComTasks, btnComTasks);

            comTasksSearch.setText("");
            listComTasks("");
        }
        if (event.getSource() == btnOnTasks) {
            setCurrentPane(pnlOnTasks, btnOnTasks);

            onTasksSearch.setText("");
            listOnTasks("");
        }
    }

    @FXML
    private void updateDetails() {
        String phone = phoneDetails.getText().trim();
        String name = fullnameDetails.getText().trim();
        String email = emailDetails.getText().trim();
        String skills = skillsDetails.getText().trim();

        if (name.isBlank() || phone.isBlank() || email.isBlank() || skills.isBlank()) {
            detailsPrompt.setText("Fields can't be empty!");
            return;
        }
        if (!email.matches("^(.+)@(.+)$")) {
            detailsPrompt.setText("Email is invalid");
            return;
        }

            if (!phone.matches("^\\d+$")) {
                detailsPrompt.setText("Phone number is invalid");
                return;
            }

            int phoneNum = Integer.parseInt(phone);

        boolean rs = DatabaseController.updateDetails(loggedUser.getId(), name, email, phoneNum, skills);

        if (rs) {
            detailsPrompt.setText("Success!");

        } else {
            detailsPrompt.setText("Failed");
        }
    }


    private void setCurrentPane(Pane pane, Button btn) {
        currentBtn.getStyleClass().remove("current-btn");
        currentBtn = btn;
        currentBtn.getStyleClass().add("current-btn");
//        currentBtn.setStyle("-fx-font-weight: bold");

        currentPane.setVisible(false);
        currentPane = pane;
        currentPane.toFront();
        currentPane.setVisible(true);

    }

    @FXML
    private void searchComTasks() {
        String search = comTasksSearch.getText();
        listComTasks(search);
    }

    @FXML
    private void searchOnTasks() {
        String search = onTasksSearch.getText();
        listOnTasks(search);
    }

    @FXML
    private void DSearchTasks() {
        String search = DSearchField.getText();
        listUnAssTasks(search);
    }

    private void listUnAssTasks(String search) {
        pnItems.getChildren().removeAll(pnItems.getChildren());
        List<Main.model.Task> tasks =  DatabaseController.getAllUnAssTasks(search);
        for (Main.model.Task task : tasks) {
            try {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(EmployeeController.class.getResource("../fxml/item.fxml"));

                Node node = loader.load();
                ItemController control = (ItemController) loader.getController();
                control.taskNameLabel.setText(task.getName());
                control.clientLabel.setText(task.getClient());
                control.proposalsLabel.setText(Integer.toString(task.getNumOfPendingEmps()));
                control.deadlineLabel.setText(task.getDeadline());

                control.moreBtn.setOnAction(e -> {
                    System.out.println(task.getId());
                    FeeProposalsBox fBox = new FeeProposalsBox();
                    boolean propose = fBox.display(task.getId(), loggedUser.getId());
                    if (propose) {
                        pnItems.getChildren().remove(node);
                    }

                });
                //give the items some effect

                node.setOnMouseEntered(event -> {
                    node.setStyle("-fx-background-color : #0A0E3F");
                });
                node.setOnMouseExited(event -> {
                    node.setStyle("-fx-background-color : #02030A");
                });
                pnItems.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void listComTasks(String search) {
        Task<ObservableList<Main.model.Task>> task = new Task<ObservableList<Main.model.Task>>() {
            @Override
            protected ObservableList<Main.model.Task> call() throws Exception {
                return FXCollections.observableArrayList(DatabaseController.findCompletedTasksByEmployee(loggedUser.getId(), search));
            }
        };

        comTasksTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

    }

    private void listOnTasks(String search) {
        Task<ObservableList<Main.model.Task>> task = new Task<ObservableList<Main.model.Task>>() {
            @Override
            protected ObservableList<Main.model.Task> call() throws Exception {
                return FXCollections.observableArrayList(DatabaseController.findAssignedTasksByEmployee(loggedUser.getId(), Database.UNDONE, search));
            }
        };

        onTasksTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }
}
