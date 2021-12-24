package com.binus.mp.wish.models;



import java.sql.Timestamp;
import java.util.UUID;

public class Comment {
    private UUID ID;
    private UUID postID;

    private UUID creatorAccountID;
    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Comment() {
    }

    public Comment(UUID ID, UUID postID, UUID creatorAccountID, String content, Timestamp createdAt, Timestamp updatedAt) {
        this.ID = ID;
        this.postID = postID;
        this.creatorAccountID = creatorAccountID;
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


    public UUID getPostID() {
        return postID;
    }

    public void setPostID(UUID postID) {
        this.postID = postID;
    }

    public UUID getCreatorAccountID() {
        return creatorAccountID;
    }

    public void setCreatorAccountID(UUID creatorAccountID) {
        this.creatorAccountID = creatorAccountID;
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
