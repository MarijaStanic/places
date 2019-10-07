package com.marija.diplomski.places.core.addeditfolder.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.FolderDao;
import com.marija.diplomski.places.core.domain.model.Folder;

public class CreateFolder extends UseCase<CreateFolder.RequestValues, CreateFolder.ResponseValue> {

    private FolderDao folderDao;

    public CreateFolder(FolderDao folderDao){
        this.folderDao = folderDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Folder folder = folderDao.addFolder(requestValues.getFolder());
        if(folder != null) {
            getUseCaseCallback().onSuccess(new ResponseValue(folder));
        }else{
            getUseCaseCallback().onError();
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private Folder folder;

        public RequestValues(Folder folder){
            this.folder = folder;
        }

        public Folder getFolder() {
            return folder;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

        private Folder folder;

        public ResponseValue(Folder folder){
            this.folder = folder;
        }

        public Folder getFolder() {
            return folder;
        }

    }
}
