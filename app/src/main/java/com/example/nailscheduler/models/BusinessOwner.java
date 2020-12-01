package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class BusinessOwner extends User {
    private String businessName;
    private BoAddress boAddress;

    public BusinessOwner() {

    }

    public BusinessOwner(String email, String fullName, String phoneNumber) {
        super(email, fullName, phoneNumber, UserType.BUSINESS_OWNER);
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public BoAddress getBoAddress() {
        return boAddress;
    }

    public void setBoAddress(BoAddress boAddress) {
        this.boAddress = boAddress;
    }
}
