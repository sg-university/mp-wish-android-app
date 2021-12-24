package com.binus.mp.wish.models;


import java.sql.Timestamp;
import java.util.UUID;

public class Post {
    private String id;

    private String creator_account_id;
    private String title;
    private String content;

    private String created_at;

    private String updated_at;

    public Post() {
    }

    public Post(String id, String creator_account_id, String title, String content, String created_at, String updated_at) {
        this.id = id;
        this.creator_account_id = creator_account_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator_account_id() {
        return creator_account_id;
    }

    public void setCreator_account_id(String creator_account_id) {
        this.creator_account_id = creator_account_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
