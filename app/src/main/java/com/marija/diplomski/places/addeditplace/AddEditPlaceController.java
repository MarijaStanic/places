package com.marija.diplomski.places.addeditplace;

import android.support.v4.app.FragmentActivity;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.addeditplace.views.AddEditPlaceFragment;
import com.marija.diplomski.places.addselectfolders.AddSelectFolderFragment;
import com.marija.diplomski.places.base.BaseController;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.AddEditFolderPresenter;
import com.marija.diplomski.places.core.addeditfolder.usecase.CreateFolder;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.addeditplace.AddEditPlacePresenter;
import com.marija.diplomski.places.core.addeditplace.usecase.GetPlace;
import com.marija.diplomski.places.core.addeditplace.usecase.SavePlace;
import com.marija.diplomski.places.core.addeditplace.usecase.UpdatePlace;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.data.Database;

import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

public class AddEditPlaceController extends BaseController {

    public static final String TAG_ADD_EDIT_PLACE = "addEditPlace";
    public static final String TAG_ADD_EDIT_FOLDER = "addEditFolder";

    private String placeApiId;
    private Place placeToAdd;

    private AddEditPlaceController(FragmentActivity fragmentActivity, String placeApiId, Place placeToAdd) {
        setFragmentActivity(fragmentActivity);
        this.placeApiId = placeApiId;
        this.placeToAdd = placeToAdd;
    }

    public static AddEditPlaceController createAddEditPlaceView(FragmentActivity fragmentActivity, String placeId, Place place) {
        AddEditPlaceController addEditPlaceController = new AddEditPlaceController(fragmentActivity, placeId, place);
        addEditPlaceController.initAddEditPlaceView();

        return addEditPlaceController;
    }

    private void initAddEditPlaceView() {
        if (isTablet(getActivity())) {
            createTabletElements();
        } else {
            createPhoneElements();
        }
    }

    private void createTabletElements() {
        AddEditPlaceFragment addEditPlaceFragment = findOrCreateFragment(R.id.right_frame, TAG_ADD_EDIT_PLACE,
                AddEditPlaceFragment.class, false);
        AddEditPlacePresenter addEditPlacePresenter = createAddEditPlacePresenter(addEditPlaceFragment);

        AddSelectFolderFragment addSelectFolderFragment = findOrCreateFragment(R.id.left_frame, TAG_ADD_EDIT_FOLDER,
                AddSelectFolderFragment.class, false);
        AddEditFolderPresenter addEditFolderPresenter = createAddEditFolderPresenter(addSelectFolderFragment);

        AddEditPlaceTabletPresenter addEditPlaceTabletPresenter = new AddEditPlaceTabletPresenter(addEditPlacePresenter,
                addEditFolderPresenter);

        addEditPlaceFragment.setPresenter(addEditPlaceTabletPresenter);
        addSelectFolderFragment.setPresenter(addEditPlaceTabletPresenter);

        showFragments(addSelectFolderFragment, addEditPlaceFragment, TAG_ADD_EDIT_FOLDER, TAG_ADD_EDIT_PLACE, false);
    }

    private void createPhoneElements() {
        AddEditPlaceFragment addEditPlaceFragment = findOrCreateFragment(R.id.content, TAG_ADD_EDIT_PLACE,
                AddEditPlaceFragment.class, true);
        AddEditPlacePresenter addEditPlacePresenter = createAddEditPlacePresenter(addEditPlaceFragment);
        addEditPlaceFragment.setPresenter(addEditPlacePresenter);
    }

    private AddEditPlacePresenter createAddEditPlacePresenter(AddEditPlaceFragment addEditPlaceFragment) {
        AddEditPlacePresenter addEditPlacePresenter = new AddEditPlacePresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()),
                addEditPlaceFragment, new SavePlace(Database.placeDao), new GetPlace(Database.placeDao),
                new UpdatePlace(Database.placeDao), placeApiId, placeToAdd);

        return addEditPlacePresenter;
    }

    private AddEditFolderPresenter createAddEditFolderPresenter(AddSelectFolderFragment addSelectFolderFragment) {
        AddEditFolderPresenter addEditFolderPresenter = new AddEditFolderPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()),
                addSelectFolderFragment, new CreateFolder(Database.folderDao),
                new GetFolders(Database.folderDao), null);

        return addEditFolderPresenter;
    }

}
