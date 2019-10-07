package com.marija.diplomski.places.core.keywords;

import com.marija.diplomski.places.core.base.view.UiContracts;

public interface PlacesListContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showPlaceDetailsUi(String placeId);
    }

    interface Presenter {

        void openPlaceDetails(String placeApiId);
    }
}
