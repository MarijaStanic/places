package com.marija.diplomski.places.keywords.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.keywords.PlacesListContract;
import com.marija.diplomski.places.placedetails.PlaceDetailsActivity;
import com.marija.diplomski.places.keywords.adapter.PlaceForKeywordViewHolder;
import com.marija.diplomski.places.keywords.adapter.PlacesForKeywordAdapter;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesListFragment extends BaseFragment implements PlacesListContract.View {

    public static final String KEY_NEXT_PAGE_TOKEN = "KEY_NEXT_PAGE_TOKEN";
    public static final String KEY_PLACES = "KEY_PLACES";

    private OnPlacesListFragmentListener listener; // The listener we are to notify to show more places

    private PlacesForKeywordAdapter placesForKeywordAdapter;

    @BindView(R.id.rv_places)
    RecyclerView rvPlaces;

    private PlacesListContract.Presenter presenter;

    private String nextPageToken;
    private List<PlaceViewModel> placesForKeyword;

    public PlacesListFragment() {
    }

    public static PlacesListFragment newInstance() {
        PlacesListFragment fragment = new PlacesListFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlacesListFragmentListener) {
            listener = (OnPlacesListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlacesListFragmentListener");
        }
    }

    private static final Comparator<PlaceViewModel> ALPHABETICAL_COMPARATOR = new Comparator<PlaceViewModel>() {
        @Override
        public int compare(PlaceViewModel o1, PlaceViewModel o2) {
            return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesForKeywordAdapter = new PlacesForKeywordAdapter(ALPHABETICAL_COMPARATOR, placeItemListener);
        updateValuesFromBundle(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String nextPageToken = savedInstanceState.getString(KEY_NEXT_PAGE_TOKEN);
            List<PlaceViewModel> places = savedInstanceState.getParcelableArrayList(KEY_PLACES);
            if (placesForKeyword == null && places != null) { // Can be populated if @refreshEvents is called on orientation change in phone mode
                refreshEvents(places, nextPageToken, true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places_list, container, false);
        ButterKnife.bind(this, root);

        setupRecyclerView();

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NEXT_PAGE_TOKEN, nextPageToken);
        outState.putParcelableArrayList(KEY_PLACES, (ArrayList<PlaceViewModel>) placesForKeyword);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void setupRecyclerView() {
        rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPlaces.setAdapter(placesForKeywordAdapter);
    }

    private PlaceForKeywordViewHolder.PlaceItemListener placeItemListener = new PlaceForKeywordViewHolder.PlaceItemListener() {
        @Override
        public void onPlaceForKeywordClick(String placeApiId) {
            if (listener != null) {
                presenter.openPlaceDetails(placeApiId);
            }
        }

        @Override
        public void onShowMorePlacesForKeywordClick() {
            if (listener != null) {
                listener.onShowMorePlaces(nextPageToken);
            }
        }
    };

    public void refreshEvents(List<PlaceViewModel> places, String nextPageToken, boolean forceUpdate) {
        this.placesForKeyword = places;
        this.nextPageToken = nextPageToken;
        placesForKeywordAdapter.refreshEvents(places, nextPageToken, forceUpdate);
    }

    @Override
    public void setPresenter(PlacesListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPlaceDetailsUi(String placeId) {
        Intent intent = new Intent(getContext(), PlaceDetailsActivity.class);
        intent.putExtra(PlaceDetailsActivity.EXTRA_PLACE_API_ID, placeId);
        startActivity(intent);
    }

    public interface OnPlacesListFragmentListener {
        void onShowMorePlaces(String nextPageToken);
    }

}