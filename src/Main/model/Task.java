package Main.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Task {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty() ;
    private SimpleStringProperty description = new SimpleStringProperty() ;
    private SimpleStringProperty client = new SimpleStringProperty();
    private SimpleStringProperty deadline = new SimpleStringProperty();
    private SimpleIntegerProperty assignedUser  = new SimpleIntegerProperty();
    private SimpleIntegerProperty fee  = new SimpleIntegerProperty();
    private SimpleStringProperty assignedDate = new SimpleStringProperty();
    private SimpleIntegerProperty done = new SimpleIntegerProperty();
    private SimpleIntegerProperty numOfPendingEmps = new SimpleIntegerProperty();

    public int getNumOfPendingEmps() {
        return numOfPendingEmps.get();
    }

    public SimpleIntegerProperty numOfPendingEmpsProperty() {
        return numOfPendingEmps;
    }

    public void setNumOfPendingEmps(int numOfPendingEmps) {
        this.numOfPendingEmps.set(numOfPendingEmps);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getClient() {
        return client.get();
    }

    public SimpleStringProperty clientProperty() {
        return client;
    }

    public void setClient(String client) {
        this.client.set(client);
    }

    public String getDeadline() {
        return deadline.get();
    }

    public SimpleStringProperty deadlineProperty() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline.set(deadline);
    }

    public int getAssignedUser() {
        return assignedUser.get();
    }

    public SimpleIntegerProperty assignedUserProperty() {
        return assignedUser;
    }

    public void setAssignedUser(int assignedUser) {
        this.assignedUser.set(assignedUser);
    }

    public int getFee() {
        return fee.get();
    }

    public SimpleIntegerProperty feeProperty() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee.set(fee);
    }

    public String getAssignedDate() {
        return assignedDate.get();
    }

    public SimpleStringProperty assignedDateProperty() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate.set(assignedDate);
    }

    public int getDone() {
        return done.get();
    }

    public SimpleIntegerProperty doneProperty() {
        return done;
    }

    public String getDoneString() {
        if (done.get() == 1) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public void setDone(int done) {
        this.done.set(done);
    }
}
