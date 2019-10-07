package com.marija.diplomski.places.core.domain.model.api.placedetails;

import com.google.gson.annotations.SerializedName;
import com.marija.diplomski.places.core.domain.model.api.place.Geometry;
import com.marija.diplomski.places.core.domain.model.api.place.Photo;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;
    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;

    private Geometry geometry;

    private String icon;

    @SerializedName("photos")
    private List<Photo> photos = new ArrayList<Photo>();

    private String name;

    private String website;

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}