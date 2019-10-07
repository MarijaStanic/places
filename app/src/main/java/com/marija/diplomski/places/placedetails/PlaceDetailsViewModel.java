package com.marija.diplomski.places.placedetails;

import java.util.LinkedHashMap;

public class PlaceDetailsViewModel {

    private String name;
    private String address;
    private String website;
    private String phoneNumber;
    private LinkedHashMap<String, String> workingHours;
    private double latitude, longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public LinkedHashMap<String, String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(LinkedHashMap<String, String> workingHours) {
        this.workingHours = workingHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
