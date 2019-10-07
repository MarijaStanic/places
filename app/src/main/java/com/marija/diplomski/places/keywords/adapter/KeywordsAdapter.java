package com.marija.diplomski.places.keywords.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.base.adapters.SortedAdapter;

import java.util.Comparator;

public class KeywordsAdapter extends SortedAdapter<Keyword> {

    private KeywordContentViewHolder.KeywordItemListener itemListener;

    public KeywordsAdapter(Comparator<Keyword> comparator, KeywordContentViewHolder.KeywordItemListener itemListener) {
        super(Keyword.class, comparator);
        this.itemListener = itemListener;
    }

    @Override
    protected AbstractViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_keyword_header, parent, false);
        return new KeywordHeaderViewHolder(view, itemListener);
    }

    @Override
    protected AbstractViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_keyword, null);
        return new KeywordContentViewHolder(view, itemListener);
    }

}
