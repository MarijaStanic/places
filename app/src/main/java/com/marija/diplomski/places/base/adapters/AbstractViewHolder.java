package com.marija.diplomski.places.base.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class AbstractViewHolder<T> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public abstract void bind(T item);
}
