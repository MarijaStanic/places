package com.marija.diplomski.places.core.addeditfolder;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.usecase.CreateFolder;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.ArrayList;
import java.util.List;

public class AddEditFolderPresenter implements AddEditFolderContract.Presenter {

    private UseCaseHandler useCaseHandler;
    private AddEditFolderContract.View addEditFolderView;
    private CreateFolder createFolder;
    private GetFolders getFolders;

    private List<Integer> placeFolderIds;
    private List<Folder> folders;

    public AddEditFolderPresenter(UseCaseHandler useCaseHandler, AddEditFolderContract.View addEditFolderView, CreateFolder createFolder, GetFolders getFolders, List<Integer> placeFolderIds) {
        this.useCaseHandler = useCaseHandler;
        this.addEditFolderView = addEditFolderView;
        this.createFolder = createFolder;
        addEditFolderView.setPresenter(this);
        this.getFolders = getFolders;
        this.placeFolderIds = placeFolderIds;
    }

    @Override
    public void loadFolders() {
        useCaseHandler.execute(getFolders, null, new UseCase.UseCaseCallback<GetFolders.ResponseValue>() {
            @Override
            public void onSuccess(GetFolders.ResponseValue response) {
                List<Folder> folders = response.getFolders();
                for (Folder folder : folders) {
                    if (placeFolderIds != null) { // if is tablet
                        for (Integer id : placeFolderIds) {
                            if (folder.getId() == id) {
                                folder.setSelected(true);
                            }
                        }
                    }
                }
                AddEditFolderPresenter.this.folders = folders;
                addEditFolderView.showFoldersList(folders);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void openAddFolder() {
        addEditFolderView.showAddFolderDialog();
    }

    @Override
    public void addFolder(String name) {
        Folder folder = new Folder(name);
        useCaseHandler.execute(createFolder, new CreateFolder.RequestValues(folder), new UseCase.UseCaseCallback<CreateFolder.ResponseValue>() {
            @Override
            public void onSuccess(CreateFolder.ResponseValue response) {
                folders.add(response.getFolder());
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void markClickedFolder(int folderId) {
        for (Folder f : folders) {
            if (f.getId() == folderId) {
                f.setSelected(!f.isSelected());
                return;
            }
        }
    }

    @Override
    public void openAddEditPlace() {
        List<Folder> selectedFolders = getSelectedFolders();
        addEditFolderView.showAddEditPlaceUi(selectedFolders);
    }

    public List<Folder> getSelectedFolders() {
        List<Folder> selectedFolders = new ArrayList<>();
        for (Folder folder : folders) {
            if (folder.isSelected()) {
                selectedFolders.add(folder);
            }
        }
        return selectedFolders;
    }

    public void setPlaceFolderIds(List<Integer> placeFolderIds) { // For tablet
        this.placeFolderIds = placeFolderIds;
    }
}
