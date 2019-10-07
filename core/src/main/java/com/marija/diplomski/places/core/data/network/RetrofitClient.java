package com.marija.diplomski.places.core.data.network;

import com.marija.diplomski.places.core.domain.model.api.placedetails.PlaceDetailsServerResponse;
import com.marija.diplomski.places.core.domain.model.api.place.PlacesServerResponse;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitClient {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    static PlacesService makePlacesService(Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(PlacesService.class);
    }


    interface PlacesService {

        String API_URL_KEY = "key=AIzaSyA1AfHp4jDiJHpR5OVnjS59pip-b_HY9_U";
        String API_GET_PLACES = "nearbysearch/json?" + API_URL_KEY;
        String API_GET_PLACES_FOR_PAGE_TOKEN = "nearbysearch/json?" + API_URL_KEY;
        String API_GET_PLACE_DETAILS = "details/json?" + API_URL_KEY;

        String KEYWORD = "keyword";
        String TYPE = "type";
        String LOCATION ="location";
        String RADIUS = "radius";
        String PAGE_TOKEN = "pagetoken";
        String PLACE_ID = "placeid";

        @GET(API_GET_PLACES)
        Call<PlacesServerResponse> getNearbyPlaces(@Query(KEYWORD) String keyword,
                                                   @Query(TYPE) String type,
                                                   @Query(LOCATION) String location,
                                                   @Query(RADIUS) int radius);

        @GET(API_GET_PLACES_FOR_PAGE_TOKEN)
        Call<PlacesServerResponse> getPlacesForPageToken(@Query(PAGE_TOKEN) String pageToken);

        @GET(API_GET_PLACE_DETAILS)
        Call<PlaceDetailsServerResponse> getPlaceDetails(@Query(PLACE_ID) String placeId);

    }
}
