package com.marija.diplomski.places.keywords.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaceForKeywordViewHolder extends AbstractViewHolder<PlaceViewModel> {

    @BindView(R.id.cv_place_for_keyword)
    CardView cvPlaceForKeyword;
    @BindView(R.id.tv_place_distance)
    TextView tvPlaceDistance;
    @BindView(R.id.tv_place_name)
    TextView tvPlaceName;
    @BindView(R.id.tv_place_vicinity)
    TextView tvPlaceVicinity;

    private PlaceItemListener listener;

    private PlaceViewModel placeViewModel;

    public PlaceForKeywordViewHolder(View view, PlaceItemListener placeItemListener) {
        super(view);
        this.listener = placeItemListener;
    }

    @OnClick(R.id.cv_place_for_keyword)
    public void onPlaceForKeywordCardClick() {
        listener.onPlaceForKeywordClick(placeViewModel.getPlaceId());
    }

    @Override
    public void bind(PlaceViewModel item) {
        placeViewModel = item;
        tvPlaceDistance.setText(placeViewModel.getDistanceToPlace() + "km");
        tvPlaceName.setText(placeViewModel.getName());
        tvPlaceVicinity.setText(placeViewModel.getVicinity());
    }

    public interface PlaceItemListener {
        void onPlaceForKeywordClick(String placeApiId);
        void onShowMorePlacesForKeywordClick();
    }
}

