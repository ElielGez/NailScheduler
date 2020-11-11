package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;
import com.example.nailscheduler.interfaces.IUser;

public class User implements IUser {
    private String email;
    private String password;
    private String fullName;
    private UserType type;

    public User() {

    }

    public User(String email, String password, String fullName,UserType type) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.type = type;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public UserType getType() {
        return type;
    }

    @Override
    public void setType(UserType type) {
        this.type = type;
    }
}
