package com.marija.diplomski.places.keywords.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.keywords.PlacesMapContract;
import com.marija.diplomski.places.core.infrastructure.network.exception.ExceptionFactory;
import com.marija.diplomski.places.placedetails.PlaceDetailsActivity;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;
import com.marija.diplomski.places.core.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PlacesMapFragment extends BaseFragment implements PlacesMapContract.View, OnMapReadyCallback {

    private static final int REQUEST_PLACE_AUTOCOMPLETE = 1;
    private static final String KEY_ICON = "KEY_ICON";
    private static final String KEY_PLACES = "KEY_PLACES";

    private PlacesMapContract.Presenter presenter;

    private View customMarkerView;
    private ImageView ivMarkerIcon;

    private Bitmap keywordIconBitmap;
    private BitmapDescriptor markerIcon;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private List<PlaceViewModel> places;

    private String keywordIcon;

    private int mapType;

    private OnPlacesMapFragmentListener listener; // The listener to notify when places are loaded

    public PlacesMapFragment() {
        places = new ArrayList<>();
    }

    public static PlacesMapFragment newInstance() {
        return new PlacesMapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlacesMapFragmentListener) {
            listener = (OnPlacesMapFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlacesMapFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            keywordIconBitmap = savedInstanceState.getParcelable(KEY_ICON);
            places = savedInstanceState.getParcelableArrayList(KEY_PLACES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places_map, container, false);

        customMarkerView = inflater.inflate(R.layout.marker_custom, container, false);
        ivMarkerIcon = ButterKnife.findById(customMarkerView, R.id.marker_image);

        showMap();
        setupSearchButton();

        return root;
    }

    private void setupSearchButton() {
        FloatingActionButton fabSearchPlace =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_search_place);
        fabSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.openSearchPlace();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        showMap();
    }

    private void showMap() {
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, mapFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_ICON, keywordIconBitmap);
        outState.putParcelableArrayList(KEY_PLACES, (ArrayList<PlaceViewModel>) places);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        removeMap();
        super.onStop();
    }

    private void removeMap() {
        getChildFragmentManager().beginTransaction()
                .remove(mapFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PLACE_AUTOCOMPLETE:
                if (resultCode == getActivity().RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    presenter.openPlaceDetails(place.getId());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    //   Log.i(TAG, status.getStatusMessage());
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setMapType(mapType);
        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                presenter.openPlaceDetails(marker.getTag().toString());
            }
        });

        showMarkers();
    }

    public void onKeywordSelected(Keyword keyword) {
        presenter.loadPlaces(keyword);
        keywordIcon = keyword.getIcon();
    }

    public void showMorePlaces(String nextPageToken) {
        presenter.loadMorePlaces(nextPageToken);
    }

    @Override
    public void setPresenter(PlacesMapContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void clearMap() {
        if (googleMap != null) {
            googleMap.clear();
        }
        places.clear();
    }

    @Override
    public void showLoadingPlacesError(Exception exception) {
        showMessage(ExceptionFactory.create(getContext(), exception));
    }

    @Override
    public void showPlaces(List<Object> places, String nextPageToken, boolean forceUpdate) {
        List<PlaceViewModel> placeViewModels = (List<PlaceViewModel>) (List) places;
        listener.onUpdatePlacesList(placeViewModels, nextPageToken, forceUpdate);

        if (isVisible()) { // not visible if @PlacesListFragment calls this method
            ivMarkerIcon.setImageResource(getResources()
                    .getIdentifier(keywordIcon, "drawable", getActivity()
                            .getPackageName()));
            keywordIconBitmap = ImageLoaderUtil.getBitmapFromView(customMarkerView);
        }
        this.places.addAll(placeViewModels);
        showMarkers();
    }

    @Override
    public void showMarkers() {
        if (googleMap != null) {
            markerIcon = BitmapDescriptorFactory.fromBitmap(keywordIconBitmap);
            for (PlaceViewModel place : places) {
                addMarker(place);
            }
        }
    }

    private void addMarker(PlaceViewModel result) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(result.getLatitude(), result.getLongitude());
        markerOptions.position(latLng)
                .title(result.getName())
                .snippet(result.getVicinity())
                .icon(markerIcon);

        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(result.getPlaceId());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    }

    @Override
    public void showNoPlacesMessage() {
        showMessage(getString(R.string.message_no_places));
    }

    @Override
    public void showPlaceDetailsUi(String placeApiId) {
        Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        intent.putExtra(PlaceDetailsActivity.EXTRA_PLACE_API_ID, placeApiId);
        startActivity(intent);
    }

    @Override
    public void showSearchPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void setMapType(String mapType) {
        if (mapType.equals("MAP_TYPE_HYBRID")) {
            this.mapType = GoogleMap.MAP_TYPE_HYBRID;
        } else if (mapType.equals("MAP_TYPE_TERRAIN")) {
            this.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        } else {
            this.mapType = GoogleMap.MAP_TYPE_NORMAL;
        }
        if (googleMap != null) {
            googleMap.setMapType(this.mapType);
        }
    }

    public interface OnPlacesMapFragmentListener {
        void onUpdatePlacesList(List<PlaceViewModel> places, String nextPageToken, boolean forceUpdate);
    }

}
