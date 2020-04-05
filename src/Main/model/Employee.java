package Main.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleIntegerProperty phone;
    private SimpleStringProperty skill_set;

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

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getPhone() {
        return phone.get();
    }

    public SimpleIntegerProperty phoneProperty() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone.set(phone);
    }

    public String getSkill_set() {
        return skill_set.get();
    }

    public SimpleStringProperty skill_setProperty() {
        return skill_set;
    }

    public void setSkill_set(String skill_set) {
        this.skill_set.set(skill_set);
    }
}
