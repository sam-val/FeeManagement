package Main.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TaskFee {
    SimpleStringProperty taskName = new SimpleStringProperty();
    SimpleIntegerProperty proposedFee = new SimpleIntegerProperty();
    SimpleStringProperty pendingEmpName = new SimpleStringProperty();
    SimpleIntegerProperty taskId = new SimpleIntegerProperty();
    SimpleIntegerProperty pendingEmpId = new SimpleIntegerProperty();
    SimpleStringProperty comment = new SimpleStringProperty();
    SimpleStringProperty pendingEmpSkills = new SimpleStringProperty();

    public String getPendingEmpSkills() {
        return pendingEmpSkills.get();
    }

    public SimpleStringProperty pendingEmpSkillsProperty() {
        return pendingEmpSkills;
    }

    public void setPendingEmpSkills(String pendingEmpSkills) {
        this.pendingEmpSkills.set(pendingEmpSkills);
    }

    public String getTaskName() {
        return taskName.get();
    }

    public SimpleStringProperty taskNameProperty() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    public int getProposedFee() {
        return proposedFee.get();
    }

    public SimpleIntegerProperty proposedFeeProperty() {
        return proposedFee;
    }

    public void setProposedFee(int proposedFee) {
        this.proposedFee.set(proposedFee);
    }

    public String getPendingEmpName() {
        return pendingEmpName.get();
    }

    public SimpleStringProperty pendingEmpNameProperty() {
        return pendingEmpName;
    }

    public void setPendingEmpName(String pendingEmpName) {
        this.pendingEmpName.set(pendingEmpName);
    }

    public int getTaskId() {
        return taskId.get();
    }

    public SimpleIntegerProperty taskIdProperty() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId.set(taskId);
    }

    public int getPendingEmpId() {
        return pendingEmpId.get();
    }

    public SimpleIntegerProperty pendingEmpIdProperty() {
        return pendingEmpId;
    }

    public void setPendingEmpId(int pendingEmpId) {
        this.pendingEmpId.set(pendingEmpId);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
