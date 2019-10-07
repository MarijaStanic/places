package com.marija.diplomski.places.core.mymap.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Place;

import java.util.List;

public class GetPlaces extends UseCase<GetPlaces.RequestValues, GetPlaces.ResponseValue> {

    private PlaceDao placeDao;

    public GetPlaces(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Place> places = placeDao.getAllPlaces();
        ResponseValue responseValue = new ResponseValue(places);
        getUseCaseCallback().onSuccess(responseValue);
    }


    public static final class RequestValues implements UseCase.RequestValues {

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Place> places;

        public ResponseValue(List<Place> places) {
            this.places = places;
        }

        public List<Place> getPlaces() {
            return places;
        }
    }
}
