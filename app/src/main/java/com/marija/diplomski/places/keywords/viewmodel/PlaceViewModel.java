package com.marija.diplomski.places.keywords.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceViewModel implements Parcelable {

    private String name;
    private  double latitude;
    private double longitude;
    private String vicinity;
    private String distanceToPlace;
    private String placeId;
    private boolean isOpen;

    public PlaceViewModel() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getDistanceToPlace() {
        return distanceToPlace;
    }

    public void setDistanceToPlace(String distanceToPlace) {
        this.distanceToPlace = distanceToPlace;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    protected PlaceViewModel(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        vicinity = in.readString();
        distanceToPlace = in.readString();
        placeId = in.readString();
        isOpen = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(vicinity);
        dest.writeString(distanceToPlace);
        dest.writeString(placeId);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceViewModel> CREATOR = new Parcelable.Creator<PlaceViewModel>() {
        @Override
        public PlaceViewModel createFromParcel(Parcel in) {
            return new PlaceViewModel(in);
        }

        @Override
        public PlaceViewModel[] newArray(int size) {
            return new PlaceViewModel[size];
        }
    };
}