package Main;

import Main.Controller.AdminController;
import Main.Controller.DatabaseController;
import Main.model.Task;
import Main.model.TaskFee;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class UnAssignedTaskDetailsBox {
    private AdminController adControl;
    private int row_index;
    private TableView<Task> adTable;

    public void display(int id) {
        Stage window = new Stage();

        Task task = DatabaseController.findUnAssignedTaskByID(id);

        Font bold = Font.font("SansSerif", FontWeight.BOLD, 12);
        Font normal = Font.font("SansSerif", FontWeight.NORMAL, 12);
        Font pending = Font.font("SansSerif", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 14 );


        Label nameLabel = new Label("Task's Name: ");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(bold);
        Label name = new Label(task.getName());
        name.setFont(normal);

        Label clientLabel = new Label("Client:  ");
        clientLabel.setTextAlignment(TextAlignment.CENTER);
        clientLabel.setFont(bold);
        Label client = new Label(task.getClient());
        client.setFont(normal);

        Label deadlineLabel = new Label("Deadline: ");
        deadlineLabel.setTextAlignment(TextAlignment.CENTER);
        deadlineLabel.setFont(bold);
        Label deadline = new Label(task.getDeadline());
        deadline.setFont(normal);

        Label descriptionLabel = new Label("Description: ");
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.setFont(bold);
        TextArea description = new TextArea(task.getDescription());
        description.setFont(normal);
        description.setEditable(false);
        description.setDisable(true);
        description.setPrefSize(200,50);

//        Button completeBtn = new Button("Set Completed");
//        if (task.getDone() == 1) {
//            completeBtn.setDisable(true);
//        }
//        completeBtn.setOnAction(e -> {
//            boolean a = false;
//            try {
//                a = ConfirmBox.display("Set Task Completion", "Do you want to set this task as complete?\n and close this window?");
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            if (a) {
//                DatabaseController.setTaskCompleted(id);
//                window.close();
//            }
//
//        });


        Label pendingEmployees = new Label("Fee-Proposals for the task: ");
        pendingEmployees.setFont(pending);
        pendingEmployees.setTextAlignment(TextAlignment.CENTER);

        // set up table:

        TableColumn nameColumn = new TableColumn<>("Employee's Name");
        nameColumn.setPrefWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<TaskFee, String>("pendingEmpName"));

        TableColumn skillColumn = new TableColumn<>("Skills");
        skillColumn.setPrefWidth(150);
        skillColumn.setCellValueFactory(new PropertyValueFactory<TaskFee, String>("pendingEmpSkills"));

        TableColumn feeColumn = new TableColumn<>("Proposed Fee (in $)");
        feeColumn.setPrefWidth(150);
        feeColumn.setCellValueFactory(new PropertyValueFactory<TaskFee, Integer>("proposedFee"));

        TableColumn commentColumn = new TableColumn<>("Comment");
        commentColumn.setPrefWidth(120);
        commentColumn.setCellValueFactory(new PropertyValueFactory<TaskFee, String>("comment"));

        TableView<TaskFee> table = new TableView();
        table.setItems(FXCollections.observableArrayList(DatabaseController.findFeeProposalsByTask(id)));
        table.getColumns().addAll(nameColumn, skillColumn, commentColumn, feeColumn);

        table.setRowFactory(new Callback<TableView<TaskFee>, TableRow<TaskFee>>() {
            @Override
            public TableRow<TaskFee> call(TableView<TaskFee> taskFeeTableView) {

                TableRow<TaskFee> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem approveItem = new MenuItem("Approve Proposal");
                MenuItem details = new MenuItem("Employee Details");

                details.setOnAction(e -> {
                    EmpDetailsBox.display(row.getItem().getPendingEmpId());
                });

                approveItem.setOnAction(e-> {
                    try {
                        boolean answer = ConfirmBox.display("Approving Fee Proposal", "Do you want to approve this proposal\nand set this user to the task?");
                        if (answer) {
                            boolean result = DatabaseController.assignUsertoTask(row.getItem().getPendingEmpId() ,task.getId(), row.getItem().getProposedFee());

                            if (result) {
                                System.out.println("success!");
                                if (adTable != null) {
                                    adTable.getItems().remove(row_index);
                                }
                            }
                            window.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                });

                contextMenu.getItems().addAll(details, approveItem);
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));

                return row;
            }
        });


        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.BOTTOM_CENTER);
        grid.setGridLinesVisible(false);
        grid.setConstraints(nameLabel, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(name, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(clientLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(client, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(deadlineLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(deadline, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(descriptionLabel, 0, 4, 1, 1, HPos.LEFT, VPos.TOP);
        grid.setConstraints(description, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);

        grid.setPrefSize(300,300);


//        grid.setConstraints(completeBtn, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
        grid.setConstraints(pendingEmployees, 0, 5, 2, 1, HPos.CENTER, VPos.BOTTOM);
        grid.setMargin(pendingEmployees, new Insets(0,0,0,0));


        grid.getChildren().addAll(nameLabel, name, clientLabel, client, deadlineLabel, deadline, descriptionLabel, description, pendingEmployees);

        BorderPane pane = new BorderPane();
        pane.setCenter(table);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(grid,pane);

        window.setScene(new Scene(root, 600,450));

        window.setTitle("Fee Proposals");
        window.show();
    }

    public void setTableAndRow(TableView<Task> table, int row_index) {
        this.adTable = table;
        this.row_index = row_index;
    }
}
