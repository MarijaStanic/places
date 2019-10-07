package com.marija.diplomski.places.keywords.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.base.adapters.SortedAdapter;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;

import java.util.Comparator;
import java.util.List;

public class PlacesForKeywordAdapter extends SortedAdapter<PlaceViewModel> {

    private PlaceForKeywordViewHolder.PlaceItemListener placeItemListener;

    public PlacesForKeywordAdapter(Comparator<PlaceViewModel> comparator, PlaceForKeywordViewHolder.PlaceItemListener placeItemListener) {
        super(PlaceViewModel.class, comparator);
        this.placeItemListener = placeItemListener;
    }

    @Override
    protected AbstractViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_place_for_keyword, parent, false);
        return new PlaceForKeywordViewHolder(view, placeItemListener);
    }

    @Override
    protected AbstractViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_footer, parent, false);
        return new PlaceForKeywordFooterViewHolder(view, placeItemListener);
    }

    public void refreshEvents(List<PlaceViewModel> places, String nextPageToken, boolean forceUpdate) {
        if(forceUpdate)
            resetList();
        hideOrShowFooter(nextPageToken);
        addAll(places);
        notifyDataSetChanged();
    }

    private void resetList() {
        removeAll();
        setHasFooter(false);
    }

    private void hideOrShowFooter(String nextPageToken) {
        if(nextPageToken == null){
            setHasFooter(false);
        }else {
            setHasFooter(true);
        }
    }
}