package com.marija.diplomski.places.core.domain.data;

public interface PlaceFolderDao {

    boolean addPlaceFolder(int placeId, int folderId);
    boolean deletePlaceFoldersForPlace(int id);

}
