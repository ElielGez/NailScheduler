package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class BusinessOwner extends User {
    private String id;
    private String businessName;
    private BoAddress boAddress;

    public BusinessOwner() {

    }

    public BusinessOwner(String email, String fullName, String phoneNumber) {
        super(email, fullName, phoneNumber, UserType.BUSINESS_OWNER);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return businessName;
    }
}
