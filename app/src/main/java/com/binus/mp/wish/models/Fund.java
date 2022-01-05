package com.binus.mp.wish.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public class Fund {
    private UUID id;
    @JsonProperty("post_id")
    private UUID postId;
    @JsonProperty("sponsor_account_id")
    private UUID sponsorAccountId;
    private String content;
    private Double amount;
    @JsonProperty("created_at")
    private Timestamp createdAt;
    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + id +
                ", postId=" + postId +
                ", sponsorAccountId=" + sponsorAccountId +
                ", content='" + content + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public Fund() {
    }

    public Fund(UUID id, UUID postId, UUID sponsorAccountId, String content, Double amount, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.postId = postId;
        this.sponsorAccountId = sponsorAccountId;
        this.content = content;
        this.amount = amount;
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

    public UUID getSponsorAccountId() {
        return sponsorAccountId;
    }

    public void setSponsorAccountId(UUID sponsorAccountId) {
        this.sponsorAccountId = sponsorAccountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
