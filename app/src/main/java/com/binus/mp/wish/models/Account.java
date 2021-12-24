package com.binus.mp.wish.models;


import java.sql.Timestamp;
import java.util.UUID;

public class Account {
    private UUID ID;
    private String username;
    private String name;
    private String email;
    private String password;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Account() {
    }

    public Account(UUID ID, String username, String name, String email, String password) {
        this.ID = ID;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
