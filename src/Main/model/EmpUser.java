package Main.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmpUser {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleIntegerProperty phone;
    private SimpleIntegerProperty type;
    private SimpleIntegerProperty approved;
    private SimpleStringProperty date;
    private SimpleStringProperty username;
    private SimpleStringProperty skills;

    public EmpUser(int id, String username, int type, int approved, String date, String name, String email, int phone, String skills) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
        this.username = new SimpleStringProperty(username);
        this.type = new SimpleIntegerProperty(type);
        this.approved = new SimpleIntegerProperty(approved);
        this.date = new SimpleStringProperty(date);
        this.skills = new SimpleStringProperty(skills);

    }

    public int getType() {
        return type.get();
    }

    public SimpleIntegerProperty typeProperty() {
        return type;
    }

    public void setType(int type) {
        this.type.set(type);
    }

    public int getApproved() {
        return approved.get();
    }

    public SimpleIntegerProperty approvedProperty() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved.set(approved);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getSkills() {
        return skills.get();
    }

    public SimpleStringProperty skillsProperty() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills.set(skills);
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
}
