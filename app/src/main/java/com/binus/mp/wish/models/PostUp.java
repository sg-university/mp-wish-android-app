package com.binus.mp.wish.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public class PostUp {
    private UUID id;
    @JsonProperty("post_id")
    private UUID postId;
    @JsonProperty("upper_account_id")
    private UUID upperAccountId;
    @JsonProperty("created_at")
    private Timestamp createdAt;
    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "PostUp{" +
                "id=" + id +
                ", postId=" + postId +
                ", upperAccountId=" + upperAccountId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public PostUp() {
    }

    public PostUp(UUID id, UUID postId, UUID upperAccountId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.postId = postId;
        this.upperAccountId = upperAccountId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getUpperAccountId() {
        return upperAccountId;
    }

    public void setUpperAccountId(UUID upperAccountId) {
        this.upperAccountId = upperAccountId;
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
