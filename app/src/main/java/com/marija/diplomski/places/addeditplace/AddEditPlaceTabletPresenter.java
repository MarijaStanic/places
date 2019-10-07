package com.marija.diplomski.places.addeditplace;

import com.marija.diplomski.places.core.addeditfolder.AddEditFolderContract;
import com.marija.diplomski.places.core.addeditfolder.AddEditFolderPresenter;
import com.marija.diplomski.places.core.addeditplace.AddEditPlaceContract;
import com.marija.diplomski.places.core.addeditplace.AddEditPlacePresenter;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.domain.model.Place;

import java.util.ArrayList;
import java.util.List;

public class AddEditPlaceTabletPresenter implements AddEditPlaceContract.Presenter,
        AddEditFolderContract.Presenter,
        AddEditPlacePresenter.TabletListener {

    private AddEditPlacePresenter addEditPlacePresenter;
    private AddEditFolderPresenter addEditFolderPresenter;

    public AddEditPlaceTabletPresenter(AddEditPlacePresenter addEditPlacePresenter,
                                       AddEditFolderPresenter addEditFolderPresenter) {
        this.addEditPlacePresenter = addEditPlacePresenter;
        this.addEditFolderPresenter = addEditFolderPresenter;
        addEditPlacePresenter.setTabletListener(this);
    }

    // AddEditPlacePresenter

    @Override
    public void loadPlace() {
        addEditPlacePresenter.loadPlace();
    }

    @Override
    public void savePlace(String description, List<Folder> folders) {
        addEditPlacePresenter.savePlace(description, addEditFolderPresenter.getSelectedFolders());
        // listener used to set selected folders from load
    }

    @Override
    public void openFolders() {

    }

    // AddEditFolderPresenter

    @Override
    public void addFolder(String name) {
        addEditFolderPresenter.addFolder(name);
    }

    @Override
    public void openAddFolder() {
        addEditFolderPresenter.openAddFolder();
    }

    @Override
    public void markClickedFolder(int folderId) {
        addEditFolderPresenter.markClickedFolder(folderId);
    }

    @Override
    public void loadFolders() {

    }

    @Override
    public void openAddEditPlace() {

    }

    @Override
    public void onLoadPlaceResult(Place place) {
        if (place != null) {
            List<Folder> folders = place.getFolders();
            List<Integer> placeFolderIds = new ArrayList<>();
            for (Folder f : folders) {
                placeFolderIds.add(f.getId());
            }
            addEditFolderPresenter.setPlaceFolderIds(placeFolderIds);
        }
        addEditFolderPresenter.loadFolders();
    }

}
