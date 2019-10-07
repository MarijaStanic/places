package com.marija.diplomski.places.placestreetview;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.views.BaseActivity;

public class PlaceStreetViewActivity extends BaseActivity {

    public static final String EXTRA_PLACE_LAT_LNG = "PLACE_LAT_LNG";

    SupportStreetViewPanoramaFragment streetViewPanoramaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LatLng placeLatLng = getIntent().getExtras().getParcelable(EXTRA_PLACE_LAT_LNG);

        setupUpNavigation();

        streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);

        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                            @Override
                            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                                if (streetViewPanoramaLocation == null) {
                                    // location not available
                                    showNoStreetViewAvailableMessage();
                                }
                            }
                        });
                        panorama.setPosition(placeLatLng);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showNoStreetViewAvailableMessage() {
        Snackbar.make(streetViewPanoramaFragment.getView(),
                R.string.message_error_no_street_view, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_street_view;
    }
}
