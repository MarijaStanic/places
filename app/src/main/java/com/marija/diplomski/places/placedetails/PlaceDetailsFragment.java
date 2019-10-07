package com.marija.diplomski.places.placedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.addeditplace.views.AddEditPlaceActivity;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.placedetails.PlaceDetailsContract;
import com.marija.diplomski.places.core.infrastructure.network.exception.ExceptionFactory;
import com.marija.diplomski.places.placestreetview.PlaceStreetViewActivity;
import com.marija.diplomski.places.core.utils.ActivityUtil;
import com.marija.diplomski.places.core.utils.ImageLoaderUtil;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceDetailsFragment extends BaseFragment implements PlaceDetailsContract.View {

    public static final int REQUEST_ADD_PLACE = 1;
    public static final int REQUEST_EDIT_PLACE = 2;

    private PlaceDetailsContract.Presenter presenter;

    @Nullable
    @BindView(R.id.iv_place)
    ImageView ivPlace;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.btn_directions)
    Button btnDirections;
    @BindView(R.id.btn_website)
    Button btnWebsite;
    @BindView(R.id.btn_phone_number)
    Button btnPhoneNumber;
    @BindView(R.id.btn_add_to_map)
    Button btnAddToMap;
    @BindView(R.id.btn_edit_place)
    Button btnEditPlace;
    @BindView(R.id.btn_remove_from_map)
    Button btnRemoveFromMap;

    @BindView(R.id.table)
    TableLayout table;
    @BindView(R.id.ll_edit_remove_place)
    LinearLayout llEditRemovePlace;

    private PlacesDetailsFragmentInteractionListener listener;

    private PlaceDetailsViewModel viewModel;

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }

    public static PlaceDetailsFragment newInstance() {
        return new PlaceDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_place_details, container, false);
        ButterKnife.bind(this, root);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return root;
    }

    @OnClick(R.id.btn_edit_place)
    public void onEditPlaceClick(View v) {
        presenter.editPlace();
    }

    @OnClick(R.id.btn_add_to_map)
    public void onAddToMapClick(View v) {
        presenter.addNewPlace();
    }

    @OnClick(R.id.btn_remove_from_map)
    public void onRemoveFromMapClick(View v) {
        presenter.deletePlace();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlacesDetailsFragmentInteractionListener) {
            listener = (PlacesDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.openPlace();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void setPresenter(PlaceDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEditRemovePlaceButtons() {
        llEditRemovePlace.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddToMapButton() {
        btnAddToMap.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPlaceDetails(PlaceDetailsViewModel viewModel, String imageUrl) {
        this.viewModel = viewModel;
        showWebSite();
        showPhoneNumber();
        showAddress();
        if (imageUrl != null) {
            if (ActivityUtil.isTablet(getContext()))
                ImageLoaderUtil.loadImage(getContext(), imageUrl, ivPlace);
            else
                listener.onSetCollapsingImage(imageUrl);
        }
        //  listener.onSetCollapsingToolbarTitle(viewModel.getName());
        showWorkingHoursTable();
    }

    private void showWebSite() {
        if (viewModel.getWebsite() != null) {
            btnWebsite.setVisibility(View.VISIBLE);
        }
    }

    private void showPhoneNumber() {
        if (viewModel.getPhoneNumber() != null) {
            btnPhoneNumber.setText(viewModel.getPhoneNumber());
            btnPhoneNumber.setVisibility(View.VISIBLE);
        }
    }

    private void showAddress() {
        txtAddress.setText(viewModel.getAddress());
    }

    @OnClick(R.id.btn_website)
    public void onWebsiteButtonClick(View v) {
        presenter.openWebPage(viewModel.getWebsite());
    }

    @OnClick(R.id.btn_phone_number)
    public void onPhoneNumberButtonClick(View v) {
        presenter.openPhoneDialer(viewModel.getPhoneNumber());
    }

    @OnClick(R.id.btn_directions)
    public void onDirectionsButtonClick(View v) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://www.google.com/maps/dir/?api=1");
        googleDirectionsUrl.append("&origin=")
                .append(viewModel.getLatitude())
                .append(",")
                .append(viewModel.getLongitude());
        googleDirectionsUrl.append("&destination=")
                .append(viewModel.getAddress());

        showWebPageUi(googleDirectionsUrl.toString());
    }

    private void showWorkingHoursTable() {
        LinkedHashMap<String, String> workingHours = viewModel.getWorkingHours();
        if (workingHours == null) {
            table.setVisibility(View.GONE);
            return;
        }
        String weekdayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        for (Map.Entry<String, String> entry : workingHours.entrySet()) {
            TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.table_row_opening_hours, null);
            ((TextView) row.findViewById(R.id.tv_weekday)).setText(entry.getKey());
            if (weekdayName.equalsIgnoreCase(entry.getKey())) {
                row.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }
            ((TextView) row.findViewById(R.id.tv_working_hours)).setText(entry.getValue());
            table.addView(row);
        }
    }

    @Override
    public void showPhoneDialerUi(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void showWebPageUi(String url) {
        launchUrl(url);
    }

    private void launchUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme() == null || uri.getScheme().isEmpty()) {
            uri = Uri.parse("http://" + url);
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.om_place_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_street_view:
                presenter.openStreetView();
                break;
        }
        return false;
    }

    @Override
    public void showStreetViewUi(double lat, double lng) {
        Intent intent = new Intent(getContext(), PlaceStreetViewActivity.class);
        intent.putExtra(PlaceStreetViewActivity.EXTRA_PLACE_LAT_LNG, new LatLng(lat, lng));
        startActivity(intent);
    }

    @Override
    public void showAddNewPlace(Place placeToAdd) {
        Intent intent = new Intent(getContext(), AddEditPlaceActivity.class);
        intent.putExtra(AddEditPlaceActivity.EXTRA_ADD_PLACE, placeToAdd);
        startActivityForResult(intent, REQUEST_ADD_PLACE);
    }

    @Override
    public void showEditPlace(String placeId) {
        Intent intent = new Intent(getContext(), AddEditPlaceActivity.class);
        intent.putExtra(AddEditPlaceActivity.EXTRA_EDIT_PLACE_API_ID, placeId);
        startActivityForResult(intent, REQUEST_EDIT_PLACE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ADD_PLACE == requestCode && getActivity().RESULT_OK == resultCode) {
            btnAddToMap.setVisibility(View.GONE);
            llEditRemovePlace.setVisibility(View.VISIBLE);
            showMessage("Saved");
        } else if (REQUEST_EDIT_PLACE == requestCode && getActivity().RESULT_OK == resultCode) {
            showMessage("Updated");
        }
    }

    @Override
    public void showPlaceDeleted() {
        btnAddToMap.setVisibility(View.VISIBLE);
        llEditRemovePlace.setVisibility(View.GONE);
    }

    public interface PlacesDetailsFragmentInteractionListener {
        void onSetCollapsingToolbarTitle(String title);

        void onSetCollapsingImage(String imageUrl);
    }

    @Override
    public void showLoadingPlaceDetailsError(Exception exception) {
        showMessage(ExceptionFactory.create(getContext(), exception));
    }

    @Override
    public void showNoPlaceDetailsMessage() {
        showMessage(getString(R.string.message_no_place_details));
    }
}
