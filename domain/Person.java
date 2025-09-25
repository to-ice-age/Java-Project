package edu.ccrm.domain;

import java.time.LocalDateTime;

public abstract class Person {
    private String id;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private boolean active;

    protected Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public abstract String getRole();

    // Getters and Setters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    
    public void setEmail(String email) { this.email = email; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("%s [ID: %s, Name: %s, Email: %s, Active: %s]",
            getRole(), id, fullName, email, active);
    }
}