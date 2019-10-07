package com.marija.diplomski.places.core.domain.model.api.place;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}