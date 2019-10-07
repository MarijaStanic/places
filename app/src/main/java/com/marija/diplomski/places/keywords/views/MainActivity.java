package com.marija.diplomski.places.keywords.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.marija.diplomski.places.core.infrastructure.permission.PermissionChecker;
import com.marija.diplomski.places.core.infrastructure.permission.PermissionAction;
import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.keywords.PlacesMvpController;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.keywords.KeywordsContract;
import com.marija.diplomski.places.core.keywords.presenters.KeywordsPresenter;
import com.marija.diplomski.places.core.keywords.usecase.AddKeyword;
import com.marija.diplomski.places.core.keywords.usecase.DeleteKeyword;
import com.marija.diplomski.places.core.keywords.usecase.GetKeywords;
import com.marija.diplomski.places.core.data.Database;

import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;
import com.marija.diplomski.places.settings.PreferenceActivity;

import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

import com.marija.diplomski.places.core.infrastructure.location.UserLocationManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements KeywordsContract.View,
        PlacesMapFragment.OnPlacesMapFragmentListener,
        PlacesListFragment.OnPlacesListFragmentListener {

    public static final String KEY_FOLDER_ID_KEY = "keyFolderId";
    public static final String KEY_TOGGLE_VISIBILITY = "keyToggleVisibility";

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView nvKeywords;
    @BindView(R.id.rv_keywords)
    RecyclerView rvKeywords;
    @Nullable
    @BindView(R.id.sb_map_list)
    SwitchButton sbMapList;
    @BindView(R.id.action_settings)
    ImageView actionSettings;

    private KeywordsContract.Presenter presenter;

    private long lastBackButtonPress;

    private PlacesMvpController placesMvpController;

    private PermissionChecker permissionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Integer currentFolderId = null;

        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_FOLDER_ID_KEY)) {
                currentFolderId = savedInstanceState.getInt(KEY_FOLDER_ID_KEY);
            }
            int toggleVisibility = savedInstanceState.getInt(KEY_TOGGLE_VISIBILITY);
            if (toggleVisibility == View.INVISIBLE)
                hideSwitchButton();
        }

        placesMvpController = PlacesMvpController.createPlacesView(this, currentFolderId, permissionChecker);
    }

    @Override
    public void initView() {
        setupSwitchButton();
        setupDelegates();
    }

    @OnClick(R.id.action_settings)
    public void onSettingsClick() {
        startActivity(new Intent(this, PreferenceActivity.class));
    }

    private void setupSwitchButton() {
        if (!isTablet(this)) {
            sbMapList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    placesMvpController.onSwitchButtonClick();
                }
            });
        }
    }

    private void setupDelegates() {
        permissionChecker = new PermissionChecker(this);
        NavigationDrawerViewExtension drawerViewExtension = new NavigationDrawerViewExtension(this);
        UserLocationManager locationManager = UserLocationManager.getInstance(this, permissionChecker);
        addMainLifecycleDelegate(drawerViewExtension);
        addMainLifecycleDelegate(locationManager);
        drawerViewExtension.setViews(drawerLayout, nvKeywords, getToolbar(), rvKeywords);

        new KeywordsPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()),
                this, new GetKeywords(Database.keywordDao), new DeleteKeyword(Database.keywordDao),
                new AddKeyword(Database.keywordDao), drawerViewExtension);
        presenter.loadKeywords();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Integer currentFolderId = placesMvpController.getCurrentFolderId();
        if (currentFolderId != null)
            outState.putInt(KEY_FOLDER_ID_KEY, currentFolderId);

        if (!isTablet(this)) {
            sbMapList.onSaveInstanceState();
            outState.putInt(KEY_TOGGLE_VISIBILITY, sbMapList.getVisibility());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!isBackStackEmpty()) {
            super.onBackPressed();
            showSwitchButtonIfNeeded();
        } else { // To prevent accidentally getting out of the application
            checkLastBackPressTime();
        }
    }

    private void showSwitchButtonIfNeeded() {
        if (!isTablet(this)) {
            if (placesMvpController.getVisibleFragmentTag().equals(PlacesMvpController.TAG_MY_MAP)) {
                hideSwitchButton();
            } else {
                showSwitchButton();
            }
        }
    }

    private void showSwitchButton() {
        if (!isTablet(this))
            sbMapList.setVisibility(View.VISIBLE);
    }

    private void hideSwitchButton() {
        if (!isTablet(this))
            sbMapList.setVisibility(View.INVISIBLE);
    }

    private boolean isBackStackEmpty() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return true;
        }
        return false;
    }

    private void checkLastBackPressTime() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackButtonPress > 5000) {
            Toast.makeText(this, R.string.warning_exit_application, Toast.LENGTH_LONG).show();
            lastBackButtonPress = currentTime;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setPresenter(KeywordsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onUpdatePlacesList(List<PlaceViewModel> places, String nextPageToken, boolean forceUpdate) {
        placesMvpController.updatePlacesList(places, nextPageToken, forceUpdate);
    }

    @Override
    public void onShowMorePlaces(String nextPageToken) {
        placesMvpController.showMorePlaces(nextPageToken);
    }

    @Override
    public void showPlacesMapForKeyword(Keyword keyword) {
        showSwitchButton();
        placesMvpController.onKeywordSelected(keyword);
    }

    @Override
    public void showMyMapForFolder(int folderId) {
        hideSwitchButton();
        placesMvpController.setFolderId(folderId);
        placesMvpController.showMyMapForFolder();
    }

    @Override
    public void showMyMapForTablet() {
        placesMvpController.showMyMapForTablet();
    }

    @Override
    public void showKeywordAddedMessage() {
        showMessage(getString(R.string.message_keyword_added));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionAction.CODE_GET_LOCATION:
                permissionChecker.checkGrantedPermission(grantResults, requestCode);
                break;
        }
    }
}
