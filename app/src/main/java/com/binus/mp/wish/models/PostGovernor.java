package com.binus.mp.wish.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public class PostGovernor {
    private UUID id;
    @JsonProperty("post_id")
    private UUID postId;
    @JsonProperty("governor_account_id")
    private UUID governorAccountId;
    @JsonProperty("created_at")
    private Timestamp createdAt;
    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "PostGovernor{" +
                "id=" + id +
                ", postId=" + postId +
                ", governorAccountId=" + governorAccountId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public PostGovernor() {
    }

    public PostGovernor(UUID id, UUID postId, UUID governorAccountId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.postId = postId;
        this.governorAccountId = governorAccountId;
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

    public UUID getGovernorAccountId() {
        return governorAccountId;
    }

    public void setGovernorAccountId(UUID governorAccountId) {
        this.governorAccountId = governorAccountId;
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
