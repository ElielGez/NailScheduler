package com.example.nailscheduler.models;

import com.example.nailscheduler.enums.AppointmentStatus;


public class Appointment {

    private String clientID;
    private String clientName;
    private String boID;
    private String boName;
    private String date;
    private int startTime;
    private int endTime;
    private AppointmentStatus status;

    public Appointment() {
    }

    public Appointment(String clientID, String clientName, String boID, String boName, String date, int startTime, int endTime , AppointmentStatus status) {
        this.clientID = clientID;
        this.clientName= clientName;
        this.boID = boID;
        this.boName=boName;
        this.date = date;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getClientName() {
        return clientName;
    }
    public String getBoID() {
        return boID;
    }

    public void setBoID(String boID) {
        this.boID = boID;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}


