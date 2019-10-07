package com.marija.diplomski.places.core.addeditfolder.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.FolderDao;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public class GetFolders extends UseCase<GetFolders.RequestValues, GetFolders.ResponseValue> {

    private FolderDao folderDao;

    public GetFolders(FolderDao folderDao){
        this.folderDao = folderDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Folder> folders = folderDao.getAllFolders();
        for(Folder f: folders)
            System.out.println("oooooooooooo in use case" + f.getName());
        getUseCaseCallback().onSuccess(new ResponseValue(folders));
    }

    public static final class RequestValues implements UseCase.RequestValues{

    }

    public static final class ResponseValue implements UseCase.ResponseValue{

        private final List<Folder> folders;

        public ResponseValue(List<Folder> folders){
            this.folders = folders;
        }

        public List<Folder> getFolders() {
            return folders;
        }
    }
}
