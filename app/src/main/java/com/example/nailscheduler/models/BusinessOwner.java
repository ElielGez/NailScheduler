package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class BusinessOwner extends User {
    private String businessName;
    private String phoneNumber;
    private String address;

    public BusinessOwner() {

    }

    public BusinessOwner(String email, String fullName) {
        super(email, fullName, UserType.BUSINESS_OWNER);
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
