package com.marija.diplomski.places.keywords;

import android.support.v4.app.FragmentActivity;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.base.BaseController;
import com.marija.diplomski.places.core.infrastructure.permission.PermissionChecker;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.data.network.ConnectivityInterceptor;
import com.marija.diplomski.places.core.data.network.PlacesRestApiImpl;
import com.marija.diplomski.places.core.folders.FoldersPresenter;
import com.marija.diplomski.places.core.folders.usecase.DeleteFolder;
import com.marija.diplomski.places.core.folders.usecase.UpdateFolder;
import com.marija.diplomski.places.core.mymap.PlacesInFolderPresenter;
import com.marija.diplomski.places.core.keywords.presenters.PlacesListPresenter;
import com.marija.diplomski.places.core.keywords.presenters.PlacesMapPresenter;
import com.marija.diplomski.places.core.mymap.usecase.GetPlaces;
import com.marija.diplomski.places.core.mymap.usecase.GetPlacesForFolder;
import com.marija.diplomski.places.core.data.Database;
import com.marija.diplomski.places.core.data.preferences.PreferencesHelperImpl;
import com.marija.diplomski.places.folders.FoldersFragment;
import com.marija.diplomski.places.mymap.MyMapFragment;
import com.marija.diplomski.places.keywords.viewmodel.mapper.PlaceViewModelMapper;
import com.marija.diplomski.places.keywords.views.PlacesMapFragment;
import com.marija.diplomski.places.keywords.views.PlacesListFragment;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;
import com.marija.diplomski.places.core.infrastructure.network.NetworkConnectionCheckerImpl;
import com.marija.diplomski.places.core.infrastructure.location.UserLocationManager;

import java.util.List;

import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

public class PlacesMvpController extends BaseController {

    public static final String TAG_PLACES_MAP = "placesMap";
    public static final String TAG_MY_MAP = "myMap";
    public static final String TAG_FOLDERS = "folders";
    public static final String TAG_PLACES_LIST = "placesList";

    private PlacesMapFragment placesMapFragment;
    private PlacesListFragment placesListFragment;
    private MyMapFragment myMapFragment;
    private FoldersFragment foldersFragment;

    private PlacesInFolderPresenter placesInFolderPresenter;

    private PermissionChecker permissionChecker;
    private Integer currentFolderId;

    private PlacesMvpController(FragmentActivity fragmentActivity, Integer currentFolderId,
                                PermissionChecker permissionChecker) {
        setFragmentActivity(fragmentActivity);
        this.currentFolderId = currentFolderId;
        this.permissionChecker = permissionChecker;
    }

    public static PlacesMvpController createPlacesView(FragmentActivity fragmentActivity, Integer currentFolderId,
                                                       PermissionChecker permissionChecker) {
        PlacesMvpController placesMvpController = new PlacesMvpController(fragmentActivity,
                currentFolderId, permissionChecker);
        placesMvpController.initPlacesView();

        return placesMvpController;
    }

    private void initPlacesView() {
        if (isTablet(getActivity())) {
            createTabletElements();
        } else {
            createPhoneElements();
        }
    }

    private void createTabletElements() {
        if (isFragmentVisited(TAG_MY_MAP)) {
            createMyMapForTablet();
        }
        placesMapFragment = findOrCreateFragment(R.id.right_frame, TAG_PLACES_MAP, PlacesMapFragment.class, true);
        PlacesMapPresenter placesMapPresenter = createPlacesMapPresenter(placesMapFragment);
        placesMapFragment.setPresenter(placesMapPresenter);

        placesListFragment = findOrCreateFragment(R.id.left_frame, TAG_PLACES_LIST, PlacesListFragment.class, true);
        PlacesListPresenter placesListPresenter = createPlacesListPresenter(placesListFragment);
        placesListFragment.setPresenter(placesListPresenter);
    }

    private void createPhoneElements() {
        if (isFragmentVisited(TAG_MY_MAP)) {
            createMyMapForPhone();
        }
        placesListFragment = findOrCreateFragment(R.id.content, TAG_PLACES_LIST, PlacesListFragment.class, true);
        PlacesListPresenter placesListPresenter = createPlacesListPresenter(placesListFragment);
        placesListFragment.setPresenter(placesListPresenter);

        placesMapFragment = findOrCreateFragment(R.id.content, TAG_PLACES_MAP, PlacesMapFragment.class, true);
        PlacesMapPresenter placesMapPresenter = createPlacesMapPresenter(placesMapFragment);
        placesMapFragment.setPresenter(placesMapPresenter);
    }

