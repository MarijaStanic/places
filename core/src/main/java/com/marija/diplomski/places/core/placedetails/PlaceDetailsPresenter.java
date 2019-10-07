package com.marija.diplomski.places.core.placedetails;

import com.marija.diplomski.places.core.common.Mapper;
import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditplace.usecase.DeletePlace;
import com.marija.diplomski.places.core.addeditplace.usecase.GetPlace;
import com.marija.diplomski.places.core.domain.model.api.placedetails.Result;
import com.marija.diplomski.places.core.domain.model.api.placedetails.PlaceDetailsServerResponse;
import com.marija.diplomski.places.core.domain.model.api.place.Location;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.domain.model.presentation.PlaceDetailsViewModel;
import com.marija.diplomski.places.core.domain.data.PlacesRestApi;
import com.marija.diplomski.places.core.placedetails.usecase.GetPlaceDetailsFromWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsPresenter implements PlaceDetailsContract.Presenter {

    private String placeApiId;
    private PlaceDetailsContract.View taskDetailView;
    private PlacesRestApi placesRestApi;
    private boolean isViewLoadedAtLeastOnce;
    private Result result;
    private Mapper mapper;
    private String url = null;
    private UseCaseHandler useCaseHandler;
    private GetPlace getPlace;
    private DeletePlace deletePlace;
    private GetPlaceDetailsFromWebService getPlaceDetailsFromWebService;

    public PlaceDetailsPresenter(UseCaseHandler useCaseHandler,
                                 String placeApiId,
                                 PlaceDetailsContract.View taskDetailView,
                                 PlacesRestApi placesRestApi,
                                 Mapper mapper,
                                 GetPlace getPlace,
                                 DeletePlace deletePlace) {
        this.useCaseHandler = useCaseHandler;
        this.placeApiId = placeApiId;
        this.taskDetailView = taskDetailView;
        this.placesRestApi = placesRestApi;
        this.taskDetailView.setPresenter(this);
        this.mapper = mapper;
        this.getPlace = getPlace;
        this.deletePlace = deletePlace;
    }

    @Override
    public void openPlace() {
        if (!isViewLoadedAtLeastOnce) {
            placesRestApi.requestPlaceDetails(placeApiId, new Callback<PlaceDetailsServerResponse>() {
                @Override
                public void onResponse(Call<PlaceDetailsServerResponse> call, Response<PlaceDetailsServerResponse> response) {
                    String statusOfRequest = response.body().getStatus();
                    if (statusOfRequest.equalsIgnoreCase(placesRestApi.STATUS_ZERO_RESULTS)) {
                        taskDetailView.showNoPlaceDetailsMessage();
                        return;
                    }
                    if (statusOfRequest.equalsIgnoreCase(placesRestApi.STATUS_OK)) {
                        isViewLoadedAtLeastOnce = true;
                        result = response.body().getResult();
                        if (result.getPhotos().size() > 0) {
                            url = placesRestApi.requestPhotoUrl(result.getPhotos().get(0).getPhotoReference(), "400");
                        }
                        taskDetailView.showPlaceDetails((PlaceDetailsViewModel) mapper.map(result), url);
                        useCaseHandler.execute(getPlace, new GetPlace.RequestValues(placeApiId), new UseCase.UseCaseCallback<GetPlace.ResponseValue>() {
                            @Override
                            public void onSuccess(GetPlace.ResponseValue response) {
                                taskDetailView.showEditRemovePlaceButtons();
                            }

                            @Override
                            public void onError() {
                                taskDetailView.showAddToMapButton();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<PlaceDetailsServerResponse> call, Throwable t) {
                    taskDetailView.showLoadingPlaceDetailsError((Exception) t);
                }
            });
        }
    }

    public void open() {
        useCaseHandler.execute(getPlaceDetailsFromWebService, new GetPlaceDetailsFromWebService.RequestValues(placeApiId), new UseCase.UseCaseCallback<GetPlaceDetailsFromWebService.ResponseValue>() {
            @Override
            public void onSuccess(GetPlaceDetailsFromWebService.ResponseValue response) {
                isViewLoadedAtLeastOnce = true;
            }

            @Override
            public void onError() {
                taskDetailView.showNoPlaceDetailsMessage();
            }

            @Override
            public void onError(Exception e) {
                taskDetailView.showLoadingPlaceDetailsError(e);
            }
        });
    }

    @Override
    public void openPhoneDialer(String number) {
        taskDetailView.showPhoneDialerUi(number);
    }

    @Override
    public void openWebPage(String url) {
        taskDetailView.showWebPageUi(url);
    }

    @Override
    public void openStreetView() {
        Location locationOfPlace = result.getGeometry().getLocation();
        taskDetailView.showStreetViewUi(locationOfPlace.getLat(), locationOfPlace.getLng());
    }

    @Override
    public void addNewPlace() {
        Place place = new Place(null, placeApiId, result.getName(), url, result.getGeometry().getLocation().getLat(),
                result.getGeometry().getLocation().getLng(), result.getIcon(), null);
        taskDetailView.showAddNewPlace(place);
    }

    @Override
    public void editPlace() {
        taskDetailView.showEditPlace(placeApiId);
    }

    @Override
    public void deletePlace() {
        useCaseHandler.execute(deletePlace, new DeletePlace.RequestValues(placeApiId), new UseCase.UseCaseCallback<DeletePlace.ResponseValue>() {
            @Override
            public void onSuccess(DeletePlace.ResponseValue response) {
                taskDetailView.showPlaceDeleted();
            }

            @Override
            public void onError() {

            }
        });
    }


}
