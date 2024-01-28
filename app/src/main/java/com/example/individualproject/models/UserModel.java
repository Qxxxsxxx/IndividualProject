package com.example.individualproject.models;

import com.google.firebase.Timestamp;

public class UserModel {
    private String password;
    private String username;
    private Timestamp timestamp;


    public UserModel() {
    }

    public UserModel(String username, String password, Timestamp timestamp) {
        this.password = password;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
