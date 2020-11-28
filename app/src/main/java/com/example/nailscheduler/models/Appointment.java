package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.AppointmentStatus;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Appointment {

    private String clientID;
    private String boID;
    private String date;
    private String time;
    private AppointmentStatus status;

    public Appointment() {
    }

    public Appointment(String clientID, String boID, String date, AppointmentStatus status, String time) {
        this.clientID = clientID;
        this.boID = boID;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBoID() {
        return boID;
    }

    public void setBoID(String boID) {
        this.boID = boID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}


