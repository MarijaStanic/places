package com.marija.diplomski.places.core.folders.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.FolderDao;

import java.util.List;

public class DeleteFolder extends UseCase<DeleteFolder.RequestValues, DeleteFolder.ResponseValue> {

    private FolderDao folderDao;

    public DeleteFolder(FolderDao placeDao) {
        this.folderDao = placeDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        boolean areDeleted = folderDao.deleteFolders(requestValues.getFolderIds());
        if (areDeleted) {
            getUseCaseCallback().onSuccess(new ResponseValue());
        } else {
            getUseCaseCallback().onError();
        }

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final List<Integer> folderIds;

        public RequestValues(List<Integer> folderIds) {
            this.folderIds = folderIds;
        }

        public List<Integer> getFolderIds() {
            return folderIds;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
