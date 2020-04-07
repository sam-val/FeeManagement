package Main;

import Main.Controller.DatabaseController;
import Main.model.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FeeProposalsBox {

    boolean propose = false;
    public boolean display(int id, int userID) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setOnCloseRequest(e -> {
            e.consume();
            window.close();
        });
        Task task = DatabaseController.findUnAssignedTaskByID(id);

        Font bold = Font.font("SansSerif", FontWeight.BOLD, 12);
        Font normal = Font.font("SansSerif", FontWeight.NORMAL, 12);
//        Font task = Font.font("SansSerif", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 14 );

        Label prompt = new Label("Task's Info");
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFont(bold);

        Label nameLabel = new Label("Task's Name: ");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setFont(bold);
        Label name = new Label(task.getName());
        name.setFont(normal);

        Label clientLabel = new Label("Client: ");
        clientLabel.setTextAlignment(TextAlignment.CENTER);
        clientLabel.setFont(bold);
        Label client = new Label(task.getClient());
        client.setFont(normal);



        Label descriptionLabel = new Label("Description: ");
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        descriptionLabel.setFont(bold);
        TextArea desription = new TextArea(task.getDescription());
        desription.setFont(normal);
        desription.setEditable(false);
        desription.setDisable(true);
        desription.setPrefSize(100,30);

        Label commentLabel = new Label("Your Comments: ");
        commentLabel.setTextAlignment(TextAlignment.CENTER);
        commentLabel.setFont(bold);
        TextArea commentArea = new TextArea();
        commentArea.setPrefSize(100,30);
        commentArea.getStyleClass().add("fee-field");

        Label feeLabel = new Label("Your Fee Proposal( in $): ");
        feeLabel.setTextAlignment(TextAlignment.CENTER);
        feeLabel.setFont(bold);
        TextField feeField = new TextField();
        feeField.setPromptText("enter your number");

        feeField.getStyleClass().add("fee-field");




        Button proposeBtn = new Button("Propose");
        proposeBtn.setOnAction(e -> {
            if (feeField.getText().isBlank() || (!feeField.getText().matches("^\\d+\\.?\\d*$"))) {
                prompt.setText("Fee is invalid");
                return;
            }

            if (Integer.parseInt(feeField.getText().trim()) <= 5) {
                prompt.setText("Fee must be more \nthan five dollars");
                return;
            }

            String com = commentArea.getText().trim();
            if (com.equals("")) {
                prompt.setText("Comment should not be empty");
                return;
            }

            int fee = Integer.parseInt(feeField.getText().trim());


            boolean a = false;
            try {
                a = ConfirmBox.display("Fee Proposing", "Do you want to propose to work on\nthis task for $" + feeField.getText() + " ?");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (a) {
                if (DatabaseController.proposeFee(id, userID, fee, com)) {
                    prompt.setText("Success");
                    proposeBtn.setDisable(true);
                    propose = true;
                }

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
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
//        grid.setGridLinesVisible(true);
        grid.setConstraints(prompt, 1,0,1,1);
        grid.setMargin(prompt, new Insets(0, 0, 10, 0));

        grid.setConstraints(nameLabel, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(name, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(clientLabel, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(client, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(descriptionLabel, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(desription, 1, 3, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(commentLabel, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(commentArea, 1, 4, 1, 1, HPos.LEFT, VPos.TOP );
        grid.setConstraints(feeLabel, 0, 5, 1, 1, HPos.LEFT, VPos.CENTER);
        grid.setConstraints(feeField, 1, 5, 1, 1, HPos.LEFT, VPos.BOTTOM);


        grid.setConstraints(proposeBtn, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setConstraints(currentTask, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
//        grid.setMargin(currentTask, new Insets(10,0,0,0));

        grid.getChildren().addAll(prompt, nameLabel, name, clientLabel, client, commentLabel,commentArea , descriptionLabel, desription,feeLabel, feeField, proposeBtn);

//        BorderPane pane = new BorderPane();
//        pane.setCenter(table);

        Parent root = new BorderPane();
        ((BorderPane) root).setCenter(grid);

        Scene s = new Scene(root);
        s.getStylesheets().add("Main/css/style.css");
        window.setScene(s);
        window.setWidth(400);
        window.setHeight(400);
        window.setTitle("Proposing a Fee");
        window.showAndWait();
        return propose;
    }
}
