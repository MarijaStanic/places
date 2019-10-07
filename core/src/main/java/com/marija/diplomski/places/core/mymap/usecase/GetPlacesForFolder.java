package com.marija.diplomski.places.core.mymap.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Place;

import java.util.List;

public class GetPlacesForFolder extends UseCase<GetPlacesForFolder.RequestValues, GetPlacesForFolder.ResponseValue> {

    private PlaceDao placeDao;

    public GetPlacesForFolder(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Place> places = placeDao.getPlacesForFolder(requestValues.getFolderId());
        ResponseValue responseValue = new ResponseValue(places);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final int folderId;

        public RequestValues(int folderId) {
            this.folderId = folderId;
        }

        public int getFolderId() {
            return folderId;
        }
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