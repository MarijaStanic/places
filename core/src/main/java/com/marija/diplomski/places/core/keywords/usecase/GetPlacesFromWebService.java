package com.marija.diplomski.places.core.keywords.usecase;

import com.marija.diplomski.places.core.domain.infrastructure.LocationService;
import com.marija.diplomski.places.core.domain.data.PlacesRestApi;
import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.model.api.place.PlacesServerResponse;
import com.marija.diplomski.places.core.domain.model.Keyword;

/**
 * Created by Marija on 2017-07-29.
 */

public class GetPlacesFromWebService extends UseCase<GetPlacesFromWebService.RequestValues, GetPlacesFromWebService.ResponseValue> {

    private PlacesRestApi placesRestApi;
    private LocationService locationService;

    public GetPlacesFromWebService(PlacesRestApi placesRestApi, LocationService locationService) {
        this.placesRestApi = placesRestApi;
        this.locationService = locationService;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues)  {
        PlacesServerResponse response = placesRestApi.requestPlaces(requestValues.getKeyword(),
                locationService.getLatitude(), locationService.getLongitude());
        String statusOfRequest = response.getStatus();
        if (statusOfRequest.equalsIgnoreCase(PlacesRestApi.STATUS_ZERO_RESULTS)) {
            getUseCaseCallback().onError();
        } else if (statusOfRequest.equalsIgnoreCase(PlacesRestApi.STATUS_OK)) {

       //     placesMapView.showPlaces(mapper.map(response.body().getResults()), response.body().getNextPageToken(), true);
        } else {
         //   placesMapView.showLoadingPlacesError(null);
        }
    //    ResponseValue responseValue = new ResponseValue(places);
       /// getUseCaseCallback().onSuccess(responseValue);
    }


    public static final class RequestValues implements UseCase.RequestValues {

        private Keyword keyword;

        public RequestValues(Keyword keyword) {
            this.keyword = keyword;
        }

        public Keyword getKeyword() {
            return keyword;
        }


    }

    public static final class ResponseValue implements UseCase.ResponseValue {

   /*     private final List<Place> places;

        public ResponseValue(List<Place> places) {
            this.places = places;
        }

        public List<Place> getPlaces() {
            return places;
        }*/
    }
}
