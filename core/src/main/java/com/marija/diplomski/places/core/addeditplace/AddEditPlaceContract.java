package com.marija.diplomski.places.core.addeditplace;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public interface AddEditPlaceContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showPlaceDetails();

        void showDescription(String description);

        void showFolders(List<Folder> folders);

        void showFoldersUi(List<Integer> placeFolderIds);
    }

    interface Presenter {

        void loadPlace();

        void savePlace(String description, List<Folder> folders);

        void openFolders();
    }
}
