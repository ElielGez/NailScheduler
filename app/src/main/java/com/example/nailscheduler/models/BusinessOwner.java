package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class BusinessOwner extends User {
    
    public BusinessOwner(){

    }
    public BusinessOwner(String email,String password,String fullName){
        super(email,password,fullName, UserType.BUSINESS_OWNER);
    }
}
