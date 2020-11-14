package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;
import com.example.nailscheduler.interfaces.IUser;

public class User implements IUser {
    private String email;
    private String fullName;
    private String phoneNumber;
    private UserType type;

    public User() {

    }

    public User(String email, String fullName,String phoneNumber,UserType type) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
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
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
