package com.marija.diplomski.places.core.addeditplace.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;

/**
 * Created by Marija on 2017-04-09.
 */
public class DeletePlace extends UseCase<DeletePlace.RequestValues, DeletePlace.ResponseValue> {

    private PlaceDao placeDao;

    public DeletePlace(PlaceDao placeDao){
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        placeDao.deletePlace(requestValues.getPlaceId());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private final String placeId;

        public RequestValues(String placeId){
            this.placeId = placeId;
        }

        public String getPlaceId() {
            return placeId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {

        }

    }
}
