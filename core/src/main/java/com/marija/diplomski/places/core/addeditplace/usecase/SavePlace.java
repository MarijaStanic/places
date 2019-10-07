package com.marija.diplomski.places.core.addeditplace.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Place;

public class SavePlace extends UseCase<SavePlace.RequestValues, SavePlace.ResponseValue> {

    private PlaceDao placeDao;

    public SavePlace(PlaceDao placeDao){
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        placeDao.addPlace(requestValues.getPlace());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private final Place place;

        public RequestValues(Place place){
            this.place = place;
        }

        public Place getPlace() {
            return place;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
