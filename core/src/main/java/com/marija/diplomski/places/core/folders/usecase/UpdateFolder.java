package com.marija.diplomski.places.core.folders.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.FolderDao;
import com.marija.diplomski.places.core.domain.model.Folder;

public class UpdateFolder extends UseCase<UpdateFolder.RequestValues, UpdateFolder.ResponseValue> {

    private FolderDao folderDao;

    public UpdateFolder(FolderDao placeDao) {
        this.folderDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        boolean isUpdated = folderDao.updateFolder(requestValues.getFolder());
        if (isUpdated) {
            getUseCaseCallback().onSuccess(new ResponseValue());
        } else {
            getUseCaseCallback().onError();
        }

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Folder folder;

        public RequestValues(Folder folder) {
            this.folder = folder;
        }

        public Folder getFolder() {
            return folder;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
