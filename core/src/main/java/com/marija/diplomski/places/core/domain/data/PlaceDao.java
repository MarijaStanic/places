package com.marija.diplomski.places.core.domain.data;

import com.marija.diplomski.places.core.domain.model.Place;

import java.util.List;

public interface PlaceDao {

    List<Place> getAllPlaces();
    boolean addPlace(Place place);
    Place getPlace(String id);
    boolean updatePlace(Place place);
    boolean deletePlace(String id);
    List<Place> getPlacesForFolder(int folderId);
}
