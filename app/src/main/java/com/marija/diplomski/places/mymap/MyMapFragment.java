package com.marija.diplomski.places.mymap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.marija.diplomski.places.placedetails.PlaceDetailsActivity;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.mymap.PlacesInFolderContract;
import com.marija.diplomski.places.keywords.adapter.PlacesInFolderAdapter;
import com.marija.diplomski.places.core.utils.ImageLoaderUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        PlacesInFolderContract.View, PlacesInFolderAdapter.CardItemListener {

    @BindView(R.id.rv_places_in_folder)
    RecyclerView rvPlacesInFolder;
    @BindDrawable(R.drawable.ic_flag)
    Drawable mapIconErrorDrawable;

    private GoogleMap googleMap;

    private PlacesInFolderAdapter placesInFolderAdapter;

    private List<Place> placesInFolder;
    private SparseArray<Marker> markers;

    private View customMarkerView;
    private ImageView ivMarkerIcon;

    private List<Target> targets;

    private int positionOfClickedCard = -1;

    private PlacesInFolderContract.Presenter presenter;

    private SupportMapFragment mapFragment;

    public MyMapFragment() {
        placesInFolderAdapter = new PlacesInFolderAdapter();
        markers = new SparseArray<>();
        targets = new ArrayList<>();
    }

    public static MyMapFragment newInstance() {
        return new MyMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_map, container, false);
        ButterKnife.bind(this, root);

        showMap();

        setupRecyclerView();

        customMarkerView = inflater.inflate(R.layout.marker_custom, container, false);
        ivMarkerIcon = ButterKnife.findById(customMarkerView, R.id.marker_image);

        return root;
    }

    private void showMap() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment).commit();
        }
    }

    private void setupRecyclerView() {
        rvPlacesInFolder.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvPlacesInFolder.setAdapter(placesInFolderAdapter);
        placesInFolderAdapter.setOnCardClickListener(this);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvPlacesInFolder);
        rvPlacesInFolder.setOnFlingListener(snapHelper);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void setPresenter(PlacesInFolderContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPlacesList(List<Place> places) {
        this.placesInFolder = places;
        placesInFolderAdapter.setList(places);
    }

    @Override
    public void clearPlacesList(List<Place> places) {
        placesInFolderAdapter.clearPlacesList();
    }

    @Override
    public void showMarkers(List<Place> places) {
        resetUiState();
        for (Place place : places) {
            addMarker(place);
        }
    }

    private void resetUiState() {
        targets.clear();
        markers.clear();
        this.positionOfClickedCard = -1;
    }

    private void addMarker(final Place place) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable loadedDrawable = new BitmapDrawable(getResources(), bitmap);
                showMarker(loadedDrawable);
            }

            private void showMarker(Drawable loadedDrawable) {
                BitmapDescriptor markerIcon = getBitmapDescriptorMarkerIcon(loadedDrawable);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                markerOptions.position(latLng)
                        .title(place.getName())
                        .icon(markerIcon);
                Marker marker = googleMap.addMarker(markerOptions);
                int targetIndex = targets.indexOf(this);
                markers.put(targetIndex, marker);
                zoomCamera(latLng, 12);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                showMarker(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        targets.add(target);

        Picasso.with(getContext()).load(place.getIcon()).error(mapIconErrorDrawable).into(target);
    }

    @NonNull
    private BitmapDescriptor getBitmapDescriptorMarkerIcon(Drawable loadedDrawable) {
        ivMarkerIcon.setImageDrawable(loadedDrawable);
        Bitmap placeIconBitmap = ImageLoaderUtil.getBitmapFromView(customMarkerView);
        return BitmapDescriptorFactory.fromBitmap(placeIconBitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int indexOfMarker = getMarkerIndex(marker);
        rvPlacesInFolder.smoothScrollToPosition(indexOfMarker);
        return false;
    }

    private int getMarkerIndex(Marker marker) {
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).getPosition().equals(marker.getPosition())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCardClick(int position) {
        if (isPlaceCardPreviouslyClicked(position)) {
            presenter.openPlaceDetails();
            return;
        }
        this.positionOfClickedCard = position;
        focusMarkerForPlaceCard();
        focusPlaceCard();
    }

    private boolean isPlaceCardPreviouslyClicked(int position) {
        return this.positionOfClickedCard == position;
    }

    private void focusMarkerForPlaceCard() {
        Marker marker = markers.get(positionOfClickedCard);
        zoomCamera(marker.getPosition(), 17);
        marker.showInfoWindow();
    }

    private void zoomCamera(LatLng latLng, int zoomLevel) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    private void focusPlaceCard() {
        rvPlacesInFolder.smoothScrollToPosition(positionOfClickedCard);
    }

    @Override
    public void showPlaceDetailsUi() {
        Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        String placeApiId = placesInFolder.get(positionOfClickedCard).getPlaceId();
        intent.putExtra(PlaceDetailsActivity.EXTRA_PLACE_API_ID, placeApiId);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        showMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadPlaces();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeMap();
    }

    private void removeMap() {
        getChildFragmentManager().beginTransaction()
                .remove(mapFragment)
                .commitAllowingStateLoss();
        mapFragment = null;
    }

    @Override
    public void showNoPlacesInFolderMessage() {
        showMessage(getString(R.string.message_error_no_places));
    }
}