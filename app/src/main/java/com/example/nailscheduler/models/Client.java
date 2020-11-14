package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class Client extends User {

    public Client(){

    }
    public Client(String email,String fullName,String phoneNumber){
        super(email,fullName,phoneNumber, UserType.CLIENT);
    }
}
