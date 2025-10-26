package com.example.hospital_manager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String status; // e.g., "WAITING", "ATTENDED"

    @Enumerated(EnumType.STRING)
    private PriorityLevel priorityLevel; // NEW field

    private LocalDateTime createdAt; // NEW field

    public Patient() {
        this.createdAt = LocalDateTime.now(); // auto set when created
    }

    public Patient(String name, int age, String status, PriorityLevel priorityLevel) {
        this.name = name;
        this.age = age;
        this.status = status;
        this.priorityLevel = priorityLevel;
        this.createdAt = LocalDateTime.now();
    }

    // --- getters & setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public PriorityLevel getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}