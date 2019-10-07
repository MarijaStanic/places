package com.marija.diplomski.places.core.keywords;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Keyword;

import java.util.List;

public interface PlacesMapContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void clearMap();

        void showPlaces(List<Object> places, String nextPageToken, boolean forceUpdate);

        void showLoadingPlacesError(Exception exception);

        void showNoPlacesMessage();

        void showMarkers();

        void showPlaceDetailsUi(String placeApiId);

        void showSearchPlace();

        void setMapType(String mapType);
    }

    interface Presenter {

        void onResume();

        void loadPlaces(Keyword keyword);

        void loadMorePlaces(String nextPageToken);

        void openPlaceDetails(String placeApiId);

        void openSearchPlace();
    }
}
