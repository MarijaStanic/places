package com.marija.diplomski.places.core.mymap;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.mymap.usecase.GetPlaces;
import com.marija.diplomski.places.core.mymap.usecase.GetPlacesForFolder;

import java.util.List;

public class PlacesInFolderPresenter implements PlacesInFolderContract.Presenter {

    public static final int LOAD_ALL_PLACES = -1;

    private UseCaseHandler useCaseHandler;
    private PlacesInFolderContract.View placesInFolderView;
    private GetPlaces getPlaces;
    private GetPlacesForFolder getPlacesForFolder;
    private Integer folderId;

    public PlacesInFolderPresenter(UseCaseHandler useCaseHandler, PlacesInFolderContract.View placesInFolderView, GetPlaces getPlaces, GetPlacesForFolder getPlacesForFolder, Integer folderId) {
        this.useCaseHandler = useCaseHandler;
        this.placesInFolderView = placesInFolderView;
        this.getPlaces = getPlaces;
        this.getPlacesForFolder = getPlacesForFolder;
        this.folderId = folderId;
    }

    public void loadPlaces() {
        if (folderId == null) // Tablet
            return;
        if (shouldLoadAllPlaces())
            loadAllPlaces();
        else
            loadPlacesForFolder();
    }

    private boolean shouldLoadAllPlaces() {
        return folderId == LOAD_ALL_PLACES;
    }

    private void loadAllPlaces() {
        useCaseHandler.execute(getPlaces, null, new UseCase.UseCaseCallback<GetPlaces.ResponseValue>() {
            @Override
            public void onSuccess(GetPlaces.ResponseValue response) {
                processPlaces(response.getPlaces());
            }

            @Override
            public void onError() {

            }
        });
    }

    private void loadPlacesForFolder() {
        useCaseHandler.execute(getPlacesForFolder, new GetPlacesForFolder.RequestValues(folderId), new UseCase.UseCaseCallback<GetPlacesForFolder.ResponseValue>() {
            @Override
            public void onSuccess(GetPlacesForFolder.ResponseValue response) {
                processPlaces(response.getPlaces());
            }

            @Override
            public void onError() {

            }
        });
    }

    private void processPlaces(List<Place> places) {
        if (places.size() == 0) {
            placesInFolderView.clearPlacesList(places);
            placesInFolderView.showNoPlacesInFolderMessage();
        } else {
            placesInFolderView.showPlacesList(places);
            placesInFolderView.showMarkers(places);
        }
    }

    @Override
    public void openPlaceDetails() {
        placesInFolderView.showPlaceDetailsUi();
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    } // Tablet
}
