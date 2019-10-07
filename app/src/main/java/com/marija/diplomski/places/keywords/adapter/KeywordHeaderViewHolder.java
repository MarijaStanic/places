package com.marija.diplomski.places.keywords.adapter;

import android.support.v7.widget.CardView;
import android.view.View;

import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.R;

import butterknife.BindView;
import butterknife.OnClick;

public class KeywordHeaderViewHolder extends AbstractViewHolder<String> {

    @BindView(R.id.cv_my_map) CardView cvMyMap;

    private KeywordContentViewHolder.KeywordItemListener itemListener;

    public KeywordHeaderViewHolder(View view, KeywordContentViewHolder.KeywordItemListener itemListener) {
        super(view);
        this.itemListener = itemListener;
    }

    @Override
    public void bind(String item) {

    }

    @OnClick(R.id.cv_my_map)
    public void onMyMapCardClick() {
        itemListener.onMyMapClick();
    }
}