    private PlacesMapPresenter createPlacesMapPresenter(PlacesMapFragment placesMapFragment) {
        PlacesMapPresenter placesMapPresenter = new PlacesMapPresenter(placesMapFragment,
                UserLocationManager.getInstance(getActivity(), permissionChecker),
                PlacesRestApiImpl.getInstance(new ConnectivityInterceptor(new NetworkConnectionCheckerImpl(getActivity()))),
                new PlaceViewModelMapper(UserLocationManager.getInstance(getActivity(), permissionChecker)),
                PreferencesHelperImpl.getInstance(getActivity().getApplicationContext()));
        return placesMapPresenter;
    }

    private PlacesListPresenter createPlacesListPresenter(PlacesListFragment placesListFragment) {
        PlacesListPresenter placesListPresenter = new PlacesListPresenter(placesListFragment);
        return placesListPresenter;
    }

    public void createMyMapForPhone() {
        myMapFragment = findOrCreateFragment(R.id.content, TAG_MY_MAP, MyMapFragment.class, false);
        placesInFolderPresenter = createPlacesInFolderPresenter(myMapFragment);
        myMapFragment.setPresenter(placesInFolderPresenter);
    }

    public void createMyMapForTablet() {
        foldersFragment = findOrCreateFragment(R.id.left_frame, TAG_FOLDERS, FoldersFragment.class, false);
        FoldersPresenter foldersPresenter = createFoldersPresenter(foldersFragment);

        myMapFragment = findOrCreateFragment(R.id.right_frame, TAG_MY_MAP, MyMapFragment.class, false);
        PlacesInFolderPresenter placesInFolderPresenter = createPlacesInFolderPresenter(myMapFragment);

        MyMapTabletPresenter myMapTabletPresenter = new MyMapTabletPresenter(foldersPresenter, placesInFolderPresenter);

        foldersFragment.setPresenter(myMapTabletPresenter);
        myMapFragment.setPresenter(myMapTabletPresenter);
    }

    private PlacesInFolderPresenter createPlacesInFolderPresenter(MyMapFragment myMapFragment) {
        placesInFolderPresenter = new PlacesInFolderPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()),
                myMapFragment, new GetPlaces(Database.placeDao), new GetPlacesForFolder(Database.placeDao), currentFolderId);
        return placesInFolderPresenter;
    }

    private FoldersPresenter createFoldersPresenter(FoldersFragment foldersFragment) {
        FoldersPresenter foldersPresenter = new FoldersPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()),
                foldersFragment, new GetFolders(Database.folderDao), new UpdateFolder(Database.folderDao),
                new DeleteFolder(Database.folderDao));
        return foldersPresenter;
    }

    public Integer getCurrentFolderId() {
        if (placesInFolderPresenter != null)
            return placesInFolderPresenter.getFolderId();
        return null;
    }

    public void setFolderId(Integer folderId) {
        this.currentFolderId = folderId;
    }

    public void onSwitchButtonClick() {
        if (placesMapFragment.isVisible()) {
            showFragment(R.id.content, placesListFragment, TAG_PLACES_LIST);
        } else {
            showFragment(R.id.content, placesMapFragment, TAG_PLACES_MAP);
        }
    }

    public void onKeywordSelected(Keyword keyword) {
        if (!placesMapFragment.isVisible()) {
            if (isTablet(getActivity())) {
                showFragments(placesListFragment, placesMapFragment, TAG_PLACES_LIST, TAG_PLACES_MAP, true);
            } else {
                showFragment(R.id.content, placesMapFragment, TAG_PLACES_MAP);
            }
        }
        placesMapFragment.onKeywordSelected(keyword);
    }

    public void showMyMapForFolder() {
        if (myMapFragment != null) {
            placesInFolderPresenter.setFolderId(currentFolderId);
            showFragment(R.id.content, myMapFragment, TAG_MY_MAP);
        } else {
            createMyMapForPhone();
        }
    }

    public void showMyMapForTablet() {
        if (myMapFragment == null)
            createMyMapForTablet();
        showFragments(foldersFragment, myMapFragment, TAG_FOLDERS, TAG_MY_MAP, true);
    }

    public void updatePlacesList(List<PlaceViewModel> places, String nextPageToken, boolean forceUpdate) {
        placesListFragment.refreshEvents(places, nextPageToken, forceUpdate);
    }

    public void showMorePlaces(String nextPageToken) {
        placesMapFragment.showMorePlaces(nextPageToken);
    }
}