package com.marija.diplomski.places.placedetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;

import com.marija.diplomski.places.core.infrastructure.location.UserLocationManager;
import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditplace.usecase.DeletePlace;
import com.marija.diplomski.places.core.addeditplace.usecase.GetPlace;
import com.marija.diplomski.places.core.data.network.ConnectivityInterceptor;
import com.marija.diplomski.places.core.data.network.PlacesRestApiImpl;
import com.marija.diplomski.places.core.placedetails.PlaceDetailsPresenter;
import com.marija.diplomski.places.core.data.Database;
import com.marija.diplomski.places.core.utils.ImageLoaderUtil;
import com.marija.diplomski.places.core.infrastructure.network.NetworkConnectionCheckerImpl;

import butterknife.BindView;

public class PlaceDetailsActivity extends BaseActivity implements PlaceDetailsFragment.PlacesDetailsFragmentInteractionListener {

    public static final String EXTRA_PLACE_API_ID = "PLACE_API_ID";

    @Nullable
    @BindView(R.id.ivCollapsing)
    ImageView ivCollapsing;
    @Nullable
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUpNavigation();

        String placeApiId = getIntent().getStringExtra(EXTRA_PLACE_API_ID);

        PlaceDetailsFragment placeDetailsFragment = (PlaceDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (placeDetailsFragment == null) {
            placeDetailsFragment = PlaceDetailsFragment.newInstance();
            addFragmentToActivity(placeDetailsFragment, R.id.content);
        }

        new PlaceDetailsPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()), placeApiId,
                placeDetailsFragment, PlacesRestApiImpl.getInstance(new ConnectivityInterceptor(new NetworkConnectionCheckerImpl(this))),
                new PlaceDetailsViewModelMapper(UserLocationManager.getInstance(this, null)), new GetPlace(Database.placeDao), new DeletePlace(Database.placeDao));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_details;
    }

    @Override
    public void onSetCollapsingToolbarTitle(String title) {
      /*  toolbar.setTitle(title);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));*/
    }

    @Override
    public void onSetCollapsingImage(String imageUrl) {
        ImageLoaderUtil.loadImage(this, imageUrl, ivCollapsing);
    }
}
