package com.marija.diplomski.places.keywords;

import com.marija.diplomski.places.core.folders.FoldersContract;
import com.marija.diplomski.places.core.folders.FoldersPresenter;
import com.marija.diplomski.places.core.mymap.PlacesInFolderContract;
import com.marija.diplomski.places.core.mymap.PlacesInFolderPresenter;

import java.util.List;

public class MyMapTabletPresenter implements FoldersContract.Presenter, PlacesInFolderContract.Presenter {

    private FoldersPresenter foldersPresenter;
    private PlacesInFolderPresenter placesInFolderPresenter;

    public MyMapTabletPresenter(FoldersPresenter foldersPresenter, PlacesInFolderPresenter placesInFolderPresenter) {
        this.foldersPresenter = foldersPresenter;
        this.placesInFolderPresenter = placesInFolderPresenter;
    }

    // FoldersPresenter

    @Override
    public void openMyMap(int folderId) {
        placesInFolderPresenter.setFolderId(folderId);
        placesInFolderPresenter.loadPlaces();
    }

    @Override
    public void loadFolders() {
        foldersPresenter.loadFolders();
    }

    @Override
    public void deleteFolders(List<Integer> folderIds) {
        foldersPresenter.deleteFolders(folderIds);
    }

    @Override
    public void openRenameFolderDialog(int folderId) {
        foldersPresenter.openRenameFolderDialog(folderId);
    }

    @Override
    public void renameFolder(String folder) {
        foldersPresenter.renameFolder(folder);
    }

    @Override
    public void openMyMapForAllPlaces() {
        placesInFolderPresenter.setFolderId(PlacesInFolderPresenter.LOAD_ALL_PLACES);
        placesInFolderPresenter.loadPlaces();
    }

    // PlacesInFolderPresenter

    @Override
    public void loadPlaces() {
        placesInFolderPresenter.loadPlaces();
    }

    @Override
    public void openPlaceDetails() {
        placesInFolderPresenter.openPlaceDetails();
    }
}
