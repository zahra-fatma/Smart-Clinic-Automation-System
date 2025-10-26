package com.dsaproj.ui.model;

import javafx.beans.property.*;

public class PatientFx {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty createdAt = new SimpleStringProperty();

    public PatientFx() {}

    // ✅ Constructor that sets properties correctly
    public PatientFx(long id, String name, int age, String priority, String status, String createdAt) {
        this.id.set(id);
        this.name.set(name);
        this.age.set(age);
        this.priority.set(priority);
        this.status.set(status);
        this.createdAt.set(createdAt);
    }

    // --- Getters & Setters with properties ---
    public long getId() { return id.get(); }
    public void setId(long v) { id.set(v); }
    public LongProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public int getAge() { return age.get(); }
    public void setAge(int v) { age.set(v); }
    public IntegerProperty ageProperty() { return age; }

    public String getPriority() { return priority.get(); }
    public void setPriority(String v) { priority.set(v); }
    public StringProperty priorityProperty() { return priority; }

    public String getStatus() { return status.get(); }
    public void setStatus(String v) { status.set(v); }
    public StringProperty statusProperty() { return status; }

    public String getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(String v) { createdAt.set(v); }
    public StringProperty createdAtProperty() { return createdAt; }
}