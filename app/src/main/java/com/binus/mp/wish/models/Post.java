package com.binus.mp.wish.models;


import java.sql.Timestamp;
import java.util.UUID;

public class Post {
    private UUID ID;

    private UUID creatorAccountID;
    private String title;
    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Post() {
    }

    public Post(UUID ID, UUID creatorAccountID, String title, String content, Timestamp createdAt, Timestamp updatedAt) {
        this.ID = ID;
        this.creatorAccountID = creatorAccountID;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getCreatorAccountID() {
        return creatorAccountID;
    }

    public void setCreatorAccountID(UUID creatorAccountID) {
        this.creatorAccountID = creatorAccountID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
