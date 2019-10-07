package com.marija.diplomski.places.core.folders;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.folders.usecase.DeleteFolder;
import com.marija.diplomski.places.core.folders.usecase.UpdateFolder;

import java.util.List;

public class FoldersPresenter implements FoldersContract.Presenter {

    private UseCaseHandler useCaseHandler;
    private FoldersContract.View foldersView;
    private GetFolders getFolders;
    private UpdateFolder updateFolder;
    private DeleteFolder deleteFolder;

    private int folderId;

    public FoldersPresenter(UseCaseHandler useCaseHandler, FoldersContract.View foldersView, GetFolders getFolders, UpdateFolder updateFolder, DeleteFolder deleteFolder) {
        this.useCaseHandler = useCaseHandler;
        this.foldersView = foldersView;
        this.getFolders = getFolders;
        this.updateFolder = updateFolder;
        this.deleteFolder = deleteFolder;
        foldersView.setPresenter(this);
    }

    @Override
    public void loadFolders() {
        useCaseHandler.execute(getFolders, null, new UseCase.UseCaseCallback<GetFolders.ResponseValue>() {
            @Override
            public void onSuccess(GetFolders.ResponseValue response) {
                List<Folder> folders = response.getFolders();
                foldersView.showFoldersList(folders);
            }


            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void deleteFolders(final List<Integer> folderIds) {
        useCaseHandler.execute(deleteFolder, new DeleteFolder.RequestValues(folderIds), new UseCase.UseCaseCallback<DeleteFolder.ResponseValue>() {
            @Override
            public void onSuccess(DeleteFolder.ResponseValue response) {
                loadFolders();
            }

            @Override
            public void onError() {
            }
        });
    }

    @Override
    public void openRenameFolderDialog(int folderId) {
        this.folderId = folderId;
        foldersView.showRenameFolderDialog();
    }

    @Override
    public void renameFolder(String folderName) {
        useCaseHandler.execute(updateFolder, new UpdateFolder.RequestValues(new Folder(folderId, folderName)), new UseCase.UseCaseCallback<UpdateFolder.ResponseValue>() {
            @Override
            public void onSuccess(UpdateFolder.ResponseValue response) {
                loadFolders();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void openMyMap(int folderId) {
        foldersView.showMyMapUiForFolder(folderId);
    }

    @Override
    public void openMyMapForAllPlaces() {
        foldersView.showMyMapUiForAllPlaces();
    }
}
