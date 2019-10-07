package com.marija.diplomski.places.core.addeditfolder;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public interface AddEditFolderContract {

    interface View extends UiContracts.BaseView<Presenter> {
        void showAddFolderDialog();

        void showFoldersList(List<Folder> folders);

        void showAddEditPlaceUi(List<Folder> folders);
    }

    interface Presenter {
        void loadFolders();

        void openAddFolder();

        void addFolder(String name);

        void markClickedFolder(int folderId);

        void openAddEditPlace();
    }
}
