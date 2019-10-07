package com.marija.diplomski.places.core.mymap;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Place;

import java.util.List;

public interface PlacesInFolderContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showPlacesList(List<Place> places);

        void showMarkers(List<Place> places);

        void showPlaceDetailsUi();

        void showNoPlacesInFolderMessage();

        void clearPlacesList(List<Place> places);
    }

    interface Presenter {

        void loadPlaces();

        void openPlaceDetails();
    }
}
