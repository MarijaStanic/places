package com.marija.diplomski.places.core.folders;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public interface FoldersContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showFoldersList(List<Folder> folders);

        void showMyMapUiForFolder(int folderId);

        void showRenameFolderDialog();

        void showMyMapUiForAllPlaces();
    }

    interface Presenter {

        void openMyMap(int folderId);

        void loadFolders();

        void deleteFolders(List<Integer> folderIds);

        void openRenameFolderDialog(int folderId);

        void renameFolder(String folder);

        void openMyMapForAllPlaces();
    }
}
