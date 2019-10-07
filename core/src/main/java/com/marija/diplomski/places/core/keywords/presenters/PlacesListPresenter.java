package com.marija.diplomski.places.core.keywords.presenters;

import com.marija.diplomski.places.core.keywords.PlacesListContract;

public class PlacesListPresenter implements PlacesListContract.Presenter {

    private PlacesListContract.View placesListView;

    public PlacesListPresenter(PlacesListContract.View placesListView) {
        this.placesListView = placesListView;
    }

    @Override
    public void openPlaceDetails(String placeApiId) {
        placesListView.showPlaceDetailsUi(placeApiId);
    }

}
