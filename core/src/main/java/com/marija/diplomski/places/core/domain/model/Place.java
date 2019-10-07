package com.marija.diplomski.places.core.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Serializable {

    private Integer id;
    private String placeId;
    private String address;
    private String name;
    private String photo;
    private double latitude;
    private double longitude;
    private String icon;
    private String description;
    private List<Folder> folders = new ArrayList<>();

    public Place(){

    }

    public Place(Integer id, String placeId, String name, String photo, double latitude, double longitude, String icon, String description) {
        this.id = id;
        this.placeId = placeId;
        this.name = name;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.description = description;
    }

    public Place(String placeId, String name, String photo, double latitude, double longitude,
                 String icon, String description){
        this(null, placeId, name, photo, latitude, longitude, icon, description);
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }
}
