package com.marija.diplomski.places.addeditplace.views;

import android.os.Bundle;

import com.marija.diplomski.places.addeditplace.AddEditPlaceController;
import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.domain.model.Place;

public class AddEditPlaceActivity extends BaseActivity {

    public static final String EXTRA_EDIT_PLACE_API_ID = "EDIT_PLACE_API_ID";
    public static final String EXTRA_ADD_PLACE = "ADD_PLACE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUpNavigation();

        String placeApiId = getIntent().getStringExtra(EXTRA_EDIT_PLACE_API_ID);
        Place placeToAdd = (Place) getIntent().getSerializableExtra(EXTRA_ADD_PLACE);

        showToolbarTitle(placeToAdd);

        AddEditPlaceController.createAddEditPlaceView(this, placeApiId, placeToAdd);
    }

    private void showToolbarTitle(Place place) {
        if(place != null){
            getToolbar().setTitle(place.getName());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_edit_place;
    }

}
