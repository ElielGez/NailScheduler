package com.example.nailscheduler.models;

public class BoAddress {
    private String city;
    private String street;
    private String number;
    private String cityName;

    public BoAddress(){

    }

    public BoAddress(String city, String cityName, String street, String number) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
