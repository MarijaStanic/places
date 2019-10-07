package com.marija.diplomski.places.core.placedetails;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.domain.model.presentation.PlaceDetailsViewModel;

public interface PlaceDetailsContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showEditRemovePlaceButtons();

        void showAddToMapButton();

        void showPlaceDetails(PlaceDetailsViewModel viewModel, String imageUrl);

        void showPhoneDialerUi(String number);

        void showWebPageUi(String url);

        void showStreetViewUi(double lat, double lng);

        void showAddNewPlace(Place placeToAdd);

        void showEditPlace(String placeId);

        void showPlaceDeleted();

        void showLoadingPlaceDetailsError(Exception exception);

        void showNoPlaceDetailsMessage();
    }

    interface Presenter {

        void openPlace();

        void openPhoneDialer(String number);

        void openWebPage(String url);

        void openStreetView();

        void addNewPlace();

        void editPlace();

        void deletePlace();
    }
}
