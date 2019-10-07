package com.marija.diplomski.places.core.keywords.presenters;

import com.marija.diplomski.places.core.common.Mapper;
import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.domain.data.PreferencesHelper;
import com.marija.diplomski.places.core.domain.model.api.place.PlacesServerResponse;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.domain.infrastructure.LocationService;
import com.marija.diplomski.places.core.domain.data.PlacesRestApi;
import com.marija.diplomski.places.core.keywords.PlacesMapContract;
import com.marija.diplomski.places.core.keywords.usecase.GetPlacesFromWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesMapPresenter implements PlacesMapContract.Presenter {

    private PlacesMapContract.View placesMapView;
    private LocationService locationService;
    private PlacesRestApi placesRestApi;
    private Mapper mapper;
    private PreferencesHelper preferencesHelper;
    private GetPlacesFromWebService getPlaces;
    private UseCaseHandler useCaseHandler;

    public PlacesMapPresenter(PlacesMapContract.View placesMapView,
                              LocationService locationService,
                              PlacesRestApi placesRestApi,
                              Mapper mapper,
                              PreferencesHelper preferencesHelper) {
        this.placesMapView = placesMapView;
        placesMapView.setPresenter(this);
        this.locationService = locationService;
        this.placesRestApi = placesRestApi;
        this.mapper = mapper;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void onResume() {
        String mapType = preferencesHelper.getMapType();
        placesMapView.setMapType(mapType);
    }

    @Override
    public void loadMorePlaces(String nextPageToken) {
        placesMapView.showProgress("Searching for more");
        placesRestApi.requestPlacesForPageToken(nextPageToken, new Callback<PlacesServerResponse>() {
            @Override
            public void onResponse(Call<PlacesServerResponse> call, Response<PlacesServerResponse> response) {
                String statusOfRequest = response.body().getStatus();
                if (statusOfRequest.equalsIgnoreCase(placesRestApi.STATUS_OK)) {
                    placesMapView.showPlaces(mapper.map(response.body().getResults()), response.body().getNextPageToken(), false);
                    placesMapView.dismissProgress();
                } else {
                    placesMapView.showLoadingPlacesError(null);
                }
            }

            @Override
            public void onFailure(Call<PlacesServerResponse> call, Throwable t) {
                placesMapView.dismissProgress();
                placesMapView.showLoadingPlacesError((Exception) t);
            }
        });
    }

    @Override
    public void loadPlaces(Keyword keyword) {
        placesMapView.showProgress("Searching for '" + keyword.getTitle() + "'");
        placesRestApi.requestPlaces(keyword, locationService.getLatitude(), locationService.getLongitude(), new Callback<PlacesServerResponse>() {
            @Override
            public void onResponse(Call<PlacesServerResponse> call, Response<PlacesServerResponse> response) {
                String statusOfRequest = response.body().getStatus();
                if (statusOfRequest.equalsIgnoreCase(PlacesRestApi.STATUS_ZERO_RESULTS)) {
                    placesMapView.showNoPlacesMessage();
                } else if (statusOfRequest.equalsIgnoreCase(PlacesRestApi.STATUS_OK)) {
                    placesMapView.clearMap();
                    placesMapView.showPlaces(mapper.map(response.body().getResults()), response.body().getNextPageToken(), true);
                } else {
                    placesMapView.showLoadingPlacesError(null);
                }
                placesMapView.dismissProgress();
            }

            @Override
            public void onFailure(Call<PlacesServerResponse> call, Throwable t) {
                placesMapView.dismissProgress();

                placesMapView.showLoadingPlacesError((Exception) t);
            }
        });
    }

    public void lPlaces(Keyword keyword) {
        placesMapView.showProgress("Searching for '" + keyword.getTitle() + "'");
        useCaseHandler.execute(getPlaces, new GetPlacesFromWebService.RequestValues(keyword),
                new UseCase.UseCaseCallback<GetPlacesFromWebService.ResponseValue>() {
                    @Override
                    public void onSuccess(GetPlacesFromWebService.ResponseValue response) {
                        placesMapView.clearMap();
                  //      placesMapView.showPlaces(mapper.map(response.getResults()),
                    //            response.getNextPageToken(), true);
                    }

                    @Override
                    public void onError() {
                        placesMapView.showNoPlacesMessage();
                    }

                    @Override
                    public void onError(Exception e) {
                        placesMapView.dismissProgress();
                        placesMapView.showLoadingPlacesError(e);
                    }
                });
    }

    @Override
    public void openPlaceDetails(String placeApiId) {
        placesMapView.showPlaceDetailsUi(placeApiId);
    }

    @Override
    public void openSearchPlace() {
        placesMapView.showSearchPlace();
    }

}
