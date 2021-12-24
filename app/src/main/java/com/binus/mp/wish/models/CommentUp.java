package com.binus.mp.wish.models;


import java.sql.Timestamp;
import java.util.UUID;

public class CommentUp {
    private UUID ID;

    private UUID postID;

    private UUID upperAccountID;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public CommentUp() {
    }

    public CommentUp(UUID ID, UUID postID, UUID governorAccountID, Timestamp createdAt, Timestamp updatedAt) {
        this.ID = ID;
        this.postID = postID;
        this.upperAccountID = governorAccountID;
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

    public UUID getUpperAccountID() {
        return upperAccountID;
    }

    public void setUpperAccountID(UUID upperAccountID) {
        this.upperAccountID = upperAccountID;
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
