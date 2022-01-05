package com.binus.mp.wish.contracts;

public class CredentialsByEmail {
    private String email;
    private String password;

    @Override
    public String toString() {
        return "CredentialsByEmail{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public CredentialsByEmail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CredentialsByEmail() {
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
}
