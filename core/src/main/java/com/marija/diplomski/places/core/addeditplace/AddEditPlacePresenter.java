package com.marija.diplomski.places.core.addeditplace;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditplace.usecase.GetPlace;
import com.marija.diplomski.places.core.addeditplace.usecase.SavePlace;
import com.marija.diplomski.places.core.addeditplace.usecase.UpdatePlace;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.domain.model.Place;

import java.util.ArrayList;
import java.util.List;

public class AddEditPlacePresenter implements AddEditPlaceContract.Presenter {

    private UseCaseHandler useCaseHandler;
    private AddEditPlaceContract.View addEditPlaceView;
    private SavePlace savePlace;
    private String placeApiId;
    private Place placeToAdd;
    private GetPlace getPlace;
    private Place savedPlace;
    private UpdatePlace updatePlace;
    private boolean isViewLoadedAtLeastOnce;

    private TabletListener tabletListener;

    public AddEditPlacePresenter(UseCaseHandler useCaseHandler, AddEditPlaceContract.View addEditPlaceView, SavePlace savePlace, GetPlace getPlace, UpdatePlace updatePlace, String placeApiId, Place placeToAdd) {
        this.useCaseHandler = useCaseHandler;
        this.addEditPlaceView = addEditPlaceView;
        this.savePlace = savePlace;
        this.getPlace = getPlace;
        this.placeApiId = placeApiId;
        this.placeToAdd = placeToAdd;
        this.updatePlace = updatePlace;
        this.addEditPlaceView.setPresenter(this);
    }

    @Override
    public void loadPlace() {
        if (!isViewLoadedAtLeastOnce) {
            if (!isNewPlace()) {
                populatePlace();
            } else {
                if (isTablet()) {
                    tabletListener.onLoadPlaceResult(null);
                }
            }
        }
    }

    private void populatePlace() {
        useCaseHandler.execute(getPlace, new GetPlace.RequestValues(placeApiId), new UseCase.UseCaseCallback<GetPlace.ResponseValue>() {
            @Override
            public void onSuccess(GetPlace.ResponseValue response) {
                isViewLoadedAtLeastOnce = true;
                savedPlace = response.getPlace();
                showPlace(savedPlace);
                if (isTablet()) {
                    tabletListener.onLoadPlaceResult(savedPlace);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void showPlace(Place place) {
        addEditPlaceView.showDescription(place.getDescription());
        addEditPlaceView.showFolders(place.getFolders());
    }

    @Override
    public void savePlace(String description, List<Folder> folders) {
        if (isNewPlace()) {
            createPlace(description, folders);
        } else {
            updatePlace(description, folders);
        }
    }

    private void createPlace(String description, List<Folder> folders) {
        placeToAdd.setDescription(description);
        placeToAdd.setFolders(folders);
        useCaseHandler.execute(savePlace, new SavePlace.RequestValues(placeToAdd), new UseCase.UseCaseCallback<SavePlace.ResponseValue>() {
            @Override
            public void onSuccess(SavePlace.ResponseValue response) {
                addEditPlaceView.showPlaceDetails();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void updatePlace(String description, List<Folder> folders) {
        savedPlace.setDescription(description);
        savedPlace.setFolders(folders);
        useCaseHandler.execute(updatePlace, new UpdatePlace.RequestValues(savedPlace), new UseCase.UseCaseCallback<UpdatePlace.ResponseValue>() {
            @Override
            public void onSuccess(UpdatePlace.ResponseValue response) {
                addEditPlaceView.showPlaceDetails();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void openFolders() {
        List<Integer> placeFolderIds = new ArrayList<>();
        if (!isNewPlace()) {
            List<Folder> folders = savedPlace.getFolders();
            for (Folder folder : folders) {
                placeFolderIds.add(folder.getId());
            }
        }
        addEditPlaceView.showFoldersUi(placeFolderIds);
    }

    private boolean isNewPlace() {
        return placeApiId == null;
    }

    private boolean isTablet() {
        return tabletListener != null;
    }

    public void setTabletListener(TabletListener tabletListener) {
        this.tabletListener = tabletListener;
    }

    public interface TabletListener {
        void onLoadPlaceResult(Place place);
    }
}
