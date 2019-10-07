package com.marija.diplomski.places.core.addeditplace.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Place;

public class UpdatePlace extends UseCase<UpdatePlace.RequestValues, UpdatePlace.ResponseValue> {

    private PlaceDao placeDao;

    public UpdatePlace(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        placeDao.updatePlace(requestValues.getPlace( ));
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Place place;

        public RequestValues(Place place) {
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue(){

        }
    }
}