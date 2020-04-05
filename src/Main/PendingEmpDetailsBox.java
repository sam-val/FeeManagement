package Main;

import Main.Controller.DatabaseController;
import Main.model.EmpUser;
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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PendingEmpDetailsBox {

    public static void display(int id) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        EmpUser emp =  DatabaseController.findEmployee(id);

        Font bold = Font.font("SansSerif", FontWeight.BOLD, 12);
        Font normal = Font.font("SansSerif", FontWeight.NORMAL, 12);
        Font task = Font.font("SansSerif", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 14 );


        Label nameLabel = new Label("Employee: ");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(bold);
        Label name = new Label(emp.getName());
        name.setFont(normal);

        Label emailLabel = new Label("Email: ");
        emailLabel.setTextAlignment(TextAlignment.CENTER);
        emailLabel.setFont(bold);
        Label email = new Label(emp.getEmail());
        email.setFont(normal);

        Label phoneLabel = new Label("Phone: ");
        phoneLabel.setTextAlignment(TextAlignment.CENTER);
        phoneLabel.setFont(bold);
        Label phone = new Label(String.valueOf(emp.getPhone()));
        phone.setFont(normal);

        Label skillsLabel = new Label("SKills: ");
        skillsLabel.setTextAlignment(TextAlignment.CENTER);
        skillsLabel.setFont(bold);
        TextArea skills = new TextArea(emp.getSkills());
        skills.setFont(normal);
        skills.setEditable(false);
        skills.setDisable(true);
        skills.setPrefSize(100,30);

        Button approveBtn = new Button("Approve");
        approveBtn.setOnAction(e -> {
            DatabaseController.setApprove(id);

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
        grid.setConstraints(emailLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(email, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(phoneLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(phone, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(skillsLabel, 0, 4, 1, 1, HPos.LEFT, VPos.TOP);
        grid.setConstraints(skills, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);

        grid.setPrefSize(300,300);


        grid.setConstraints(approveBtn, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setConstraints(currentTask, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setMargin(currentTask, new Insets(10,0,0,0));

        grid.getChildren().addAll(nameLabel, name, emailLabel, email, phoneLabel, phone, skillsLabel, skills, approveBtn);

//        BorderPane pane = new BorderPane();
//        pane.setCenter(table);

        BorderPane root = new BorderPane();
        root.setCenter(grid);

        window.setScene(new Scene(root));

        window.setTitle("Employee's Details");
        window.showAndWait();
    }
}
