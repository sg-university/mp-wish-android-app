package com.binus.mp.wish.contracts;

public class Result<T> {
    private T content;
    private String status;
    private Integer code;

    @Override
    public String toString() {
        return "Result{" +
                "content=" + content +
                ", status='" + status + '\'' +
                ", code=" + code +
                '}';
    }

    public Result() {
    }

    public Result(T content, String status) {
        this.content = content;
        this.status = status;
    }

    public Result(T content, String status, Integer code) {
        this.content = content;
        this.status = status;
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}