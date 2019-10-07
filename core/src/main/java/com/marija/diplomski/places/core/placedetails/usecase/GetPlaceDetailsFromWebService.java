package com.marija.diplomski.places.core.placedetails.usecase;

import com.marija.diplomski.places.core.domain.data.PlacesRestApi;
import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.api.placedetails.PlaceDetailsServerResponse;
import com.marija.diplomski.places.core.domain.model.api.place.Photo;
import com.marija.diplomski.places.core.domain.model.Place;

import java.io.IOException;
import java.util.List;

/**
 * Created by Marija on 2017-07-29.
 */

public class GetPlaceDetailsFromWebService extends UseCase<GetPlaceDetailsFromWebService.RequestValues, GetPlaceDetailsFromWebService.ResponseValue> {

    private PlacesRestApi placesRestApi;
    private PlaceDao placeDao;

    public GetPlaceDetailsFromWebService(PlacesRestApi placesRestApi, PlaceDao placeDao) {
        this.placesRestApi = placesRestApi;
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        PlaceDetailsServerResponse response;
        String url;
        try {
             response = placesRestApi.requestPlaceDetails(requestValues.getPlaceId());
        } catch (IOException e) {
            getUseCaseCallback().onError(e);
            return;
        }
        String statusOfRequest = response.getStatus();
        if (statusOfRequest.equalsIgnoreCase(placesRestApi.STATUS_ZERO_RESULTS)) {
            getUseCaseCallback().onError();
            return;
        }
        List<Photo> placePhotos = response.getResult().getPhotos();
        if (placePhotos.size() > 0) {
            url = placesRestApi.requestPhotoUrl(placePhotos.get(0).getPhotoReference(), "400");
        }
        Place place = placeDao.getPlace(requestValues.getPlaceId());
        if(place != null) {
            getUseCaseCallback().onSuccess(new ResponseValue(place));
        }else{
            getUseCaseCallback().onError();
        }
      //  ResponseValue responseValue = new ResponseValue(places);
    //    getUseCaseCallback().onSuccess(responseValue);
    }


    public static final class RequestValues implements UseCase.RequestValues {

        private final String placeId;

        public RequestValues(String placeId){
            this.placeId = placeId;
        }

        public String getPlaceId() {
            return placeId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Place place;

        public ResponseValue(Place place) {
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

}
