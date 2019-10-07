package com.marija.diplomski.places.core.addeditplace.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Place;

public class GetPlace extends UseCase<GetPlace.RequestValues, GetPlace.ResponseValue> {

    private PlaceDao placeDao;

    public GetPlace(PlaceDao placeDao){
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Place place = placeDao.getPlace(requestValues.getPlaceId());
        if(place != null) {
            getUseCaseCallback().onSuccess(new ResponseValue(place));
        }else{
            getUseCaseCallback().onError();
        }
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

    public static final class ResponseValue implements UseCase.ResponseValue{

        private final Place place;

        public ResponseValue(Place place){
            this.place = place;
        }

        public Place getPlace(){
            return place;
        }
    }
}