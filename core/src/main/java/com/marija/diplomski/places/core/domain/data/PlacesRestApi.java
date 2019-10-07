package com.marija.diplomski.places.core.domain.data;

import com.marija.diplomski.places.core.domain.model.api.placedetails.PlaceDetailsServerResponse;
import com.marija.diplomski.places.core.domain.model.api.place.PlacesServerResponse;
import com.marija.diplomski.places.core.domain.model.Keyword;

import java.io.IOException;

import retrofit2.Callback;

public interface PlacesRestApi {

    int PROXIMITY_RADIUS = 5000;
    String API_BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    String STATUS_OK = "OK";
    String STATUS_ZERO_RESULTS = "ZERO_RESULTS";

    void requestPlaces(Keyword keyword, double latitude, double longitude, Callback<PlacesServerResponse> callback);

    void requestPlaceDetails(String placeId, Callback<PlaceDetailsServerResponse> callback);

    void requestPlacesForPageToken(String placeToken, Callback<PlacesServerResponse> callback);

    String requestPhotoUrl(String photoReference, String maxHeight);

    PlacesServerResponse requestPlaces(Keyword keyword, double latitude, double longitude);

    PlaceDetailsServerResponse requestPlaceDetails(String placeId) throws IOException;
}
