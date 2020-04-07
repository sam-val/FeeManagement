package Main.Controller;

import Main.*;
import Main.model.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AdminController implements Initializable {
    public App app;
    private User loggedUser;
    public SignUpBox signUpBox = new SignUpBox();
    private Timer Emptimer;
    private Style labelStyle;
    private Style Hbox;
    private SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
    public static ObservableList<Client> allClients = FXCollections.observableArrayList(DatabaseController.getAllClients());
    private static HBox currentOnBox;

//    @FXML
//    private

    @FXML
    private DatePicker taskDeadlinePicker;

    @FXML
    private Tab searchTab;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private CheckBox approvedCheckBox;

    @FXML
    private CheckBox onTasksCheckBox;


    @FXML
    private TableView<EmpUser> empTable;

    @FXML
    public TableView<EmpUser> pendingTable;

    @FXML
    public TableView<Task> searchTaskTable;

    @FXML
    private TextField searchTaskField;

    @FXML
    private Button searchTaskBtn;

    @FXML
    private Tab searchTaskTab;

    @FXML
    private RadioButton assignedRadio;

    @FXML
    private RadioButton unassignedRadio;



    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private HBox employeeBox;

    @FXML
    private HBox clientBox;

    @FXML
    private HBox taskBox;

    @FXML
    private Pane empPane;

    @FXML
    private Pane taskPane;

    @FXML
    private Label clientLabel;

    @FXML
    private Label empLabel;

    @FXML
    private Label taskLabel;

    @FXML
    private TextField taskNameField;

    @FXML
    private TextArea descripTextArea;

    @FXML
    private ChoiceBox<Client> clientChoiceBox;

    @FXML
    private Button addTaskBtn;

    @FXML
    private Tab addTaskTab;

    @FXML
    private Label addTaskPrompt;

    @FXML
    private TableColumn taskCompleteColumn;



    public void setApp(App app) {
        this.app = app;
        loggedUser = app.getLoggedUser();
        System.out.println("set App (of adminController class) ran.");
        if (loggedUser != null) {
            System.out.println("You signed in as " + loggedUser.getUsername());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("admin's initialize is running:.");

        currentOnBox = employeeBox;
        currentOnBox.setStyle("-fx-background-color: darkgrey");
        currentOnBox.getChildren().get(0).setStyle("-fx-color-label-visible: white");
        currentOnBox.getChildren().get(0).setStyle("-fx-font-weight: bold");

        //////////////// set ADD TASK: /////////////////////



        // set client choice box:
        clientChoiceBox.setItems(allClients);

        clientChoiceBox.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                if (client != null) {
                    return client.getName();
                }
                return null;
            }

            @Override
            public Client fromString(String s) {
                return null;
            }
        });




        taskDeadlinePicker.setValue(LocalDate.now());

        taskDeadlinePicker.setEditable(false);
        taskDeadlinePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return spf.format(java.sql.Date.valueOf(localDate));
            }


            @Override
            public LocalDate fromString(String s) {
                return null;
            }
        });




        approvedCheckBox.setSelected(true);


        autoRefresbPendingEmpTable(0, 30000);

    }

    public void userLogOut(ActionEvent actionEvent) {
        if (app != null) {
            app.logOut();
        }
    }

    public void quitApp() {
        if (app != null) {
            app.closeProgram();
        }
    }


    @FXML
    public void handleClicks(MouseEvent event) {
        currentOnBox.getChildren().get(0).setStyle("-fx-color-label-visible: black");
        currentOnBox.getChildren().get(0).setStyle("-fx-font-weight: none");

        if (event.getSource() == employeeBox) {
            currentOnBox.setStyle("-fx-background-color: none");
            currentOnBox = employeeBox;
            currentOnBox.setStyle("-fx-background-color: darkgrey");


            empPane.toFront();
            empPane.setVisible(true);
            System.out.println("Emp timer runs");
            autoRefresbPendingEmpTable(0, 30000);
            taskPane.setVisible(false);

        }
        if (event.getSource() == clientBox) {
            currentOnBox.setStyle("-fx-background-color: none");
            currentOnBox = clientBox;
            currentOnBox.setStyle("-fx-background-color: darkgrey");
        }

        if (event.getSource() == taskBox) {
            clientChoiceBox.getItems().removeAll();
            clientChoiceBox.setItems(allClients);
            currentOnBox.setStyle("-fx-background-color: none");
            currentOnBox = taskBox;
            currentOnBox.setStyle("-fx-background-color: darkgrey");
            taskPane.toFront();
            taskPane.setVisible(true);
            empPane.setVisible(false);
            Emptimer.cancel();
            System.out.println("Emp timer cancels");

        }
        currentOnBox.getChildren().get(0).setStyle("-fx-color-label-visible: white");
        currentOnBox.getChildren().get(0).setStyle("-fx-font-weight: bold");

    }

    @FXML
    public void newEmp(ActionEvent event) throws IOException {
        signUpBox.setAdControl(this);
        signUpBox.displayForAdmin();
    }

    private void autoRefresbPendingEmpTable(int delay, int period) {
        Emptimer = new Timer();
        Emptimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updatePendingEmpTable();
                    }
                });
            }
        }, delay, period);
    }


    @FXML
    private void searchEmployees() {
        String searchInput = searchField.getText();
        boolean approve = approvedCheckBox.selectedProperty().getValue();
        boolean onTasks = onTasksCheckBox.selectedProperty().getValue();
        if (approve == false) {
            onTasks = false;
        }
        listEmployees(approve, onTasks, searchInput);
    }


    private void listEmployees(boolean approved, boolean onTasks, String searchInput) {
        javafx.concurrent.Task task = new javafx.concurrent.Task() {
            @Override
            protected ObservableList<EmpUser> call() throws Exception {
                return FXCollections.observableArrayList(DatabaseController.queryEmployees(approved, onTasks, searchInput));
            }
        };

        empTable.itemsProperty().bind(task.valueProperty());

        empTable.setRowFactory(new Callback<TableView<EmpUser>, TableRow<EmpUser>>() {
            @Override
            public TableRow<EmpUser> call(TableView<EmpUser> empUserTableView) {
                final TableRow<EmpUser> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showIDMenuItem = new MenuItem("Show ID and Username");
                final MenuItem approveItem = new MenuItem("Approve User");
                final MenuItem detailsItem = new MenuItem("Details");

                detailsItem.setOnAction(e -> {
                    if (row.getItem().getApproved() != 1) {
                        PendingEmpDetailsBox.display(row.getItem().getId());
                    } else {
                        EmpDetailsBox.display(row.getItem().getId());
                    }
                });
                showIDMenuItem.setOnAction(e -> {
                    System.out.println("ID: " + row.getItem().getId());
                    System.out.println("username: " + row.getItem().getUsername());
                });

                approveItem.setOnAction(e -> {
                    boolean answer;
                    try {
                        answer = ConfirmBox.display("Approve account", "Do you want to approve this account\n and remove it from list?");
                        if (answer) {
                            boolean success = setApprove(row.getItem().getId());
                            if (success) {
                                empTable.getItems().remove(row.getItem());
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                contextMenu.getItems().addAll(showIDMenuItem, detailsItem);

                row.setOnContextMenuRequested(e -> {
                    System.out.println("hello");
                    if (row.getItem().getApproved() != 1) {
                        if (!contextMenu.getItems().contains(approveItem)) {
                            contextMenu.getItems().add(approveItem);
                        }
                    }
                });


                    row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

                return row;
            }
        });

        new Thread(task).start();
    }


    private void updatePendingEmpTable() {
        javafx.concurrent.Task task = new getAllPendingUsersTask();
        Thread thread = new Thread(task);
        pendingTable.itemsProperty().bind(task.valueProperty());

        pendingTable.setRowFactory(new Callback<TableView<EmpUser>, TableRow<EmpUser>>() {
            @Override
            public TableRow<EmpUser> call(TableView<EmpUser> empUserTableView) {
                final TableRow<EmpUser> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showIDMenuItem = new MenuItem("Show ID and Username");
                final MenuItem approveItem = new MenuItem("Approve User");
                final MenuItem detailsItem = new MenuItem("Details");

                detailsItem.setOnAction(e -> {
                    PendingEmpDetailsBox.display(row.getItem().getId());
                });
                showIDMenuItem.setOnAction(e -> {
                    System.out.println("ID: " + row.getItem().getId());
                    System.out.println("username: " + row.getItem().getUsername());
                });


                approveItem.setOnAction(e -> {
                    boolean answer;
                    try {
                        answer = ConfirmBox.display("Approve account", "Do you want to approve this account\n and remove it from list?");
                        if (answer) {
                            boolean success = setApprove(row.getItem().getId());
                            if (success) {
                                pendingTable.getItems().remove(row.getItem());
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                contextMenu.getItems().addAll(showIDMenuItem, approveItem, detailsItem);

                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

                return row;
            }
        });
        thread.start();
    }

    public boolean setApprove(int id) {
        return DatabaseController.setApprove(id);
    }


    @FXML
    public void handleMouseEnter(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == currentOnBox) {
//            currentOnBox.getChildren().get(0).setStyle("-fx-color-label-visible: white");
//            currentOnBox.getChildren().get(0).setStyle("-fx-font-weight: bold");
        } else
        {
            if (mouseEvent.getSource() == employeeBox) {

                empLabel.setStyle("-fx-color-label-visible: white");
                empLabel.setStyle("-fx-font-weight: bold");
            }
            if (mouseEvent.getSource() == clientBox) {

                clientLabel.setStyle("-fx-color-label-visible: white");
                clientLabel.setStyle("-fx-font-weight: bold");
            }
            if (mouseEvent.getSource() == taskBox) {

                taskLabel.setStyle("-fx-color-label-visible: white");
                taskLabel.setStyle("-fx-font-weight: bold");
            }
        }
    }

    @FXML
    public void handleMouseExited(MouseEvent event) {
        if (event.getSource() == currentOnBox) {
//            currentOnBox.getChildren().get(0).setStyle("-fx-color-label-visible: black");
//            currentOnBox.getChildren().get(0).setStyle("-fx-font-weight: none");
        } else {

            if (event.getSource() == employeeBox) {
//                employeeBox.setStyle("-fx-background-color: none");
                empLabel.setStyle("-fx-color-label-visible: black");
                empLabel.setStyle("-fx-font-weight: none");
            }
            if (event.getSource() == clientBox) {
//                clientBox.setStyle("-fx-background-color: none");
                clientLabel.setStyle("-fx-color-label-visible: black");
                clientLabel.setStyle("-fx-font-weight: none");
            }
            if (event.getSource() == taskBox) {
//                taskBox.setStyle("-fx-background-color: none");
                taskLabel.setStyle("-fx-color-label-visible: black");
                taskLabel.setStyle("-fx-font-weight: none");
            }
        }

    }

    public void toggleApprove(ActionEvent event) {
        if (approvedCheckBox.isSelected()) {
            onTasksCheckBox.setDisable(false);
        } else {
            onTasksCheckBox.setDisable(true);
        }
    }

    public void manageClient(ActionEvent event) throws IOException {
        NewClientBox.display();
    }

    public void searchTask(ActionEvent event) {
        String search = searchTaskField.getText();
        javafx.concurrent.Task task = null;
        if (assignedRadio.isSelected()) {
            System.out.println("assign is selected");
                 task = new javafx.concurrent.Task() {
                    @Override
                    public ObservableList<Task> call() throws Exception {
                        return FXCollections.observableArrayList(DatabaseController.searchTasks(search, Database.ASSIGNED));
                    }
                };

        }else {
            task = new javafx.concurrent.Task() {
                @Override
                protected ObservableList<Task> call() throws Exception {
                    return FXCollections.observableArrayList(DatabaseController.searchTasks(search, Database.UNASSIGNED));
                }
            };
        }

        new Thread(task).start();
        searchTaskTable.itemsProperty().bind(task.valueProperty());

        searchTaskTable.setEditable(true);

        taskCompleteColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                if (integer == 0) {
                    return "No";
                } else {
                    return "Yes";
                }
            }
            @Override
            public Integer fromString(String s) {
                return null;
            }

        }));
        searchTaskTable.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> taskTableView) {
                final TableRow<Task> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showIDMenuItem = new MenuItem("Show ID in terminal");
                final MenuItem moreMenuItem = new MenuItem("More...");
                final MenuItem removeTaskItem = new MenuItem("Remove Task");


                removeTaskItem.setOnAction(e -> {

                    boolean answer;
                    try {
                        answer = ConfirmBox.display("Removing Unassigned Task", "Do you want to delete this\n unassigned Task?");
                        if (answer) {
                            boolean success = DatabaseController.removeATask(row.getItem().getId());
                            if (success) {
                                searchTaskTable.getItems().remove(row.getItem());
                                searchTaskTable.getItems().remove(row.getIndex());
                            } else {
                                System.out.println("error removing task!");
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                });

                showIDMenuItem.setOnAction(e -> System.out.println(row.getItem().getId()));

                moreMenuItem.setOnAction(e -> {
                    if (row.getItem().getAssignedUser() != 0) {
                        AssignedTaskDetailsBox.display(row.getItem().getId());
                    } else {
                        UnAssignedTaskDetailsBox box = new UnAssignedTaskDetailsBox();
                        box.setTableAndRow(AdminController.this.searchTaskTable, row.getIndex());
                        box.display(row.getItem().getId());
                    }
                });

                contextMenu.getItems().addAll(showIDMenuItem, moreMenuItem);

                row.setOnContextMenuRequested(e -> {
                    if (row.getItem().getAssignedUser() == 0) {
                        if (!contextMenu.getItems().contains(removeTaskItem)) {
                            contextMenu.getItems().add(removeTaskItem);
                        }
                    }
                });

                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                return row;
            }
        });


    }

    public void TabChanged(Event event) {
        if (event.getSource() == searchTab) {
            if (searchTab.isSelected()) {
                System.out.println("tab is selected");
                searchBtn.setDefaultButton(true);
            } else {
                searchBtn.setDefaultButton(false);
            }
        }
        if (event.getSource() == addTaskTab) {
            if (addTaskTab.isSelected()) {
                System.out.println("add tab is selected");
                clientChoiceBox.getItems().removeAll();
                clientChoiceBox.setItems(allClients);
                addTaskBtn.setDefaultButton(true);
            } else {
                addTaskBtn.setDefaultButton(false);
            }
        }
        if (event.getSource() == searchTaskTab) {
            if (searchTaskTab.isSelected()) {
                System.out.println("search task is selected");
                searchTaskBtn.setDefaultButton(true);
            } else {
                searchTaskBtn.setDefaultButton(false);
            }
        }
    }

    public void addTask(ActionEvent event) {
        String taskName = taskNameField.getText();
        String description = descripTextArea.getText();
        if (taskName.isBlank() || description.isBlank()) {
            addTaskPrompt.setText("Name or Description can't be empty");
            return;
        }
        String deadline = spf.format(Date.valueOf(taskDeadlinePicker.getValue()));

        try {
            java.util.Date deadlineDate = spf.parse(deadline);
            java.util.Date today = spf.parse(spf.format(new java.util.Date()));
            if (deadlineDate.compareTo(today) <= 0) {
                addTaskPrompt.setText("Deadline must be at least 1 day after today!");
                return;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        int clientId = 0;
        if (clientChoiceBox.getValue() != null) {
            clientId = clientChoiceBox.getSelectionModel().getSelectedItem().getId();
        } else {
            addTaskPrompt.setText("a client must be chosen!");
            return;
        }


        boolean result = DatabaseController.addTask(taskName, clientId, deadline, description);

        if (result) {
            addTaskPrompt.setText("Add successfully!");
            taskDeadlinePicker.setValue(LocalDate.now());
        } else {
            addTaskPrompt.setText("Error adding task");
        }



        System.out.println(clientId);
        System.out.println(deadline);
    }

    public void info(ActionEvent event) {
        AlertBox.display("by Sam Val", "Info");
    }

//    public void setApprove(int id) {
//        final EmpUser emp = (EmpUser) pendingTable.getSelectionModel().getSelectedItem();
//        if (emp == null) {
//            System.out.println("No record is selected");
//            return;
//        }
//        Task<ObservableList<EmpUser>> task = new Task<ObservableList<EmpUser>>() {
//            @Override
//            protected ObservableList<EmpUser> call() throws Exception {
//                return null;
//            }
//        };
//
//    }

    class getAllPendingUsersTask extends javafx.concurrent.Task {

        @Override
        public ObservableList<EmpUser> call() {

            return FXCollections.observableArrayList(DatabaseController.queryPendingEmpUsers());

        }
    }

    class getAllClientsTask extends javafx.concurrent.Task {

        @Override
        protected Object call() throws Exception {
            return FXCollections.observableArrayList(DatabaseController.getAllClients());
        }
    }

}




