package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.UserType;

public class Client extends User {

    public Client(){

    }
    public Client(String email,String password,String fullName){
        super(email,password,fullName, UserType.CLIENT);
    }
}
