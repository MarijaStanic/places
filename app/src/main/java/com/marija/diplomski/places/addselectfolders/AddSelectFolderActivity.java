package com.marija.diplomski.places.addselectfolders;

import android.os.Bundle;

import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.AddEditFolderPresenter;
import com.marija.diplomski.places.core.addeditfolder.usecase.CreateFolder;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.data.Database;

import java.util.ArrayList;
import java.util.List;

public class AddSelectFolderActivity extends BaseActivity {

    public static final String EXTRA_PLACE_FOLDER_IDS = "EXTRA_PLACE_FOLDER_IDS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUpNavigation();
        getToolbar().setTitle(R.string.toolbar_title_folders);

        AddSelectFolderFragment addSelectFolderFragment = (AddSelectFolderFragment) getSupportFragmentManager().findFragmentById(R.id.content);

        List<Integer> placeFolderIds = (ArrayList<Integer>) getIntent().getSerializableExtra(EXTRA_PLACE_FOLDER_IDS);

        if (addSelectFolderFragment == null) {
            addSelectFolderFragment = AddSelectFolderFragment.newInstance();
            addFragmentToActivity(addSelectFolderFragment, R.id.content);
        }

        new AddEditFolderPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()), addSelectFolderFragment,
                new CreateFolder(Database.folderDao), new GetFolders(Database.folderDao), placeFolderIds);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_edit_folder;
    }

}
