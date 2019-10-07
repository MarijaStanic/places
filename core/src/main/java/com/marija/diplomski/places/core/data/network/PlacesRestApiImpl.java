package com.marija.diplomski.places.core.data.network;

import com.marija.diplomski.places.core.domain.data.PlacesRestApi;
import com.marija.diplomski.places.core.domain.model.api.placedetails.PlaceDetailsServerResponse;
import com.marija.diplomski.places.core.domain.model.api.place.PlacesServerResponse;
import com.marija.diplomski.places.core.domain.model.Keyword;

import java.io.IOException;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;

public class PlacesRestApiImpl implements PlacesRestApi {

    public static PlacesRestApiImpl INSTANCE;

    private final RetrofitClient.PlacesService placesService;

    public static PlacesRestApiImpl getInstance(Interceptor interceptor) {
        if (INSTANCE == null) {
            INSTANCE = new PlacesRestApiImpl(interceptor);
        }
        return INSTANCE;
    }

    private PlacesRestApiImpl(Interceptor interceptor) {
        placesService = RetrofitClient.makePlacesService(interceptor);
    }

    public void requestPlaces(Keyword keyword, double latitude, double longitude, Callback<PlacesServerResponse> callback) {
        Call<PlacesServerResponse> call;
        if (keyword.getType().equals("")) {
            call = placesService.getNearbyPlaces(keyword.getTitle(), "", latitude + "," + longitude, PROXIMITY_RADIUS);
        } else {
            call = placesService.getNearbyPlaces("", keyword.getType(), latitude + "," + longitude, PROXIMITY_RADIUS);
        }
        call.enqueue(callback);
    }

    public PlacesServerResponse requestPlaces(Keyword keyword, double latitude, double longitude) {
        Call<PlacesServerResponse> call;
        if (keyword.getType().equals("")) {
            call = placesService.getNearbyPlaces(keyword.getTitle(), "", latitude + "," + longitude, PROXIMITY_RADIUS);
        } else {
            call = placesService.getNearbyPlaces("", keyword.getType(), latitude + "," + longitude, PROXIMITY_RADIUS);
        }
       try{
           return call.execute().body();
       }catch (Exception ex) {
           return null;
       }
    }

    public void requestPlaceDetails(String placeId, Callback<PlaceDetailsServerResponse> callback) {
        Call<PlaceDetailsServerResponse> call = placesService.getPlaceDetails(placeId);
        call.enqueue(callback);
    }

    public PlaceDetailsServerResponse requestPlaceDetails(String placeId) throws IOException {
        Call<PlaceDetailsServerResponse> call = placesService.getPlaceDetails(placeId);
        return call.execute().body();
    }

    public void requestPlacesForPageToken(String placeToken, Callback<PlacesServerResponse> callback) {
        Call<PlacesServerResponse> call = placesService.getPlacesForPageToken(placeToken);
        call.enqueue(callback);
    }

    public String requestPhotoUrl(String photoReference, String maxHeight) {
        StringBuilder requestPhotoUrl = new StringBuilder(API_BASE_URL);
        requestPhotoUrl.append("photo?key=AIzaSyA1AfHp4jDiJHpR5OVnjS59pip-b_HY9_U")
                .append("&photoreference=").append(photoReference).append("&maxheight=").append(maxHeight);
        return requestPhotoUrl.toString();
    }
}
