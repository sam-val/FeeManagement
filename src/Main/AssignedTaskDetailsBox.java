package Main;

import Main.Controller.DatabaseController;
import Main.model.Task;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AssignedTaskDetailsBox {
    public static void display(int id) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Task task = DatabaseController.findAssignedTaskByID(id);

        Font bold = Font.font("SansSerif", FontWeight.BOLD, 12);
        Font normal = Font.font("SansSerif", FontWeight.NORMAL, 12);
//        Font task = Font.font("SansSerif", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 14 );


        Label nameLabel = new Label("Task's Name: ");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(bold);
        Label name = new Label(task.getName());
        name.setFont(normal);

        Label statusLabel = new Label("Status: ");
        statusLabel.setTextAlignment(TextAlignment.CENTER);
        statusLabel.setFont(bold);
        Label status = new Label((task.getDone() == 1)? "Completed" : "Uncompleted");
        status.setFont(normal);

        Label assignedToLabel = new Label("Assigned to Employee: ");
        assignedToLabel.setTextAlignment(TextAlignment.CENTER);
        assignedToLabel.setFont(bold);
        Label assignedTo = new Label(DatabaseController.findEmployee(task.getAssignedUser()).getName());
        assignedTo.setFont(normal);

        Label descriptionLabel = new Label("Description: ");
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.setFont(bold);
        TextArea desription = new TextArea(task.getDescription());
        desription.setFont(normal);
        desription.setEditable(false);
        desription.setDisable(true);
        desription.setPrefSize(100,30);

        Button completeBtn = new Button("Set Completed");
        if (task.getDone() == 1) {
            completeBtn.setDisable(true);
        }
        completeBtn.setOnAction(e -> {
            boolean a = false;
            try {
                a = ConfirmBox.display("Set Task Completion", "Do you want to set this task as complete?\n and close this window?");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (a) {
                DatabaseController.setTaskCompleted(id);
                window.close();
            }

        });


//        Label currentTask = new Label("Tasks currently working on: ");
//        currentTask.setFont(task);
//        currentTask.setTextAlignment(TextAlignment.CENTER);
//
//        // set up table:
//
//        TableColumn nameColumn = new TableColumn<>("Task's Name");
//        nameColumn.setPrefWidth(150);
//        nameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
//
//        TableColumn clientColumn = new TableColumn<>("Client");
//        clientColumn.setPrefWidth(150);
//        clientColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("client"));
//
//        TableColumn feeColumn = new TableColumn<>("Fee (in $)");
//        feeColumn.setPrefWidth(50);
//        feeColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("fee"));
//
//        TableColumn deadlineColumn = new TableColumn<>("Deadline");
//        deadlineColumn.setPrefWidth(120);
//        deadlineColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));
//
//        TableColumn assignmentDateColumn = new TableColumn<>("Date Assigned");
//        assignmentDateColumn.setPrefWidth(120);
//        assignmentDateColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("assignedDate"));
//
//        TableView table = new TableView();
//        table.setItems(FXCollections.observableArrayList(DatabaseController.findAssignedTasksByEmployee(id, Database.UNDONE)));
//        table.getColumns().addAll(nameColumn, clientColumn, feeColumn, deadlineColumn, assignmentDateColumn);


        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(false);
        grid.setConstraints(nameLabel, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(name, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(statusLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(status, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(assignedToLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(assignedTo, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(descriptionLabel, 0, 4, 1, 1, HPos.LEFT, VPos.TOP);
        grid.setConstraints(desription, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);

        grid.setPrefSize(300,300);


        grid.setConstraints(completeBtn, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setConstraints(currentTask, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setMargin(currentTask, new Insets(10,0,0,0));

        grid.getChildren().addAll(nameLabel, name, statusLabel, status, assignedToLabel, assignedTo, descriptionLabel, desription, completeBtn);

//        BorderPane pane = new BorderPane();
//        pane.setCenter(table);

        BorderPane root = new BorderPane();
        root.setCenter(grid);

        window.setScene(new Scene(root));

        window.setTitle("Assigned Task Details");
        window.showAndWait();
    }
}
