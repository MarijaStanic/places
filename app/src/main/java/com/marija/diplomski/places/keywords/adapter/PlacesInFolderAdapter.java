package com.marija.diplomski.places.keywords.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlacesInFolderAdapter extends RecyclerView.Adapter<PlacesInFolderAdapter.PlaceInFolderViewHolder> {

    private List<Place> places = new ArrayList<>();

    private CardItemListener cardItemListener;

    public PlacesInFolderAdapter() {

    }

    @Override
    public PlaceInFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_place_in_folder, parent, false);
        return new PlaceInFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceInFolderViewHolder holder, int position) {
        holder.bind(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setList(List<Place> places) {
        clearPlacesList();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    public void clearPlacesList() {
        this.places.clear();
        notifyDataSetChanged();
    }

    class PlaceInFolderViewHolder extends AbstractViewHolder<Place> {

        @BindView(R.id.iv_place)
        ImageView ivPlace;
        @BindView(R.id.txt_place_name)
        TextView txtPlaceName;

        public PlaceInFolderViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardItemListener.onCardClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void bind(Place item) {
            ImageLoaderUtil.loadImage(ivPlace.getContext(), item.getPhoto(), ivPlace, R.color.colorPrimary);
            txtPlaceName.setText(item.getName());
        }

    }

    public interface CardItemListener {
        void onCardClick(int position);
    }

    public void setOnCardClickListener(CardItemListener cardItemListener) {
        this.cardItemListener = cardItemListener;
    }
}