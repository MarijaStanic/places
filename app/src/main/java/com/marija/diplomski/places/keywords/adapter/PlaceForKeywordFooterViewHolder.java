package com.marija.diplomski.places.keywords.adapter;

import android.view.View;
import android.widget.TextView;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.adapters.AbstractViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

class PlaceForKeywordFooterViewHolder extends AbstractViewHolder {

    @BindView(R.id.txt_showMore)
    TextView txtShowMore;

    private PlaceForKeywordViewHolder.PlaceItemListener placeItemListener;

    public PlaceForKeywordFooterViewHolder(View view, PlaceForKeywordViewHolder.PlaceItemListener placeItemListener) {
        super(view);
        this.placeItemListener = placeItemListener;
    }

    @OnClick(R.id.txt_showMore)
    public void onShowMorePlacesForKeyword() {
        placeItemListener.onShowMorePlacesForKeywordClick();
    }

    @Override
    public void bind(Object item) {

    }
}
